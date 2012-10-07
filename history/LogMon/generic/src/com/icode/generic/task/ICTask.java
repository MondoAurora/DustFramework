/**
 * 
 */
package com.icode.generic.task;

import java.lang.ref.WeakReference;
import java.util.Date;

import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.log.ICLogSender;

public abstract class ICTask implements ICGenConfigurable, ICLogSender, Runnable, Comparable, ICTaskConstants {
	public static interface EndListener {
		void taskEnded(ICTask task);
	}

	private String groupName;
	private String name;
	private WeakReference ownerRef;
	private static final Object NULL_OWNER = new Object();

	private Object statusLock = new Object();
	private int status;
	private Exception ex;

	private StringBuffer message = new StringBuffer();
	private long messageTime;

	private Thread myThread;

	public ICTask() {
		this(null, null, null);
	}

	public ICTask(Object owner) {
		this(owner, null, null);
	}

	public ICTask(Object owner, String groupName, String name) {
		setStatus(ICTaskManager.STATUS_CREATED);

		this.groupName = groupName;
		this.name = name;
		ownerRef = new WeakReference((null == owner) ? NULL_OWNER : owner);
		
		setMessage("Created.");
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		groupName = (String) config.getOptional("groupName", groupName);
		name = (String) config.getOptional("name", (null == name) ? config.getName() : name);
	}

	public final String getName() {
		synchronized (this.message) {
			return name;
		}
	}

	public final String getGroupName() {
		synchronized (this.message) {
			return groupName;
		}
	}

	public final void setOwner(Object owner) {
		Object oldOwner = ownerRef.get();
		if ((null != owner) && ((NULL_OWNER == oldOwner) || (null == oldOwner))) {
			ownerRef = new WeakReference(owner);
		} else {
			throw new RuntimeException("FATAL ICTask.setOwner: thread " + this + " is alreadí owned by " + oldOwner);
		}
	}

	public final Object getOwner() {
		Object o = ownerRef.get();
		return (NULL_OWNER == o) ? null : o;
	}

	public final boolean stateCheck(byte checkmode) {
		synchronized (statusLock) {
			return stateCheck(checkmode, status);
		}
	}

	public final int getStatus() {
		synchronized (statusLock) {
			return status;
		}
	}

	final void setThread(Thread t) {
		myThread = t;
	}

	protected boolean init() throws Exception {
		return true;
	};

	public final void run() {
		try {
			setMessage("Initializing...");

			if (init()) {
				setStatus(ICTaskManager.STATUS_RUNNING);
				myThread = Thread.currentThread();

				setMessage("Running...");
				doTask();

				setMessage("Ended.");
				synchronized (statusLock) {
					setStatus((ICTaskManager.STATUS_RUNNING == status) ? ICTaskManager.STATUS_DONE_SUCCESS : ICTaskManager.STATUS_DONE_STOPPED);
				}
				setStatus(ICTaskManager.STATUS_DONE_SUCCESS);
			} else {
				setStatus(ICTaskManager.STATUS_INIT_FAILED);
			}
		} catch (InterruptedException ex) {
			setStatus(ICTaskManager.STATUS_DONE_STOPPED);
		} catch (Exception ex) {
			this.ex = ex;
			ex.printStackTrace(System.err);
			setStatus(ICTaskManager.STATUS_DONE_FAILURE);
		}
	}

	protected void stopRequested() {
	};
	
	protected void goingToSleep() {
	};

	protected abstract void doTask() throws Exception;

	protected final void sleepTask(long waitMillis) {
		synchronized (this) {
			goingToSleep();
			try {
				wait(waitMillis);
			} catch (InterruptedException e) {
				// someone has waken me up...
			}
		}
	}

	protected final void setStatus(int status) {
		synchronized (statusLock) {
			this.status = status;
			statusLock.notifyAll();
		}
	}

