package com.juliusbaer.itasia.crm.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.runtime.help.impl.BatchExecutionHelperProviderImpl;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.RuleServicesClient;

public class KieServicesAdapter implements IKieServicesAdapter {
    private RuleServicesClient jaxbRulesClient;
    private RuleServicesClient jsonRulesClient;
    private RuleServicesClient xstreamRulesClient;
    private String containerId;
    private List<Command<?>> commandList = new ArrayList<Command<?>>();
    private KieCommands commandsFactory = KieServices.Factory.get().getCommands();
    private Map<Object, FactHandle> trackedFactHandles = new HashMap<Object, FactHandle>();
    private String errors;
    
    public KieServicesAdapter(KieServicesClient jaxbKieServicesClient, KieServicesClient jsonKieServicesClient, KieServicesClient xstreamKieServicesClient, String containerId) {
        this.containerId = containerId;
        this.jaxbRulesClient = jaxbKieServicesClient.getServicesClient(RuleServicesClient.class);
        this.jsonRulesClient = jsonKieServicesClient.getServicesClient(RuleServicesClient.class);
        this.xstreamRulesClient = xstreamKieServicesClient.getServicesClient(RuleServicesClient.class);
    }
    
    @Override
    public void insert(Object object) {
        ServiceResponse<ExecutionResults> result = xstreamRulesClient.executeCommandsWithResults(containerId,
    			commandsFactory.newInsert(object, object.getClass().getName()));
    	trackedFactHandles.put(object, (FactHandle) result.getResult().getFactHandle(object.getClass().getName()));
    }

    private void insertElements(Iterable<?> objects) {
        jsonRulesClient.executeCommandsWithResults(containerId,
                commandsFactory.newInsertElements(objects));
    }

    @Override
    public void fireAllRules() {
        commandList.add(commandsFactory.newFireAllRules("numberOfFiredRules"));
        ServiceResponse<ExecutionResults> result = jsonRulesClient.executeCommandsWithResults(containerId,
                commandsFactory.newBatchExecution(commandList));
        
        if(result.getType() == ResponseType.SUCCESS) {
            errors = null;
        } else if(result.getType() != ResponseType.SUCCESS) {
            errors = result.getMsg();
        }
    }

    @Override
    public FactHandle getFactHandle(Object object) {
         return trackedFactHandles.get(object);
    }

    @Override
    public void retract(FactHandle factHandle) {
        jsonRulesClient.executeCommandsWithResults(containerId, commandsFactory.newDelete(factHandle));
    }

    @Override
    @SuppressWarnings("unchecked")
	public Collection<FactHandle> getFactHandles() {
    	ServiceResponse<ExecutionResults> executeCommandsWithResults = jsonRulesClient.executeCommandsWithResults(containerId, commandsFactory.newGetFactHandles("factHandles"));
    	return (Collection<FactHandle>)executeCommandsWithResults.getResult().getValue("factHandles");
    }
    
    @Override
    @SuppressWarnings("unchecked")
	public Collection<Object> getObjects() {
    	ServiceResponse<ExecutionResults> results = xstreamRulesClient.executeCommandsWithResults(containerId, commandsFactory.newGetObjects("objects"));
    	return (Collection<Object>)results.getResult().getValue("objects");
    }

    @Override
    public void dispose() {
        commandList.clear();
        trackedFactHandles.clear();
        jsonRulesClient.executeCommandsWithResults(containerId, commandsFactory.newDispose());
        errors = null;
    }

    @Override
    public Object getObject(FactHandle factHandle) {
    	ServiceResponse<ExecutionResults> result = xstreamRulesClient.executeCommandsWithResults(containerId, commandsFactory.newGetObject(factHandle, "outObject"));
    	return result.getResult().getValue("outObject");
    }

    @Override
	public void fireAllRules(AgendaFilter agendaFilter) {
		FireAllRulesCommand fireAllRulesCommand = new FireAllRulesCommand(agendaFilter);
		fireAllRulesCommand.setOutIdentifier("numberOfFiredRules");
		
        commandList.add(fireAllRulesCommand);

        jaxbRulesClient.executeCommandsWithResults(containerId,
        		commandsFactory.newBatchExecution(commandList, "defaultKieSession"));
	}

    @Override
    public void startProcess(String processId) {
        jsonRulesClient.executeCommandsWithResults(containerId, commandsFactory.newStartProcess(processId));
    }
    
    @Override
    public void setGlobal(String identifier, Object object) {
        jaxbRulesClient.executeCommandsWithResults(containerId, commandsFactory.newSetGlobal(identifier, object));
    }

    @Override
    public void execute(Iterable<?> objects) {
        insertElements(objects);
        fireAllRules();
        dispose();
    }

    @Override
    public void execute(Object object) {
        insert(object);
        fireAllRules();
        dispose();
    }
    
    @Override
    public boolean hasErrors() {
        return errors != null;
    }
    
    @Override
    public String getErrors() {
        return errors;
    }

    @Override
    public String getInsertCommandPayload(Object object) {
        return new BatchExecutionHelperProviderImpl().newXStreamMarshaller().toXML(commandsFactory.newInsert(object, object.getClass().getName()));
    }

    @Override
    public void insert(Object object, String commandPayload) {
        ServiceResponse<ExecutionResults> result = xstreamRulesClient.executeCommandsWithResults(containerId, commandPayload);
        trackedFactHandles.put(object, (FactHandle) result.getResult().getFactHandle(object.getClass().getName()));
    }

    @Override
    public void insertAs(Object object, Class<?> asClass) {
        String cp = getInsertCommandPayload(object);
        cp = cp.replaceAll("<" + object.getClass().getName() + ">", "<" + asClass.getName() + ">")
                .replaceAll("</" + object.getClass().getName() + ">", "</" + asClass.getName() + ">");
        insert(object, cp);
    }

}