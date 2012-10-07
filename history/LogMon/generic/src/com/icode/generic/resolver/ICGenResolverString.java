/**
 * 
 */
package com.icode.generic.resolver;

import java.util.Locale;

import com.icode.generic.base.ICGenObject;

public class ICGenResolverString implements ICGenResolver.Value, ICGenResolver.Writer {
	String str;

	public ICGenResolverString(String str) {
		this.str = str;
	}

	public byte getType() {
		return SRCTYPE_CONSTANT_STR;
	}
	public void write(WriterTarget target, ICGenObject param, Object context, Locale locale) {
		target.write(str);
	}
	
	public Object getResolvedValue(ICGenObject param, Object context, Locale locale) {
		return str;
	}
}