/**
 * 
 */
package com.icode.generic.shell;

import com.icode.generic.ICGenUtils;
import com.icode.generic.base.ICGenConstants;
import com.icode.generic.shell.ICShell.LineProcessor;

abstract class ICShellCmdProcTrap extends LineProcessor implements ICGenConstants {
	private String[] myCommands;
	private LineProcessor parent;
	
	protected ICShellCmdProcTrap(String[] myCommands, LineProcessor parent) {
		super(false);
		
		this.myCommands = myCommands;
		this.parent = parent;
	}

	public Object processLine(String line, boolean partial) throws Exception {
		int idx = ICGenUtils.indexOf(myCommands, line);
		
		if ( -1 != idx ) {
			return processTrappedCmd(idx);
		} else {
			Object ret = parent.processLine(line, partial);
			if ( ICShellConsole.ICSHELL_CMD_EXIT == ret) {
				getShell().endShell();	
			}
			return ret;
		}
	}
	
	protected abstract Object processTrappedCmd(int cmdIdx) throws Exception; 
	
	public static class Exit extends ICShellCmdProcTrap {
		private static final String[] MCMD = new String[] {ICSHELL_CMD_EXIT};
		protected Exit(LineProcessor parent) {
			super(MCMD, parent);
		}

		protected Object processTrappedCmd(int cmdIdx) throws Exception {
			getShell().endShell();	
			return ICSHELL_CMD_EXIT;
		}	
	}

}