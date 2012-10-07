package com.icode.generic.resolver;

import java.util.ArrayList;
import java.util.Locale;

import com.icode.generic.base.*;

public class ICGenResolverTemplate implements ICGenResolver.Value, ICGenResolver.Writer, ICGenConstants {

	ArrayList alContent = new ArrayList();

	public ICGenResolverTemplate(String def) {
		setTemplateStr(def);
	}

	void setTemplateStr(String strTemplate) {
		int sep, pos, start;
		StringBuffer sb = new StringBuffer();

		for (pos = 0, sep = strTemplate.indexOf(REFERENCE_START); -1 != sep; sep = strTemplate.indexOf(REFERENCE_START, pos = sep + 1)) {
			sb.append(strTemplate.substring(pos, sep));

			start = sep + 1;
			if (strTemplate.length() > start) {
				if (REFERENCE_START != strTemplate.charAt(start)) {
					if (0 < sb.length()) {
						addContentStr(sb.toString());
						ICGenUtilsBase.delSB(sb);
					}
					sep = strTemplate.indexOf(REFERENCE_END, start);

					alContent.add(new ICGenResolverReference(strTemplate.substring(start, sep), true));
				} else {
					++pos;
				}
			}
		}

		if (pos < strTemplate.length()) {
			addContentStr(strTemplate.substring(pos, strTemplate.length()));
		}
	}

	private void addContentStr(String str) {
		alContent.add(new ICGenResolverString(str));
	}
	
	public byte getType() {
		return SRCTYPE_RESOURCE;
	}

	public void write(WriterTarget target, ICGenObject param, Object context, Locale locale) {
		for (int i = 0; i < alContent.size(); ++i) {
			((ICGenResolver.Writer) alContent.get(i)).write(target, param, context, locale);
		}
	}

	public Object getResolvedValue(ICGenObject param, Object context, Locale locale) {
		ICGenResolver.StrWriter sw = new ICGenResolver.StrWriter();
		write(sw, param, context, locale);
		return sw.getContent();
	}

}
