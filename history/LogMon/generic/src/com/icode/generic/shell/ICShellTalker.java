package com.icode.generic.shell;

import java.io.PrintStream;
import java.util.*;

import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.resolver.ICGenResolver;
import com.icode.generic.resolver.ICGenResolverTemplate;
import com.icode.generic.resolver.ICGenResolver.Value;
import com.icode.generic.workflow.ICGenWorkflow;

public class ICShellTalker implements ICGenConfigurable {
	protected static final byte RESPCODE_NOT_MINE = -1;
	protected static final byte RESPCODE_FINISHED_OK = 0;
	protected static final byte RESPCODE_FINISHED_ERR = 1;
	protected static final byte RESPCODE_OK_WAITMORE = 2;

	class ResponseProcessor extends ICShell.LineProcessor {
		protected ResponseProcessor(boolean acceptPartial) {
			super(acceptPartial);
		}

		public Object processLine(String line, boolean partial) throws Exception {

			synchronized (cmdLock) {
				if (!ICGenUtils.isEmpty(line)) {
					Command cmd = null;
					byte respCode = RESPCODE_NOT_MINE;
					
					System.out.println("in :" + line);


					for (Iterator it = alActiveCmds.iterator(); it.hasNext();) {
						cmd = (Command) it.next();
						respCode = cmd.processResponse(line, partial);
						if (RESPCODE_NOT_MINE != respCode) {
							break;
						} else {
							cmd = null;
						}
					}

					if (null != cmd) {
						String nextCmd = cmd.getCommandToSend();
						if ( null != nextCmd ) {
							doSendCommand(nextCmd);
						}
						
						if (RESPCODE_OK_WAITMORE != respCode) {
							alActiveCmds.remove(cmd);
							cmdLock.notify();
						}
					}
				}
			}
			return null;
		}
	}

	public static abstract class Command implements ICGenConfigurable {
		String id;
		long wait;
		
		ICGenObject param;
		Object context;
		Locale locale;

		Object retObj;

		public final boolean exclusive;

		public Command() {
			this(true);
		}

		protected Command(boolean exclusive) {
			this.exclusive = exclusive;
		}

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			id = node.getNameAtt(CFG_GEN_ID);
		}
		
		void init(ICGenObject param, Object context, Locale locale) {
			this.param = param;
			this.context = context;
			this.locale = locale;
		}

		protected abstract ICGenResolver.Value getResolver();
		protected abstract byte processResponse(String line, boolean partial) throws Exception;

