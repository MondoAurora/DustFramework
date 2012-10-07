package com.icode.generic.msg;

import java.util.*;

import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.resource.ICGenResourceRef;
import com.skype.Chat;
import com.skype.Skype;

public class ICGenMsgChannelSkype extends ICGenMsgChannel {

	public ICGenMsgChannelSkype() {
		super(NOTIF_TYPE_SKYPE);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
	}

	public void transmitMsg(ICGenMsg notif, Set targets) {
		for (Iterator it = ((Set) targets).iterator(); it.hasNext();) {
			enqueueMsg(new QueuedMsgSingle(notif, (ICGenMsgListener) it.next()));
		}
	}

	protected void doSendMsg(QueuedMsg queuedMsg) throws Exception {
		QueuedMsgSingle qms = (QueuedMsgSingle) queuedMsg;		
		ICGenResourceRef rref = qms.msg.def.getResRef(type);
				
		Locale locale = qms.listener.getPreferredLocale();
		String msg = rref.getFormattedContent(qms.msg.param, qms.msg.context, locale);
		
		Chat c = Skype.chat(qms.listener.getChannelAddress(type));
		c.send(msg);
	}
}
