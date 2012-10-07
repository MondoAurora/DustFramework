package com.icode.generic.event;

import java.util.Iterator;

import com.icode.generic.ICGenLineProcessor;
import com.icode.generic.file.ICFileDirReader;
import com.icode.generic.file.ICFileReader;
import com.icode.generic.task.ICTask;

public class ICEventDirReader extends ICFileDirReader {
	public ICEvent.Collector collector;

	public void setCollector(ICEvent.Collector coll) {
		this.collector = coll;
	}

	public long initStreamStart() throws Exception {
		refreshFiles();

		long rt = -1;

		for (Iterator it = knownFiles.values().iterator(); it.hasNext();) {
			long t = getSrc(it.next()).getStreamTime();

			if ((-1 != t) && ((-1 == rt) || (t < rt))) {
				rt = t;
			}
		}

		return rt;
	}

	public void setStartTime(long time) {
		super.setStartTime(time);
		
		for (Iterator it = knownFiles.values().iterator(); it.hasNext();) {
			getSrc(it.next()).setStartTime(time);
		}
	}

	protected ICEventSourceStream getSrc(Object fi) {
		return (ICEventSourceStream)((FileReaderInfo)fi).fileReader.getProcessor();
	}
	
	protected ICGenLineProcessor createProcessor(String name) {
		ICEventSourceStream ess = (ICEventSourceStream) super.createProcessor(name);
		ess.collector = collector;
		ess.startTime = getStartTime();
		ess.setName(name);
		collector.addSource(ess);

		return ess;
	}
	
	public void taskEnded(ICTask task) {
		super.taskEnded(task);
		ICEventSourceStream ess = (ICEventSourceStream)((ICFileReader) task).getProcessor();
		collector.removeSource(ess);
	}

}
