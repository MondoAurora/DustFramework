package com.icode.generic.msg;

import java.text.SimpleDateFormat;
import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.file.ICFileQueue;
import com.icode.generic.resolver.ICGenResolver;
import com.icode.generic.resolver.ICGenResolverTemplate;
import com.icode.generic.resource.ICGenResourceRef;
import com.icode.generic.shell.ICShellTalker;

public class ICGenMsgChannelSmsGSM extends ICGenMsgChannel {
	private static final String[] SMS_FIELDS = new String[]{MSG_FLD_FROM, MSG_FLD_SUBJECT, MSG_FLD_CONTENT};

	SimpleDateFormat smsDateFormat;
	
	ICGenResolver.Value resContent;
	
	ICShellTalker modem;

	public ICGenMsgChannelSmsGSM() {
		super(NOTIF_TYPE_SMS);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		String s = (String) config.getOptional("smsDateFormat", "yyyy MMM dd. HH:mm:ss");
		smsDateFormat = new SimpleDateFormat(s);
		
		s = (String) config.getOptional(CFG_GEN_TEXT, null);
		resContent = (null == s) ? null : new ICGenResolverTemplate(s); 

		modem = (ICShellTalker) ICAppFrame.getComponent(config.getChild("modem"), ICFileQueue.class);
		
		super.loadDataFrom(config, hint);
	}

	protected boolean init() throws Exception {
		modem.init();
		
		return super.init();
	}

	public void transmitMsg(ICGenMsg notif, Set targets) {
		for (Iterator it = ((Set) targets).iterator(); it.hasNext();) {
			enqueueMsg(new QueuedMsgSingle(notif, (ICGenMsgListener) it.next()));
		}
	}

	protected void doSendMsg(QueuedMsg queuedMsg) throws Exception {
		QueuedMsgSingle qms = (QueuedMsgSingle) queuedMsg;		
		ICGenResourceRef rref = qms.msg.def.getResRef(type);
		
		ICGenResolver.PathElementMap pm = new ICGenResolver.PathElementMap();
		
		pm.put("date", smsDateFormat.format(qms.msg.dateCreate));
		pm.put("to", qms.listener.getChannelAddress(type));
		
		Locale locale = qms.listener.getPreferredLocale();
		rref.getFormattedContent(qms.msg.param, qms.msg.context, locale, SMS_FIELDS, pm);
		
		String text = (String) resContent.getResolvedValue(null, pm, locale);
		
		pm.clear();
		pm.put("phoneNumber", "+" + qms.listener.getChannelAddress(type));
		pm.put("smsText", text);
		
		modem.sendCommand("sms", null, pm);
	}
}
