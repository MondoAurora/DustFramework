package com.icode.generic.resolver;

import java.util.Locale;

import com.icode.generic.base.ICGenConstants;
import com.icode.generic.base.ICGenObject;
import com.icode.generic.resource.ICGenResourceRef;

public class ICGenResolverResource implements ICGenResolver.Writer, ICGenConstants{
	ICGenResourceRef resRef;

	public ICGenResolverResource(String def) {
		resRef = new ICGenResourceRef(def);
	}

	public byte getType() {
		return SRCTYPE_RESOURCE;
	}
	
	public void write(WriterTarget target, ICGenObject param, Object context, Locale locale) {
		ICGenResolverTemplate rt = resRef.getResTemplate(locale);
		
		rt.write(target, param, context, locale);
	}

}