		public String getCommandToSend() {
			ICGenResolver.Value res = getResolver(); 
			return (null == res) ? null : (String) res.getResolvedValue(param, context, locale); 
		}
	}

	public static class CommandSimple extends Command {
		ICGenResolver.Value resolver;
		boolean fired = false;

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			super.loadDataFrom(node, hint);
			resolver = new ICGenResolverTemplate(node.getMandatory(CFG_GEN_TEXT));
		}

		protected byte processResponse(String line, boolean partial) throws Exception {
			return "OK".equals(line) ? RESPCODE_FINISHED_OK : RESPCODE_NOT_MINE;
		}

		public String getCommandToSend() {
			if ( fired ) {
				return null;
			} else {
				fired = true;
				return super.getCommandToSend();
			}
		}

		protected Value getResolver() {
			return resolver;
		}
	}

	public static class CommandWFState extends ICGenWorkflow.State {
		Map mapOutcomes = new HashMap();
		Map mapPrefixes = new HashMap();
		ICGenResolver.Value resolver;

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			super.loadDataFrom(node, hint);

			String tmpl = node.getOptional(CFG_GEN_TEXT, null);
			resolver = (null == tmpl) ? null : new ICGenResolverTemplate(tmpl);

			ICGenTreeNode n = node.getChild("outcomes");
			if ( null != n ) {
				for (Iterator it = n.getChildren(); it.hasNext();) {
					ICGenTreeNode oc = (ICGenTreeNode) it.next();
					String key = oc.getName();
					if ( key.endsWith("*") ) {
						mapPrefixes.put(key.substring(0, key.length()-1), oc.getValue());
					} else {
						mapOutcomes.put(key, oc.getValue());
					}
				}
			}
		}

		protected String processMessage(Object ob) throws Exception {
			String ret = (String) mapOutcomes.get(ob);
			
			if ( null == ret ) {
				for ( Iterator it = mapPrefixes.entrySet().iterator(); it.hasNext(); ) {
					Map.Entry e = (Map.Entry) it.next();
					if ( ((String)ob).startsWith((String)e.getKey()) ) {
						ret = (String) e.getValue();
						break;
					}
				}
			}
			
			return ret;
		}
	}

	public static class CommandStateful extends Command {
		ICGenWorkflow wf;
		
		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			super.loadDataFrom(node, hint);
			wf = (ICGenWorkflow) ICAppFrame.getComponent(node.getChild("workflow"), ICGenWorkflow.class);
		}

		protected byte processResponse(String line, boolean partial) throws Exception {
			return ( null == wf.processMessage(line.trim()) ) ? RESPCODE_NOT_MINE :
				wf.isFinished() ? RESPCODE_FINISHED_OK : RESPCODE_OK_WAITMORE;
		}

		protected Value getResolver() {
			ICGenWorkflow.State st = wf.getState();
			return (st instanceof CommandWFState) ? ((CommandWFState)st).resolver : null;
		}
	}

	static {
		try {
			ICAppFrame.addDefClass(Command.class, CommandSimple.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Object cmdLock = new Object();

	boolean inited = false;
	ICShell shell;
	ResponseProcessor rp;

	ArrayList alActiveCmds;

	Map mapCommandDefs;

	ICGenTreeNode settings;
	String[] initCmds;
	String cmdTerminator;
	boolean partialShell;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		shell = (ICShell) ICAppFrame.getComponent(node.getChild("shell"), ICShell.class);

		ICGenTreeNode n = node.getChild(CFG_COMMANDS);
		if (null != n) {
			mapCommandDefs = new HashMap();

			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				mapCommandDefs.put(n.getName(), n);
			}
		}
		
		cmdTerminator = node.getOptional("cmdTerminator", null);
		partialShell =  Boolean.valueOf(node.getOptional("nonTerminatedLines", "")).booleanValue();

		settings = node.getChild(CFG_SETTINGS);

		initCmds = ICGenUtilsBase.str2arr(node.getOptional("initCmds", ""), ',');
	}

	public synchronized void init() throws Exception {
		if (!inited) {
			rp = new ResponseProcessor(partialShell);
			shell.setProcessor(rp);
			shell.startShell();

			alActiveCmds = new ArrayList();

			inited = true;

			for (int i = 0; i < initCmds.length; ++i) {
				sendCommand(initCmds[i], null, settings);
			}
		}
	}

	protected PrintStream getOutStream() throws Exception {
		return shell.getToShellPrintStream();
	}

	void doSendCommand(String cmdline) throws Exception {
		final PrintStream ps = getOutStream();

		System.out.println("out:" + cmdline);
		
		if (!ICGenUtilsBase.isEmpty(cmdTerminator)) {
			cmdline += cmdTerminator;
		}
		
		ps.write(cmdline.getBytes());
		ps.flush();
	}

	public Object sendCommand(String cmdId, ICGenObject param, ICGenResolver.PathElement info) throws Exception {
		init();

		ICGenTreeNode cmdDef = (ICGenTreeNode) mapCommandDefs.get(cmdId);
		Command cmd = (Command) ICAppFrame.getComponent(cmdDef, Command.class);

		cmd.init(param, info, null);
		
		String strOut = (String) cmd.getCommandToSend();

		synchronized (cmdLock) {
			if (!ICGenUtilsBase.isEmpty(strOut)) {
				alActiveCmds.add(cmd);

				doSendCommand(strOut);

				cmdLock.wait(cmd.wait);
			}
		}

		return cmd.retObj;
	}
}
