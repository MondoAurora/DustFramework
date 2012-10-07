package com.icode.generic.log;

public class ICLogChannelConsole extends ICLogChannel {

	protected boolean acceptLog(ICLogMessage msg) {
		return true;
	}

	protected void writeLogLine(String line, Object param) {
		if (param instanceof Exception) {
			System.out.println(line);
			((Exception) param).printStackTrace(System.out);
		} else if ( null != param ){
			System.out.print(line);
			System.out.println(param);			
		} else {
			System.out.println(line);						
		}
	}
	
}
