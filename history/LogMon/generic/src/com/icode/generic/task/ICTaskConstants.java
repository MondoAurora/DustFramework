package com.icode.generic.task;

import com.icode.generic.base.ICGenConstants;

public interface ICTaskConstants extends ICGenConstants {
	int STATUS_CREATED = 1;
	int STATUS_INIT_RUNNING = 2;
	int STATUS_RUNNING = 3;
	int STATUS_STOP_REQUESTED = 4;

	int STATUS_DONE_SUCCESS = 0;
	int STATUS_DONE_FAILURE = -1;
	int STATUS_DONE_STOPPED = -2;
	int STATUS_INIT_FAILED = -3;
	
	int STATUS_MSGOFFSET = 3; 
	
	String[] STAT_MESSAGES = new String[] {"Init failure", "Stopped", "Failed", "Succeeded", "Created", "Initialized", "Running", "Stop requested", };
	
	byte STATECHECK_RUNNING = 0;
	byte STATECHECK_FINISHED = 1;
	byte STATECHECK_INIT_OR_RUN = 2;
	byte STATECHECK_CREATED = 3;
	byte STATECHECK_STARTED = 4;
}
