/**
 * 
 */
package com.icode.generic.resolver;

import java.util.Locale;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenObject;
import com.icode.generic.base.ICGenUtilsBase;
import com.icode.generic.obj.ICGenObjTranslator;

public class ICGenResolverReference implements ICGenResolver.Value, ICGenResolver.Writer {
	private static final char SEP_PATH = '.';
	private static final char SEP_FIELD = '$';

	private static final String PATHROOT_PREFIXES = ".>/#_";

	boolean wantString;
	byte srcType;

	ICGenResolver.Writer relayResource;

	int pathLen;
	String[] path;

	ICGenObjTranslator tr;

	public ICGenResolverReference(String def, boolean wantString) {
		this.wantString = wantString;
		setDef(def);
	}

	void setDef(String def) {
		if (!ICGenUtilsBase.isEmpty(def)) {
			char c = def.charAt(0);
			if (PATHROOT_PREFIXES.charAt(SRCTYPE_RESOURCE) == c) {
				srcType = SRCTYPE_RESOURCE;
				def = def.substring(1);

				if (def.startsWith("(")) {
					int ei = def.indexOf(')');
					def = def.substring(ei + 1);
				}

				pathLen = 0;
				relayResource = new ICGenResolverResource(def);
				return;

			} else if (PATHROOT_PREFIXES.charAt(SRCTYPE_PERSFIELD) == c) {
				srcType = SRCTYPE_PERSFIELD;
				def = def.substring(1);

				pathLen = 0;
				relayResource = new ICGenResolverString(def);
				return;
			}
		}

		String[] ss = ICGenUtilsBase.str2arr(def, SEP_FIELD);
		int len = ss.length;

		if (1 < len) {
			String fldName = ss[len - 1];
			tr = new ICGenObjTranslator(fldName);
			def = def.substring(0, def.length() - fldName.length() - 1);
		} else {
			tr = null;
		}

		if (ICGenUtilsBase.isEmpty(def)) {
			srcType = SRCTYPE_OBJECT;
			pathLen = 0;
			path = ICGenUtilsBase.EMPTYARR;
		} else {
			path = ICGenUtilsBase.str2arr(def, SEP_PATH);
			pathLen = path.length;

			srcType = (byte) PATHROOT_PREFIXES.indexOf(ss[0].charAt(0));
			path[0] = path[0].substring(1);
		}
	}

	public byte getType() {
		return srcType;
	}
	public Object getResolvedValue(ICGenObject param, Object context, Locale locale) {
		Object retOb = null;
		int pathIdx = 0;

		switch (srcType) {
		case SRCTYPE_OBJECT:
			retOb = param;
			break;
		case SRCTYPE_CONTEXT:
			retOb = context;
			if (ICGenUtilsBase.isEmpty(path[0])) {
				pathIdx = 1;
			}
			break;
		case SRCTYPE_GLOBAL:
			retOb = ICAppFrame.getComponent(path[0], null);
			pathIdx = 1;
			break;
		case SRCTYPE_RESOURCE:
			ICGenResolver.StrWriter sw = new ICGenResolver.StrWriter();
			write(sw, param, context, locale);
			return sw.getContent();
		case SRCTYPE_PERSFIELD:
			return ((ICGenResolver.Value) relayResource).getResolvedValue(param, context, locale);
		}

		for (int i = pathIdx; i < pathLen; ++i) {
			retOb = ((ICGenResolver.PathElement) retOb).getPathElement(path[i]);
		}

		if (null == retOb) {
			return null;
		}

		return (null == tr) ? ((wantString) ? retOb.toString() : retOb) : (wantString) ? tr.getAttStrByIdx((ICGenObject) retOb, 0) : tr.getAttByIdx((ICGenObject) retOb, 0);
	}

	public void write(WriterTarget target, ICGenObject param, Object context, Locale locale) {
		switch (srcType) {
		case SRCTYPE_RESOURCE:
		case SRCTYPE_PERSFIELD:
			relayResource.write(target, param, context, locale);
			break;
		default:
			Object o = getResolvedValue(param, context, locale);
			if (null != o) {
				target.write(o.toString());
			}
			break;
		}
	}
}
