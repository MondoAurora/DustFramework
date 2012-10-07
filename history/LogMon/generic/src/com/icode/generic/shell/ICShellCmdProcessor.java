package com.icode.generic.shell;

import com.icode.generic.ICGenCommand;
import com.icode.generic.base.ICGenConstants;

public class ICShellCmdProcessor extends ICShell.LineProcessor implements ICGenConstants {
	ICGenCommand cmd;
	
	public ICShellCmdProcessor(ICGenCommand cmd) {
		super(false);
		this.cmd = cmd;
	}

	public Object processLine(String line) throws Exception {
		ICShell shell = getShell();
		
		Object ret = cmd.execute(line, shell.getToShellPrintStream());
		
		if ( ICSHELL_CMD_EXIT != ret) {
			shell.endOfAnswer(ICSHELL_TO);
		}
		
		return ret;
	}
}
