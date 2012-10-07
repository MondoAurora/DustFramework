package com.icode.generic.msg;

import java.util.Date;

import com.icode.generic.base.ICGenObject;

public class ICGenMsg {
	Date dateCreate;
	
	ICGenMsgDef def;
	ICGenObject param;
	Object context;
	
	public ICGenMsg(ICGenMsgDef def, ICGenObject param, Object context) {
		dateCreate = new Date(System.currentTimeMillis());
		
		this.def = def;
		this.param = param;
		this.context = context;
	}
	
	
}
