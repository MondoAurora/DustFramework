/**
 * 
 */
package com.icode.generic.task;

import java.io.*;

import com.icode.generic.ICGenLineProcessor;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenTreeNode;

public class ICTaskStreamReader extends ICTask {
	private ICGenLineProcessor processor;

	InputStream is;
	InputStreamReader isr;

	private static final int bufSize = 1000;
	private char[] readBuf = new char[bufSize];
	private int bufPos;
	private StringBuffer line = new StringBuffer();
	private boolean readPending;
	private boolean partial;

	int retryCount;
	long retryWait;
	int retries;

	long lineCount;

	public ICTaskStreamReader(Object owner, String group, String type, InputStream is, ICGenLineProcessor processor) {
		super(owner, group, type);
		this.is = is;
		this.processor = processor;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		retryCount = config.getOptionalInt("retryCount", 0);
		retryWait = config.getOptionalLong("retryWait", 10000);
		processor = (ICGenLineProcessor) ICAppFrame.getComponent(config.getChild("processor"), null);
	}

	protected boolean init() throws Exception {
		retries = 0;
		lineCount = 0;
		return super.init();
	}

	public long getLineCount() {
		return lineCount;
	}

	public void setRetry(int count, long wait) {
		retryCount = count;
		retryWait = wait;
	}

	protected void doTask() throws Exception {
		boolean doRetry;
		do {
			startReading();

			String line;
			while (null != (line = readLineBuf())) {
				retries = 0;
//				log(EVENT_LEVEL_DEBUG_FINEST, lineCount + " read line:", line);
				if (null != processor) {
					processor.processLine(line, partial);
				}
				++lineCount;
				if (0 == (lineCount % 10000)) {
					setMessage("Processed " + lineCount + "lines");
				}
			}

			doRetry = ((++retries) <= retryCount);

			if (stateCheck(STATECHECK_RUNNING)) {
				if (doRetry && (WAIT_NONE != retryWait)) {
					setMessage("Sleeping for " + retryWait + "msec, retries " + retries + "/" + retryCount);
					sleepTask(retryWait);
				}
			}
		} while (stateCheck(STATECHECK_RUNNING) && doRetry);

		setMessage("Finished, processed " + lineCount + "lines");
	}

	/*
	 * replacement for theReader.readLine(): when meeting the top of the stream,
	 * we can reach the end, so readLine returns only a part of the line. We must
	 * always wait for the line break, even it means to keep on reading on a
	 * finished stream until the new segment becomes available
	 */

	protected String getLineFromBuffer() throws Exception {
		int lEnd = 1;
		char c;
		boolean found = false;
		int len = bufPos;
		String ret = null;

		for (bufPos = 0; !found && (bufPos < len); ++bufPos) {
			c = readBuf[bufPos];
			lEnd = bufPos;
			if ('\r' == c) {
				if ('\n' == readBuf[bufPos + 1]) {
					++bufPos;
				}
				found = true;
			} else if ('\n' == c) {
				found = true;
			}
		}
		
		if ( found ) {
			partial = false;
		} else if ( !readPending && isacceptPartial() ) {
			partial = true;
			lEnd = bufPos;
			found = true;
		}

		line.append(readBuf, 0, found ? lEnd : bufPos);
		if (found) {
			ret = line.toString();
			line.delete(0, line.length());
		}

		len -= bufPos;
		if (0 < len) {
			System.arraycopy(readBuf, bufPos, readBuf, 0, len);
			bufPos = len;
		} else {
			bufPos = 0;
		}

		return ret;
	}

	protected String readLineBuf() throws Exception {
		while (stateCheck(STATECHECK_RUNNING)) {
			if (0 < bufPos) {
				String l;
				do {
					l = getLineFromBuffer();
					if (null == l) {
						break;
					} else if ( 0 < l.length() ) {
						return l;
					}
				} while (true);
			}

			try {
				int readCount = isr.read(readBuf, bufPos, bufSize - bufPos);
				readPending = isr.ready();
//				System.out.println("streamReader: [" + new String(readBuf, bufPos, readCount) + "]");
				if (-1 != readCount) {
					bufPos += readCount;
				} else {
					return null;
				}
			} catch (Exception ex) {
				if (stateCheck(STATECHECK_RUNNING)) {
					throw new Exception(ex);
				}
			}
		}

		return null;
	}

	protected void startReading() throws Exception {
		isr = new InputStreamReader(is);
	}

	protected void goingToSleep() {
		try {
			isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	protected void stopRequested() {
		try {
			if (null != is) {
				is.close();
			}
			if (null != isr) {
			// on COM port, this hangs in sun.nio.*.CharsetDecoder close() - removed, hopefully without side effects				
//							isr.close();
						}
		} catch (IOException e) {
		}
	};

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public ICGenLineProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ICGenLineProcessor processor) {
		if (stateCheck(STATECHECK_RUNNING)) {
			throw new RuntimeException("ICTaskStreamReader.setIs - the reader is running");
		}
		this.processor = processor;
	}

	public boolean isacceptPartial() {
		return (null != processor) && processor.isPartialAccepted();
	}

}