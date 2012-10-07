package com.icode.generic.notifier;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.file.ICFileQueue;

public class ICNotifChannelSms extends ICNotifChannel {
	SimpleDateFormat smsDateFormat;
	String smsContentFormat;
	
	ICFileQueue fileQueue;

	public ICNotifChannelSms() {
		super(NOTIF_TYPE_SMS);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		smsContentFormat = (String) config.getMandatory("smsContentFormat");
		String df = (String) config.getOptional("smsDateFormat", "dd.MM.yyyy HH:mm:ss");
		smsDateFormat = new SimpleDateFormat(df);

		fileQueue = (ICFileQueue) ICAppFrame.getComponent(config.getChild("fileQueue"), ICFileQueue.class);
		
		super.loadDataFrom(config, hint);
	}

	protected String formatContent(ICNotification notif, Object target) {
		String[] content = new String[] {
				smsDateFormat.format(new Date(notif.getTimeMsec())), 
				(String) target,
				notif.getAttrib(NOTIF_FROM),
				notif.getAttrib(NOTIF_SUBJECT),
				notif.getAttrib(NOTIF_CONTENT)
		};
		
		return MessageFormat.format(smsContentFormat, content);
	}

	public void addNotification(ICNotification notif, Set targets) {
		for (Iterator it = ((Set) targets).iterator(); it.hasNext();) {
			addChannelNotification(notif, (String) it.next());
		}
	}

	protected void sendNotif(ICNotification notif, Object target) throws Exception {
		fileQueue.toQueue(formatContent(notif, target));
	}

}
