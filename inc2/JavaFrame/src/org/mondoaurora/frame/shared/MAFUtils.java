package org.mondoaurora.frame.shared;

import java.util.ArrayList;
import java.util.Iterator;

public class MAFUtils {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final int safeCmp(Comparable c, Object o) {
		return (null == c) ? (null == o) ? 0 : -1 : (null == o) ? 1 : c.compareTo(o);
	}

	public static final boolean isEmpty(String s) {
		return ((null == s) || (0 == s.length()));
	}

	public static final boolean isNull(MAFVariant var) {
		return ((null == var) || var.isNull());
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
		} else if (1 == l) {
			Object o = content[0];
			return (null == o) ? "" : o.toString();
		} else {
			StringBuilder sb = null;
			for (Object o : content) {
				if (null == sb) {
					sb = new StringBuilder();
				} else {
					sb.append(separator);
				}
				sb.append(o);
			}
			return sb.toString();
		}
	}

	public static final StringBuilder setSB(StringBuilder buf, String value) {
		if (null == buf) {
			buf = new StringBuilder(value);
		} else {
			buf.replace(0, buf.length(), value);
		}
		return buf;
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

	public static final String escape(String str, char what, char prefix) {
		String ret = str;

		if (-1 != ret.indexOf(prefix)) {
			String p = String.valueOf(prefix);
			ret.replace(p, p + p);
		}

		if (-1 != ret.indexOf(what)) {
			String w = String.valueOf(what);
			ret.replace(w, w + String.valueOf(prefix));
		}

		return ret;
	}
	
	public static class EmptyIter<Target> implements Iterator<Target> {
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Target next() {
			return null;
		}

		@Override
		public void remove() {
		}
	}
	
	public static class SingleIter<Target> implements Iterator<Target> {
		Target ob;
		
		public SingleIter(Target ob) {
			this.ob = ob;
		}

		@Override
		public boolean hasNext() {
			return (null != ob);
		}

		@Override
		public Target next() {
			Target ret = ob;
			ob = null;
			return ret;
		}

		@Override
		public void remove() {
		}
	}
	
	public static final Iterable<? extends MAFConnector> NO_MEMBER = new Iterable<MAFConnector>() {
		Iterator<MAFConnector> it = new EmptyIter<MAFConnector>();
		@Override
		public Iterator<MAFConnector> iterator() {
			return it;
		}
	};	
	
}
