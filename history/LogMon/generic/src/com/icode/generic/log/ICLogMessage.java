package com.icode.generic.log;

import java.text.MessageFormat;

import com.icode.generic.base.ICGenConstants;
import com.icode.generic.event.ICEventDefault;

public class ICLogMessage extends ICEventDefault implements ICGenConstants {
	private Object param;
	
	public ICLogMessage(byte level, String from, String message, Object param) {
		super(LOG_DEF, System.currentTimeMillis(), level, EVENT_TYPE_LOG);
		
		setAttribObj(LOG_FROM, from);
		setAttribObj(LOG_MESSAGE, message);
		setAttribObj(LOG_FORMAT, "[{0}] - {1}");
		
		setParam(param);
	}


	protected ICLogMessage(long timestamp, byte level, int attCount) {
		super(LOG_DEF, timestamp, level, EVENT_TYPE_LOG);
	}


	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}
	
	public String toString() {
		return MessageFormat.format(getAttrib(LOG_FORMAT), new Object[]{getAttrib(LOG_FROM), getAttrib(LOG_MESSAGE)});
	}

	Object[] getAttribs() {
		return getAtts();
	}
}
