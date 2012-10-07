package com.icode.logmonitor;

import java.util.Set;

import com.icode.generic.base.*;

public interface LMConstants extends ICGenConstants {
	String CFG_LM_CUTTIME = "cutTime";
	String CFG_LM_CUTFIELD = "cutField";

	String CFG_SUPERVISOR_COLLECTORS = "collectors";
	String CFG_SUPERVISOR_AGENTS = "agents";

	String CFG_AGENT_WND_SEG_COUNT = "wndSegCount";
	String CFG_AGENT_LINKS = "links";
	String CFG_AGENT_FILTERS = "filters";
	
	String PROP_STORAGE = "Storage";

	String[] MTA_FIELDS = new String[]{"chFrom", "chTo", "status", "from", "to", "fwdTo", 
			"msgid", "authUser", "rest", "ip", "srv", "size", EVT_TIME };
	
	int ATT_CH_FROM = 0;
	int ATT_CH_TO = 1;

	int ATT_STATUS = 2;

	int ATT_FROM = 3;
	int ATT_TO = 4;
	int ATT_FWD_TO = 5;

	int ATT_MSG_ID = 6;
	int ATT_AUTH_USER = 7;

	int ATT_REST = 8;
	int ATT_IP = 9;

	int ATT_SRV = 10;

	int ATT_SIZE = 11;	
	int ATT_TIME = 12;	
		
	String LINETOOSHORT = "Line too short";

	
	public static interface Storage extends ICGenConfigurable {
		long authenticate(String user, String passwd) throws Exception;
		void logout();
		String getAuthUser();
		
		Set getAdmins() throws Exception;

		ICGenTreeNode getStatus() throws Exception;
		void storeStatus(ICGenTreeNode status) throws Exception;
	}

}
