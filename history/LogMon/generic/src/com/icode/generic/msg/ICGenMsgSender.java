package com.icode.generic.msg;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICGenMsgSender implements ICGenConfigurable {
	static final ICGenMsgDispatcher dispatcher = (ICGenMsgDispatcher) ICAppFrame.getComponent(APP_MSG_DISPATCHER, null);

	Map mapMsgDefs;
	String from;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		from = node.getValue();
		mapMsgDefs = new HashMap(node.getChildCount());
		
		for (Iterator it = node.getChildren(); it.hasNext();) {
			ICGenTreeNode n = (ICGenTreeNode) it.next();
			ICGenMsgDef def = (ICGenMsgDef) ICAppFrame.getComponent(n, ICGenMsgDef.class);
			def.from = from;
			mapMsgDefs.put(n.getName(), def);
		}
	}

	public boolean isMsgListened(String msgId) {
		return dispatcher.isMsgListened((ICGenMsgDef) mapMsgDefs.get(msgId));
	}

	public void sendMsg(String msgId, ICGenObject param, Object context) {
		ICGenMsgDef def = (ICGenMsgDef) mapMsgDefs.get(msgId);
		ICGenMsg msg = new ICGenMsg(def, (null == param) ? null : param.replicate(), context);
		dispatcher.dispatchMsg(msg);
	}

}
