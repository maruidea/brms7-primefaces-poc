package com.juliusbaer.itasia.crm.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.RuleServicesClient;

public class KieServicesAdapter {
    private RuleServicesClient rulesClient;
    private String containerId;
    private List<Command<?>> commandList = new ArrayList<Command<?>>();
    private KieCommands commandsFactory = KieServices.Factory.get().getCommands();
    private List<String> identifiers = new ArrayList<String>();
    private ServiceResponse<ExecutionResults> recentResult = null;
    
    public KieServicesAdapter(KieServicesClient kieServicesClient, String containerId) {
        this.containerId = containerId;
        this.rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
    }
    
    public void insert(Object object) {
        commandList.add(commandsFactory.newInsert(object, object.getClass().getName()));
        identifiers.add(object.getClass().getName());
    }

    public void fireAllRules() {
        commandList.add(commandsFactory.newFireAllRules("numberOfFiredRules"));
        recentResult = rulesClient.executeCommandsWithResults(containerId,
                commandsFactory.newBatchExecution(commandList));
    }

    public FactHandle getFactHandle(Object object) {
        return (FactHandle) recentResult.getResult().getFactHandle(object.getClass().getName());
    }

    public void retract(FactHandle factHandle) {
        rulesClient.executeCommandsWithResults(containerId, commandsFactory.newDelete(factHandle));
    }

    public Collection<FactHandle> getFactHandles() {
        List<FactHandle> factHandles = new ArrayList<FactHandle>();
        for(String identifier : identifiers) {
            factHandles.add((FactHandle) recentResult.getResult().getFactHandle(identifier));
        }

        return factHandles;
    }

    public void dispose() {
        commandList.clear();
        identifiers.clear();
        rulesClient.executeCommandsWithResults(containerId, commandsFactory.newDispose());
    }

    public Object getObject(Object object) {
        return recentResult.getResult().getValue(object.getClass().getName());
    }

}
