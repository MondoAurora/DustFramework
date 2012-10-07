package com.icode.generic.log;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.icode.generic.base.*;
import com.icode.generic.event.ICEvent;

public abstract class ICLogChannel implements ICGenConfigurable, ICGenConstants {
		SimpleDateFormat dateFormat;
		String lineFormat;
		
		public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
			dateFormat = new SimpleDateFormat((String)config.getOptional("dateFormat", "yyyy-MM-dd HH:mm:ss.SS"));
			lineFormat = (String)config.getOptional("lineFormat", "{0} [{1}] {2} {3}");
		}

		void processLog(ICLogMessage msg) {
			if ( acceptLog(msg) ) {
				synchronized (dateFormat) {
					String logLine = MessageFormat.format(lineFormat, new Object[] {
							dateFormat.format(new Date(msg.getTimeMsec())),
							msg.getAttrib(LOG_FROM),
							ICEvent.Util.levelToString(msg.getLevel()),
							msg.getAttrib(LOG_MESSAGE)
					});
					writeLogLine(logLine, msg.getParam());
				}
			}
		}
		
		protected abstract boolean acceptLog(ICLogMessage msg);	
		protected abstract void writeLogLine(String line, Object param);	

}
