/**
 * 
 */
package com.icode.generic.resolver;

import java.util.Locale;

import com.icode.generic.base.ICGenObject;
import com.icode.generic.obj.ICGenObjTranslator;

public class ICGenResolverValue implements ICGenResolver.Value {
	boolean direct;
	Object resolver;

	int pathLen;
	ICGenObjTranslator[] arrTrPath; // in reverse order!!
	
	byte srcType;

	public ICGenResolverValue(String def) {
		this(def, false);
	}
	
	public ICGenResolverValue(String def, boolean wantString) {
		if (null == def) {
			direct = true;
			resolver = null;
		} else {
			String v = ((String) def).trim();
			char c = v.charAt(0);

			switch (c) {
			case '\'':
				direct = true;
				resolver = v.substring(1, v.length() - 1);
				srcType = SRCTYPE_CONSTANT_STR;
				break;

			case '[':
				ICGenResolver rr = new ICGenResolverReference(v.substring(1, v.length() - 1), wantString);
				srcType = rr.getType();
				resolver = rr;
				break;

			default:
				direct = true;
				resolver = new Long(v);
				srcType = SRCTYPE_CONSTANT_NUM;
				break;
			}
		}
	}

	public byte getType() {
		return srcType;
	}
	
	public Object getResolvedValue(ICGenObject param, Object context, Locale locale) {
		return direct ? resolver : ((ICGenResolver.Value) resolver).getResolvedValue(param, context, locale);
	}

}