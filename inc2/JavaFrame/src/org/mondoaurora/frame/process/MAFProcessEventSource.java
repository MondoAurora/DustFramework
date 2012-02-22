package org.mondoaurora.frame.process;

public abstract class MAFProcessEventSource {
	MAFProcessManager mgr;
	
	void start(MAFProcessManager mgr) {
		this.mgr = mgr;
		start();
	}
	
	protected void sendEvent(Object event) {
		mgr.processEvent(this, event);
	}
	
	protected abstract void start();
	
	protected abstract Object mark();
	protected abstract void rollback(Object mark);
	protected abstract void releaseMark(Object mark);
}
