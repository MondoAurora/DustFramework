package com.icode.generic.event;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICEventCyclicBuffer implements ICEvent.Collector, ICGenConfigurable, ICGenDataManageable, ICGenConstants {

	public static interface SegmentedProcessor {
		Object addToSegment(long time, Object segment, ICEvent info);

		Object closeSegment(long time, Object segment);
	};

	private int segmentCount;
	private long segmentMsec;

	long currTime = -1;
	int wndIdx = 0;
	long count = 0;
	long skipped = 0;

	private Object[][] window;
	private Object segmentLock = new Object();

	private SegmentedProcessor[] processors;
	private Set procSet = new HashSet();

	Set setSources = new HashSet();
	long latestTimestamp = -1;

	public ICEventCyclicBuffer() {
	}

	public long getEventTime(ICEvent info) {
		return ((info.getTimeMsec()+1) / segmentMsec) * segmentMsec;
	}

	public void init(long startTime) {
		this.currTime = startTime;
		this.latestTimestamp = startTime;

		processors = (SegmentedProcessor[]) procSet.toArray(new SegmentedProcessor[procSet.size()]);

		window = new Object[segmentCount][processors.length];
	}

	public void readyToStart() {
		currTime = getLatestTS();
	}

	public boolean addProcessor(SegmentedProcessor proc) {
		return procSet.add(proc);
	}

	private void updateSourceTimes() {
		synchronized (setSources) {
			latestTimestamp = -1;
			for (Iterator it = setSources.iterator(); it.hasNext();) {
				long ft = ((ICEvent.Source) it.next()).getLastProcessedMsec();
				if ((-1 != ft) && ((-1 == latestTimestamp) || (ft < latestTimestamp))) {
					latestTimestamp = ft;
				}
			}
			setSources.notifyAll();
		}
	}

	/*
	 * public void setLatestTS(long ts) { synchronized (setSources) {
	 * latestTimestamp = ts; } }
	 */
	public long getLatestTS() {
		synchronized (setSources) {
			return latestTimestamp;
		}
	}

	public void addSource(ICEvent.Source source) {
		synchronized (setSources) {
//			System.out.println("CyclicBuffer: addSource: " + source.toString());
			setSources.add(source);
			source.setLastProcessedMsec(-1);
			updateSourceTimes();
		}
	}

	public void removeSource(ICEvent.Source source) {
		synchronized (setSources) {
			setSources.remove(source);
			updateSourceTimes();
		}
	}

	public boolean processEvent(ICEvent.Source source, ICEvent info) throws Exception {
		++count;
		boolean ret = true;

		long time = getEventTime(info);
		long diff = 0;

		if (source.getLastProcessedMsec() != time) {
			source.setLastProcessedMsec(time);
			updateSourceTimes();
		}

		diff = (time - currTime) / segmentMsec;

		if (-diff >= segmentCount) {
			++skipped;
			log(EVENT_LEVEL_WARNING, info.toString(), source.toString());
			return false; // quick exit on too old event
		}

		if (0 < diff) {
			long targetSegTime = time - (segmentCount * segmentMsec);

			synchronized (setSources) {
				if (getLatestTS() <= targetSegTime) {
//					System.out.println(source + " Waiting for segment available " + new Date(targetSegTime));
					while (getLatestTS() <= targetSegTime) {
						setSources.wait();
					}
//					System.out.println(source + " WOKE UP after segment became available " + new Date(targetSegTime));
				}
			}

			synchronized (segmentLock) {
				diff = (time - currTime) / segmentMsec;
				if (0 < diff) {
					Object[] segment;
					Object pseg;
					SegmentedProcessor proc;
					long segmentTime;

					int toClose = (int) ((diff > segmentCount) ? (segmentCount + 1) : diff + 1);

					for (int i = 1; i < toClose; ++i) {
						int idx = (wndIdx + i) % segmentCount;
						segment = window[idx];
						segmentTime = currTime + (i - segmentCount) * segmentMsec;

//						System.out.println(source + " Closing segment " + new Date(segmentTime));

						for (int j = processors.length; j-- > 0;) {
							pseg = segment[j];
							if (null != pseg) {
								proc = processors[j];
								synchronized (proc) {
									segment[j] = proc.closeSegment(segmentTime, pseg);
								}
							}
						}
					}
				}
				currTime = time;
				wndIdx = (int) (wndIdx + diff) % segmentCount;
				while (wndIdx < 0) {
					wndIdx += segmentCount;
				}
			}
		}

		Object[] segment = window[wndIdx];
		SegmentedProcessor proc;
		for (int i = processors.length; i-- > 0;) {
			proc = processors[i];
			synchronized (proc) {
				segment[i] = proc.addToSegment(time, segment[i], info);
			}
		}

		return ret;
	}

	public synchronized String toString() {
		StringBuffer sb = new StringBuffer("added: ").append(count).append(", skipped: ").append(skipped);
		for (int i = processors.length; i-- > 0;) {
			SegmentedProcessor proc = processors[i];
			synchronized (proc) {
				sb.append("\n").append(proc);
			}
		}
		return sb.toString();
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		segmentCount = config.getOptionalInt("segmentCount", 15);
		segmentMsec = config.getOptionalLong("segmentMsec", TIME_MINUTE);
	}

	public void storeDataInto(ICGenTreeNode node, Object hint) {
		node.addChild(BUFFER_CFG_SEGCOUNT, String.valueOf(segmentCount));
		node.addChild(BUFFER_CFG_SEGMSEC, String.valueOf(segmentMsec));
	}

	public void log(byte level, String message, Object param) {
		ICAppFrame.log(level, getClass().getName(), message, param);
	}
}
