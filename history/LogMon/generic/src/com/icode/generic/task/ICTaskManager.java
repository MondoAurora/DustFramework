package com.icode.generic.task;

import java.util.*;

import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.log.ICLogSender;

public class ICTaskManager implements ICLogSender, ICGenConfigurable, ICTaskConstants {
	private static String ALL_GROUPS = "All";

	Set tasks = new TreeSet();
	Set activeGroups = new TreeSet();

	String[] startupSequence;
	long shutdownWait;

	class TaskThread extends Thread {
		ICTask myTask;

		public void run() {
			try {
				super.run();
			} finally {
				synchronized (tasks) {
					tasks.remove(myTask);
				}
				Object owner = myTask.getOwner();
				if (owner instanceof ICTask.EndListener) {
					((ICTask.EndListener) owner).taskEnded(myTask);
				}
			}
		}

		TaskThread(ICTask task) {
			super(task, task.getName());
			task.setStatus(STATUS_INIT_RUNNING);
			task.setThread(this);
			myTask = task;
		}
	}

	public void addTask(ICTask task) {
		synchronized (tasks) {
			if (!tasks.contains(task)) {
				tasks.add(task);
				synchronized (activeGroups) {
					if (activeGroups.contains(task.getGroupName())) {
						startTask(task);
					}
				}
			}
		}
	}

	void startTask(ICTask task) {
		synchronized (tasks) {
			if (STATUS_CREATED == task.getStatus()) {
				new TaskThread(task).start();
			}
		}
	}

	void stopTask(ICTask task, long wait) {
		synchronized (tasks) {
			task.requestStop(wait);
		}
	}

	public boolean startGroup(String groupName, Object owner, long waitMillis) {
		activeGroups.add(groupName);
		ArrayList waitList = buildWaitList(groupName, STATECHECK_CREATED, owner);

		for (int i = waitList.size(); i-- > 0;) {
			startTask((ICTask) waitList.get(i));
		}

		return waitForListState(waitList, STATECHECK_STARTED, waitMillis);
	}

	public boolean stopGroup(String groupName, Object owner, long waitMillis) {
		activeGroups.remove(groupName);

		ArrayList waitList = buildWaitList(groupName, STATECHECK_INIT_OR_RUN, owner);

		for (int i = waitList.size(); i-- > 0;) {
			stopTask((ICTask) waitList.get(i), WAIT_NONE);
		}

		return waitForListState(waitList, STATECHECK_FINISHED, waitMillis);
	}

	public ArrayList buildWaitList(String groupName, byte statecheck, Object owner) {
		ICTask t;
		ArrayList waitList = new ArrayList();

		synchronized (tasks) {
			for (Iterator it = tasks.iterator(); it.hasNext();) {
				t = (ICTask) it.next();
				// "ALL_GROUPS == groupName" intentional: not the "All" string, this can only come from shutdown()
				if (t.stateCheck(statecheck) && ((ALL_GROUPS == groupName) || groupName.equals(t.getGroupName())) && ((null == owner) || (owner == t.getOwner()))) {
					waitList.add(t);
				}
			}
		}
		return waitList;
	}

	public boolean waitForGroupFinish(String groupName, Object owner, long waitMillis) {
		ArrayList waitList = buildWaitList(groupName, STATECHECK_INIT_OR_RUN, owner);

		return waitForListState(waitList, STATECHECK_FINISHED, waitMillis);
	}

	public boolean waitForState(ICTask task, byte statecheck, long waitMillis) {
		ArrayList wl = new ArrayList();
		wl.add(task);
		return waitForListState(wl, statecheck, waitMillis);
	}

	public boolean waitForListState(ArrayList waitList, byte statecheck, long waitMillis) {
		long start = System.currentTimeMillis();
		long waitLimit;
		ICTask t;

		for (waitLimit = waitMillis; !waitList.isEmpty() && (WAIT_NONE != waitLimit);) {
			for (int i = waitList.size(); i-- > 0;) {
				t = (ICTask) waitList.get(i);
				waitLimit = ICTask.getRemainingWait(start, waitMillis);
				if (t.waitForState(statecheck, waitLimit)) {
					waitList.remove(i);
				}
			}
		}

		return waitList.isEmpty();
	}

	public String getStatus() {
		TreeSet msgs = new TreeSet();
		
		synchronized (tasks) {
			for (Iterator it = tasks.iterator(); it.hasNext();) {
				msgs.add(((ICTask) it.next()).toString());
			}
		}

		StringBuffer ret = new StringBuffer();
		for (Iterator it = msgs.iterator(); it.hasNext();) {
			ret.append(it.next()).append("\n");
		}
		
		return ret.toString();
	}

	public synchronized void startup() {
		for (int i = 0; i < startupSequence.length; ++i) {
			startGroup(startupSequence[i], null, WAIT_FOREVER);
		}
	}

	public synchronized void shutdown() {
		long start = System.currentTimeMillis();
		log(EVENT_LEVEL_DEBUG, "Shutdown initiated...", null);
		boolean graceful = true;

		for (int i = startupSequence.length; i-- > 0;) {
			log(EVENT_LEVEL_DEBUG, "Stopping group " + startupSequence[i], null);
			graceful &= stopGroup(startupSequence[i], null, ICTask.getRemainingWait(start, shutdownWait));
		}

		ArrayList rest = buildWaitList(ALL_GROUPS, STATECHECK_INIT_OR_RUN, null);
		if ( !rest.isEmpty() ) {
			log(EVENT_LEVEL_DEBUG, "Stopping the rest...", null);
			graceful &= stopGroup(ALL_GROUPS, null, ICTask.getRemainingWait(start, shutdownWait));
		}
		
		if ( graceful ) {
			log(EVENT_LEVEL_INFO, "Successful shutdown", null);
		} else {
			log(EVENT_LEVEL_ERROR, "Graceful shutdown failed - time out!", null);
		}
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		startupSequence = ICGenUtils.str2arr((String) config.getOptional("startupSequence", ""), ',');
		shutdownWait = config.getOptionalLong("shutdownWait", 5000);
	}

	public void log(byte level, String message, Object param) {
		ICAppFrame.log(level, getClass().getName(), message, param);
	}

}
