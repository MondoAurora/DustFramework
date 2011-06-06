package dust.api.utils;

import java.util.ArrayList;

import dust.api.DustConstants;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;

public class DustUtils implements DustConstants {
	private static DustWorld world = null;
	private static boolean booted = false;

	public static DustWorld getWorld() {
		return world;
	}

	public static void setWorld(DustWorld world_, boolean boot) {
		if ( (null == world ) || ( !booted && !boot) ) {
			world = world_;
			booted = !boot;
		} else {
			throw new IllegalStateException("Invalid call to DustUtils.setAPI().");
		}
	}

	public static DustEntity getEntity(DustDeclId primaryType, DustVariant[] knownFields) throws Exception {
		DustUtilInvokeEntityProcessor iep = new DustUtilInvokeEntityProcessor(true, true);
		return iep.searchOrCreate(primaryType, knownFields, true);
	}
	
	public static String getModuleId(String vendor, String domain, String version) {
		return new StringBuilder(vendor).append(".").append(domain).append(".").append(version).toString();
	}

	public static final boolean isList(FieldType fldType) {
		switch (fldType) {
		case ObArray:
		case ObSet:
		case ObMap:
			return true;
		default:
			return false;
		}
	}

	public static final boolean isGeneric(FieldType fldType) {
		switch (fldType) {
		case ObArray:
		case ObSet:
		case ObMap:
		case ObSingle:
			return false;
		default:
			return true;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final int safeCmp(Comparable c, Object o) {
		return (null == c) ? (null == o) ? 0 : -1 : (null == o) ? 1 : c.compareTo(o);
	}

	public static final boolean isEmpty(String s) {
		return ((null == s) || (0 == s.length()));
	}

	/**
	 * Finds the index of str in strs, returns -1 if not found. If str is null,
	 * returns -1!!
	 */
	public static final int indexOf(String[] strs, String str) {
		if ((null == strs) || (0 == strs.length) || (null == str)) {
			return -1;
		}

		for (int i = strs.length; i-- > 0;) {
			if (str.equals(strs[i])) {
				return i;
			}
		}
		return -1;
	}

	public static final String[] EMPTYARR = new String[0];

	/**
	 * Finds the index of str in strs, returns -1 if not found. If str is null,
	 * returns -1!!
	 */
	public static final String[] str2arr(String str, char separator) {
		if (isEmpty(str)) {
			return EMPTYARR;
		}
		ArrayList<String> al = new ArrayList<String>();
		int sep, pos;
		for (pos = 0, sep = str.indexOf(separator); -1 != sep; sep = str.indexOf(separator, pos = sep + 1)) {
			al.add(str.substring(pos, sep));
		}
		if (pos < str.length()) {
			al.add(str.substring(pos, str.length()));
		}
		int l = al.size();
		if (0 < l) {
			String[] ret = new String[l];
			al.toArray(ret);
			return ret;
		}

		return EMPTYARR;
	}

	public static final String arr2str(Object[] content, char separator) {
		int l;

		if ((null == content) || (0 == (l = content.length))) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0;;) {
			sb.append(content[i++]);
			if (i < l) {
				sb.append(separator);
			} else {
				break;
			}
		}
		return sb.toString();
	}

	public static final void setSB(StringBuffer buf, String value) {
		buf.replace(0, buf.length(), value);
	}

	public static final void delSB(StringBuffer buf) {
		buf.delete(0, buf.length());
	}

	public static final void setSB(StringBuffer buf, StringBuffer value) {
		buf.delete(0, buf.length());
		buf.append(value);
	}

	public static final boolean isEqual(Object s1, Object s2) {
		return (null == s1) ? (null == s2) : (null == s2) ? false : s1.equals(s2);
	}

}
