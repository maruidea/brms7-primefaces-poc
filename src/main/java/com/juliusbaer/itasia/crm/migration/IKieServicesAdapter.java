package com.juliusbaer.itasia.crm.migration;

import java.util.Collection;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;

public interface IKieServicesAdapter {

    public void insert(Object object);

    public void fireAllRules();

    public FactHandle getFactHandle(Object object);

    public void retract(FactHandle factHandle);

	public Collection<FactHandle> getFactHandles();
    
	public Collection<Object> getObjects();

    public void dispose();

    public Object getObject(FactHandle factHandle);

	public void fireAllRules(AgendaFilter agendaFilter);
	
	public void startProcess(String processId);
	
	public void setGlobal(String identifier, Object object);
	
	public void execute(Iterable<?> objects);
	
	public void execute(Object object);

    boolean hasErrors();

    String getErrors();
    
    String getInsertCommandPayload(Object object);

    public void insert(Object object, String commandPayload);

    public void insertAs(Object object, Class<?> asClass);
}