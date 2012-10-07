package com.icode.generic.base;

import java.util.*;

public class ICGenUtilsBase {
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
	public static final Map EMPTYMAP = Collections.unmodifiableMap(new HashMap(0));

	/**
	 * Finds the index of str in strs, returns -1 if not found. If str is null,
	 * returns -1!!
	 */
	public static final String[] str2arr(String str, char separator) {
		if (isEmpty(str)) {
			return EMPTYARR;
		}
		ArrayList al = new ArrayList();
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

	public static final boolean safeAdd(Collection coll, Object value) {
		return (null == value) ? false : coll.add(value);
	}

	public static final String toString(Set set) {
		return toString(set, ",");
	}

	public static final String toString(Set set, String sep) {
		StringBuffer ret = null;
		if (null != set) {
			for (Iterator it = set.iterator(); it.hasNext();) {
				Object o = it.next();
				if (null != o) {
					if (null == ret) {
						ret = new StringBuffer(o.toString());
					} else {
						ret.append(sep).append(o.toString());
					}
				}
			}
		}
		return (null == ret) ? "" : ret.toString();
	}

	public static final String toString(Map map) {
		return toString(map, ",", ":");
	}

	public static final String toString(Map map, String sep) {
		return toString(map, sep, ":");
	}

	public static final String toString(Map map, String sep, String valDelim) {
		StringBuffer ret = null;
		if (null != map) {
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				Object o = e.getKey();
				if (null != o) {
					if (null == ret) {
						ret = new StringBuffer(o.toString());
					} else {
						ret.append(sep).append(o.toString());
					}
				}
				ret.append(valDelim);
				o = e.getValue();
				if (null != o) {
					ret.append(o.toString());
				}
			}
		}
		return (null == ret) ? "" : ret.toString();
	}
	
	public static final char NODESEP = '|';
	
	public static final ICGenTreeNode strToNode(String content, String[] names, ICGenTreeNode into) {
		return strToNode(content, names, NODESEP, into);
	}
	
	public static final ICGenTreeNode strToNode(String content, String[] names, char sep, ICGenTreeNode into) {
		if ( null == into ) {
			into = new ICGenTreeNode("");
		}
		String[] ss = str2arr(content, sep);
		int c = ss.length > names.length ? names.length : ss.length;
		for ( int i = 0; i < c; ++i ) {
			into.addChild(names[i], ss[i]);
		}

		return into;
	}
	
	public static final StringBuffer nodeToStr(ICGenTreeNode node, String[] names, StringBuffer into) {
		return nodeToStr(node, names, NODESEP, into);
	}
	
	public static final StringBuffer nodeToStr(ICGenTreeNode node, String[] names, char sep, StringBuffer into) {
		if ( null == into ) {
			into = new StringBuffer();
		} else {
			delSB(into);
		}
		for ( int i = 0; i < names.length; ++i ) {
			if ( 0 < i ) {
				into.append(sep);
			}
			into.append(node.getOptional(names[i], ""));
		}
		return into;
	}
	
	public static String getAttByName(ICGenObject ob, String att) {
		int idx = ob.getDefinition().getAttIdx(att);
		return (-1 == idx) ? null : ob.getAttrib(idx);
	}
	
	public static long getTimeSegment(long time, long segmentSize) {
		return ((time+1) / segmentSize) * segmentSize;
	}
}
