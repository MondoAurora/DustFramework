package com.icode.generic.app;

import java.io.PrintStream;

import com.icode.generic.ICGenCommand;
import com.icode.generic.base.ICGenConstants;

public class ICAppCommand implements ICGenCommand.Dispatcher, ICGenConstants {
	int cmdIdx;

	public Object execute(PrintStream out) throws Exception {
		Object ret = null;
		
		switch (cmdIdx) {
		case 0:
			ICAppFrame.theFrame.theApp.printAppStatus(out);
			break;
		case 1:
			out.println(ICAppFrame.getString(APPINFO_LONG));
			out.println();
			out.println(ICAppFrame.getTaskManager().getStatus());
			break;
		case 2:
			ICAppFrame.printAppHeader(out);
			out.println(ICAppFrame.getString(APPINFO_LONG));
			// list commands
			break;
		case 3:
			out.println("Shutting down...");
			ICAppFrame.shutdown();
			out.println("Finished.");
			ret = ICSHELL_CMD_EXIT;
			break;
		}

		out.println(RCONSOLE_END_OF_ANSWER);
		
		return ret;
	}

	public boolean handleArgument(String arg) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean handleParamValue(int paramIdx, String value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public void selectSubcommand(int cmdIdx) {
		this.cmdIdx = cmdIdx;
	}

	public void init() {
		cmdIdx = -1;
	}

}