	public final boolean waitForState(byte checkmode, long waitMillis) {
		if (Thread.currentThread() == myThread) {
			setStatus(STATUS_DONE_STOPPED);
			return true; // I am on the same thread that requested the stop, should
										// let the process go and finish stopping
		}
		synchronized (statusLock) {
			long start = System.currentTimeMillis();
			for (long waitLimit = waitMillis; !stateCheck(checkmode) && (WAIT_NONE != waitLimit); waitLimit = getRemainingWait(start, waitMillis)) {
				try {
					statusLock.wait(waitLimit);
				} catch (InterruptedException e) {
					// do nothing
				}
			}
			return stateCheck(checkmode);
		}
	}

	public final int requestStop(long waitMillis) {
		synchronized (statusLock) {
			if (!stateCheck(STATECHECK_FINISHED)) {
				setStatus(ICTaskManager.STATUS_STOP_REQUESTED);
				stopRequested();
				myThread.interrupt();
				waitForState(STATECHECK_FINISHED, waitMillis);
			}
		}

		return status;
	}

	public final Exception getException() {
		synchronized (statusLock) {
			return (ICTaskManager.STATUS_DONE_FAILURE == status) ? ex : null;
		}
	}

	protected final void setName(String name) {
		synchronized (this.message) {
			this.name = name;
		}
	}

	protected final void setGroupName(String name) {
		synchronized (this.message) {
			this.groupName = name;
		}
	}

	protected final void setMessage(String msg) {
		synchronized (this.message) {
			message.replace(0, message.length(), msg);
			messageTime = System.currentTimeMillis();
//			log(EVENT_LEVEL_DEBUG_FINEST, "Update status message: " + msg, null);
		}
	}

	protected final void updateMessage() {
		synchronized (this.message) {
			buildMessage(message);
			messageTime = System.currentTimeMillis();
		}
	}

	protected void buildMessage(StringBuffer message) {
		throw new RuntimeException("To use ICTaskManager.Task.updateMessage(), you must override ICTaskManager.Task.buildMessage()!");
	}

	public String getMessage() {
		synchronized (message) {
			return message.toString();
		}
	}

	public String toString() {
		synchronized (message) {
			StringBuffer ret = new StringBuffer(groupName).append(" ").append(name);
			ret.append(" [").append(getStatMessage(getStatus())).append("] at ");
			ret.append(new Date(messageTime)).append(" message: ").append(message);
			return ret.toString();
		}
	}

	public final int compareTo(Object arg0) {
		ICTask other = (ICTask) arg0;

		int cmp = ICGenUtils.safeCmp(groupName, other.groupName);
		if (0 == cmp) {
			cmp = ICGenUtils.safeCmp(name, other.name);
			if (0 == cmp) {
				cmp = this.hashCode() - other.hashCode();
			}
		}
		return cmp;
	}

	public static boolean stateCheck(byte checkmode, int status) {
		switch (checkmode) {
		case STATECHECK_RUNNING:
			return STATUS_RUNNING == status;
		case STATECHECK_FINISHED:
			return status <= 0;
		case STATECHECK_INIT_OR_RUN:
			return (STATUS_RUNNING == status) || (STATUS_INIT_RUNNING == status);
		case STATECHECK_CREATED:
			return STATUS_CREATED == status;
		case STATECHECK_STARTED:
			return (STATUS_CREATED != status) && (STATUS_INIT_RUNNING != status);
		}
		return false;
	}

	public static String getStatMessage(int status) {
		return STAT_MESSAGES[STATUS_MSGOFFSET + status];
	}

	public static long getRemainingWait(long start, long waitMillis) {
		if (0 < waitMillis) {
			waitMillis = start + waitMillis - System.currentTimeMillis();
			if (waitMillis <= 0) {
				waitMillis = WAIT_NONE;
			}
		}
		return waitMillis;
	}

	public void log(byte level, String message, Object param) {
		ICAppFrame.log(level, name, message, param);
	}
}