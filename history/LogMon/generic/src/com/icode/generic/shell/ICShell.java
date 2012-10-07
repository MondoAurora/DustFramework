package com.icode.generic.shell;

import java.io.*;
import java.util.ArrayList;

import com.icode.generic.ICGenLineProcessor;
import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.task.*;

public abstract class ICShell implements ICGenConfigurable, ICGenConstants, ICTask.EndListener {
	protected static final String[] ICSHELL_TO_SINGLE = new String[] { ICSHELL_TO };
	protected static final String[] ICSHELL_FROM_SINGLE = new String[] { ICSHELL_FROM };
	protected static final String[] ICSHELL_TO_ERR = new String[] { ICSHELL_TO, ICSHELL_ERR };
	protected static final String[] ICSHELL_FROM_ERR = new String[] { ICSHELL_FROM, ICSHELL_ERR };

	public static abstract class LineProcessor implements ICGenLineProcessor, ICGenConfigurable {
		private ICShell myShell;
		private final boolean acceptPartial;

		protected LineProcessor(boolean acceptPartial) {
			this.acceptPartial = acceptPartial;
		}

		public final boolean isPartialAccepted() {
			return acceptPartial;
		}

		public Object processLine(String line, boolean partial) throws Exception {
			return acceptPartial ?  processLine(line) : null;
		}
		
		public Object processLine(String line) throws Exception {
			return null;
		}

		protected ICShell getShell() {
			return myShell;
		}
		
		public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		}
	}

	public static final class Redirector extends LineProcessor {
		String prefix;
		PrintStream toStream;

		public Redirector() {
			this(System.out, null);
		}

		public Redirector(String prefix) {
			this(System.out, prefix);
		}

		public Redirector(PrintStream toStream, String prefix) {
			super(false);
			
			this.prefix = prefix;
			this.toStream = toStream;
		}

		public Object processLine(String line) throws Exception {
			getShell().getToShellPrintStream(ICSHELL_TO).println(prefix + ": " + line);
			getShell().endOfAnswer(ICSHELL_TO);
			return null;
		}
	}

	private String[] fromShellStreamNames;
	private String[] toShellStreamNames;
	LineProcessor[] processors;
	boolean defaultToConsole = true;
	boolean started = false;
	private long stopWait = WAIT_NONE;

	private PrintStream[] toShellPrintStreams;
	
	private String name;

	protected ICShell(String[] toShellStreamNames, String[] fromShellStreamNames, boolean defaultToConsole) {
		int len;
		this.defaultToConsole = defaultToConsole;

		len = toShellStreamNames.length;
		this.toShellStreamNames = new String[len];
		if (0 < len) {
			System.arraycopy(toShellStreamNames, 0, this.toShellStreamNames, 0, len);
		} else {
			throw new RuntimeException("FATAL: ICShell toShellStreamNames not provided!");
		}

		len = (null == fromShellStreamNames) ? 0 : fromShellStreamNames.length;
		this.fromShellStreamNames = new String[len];
		if (0 < len) {
			System.arraycopy(fromShellStreamNames, 0, this.fromShellStreamNames, 0, len);
		} else {
			throw new RuntimeException("FATAL: ICShell fromShellStreamNames not provided!");
		}

		this.processors = new LineProcessor[len];
	}
	
	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		stopWait = config.getOptionalLong("stopWait", 2000);
	}


	protected LineProcessor getProcessor(String streamName) {
		return processors[ICGenUtils.indexOf(fromShellStreamNames, streamName)];
	}

	public void setProcessor(LineProcessor processor) {
		setProcessor(fromShellStreamNames[0], processor);
	}

	public void setProcessor(String streamName, LineProcessor processor) {
		if (started) {
			throw new RuntimeException("FATAL ICShell.startShell(): this shell has been started! " + toString());
		}
		processors[ICGenUtils.indexOf(fromShellStreamNames, streamName)] = processor;
		processor.myShell = this;
	}

	public synchronized void startShell() throws Exception {
		if (started) {
			throw new Exception("FATAL ICShell.startShell(): this shell has been started! " + toString());
		}
		
		toShellPrintStreams = new PrintStream[toShellStreamNames.length];

		String name;
		LineProcessor proc;

		if (defaultToConsole) {
			for (int i = fromShellStreamNames.length; i-- > 0;) {
				name = fromShellStreamNames[i];
				proc = processors[i];
				if (null == proc) {
					setProcessor(name, new Redirector(System.out, name));
				}
			}
		}

		initShell();

		ICTaskManager thm = ICAppFrame.getTaskManager();
		ArrayList gobblers = new ArrayList();
		for (int i = fromShellStreamNames.length; i-- > 0;) {
			name = fromShellStreamNames[i];
			if ( !ICGenUtils.isEmpty(this.name) ) {
				name = this.name + " " + name;
			}
			ICTaskStreamReader gobbler = new ICTaskStreamReader(this, ICSHELL_STREAMGOBBLER, name, getFromShellStream(name), processors[i]);
			thm.addTask(gobbler);
			gobblers.add(gobbler);
		}

		thm.startGroup(ICSHELL_STREAMGOBBLER, this, ICTaskManager.WAIT_FOREVER);
		thm.waitForListState(gobblers, ICTaskManager.STATECHECK_RUNNING, ICTaskManager.WAIT_FOREVER);
		
		initConversation();
		
		started = true;
	}

	public synchronized final boolean endShell() throws Exception {
		return endShell(stopWait);
	}

	public synchronized final boolean endShell(long stopWait) throws Exception {
		killShellInt();
		return ICAppFrame.getTaskManager().stopGroup(ICSHELL_STREAMGOBBLER, this, stopWait);
	}

	public int waitFor() throws Exception {
		ICTaskManager thm = ICAppFrame.getTaskManager();
		return thm.waitForGroupFinish(ICSHELL_STREAMGOBBLER, this, ICTaskManager.WAIT_FOREVER) ? 0 : -1;
	}

	public final void endOfAnswer() throws Exception {
		endOfAnswerInt(toShellStreamNames[0]);
	}

	public final void endOfAnswer(String stream) throws Exception {
		endOfAnswerInt(stream);
	}

	public PrintStream getToShellPrintStream()  throws Exception {
		return getToShellPrintStream(toShellStreamNames[0]);
	}

	public PrintStream getToShellPrintStream(String stream)  throws Exception{
		int idx = ICGenUtils.indexOf(toShellStreamNames, stream);
		PrintStream ret = null;
		synchronized (toShellPrintStreams) {
			if (-1 != idx) {
				ret = toShellPrintStreams[idx];
				if (null == ret) {
					ret = new PrintStream(getToShellStream(stream), true);
					toShellPrintStreams[idx] = ret;
				}
			}
		}
		return ret;
	}

	protected abstract void initShell() throws Exception;

	protected void killShellInt() throws Exception {		
	}

	protected void initConversation() throws Exception {		
	}

	protected void streamClosed(String streamName) throws Exception {		
	}

	protected abstract InputStream getFromShellStream(String name) throws Exception;

	protected abstract OutputStream getToShellStream(String name) throws Exception;

	protected abstract void endOfAnswerInt(String stream) throws Exception;

	public final void taskEnded(ICTask task) {
		if (task instanceof ICTaskStreamReader) {
			try {
				streamClosed(((ICTaskStreamReader) task).getName());
			} catch (Exception e) {
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
