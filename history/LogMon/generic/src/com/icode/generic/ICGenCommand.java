package com.icode.generic;

import java.io.PrintStream;
import java.text.ParsePosition;
import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICGenCommand implements ICGenConfigurable, ICGenConstants {
	public static final byte PARAM_UNKNOWN = 0;
	public static final byte PARAM_FLAG = 1;
	public static final byte PARAM_WAIT_VALUE = 2;

	public static interface Executor {
		void init();
		boolean handleArgument(String arg) throws Exception;
		boolean handleParamValue(int paramIdx, String value) throws Exception;
		Object execute(PrintStream out) throws Exception;
	}

	public static interface Dispatcher extends Executor {
		void selectSubcommand(int cmdIdx);
	}

	private String[] knownParams;
	private byte[] paramTypes;

	private String[] subCmdNames;
	private Map mapSubCmds;

	// OPTIONAL: a command from which this was derived from
	private ICGenCommand ancestor;
	// OPTIONAL: a command of which this one is a subcommand
	private ICGenCommand parent;
	private Executor exec;

	public Object execute(String cmdLine, PrintStream out) throws Exception {
		ArrayList args = new ArrayList();
		ParsePosition pos = new ParsePosition(0);

		while (pos.getIndex() < cmdLine.length()) {
			String word = ICGenUtils.getWord(cmdLine, ' ', pos);
			args.add(word);
		}

		int ac = args.size();
		String[] argArr = new String[ac];
		if (0 < ac) {
			argArr = (String[]) args.toArray(argArr);
		}

		return execute(argArr, 0, out);

	}

	public Object execute(String[] params, PrintStream out) throws Exception {
		return execute(params, 0, out);
	}

	private byte checkParam(String param, String value) throws Exception {
		byte ret = (null != parent) ? parent.checkParam(param, value) : PARAM_UNKNOWN;

		if (PARAM_UNKNOWN == ret) {
			int pidx = ICGenUtils.indexOf(knownParams, param);
			if (-1 != pidx) {
				ret = paramTypes[pidx];
				if ((null != value) || (ret == PARAM_FLAG)) {
					exec.handleParamValue(pidx, value);
				}
			}
		}

		if ((PARAM_UNKNOWN == ret) && (null != ancestor)){
			ret = ancestor.checkParam(param, value);
		}

		return ret;
	}

	private Object execute(String[] params, int idx, PrintStream out) throws Exception {
		int count = params.length;
		String currParam = null;
		String str;

		if ( null != exec ) {
			exec.init();
		}
		
		if (0 != subCmdNames.length) {
			str = params[idx];
			ICGenCommand scmd = (null == mapSubCmds) ? null : (ICGenCommand) mapSubCmds.get(str);
			
			if (null != scmd) {
				return scmd.execute(params, idx + 1, out);
			} else {
				int cmdIdx = ICGenUtils.indexOf(subCmdNames, str);

				if (-1 == cmdIdx) {
					throw new Exception("Unknown subcommand " + str);
				}

				if (exec instanceof Dispatcher) {
					Dispatcher ed = (Dispatcher) exec;
					ed.selectSubcommand(cmdIdx);
				} else {
					throw new Exception("Unhandled subcommand " + str);
				}

				++idx;
			}
		}
		
		for (int i = idx; i < count; ++i) {
			str = params[i];

			if (null == currParam) {
				if (str.startsWith("-")) {
					str = str.substring(1);
					if (PARAM_WAIT_VALUE == checkParam(str, null)) {
						currParam = str;
					}
				} else {
					exec.handleArgument(str);
				}
			} else {
				checkParam(currParam, str);
				currParam = null;
			}
		}

		return ( null != exec ) ? exec.execute(out) : null;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		int count;
		String cfgStr;

		ICGenTreeNode ex = config.getChild("executor");
		if (null != ex) {
			exec = (Executor) ICAppFrame.getComponent(ex, ICGenCommand.class);
		} else {
			if (null != parent) {
				exec = parent.exec;
			}
		}

		ICGenTreeNode ancestorNode = config.getChild("ancestor");
		if (null != ancestorNode) {
			ancestor = (ICGenCommand) ICAppFrame.getComponent(ancestorNode, ICGenCommand.class);
		}

		cfgStr = config.getOptional("params", null);
		knownParams = ICGenUtils.str2arr(cfgStr, ',');
		count = knownParams.length;
		paramTypes = new byte[count];
		for (int i = 0; i < count; ++i) {
			String p = knownParams[i];
			if (p.startsWith("!")) {
				paramTypes[i] = PARAM_FLAG;
				knownParams[i] = p.substring(1);
			} else {
				paramTypes[i] = PARAM_WAIT_VALUE;
			}
		}

		cfgStr = config.getOptional("subCmdNames", null);
		subCmdNames = ICGenUtils.str2arr(cfgStr, ',');
		count = subCmdNames.length;

		ICGenTreeNode commands = config.getChild("subCommands");
		if (null != commands) {
			mapSubCmds = new HashMap();

			for (int i = 0; i < count; ++i) {
				cfgStr = subCmdNames[i];
				ICGenTreeNode cmd = commands.getChild(cfgStr);
				if (null != cmd) {
					ICGenCommand subCmd = new ICGenCommand();
					subCmd.parent = this;

					subCmd.loadDataFrom(cmd, hint);

					mapSubCmds.put(cfgStr, subCmd);
				}
			}
		}
	}
}
