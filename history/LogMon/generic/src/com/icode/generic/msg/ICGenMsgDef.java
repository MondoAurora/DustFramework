package com.icode.generic.msg;

import java.util.*;

import com.icode.generic.base.*;
import com.icode.generic.resource.ICGenResourceRef;

public class ICGenMsgDef implements ICGenConfigurable {
	private String id;
	
	private String type;
	private byte level;
	
	private ICGenResourceRef resDef;	
	private Map mapChannel;

	String from;
	
	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		id = node.getNameAtt(CFG_GEN_ID);
		
		type = node.getOptional(CFG_GEN_TYPE, "");
		level = (byte) node.getOptionalLong(CFG_MSG_LEVEL, EVENT_LEVEL_INFO);
		
		String s = node.getOptional(CFG_RES_TEXT, null);
		resDef = (null == s) ? null : new ICGenResourceRef(s);
		
		ICGenTreeNode n = node.getChild(CFG_CHANNEL_TEXTS);
		if (null != n) {
			mapChannel = new HashMap(n.getChildCount());
			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				mapChannel.put(n.getName(), new ICGenResourceRef(n.getValue()));
			}
		} else {
			mapChannel = ICGenUtilsBase.EMPTYMAP;
		}
	}
	
	public ICGenResourceRef getResRef(String channel) {
		ICGenResourceRef chResRef = (ICGenResourceRef) mapChannel.get(channel);
		return (null == chResRef) ? resDef : chResRef;
	}

	public String getId() {
		return id;
	}

	public String getFrom() {
		return from;
	}

	public String getType() {
		return type;
	}

	public byte getLevel() {
		return level;
	}

	public ICGenResourceRef getResDef() {
		return resDef;
	}


}
