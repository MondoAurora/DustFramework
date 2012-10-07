/**
 * 
 */
package com.icode.generic.notifier;

import com.icode.generic.base.ICGenConstants;
import com.icode.generic.event.ICEventDefault;

public class ICNotification extends ICEventDefault implements ICGenConstants {
	public ICNotification(byte level, String subject, String content) {
		super(NOTIF_DEF, System.currentTimeMillis(), level, EVENT_TYPE_NOTIF);
		
		setAttribObj(NOTIF_SUBJECT, subject);
		setAttribObj(NOTIF_CONTENT, content);
		setAttribObj(NOTIF_FROM, "SpamCatcher");
	}
}