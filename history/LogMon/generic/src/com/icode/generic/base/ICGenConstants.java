package com.icode.generic.base;

import java.util.Date;


public interface ICGenConstants {
	String DEFAULT_APPRES = "appres";

	String CFG_GEN_ORDER = "!order";
	
	String CFG_GEN_ID = "id";
	String CFG_GEN_NAME = "name";
	String CFG_GEN_TYPE = "type";
	String CFG_GEN_FROM = "from";
	String CFG_GEN_FIELDS = "fields";
	String CFG_GEN_MESSAGES = "messages";
	String CFG_GEN_TEXT = "text";
	String CFG_SEGMENT_SEC = "segmentSec";

	String CFG_GEN_PARSER = "parser";
	String CFG_GEN_PROCESSOR = "processor";

	String CFG_MSG_LEVEL = "level";

	String CFG_OB_DEF = "def";

	String CFG_RES_TEXT = "resText";
	String CFG_CHANNEL_TEXTS = "channelTexts";

	String CFG_COMMANDS = "commands";

	String CFG_SETTINGS = "settings";


	String CFG_MODULE_ID = "moduleId";
	String CFG_RES_ID = "resId";


	String CFG_GEN_TRANSLATOR = "translator";
	
	String CFG_FRAME = "AppFrame";
	String CFG_MAINCLASS = "main";
	String CFG_CMDLINEPROCESSOR = "CmdLineProc";
	
	String CMDEXEC_FAILURE = "ExecFailure";
	
	String SERVLET_PARAM_CMD = "cmd";
	String SERVLET_CMD_PROC = "servletCmdProcessor";
	
	String FRAME_DEFTYPES = "defTypes";
	

	String APPINFO_NAME = "appname";
	String APPINFO_VERSION = "appver";
	String APPINFO_SHORT = "appinfoshort";
	String APPINFO_LONG = "appinfolong";

	String DEFAULT_CONFIGFILE = "config.properties";
	
	String APP_TASK_MANAGER = "TaskManager";
	String APP_LOGGER = "Log";
	String APP_SHELL = "Shell";
	String APP_MONITOR = "Monitor";
	String APP_CMD_EXEC = "CmdExecutor";
	String APP_COMMANDS = "AppCommands";
	String APP_RESOURCES = "Resources";
	String APP_MSG_DISPATCHER = "MsgDispatcher";
	
	int RETURN_SUCCESS = 0;
	
	String EVENT_TYPE_APP = "appevent";
	String EVENT_TYPE_NOTIF = "notification";
	String EVENT_TYPE_LOG = "log";
	
	byte EVENT_LEVEL_FATAL = 0;
	byte EVENT_LEVEL_ERROR = 1;
	byte EVENT_LEVEL_ALERT = 2;
	byte EVENT_LEVEL_WARNING = 3;
	byte EVENT_LEVEL_INFO = 4;
	byte EVENT_LEVEL_DEBUG = 5;
	byte EVENT_LEVEL_DEBUG_FINEST = 6;
	
	byte EVENT_LEVEL_LOWEST = 127;
	
	String EVT_TIME = "evtTime";
	String EVT_ORIGIN = "evtOrigin";
	String EVT_TYPE = "evtType";
	String EVT_LEVEL = "evtLevel";
	
	ICGenObject.ObDef EVENT_BASE_DEF = new ICGenObjectDefDefault("EventBase", 1, new ICGenAttDefDefault[] {
			new ICGenAttDefDefault(EVT_TIME, Date.class), 
			new ICGenAttDefDefault(EVT_ORIGIN), 
			new ICGenAttDefDefault(EVT_TYPE), 
			new ICGenAttDefDefault(EVT_LEVEL, Byte.class)}
	);

	int LOG_FROM = 0;
	int LOG_MESSAGE = 1;
	int LOG_FORMAT = 2;
	int LOG_ATTCOUNT = 3;
	
	ICGenObject.ObDef MSG_DEF = new ICGenObjectDefDefault("MessageDef", 1, new ICGenAttDefDefault[] {			
			new ICGenAttDefDefault(CFG_GEN_ID), 
			new ICGenAttDefDefault(CFG_GEN_TYPE), 
			new ICGenAttDefDefault(CFG_GEN_FROM), 
			new ICGenAttDefDefault(CFG_MSG_LEVEL, Byte.class)}
	);

	ICGenObject.ObDef LOG_DEF = new ICGenObjectDefDefault(EVENT_BASE_DEF, "DefaultLog", 1, new String[] {"from", "msg", "format"});

	String NOTIF_TYPE_MAIL = "email";
	String NOTIF_TYPE_SMS = "sms";
	String NOTIF_TYPE_SKYPE = "skype";

	String MSG_FLD_FROM = "from";
	String MSG_FLD_SUBJECT = "subject";
	String MSG_FLD_CONTENT = "content";

	int NOTIF_SUBJECT = 0;
	int NOTIF_CONTENT = 1;
	int NOTIF_FROM = 2;
	int NOTIF_ATTCOUNT = 3;

	ICGenObject.ObDef NOTIF_DEF = new ICGenObjectDefDefault(EVENT_BASE_DEF, "DefaultNotif", 1, new String[] {"subj", "content", "from"});

	String NOTIF_CHANNEL = "NotifChannel";
	String STATUS_REPORTING = "StatusReporting";

	String ICSHELL_STREAMGOBBLER = "ShellStreamGobbler";
	String ICSHELL_FROM = "shellFROM";
	String ICSHELL_TO = "shellTO";
	String ICSHELL_ERR = "shellERR";
	
	String ICSHELL_CMD_EXIT = "exit";

	String SOCKET_SERVER = "ServerSocket";

	int WAIT_NONE = -1;
	int WAIT_FOREVER = 0;

	long TIME_SECOND = 1000;
	long TIME_MINUTE = 60 * TIME_SECOND;
	long TIME_HOUR = 60 * TIME_MINUTE;
	
	String RCONSOLE_END_OF_ANSWER = "endOfAnswer";
	
	String STATUS_BUFFER = "CyclicBuffer";
	String BUFFER_CFG_SEGCOUNT = "segmentCount";
	String BUFFER_CFG_SEGMSEC = "segmentMsec";
	
	
	int USER_ID = 0;
	int USER_NAME = 1;
	int USER_EMAIL = 2;
	int USER_SMS = 3;
	int USER_REQ_NOTIF_LEVELS = 4;
	int USER_RIGHTS = 5;
	int USER_SKYPE = 6;
	int USER_ATTCOUNT = 7;
	
	ICGenObject.ObDef USER_DEF = new ICGenObjectDefDefault("ICGenUser", 1, new String[] {"id", "name", 
			"email", "sms", "notiflevel", "rights", "skype"});

	
	char REFERENCE_START = '[';
	char REFERENCE_END = ']';

}
