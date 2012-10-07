package com.icode.generic.resource;

import java.util.Locale;
import java.util.Map;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.resolver.ICGenResolverTemplate;

public class ICGenResourceRef implements ICGenConfigurable {
	
	private static final ICGenResource res = (ICGenResource) ICAppFrame.getComponent(APP_RESOURCES, null);
	
	String module;
	String id;
	
	public ICGenResourceRef() {
		
	}

	public ICGenResourceRef(String defStr) {
		int idx = defStr.indexOf('.');
		init(defStr.substring(0, idx), defStr.substring(idx+1, defStr.length()));
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	void init(String module, String id) {
		this.module = module;
		this.id = id;
	}
	
	public String getFormattedContent(ICGenObject param, Object context, Locale requiredLocale) {
		return resolve(id, requiredLocale, param, context);
	}
	
	public void getFormattedContent(ICGenObject param, Object context, Locale requiredLocale, String[] subNames, Map mapValues) {
		StringBuffer sb = new StringBuffer(id).append('.');
		int l = sb.length();
		
		for ( int i = 0; i < subNames.length; ++i ) {
			sb.append(subNames[i]);
			mapValues.put(subNames[i], resolve(sb.toString(), requiredLocale, param, context));
			sb.delete(l, sb.length());
		}
	}
	
	public ICGenResolverTemplate getResTemplate(Locale requiredLocale) {
		return getResTemplate(id, requiredLocale);
	}
	
	private ICGenResolverTemplate getResTemplate(String optExtendedId, Locale requiredLocale) {
		String templ = res.getResStr(module, optExtendedId, requiredLocale);
		return new ICGenResolverTemplate(templ);		
	}
	
	private String resolve(String id, Locale requiredLocale, ICGenObject param, Object context) {
		ICGenResolverTemplate rt = getResTemplate(id, requiredLocale);
		return (String) rt.getResolvedValue(param, context, requiredLocale);
	}

}
