package com.icode.generic.event;

import java.util.Date;

import com.icode.generic.ICGenLineProcessor;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICEventSourceStream extends ICGenLineProcessor.WholeLine implements ICGenConfigurable, ICEvent.Source, ICGenConstants {
	String name;
	
	ICEvent.Parser parser;
	ICEvent currEvent;
	Object startLock = new Object();

	long startTime = -1;
	long lastMsec = -1;
	public ICEvent.Collector collector;

	long startSkipRows = 0;
	boolean starting = true;

	public Object processLine(String line) throws Exception {
		synchronized (this) {
			try {
				currEvent = parser.parseEvent(line);
			} catch (Exception e) { 
				ICAppFrame.log(EVENT_LEVEL_ERROR, "ICEventSourceStream", line, e);
				return null;
			}
			notifyAll();
		}
		
		if ( -1 == lastMsec ) {
			lastMsec = currEvent.getTimeMsec();
		}

		synchronized (startLock) {
			if (-1 == startTime) {
				startLock.wait();
			}
		}
		
		if (currEvent.getTimeMsec() < startTime) {
			++startSkipRows;
		} else {
			if ( starting ) {
				starting = false;
				ICAppFrame.log(EVENT_LEVEL_DEBUG, "ICEventSourceStream", "skipped " + startSkipRows + " due to start time " 
						+ new Date(startTime) + " first event: " + currEvent, null);
			}
			collector.processEvent(this, currEvent);
		}

		return null;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		parser = (ICEvent.Parser) ICAppFrame.getComponent(config.getChild("parser"), ICEvent.Parser.class);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartTime(long time) {
		synchronized (startLock) {
			this.startTime = time;
			startLock.notify();
		}
	}

	public long getStreamTime() {
		synchronized (this) {
			if ( starting && (null == currEvent)) {
				try {
					wait();
				} catch (InterruptedException e) { }
			}
			return (null == currEvent) ? -1 : currEvent.getTimeMsec();
		}
	}

	public long getLastProcessedMsec() {
		return lastMsec;
	}

	public void setLastProcessedMsec(long ts) {
		lastMsec = ts;
	}
	
	public String toString() {
		return "EvtSrc " + name;
	}
}
