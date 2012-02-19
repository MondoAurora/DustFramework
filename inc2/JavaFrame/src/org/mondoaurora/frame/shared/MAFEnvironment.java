package org.mondoaurora.frame.shared;

public abstract class MAFEnvironment {
	protected static MAFEnvironment env;
	
	protected abstract MAFConnector getThisI();	
	protected abstract MAFConnector getCallContextI();
	protected abstract MAFConnector getInstanceI(MAFIdentifier id, String[] names);
	protected abstract MAFIdentifier getIdI(String id);
	
	public static MAFConnector getThis() {
		return env.getThisI();
	}
	
	public static MAFConnector getCallContext() {
		return env.getCallContextI();
	}	
	
	public static MAFConnector getInstance(MAFIdentifier id, String[] names) {
		return env.getInstanceI(id, names);
	}
	
	public static MAFIdentifier getId(String id) {
		return env.getIdI(id);
	}
}
