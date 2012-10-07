package com.icode.generic.obj;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICGenObjStringCvt implements ICGenConfigurable, ICGenConstants {
	char chSepField;
	char chEscape;
	char chSepKey;
	
	boolean includeKeys;
	
	String[] fields;
	ICGenObjTranslator tr;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		String str;		
		tr = (ICGenObjTranslator) ICAppFrame.getComponent(node.getChild(CFG_GEN_TRANSLATOR), ICGenObjTranslator.class);
		
		str = node.getOptional("includeKeys", null);
		includeKeys = (null == str) ? false : Boolean.valueOf(str).booleanValue();
		
		str = node.getOptional("chSepField", "|");
		chSepField = str.trim().charAt(0);
		str = node.getOptional("chEscape", "\\");
		chEscape = str.trim().charAt(0);
		str = node.getOptional("chSepKey", ":");
		chSepKey = str.trim().charAt(0);
	}
	
	public String toString(ICGenObject obj) {
		return tr.toString(obj, chSepField, includeKeys ? chSepKey : 0, chEscape);
	}
	
	public void fromString(ICGenObject obj, String data) {
		tr.loadString(obj, data, chSepField, includeKeys ? chSepKey : 0, chEscape);
	}
}
