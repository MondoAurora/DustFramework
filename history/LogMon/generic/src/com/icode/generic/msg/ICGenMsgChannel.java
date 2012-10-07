/**
 * 
 */
package com.icode.generic.msg;

import java.util.ArrayList;
import java.util.Set;

import com.icode.generic.base.ICGenConstants;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.task.ICTaskTimed;

abstract class ICGenMsgChannel extends ICTaskTimed implements ICGenConstants {
	public static final byte DROPREASON_POOL_LIMIT_EXCEEDED = 1;
	public static final byte DROPREASON_RETRY_COUNT_EXCEEDED = 2;

	public static final long DEFAULT_SLEEP_MILLIS = 2000;

	protected abstract class QueuedMsg {
		public final ICGenMsg msg;
		
		int sendCount = 0;
		long nextSendTime = 0;

		protected QueuedMsg(ICGenMsg msg) {
			this.msg = msg;
		}
	}

	protected class QueuedMsgSingle extends QueuedMsg {
		ICGenMsgListener listener;

		protected QueuedMsgSingle(ICGenMsg msg, ICGenMsgListener listener) {
			super(msg);
			this.listener = listener;
		}
	}

	protected class QueuedMsgSet extends QueuedMsg {
		Set setListeners;

		protected QueuedMsgSet(ICGenMsg msg, Set setListeners) {
			super(msg);
			this.setListeners = setListeners;
		}
	}

	public final String type;
	private int poolSize = 1000;
	private int flushWakeupLimit = 700;
	private long retryWait = 10000;
	private int retryCount = 5;
	
	private long sentCount;
	private long failureCount;

	ArrayList notifs = new ArrayList();
	
	public ICGenMsgChannel(String type) {
		super(null, NOTIF_CHANNEL, type, DEFAULT_SLEEP_MILLIS);
		this.type = type;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		poolSize = config.getOptionalInt("poolSize", 1000);
		flushWakeupLimit = config.getOptionalInt("flushWakeupLimit", 700);
		retryCount = config.getOptionalInt("retryCount", 5);
		retryWait = config.getOptionalLong("retryWait", 10000);

		super.loadDataFrom(config, hint);
	}
	
	protected boolean init() throws Exception {
		sentCount = failureCount = 0;
		return super.init();
	};

	public abstract void transmitMsg(ICGenMsg msg, Set targets);

	protected abstract void doSendMsg(QueuedMsg queuedMsg) throws Exception;

	protected void sendInit() throws Exception {
	}

	protected void sendFinished() throws Exception {
	}

	private void dropMsg(QueuedMsg chn, byte reason, String message) {
		// log it
	}

	protected void enqueueMsg(QueuedMsg chn) {
		synchronized (this) {
			if (notifs.size() > poolSize) {
				dropMsg((QueuedMsg) notifs.remove(0), DROPREASON_POOL_LIMIT_EXCEEDED, "");
			}
			notifs.add(chn);
			if (notifs.size() > flushWakeupLimit) {
				notifyAll(); // wake up the flush thread!
			}
		}
	}

	protected final boolean doRepeatedTask() throws Exception {
		ArrayList send;

		synchronized (this) {
			if ( notifs.isEmpty() ) {
				return true;
			}
			send = notifs;
			notifs = new ArrayList();
		}

		QueuedMsg chn;
		long currTime = System.currentTimeMillis();
		boolean needInit = true;

		for (int i = send.size(); i--> 0;) {
			chn = (QueuedMsg) send.get(i);
			if (currTime < chn.nextSendTime) {
				continue;
			}
			try {
				if (needInit) {
					try {
						sendInit();
					} catch (Exception e) {
						// TODO at least log this, but it should no affect the sending
						break; // try the same after a bit of sleep... the send array will be put back to the msg queue
					}
					needInit = false;
				}
				doSendMsg(chn);
				++sentCount;
				send.remove(i);
			} catch (Exception e) {
				e.printStackTrace();
				++failureCount;
				// clever future: the exception should tell if the problem is temporal,
				// or the item should be dropped
				// now do nothing: the item will stay in the "send" array
				if (retryCount > ++chn.sendCount) {
					chn.nextSendTime = currTime + retryWait;
				} else {
					send.remove(i);
					dropMsg(chn, DROPREASON_RETRY_COUNT_EXCEEDED, "");
				}

			}
		}

		if (!needInit) {
			try {
				sendFinished();
			} catch (Exception e) {
				// TODO at least log this, but it should no affect the sending
			}
		}

		int ss = send.size();

		if (0 < ss) {
			synchronized (this) {
				int n = poolSize - notifs.size();

				if (n >= ss) {
					notifs.addAll(0, send);
				} else {
					notifs.ensureCapacity(poolSize);
					for (int i = notifs.size(); i-- > 0;) {
						notifs.set(i + n, notifs.get(i));
					}
					int j = ss;
					for (int i = n; i-- > 0;) {
						notifs.set(i, send.get(--j));
					}
					for (; j-- > 0;) {
						dropMsg((QueuedMsg) send.get(j), DROPREASON_POOL_LIMIT_EXCEEDED, "");
					}
				}

			}
		}

		setMessage("Sent total: " + sentCount + ", failures: " + failureCount);
		
		return true;
	}
}