package com.icode.generic.task;

import com.icode.generic.base.ICGenTreeNode;

public abstract class ICTaskTimed extends ICTask {
	long waitMillis;

	public ICTaskTimed() {
		super();
	}

	public ICTaskTimed(Object owner, String groupName, String name, long waitMillis) {
		super(owner, groupName, name);
		this.waitMillis = waitMillis;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		
		long l = config.getOptionalLong("waitMillis", -1);
		if ( -1 != l ) {
			setWaitMillis(l);
		}
	}

	public long getWaitMillis() {
		synchronized (this) {
			return waitMillis;
		}
	}

	public void setWaitMillis(long waitMillis) {
		synchronized (this) {
			this.waitMillis = waitMillis;
		}
	}

	protected void doTask() throws Exception {
		while ( stateCheck(STATECHECK_RUNNING) ) {
			if ( doRepeatedTask() ) {
				sleepTask(waitMillis);
			} else {
				break;
			}
		}
	}

	protected abstract boolean doRepeatedTask() throws Exception;

}
