package com.icode.generic.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.icode.generic.base.ICGenObject.AttStringConverter;

public class ICGenAttCvtFactory {
	protected final Map mapClassCvt = new HashMap();
	Locale DEF_DATE_LOCALE = Locale.ENGLISH;
	String DEF_DATE_PATTERN = "yyyy.MM.dd HH:mm:ss.SS";

	public static final ICGenAttCvtFactory DEFAULT = new ICGenAttCvtFactory();

	public ICGenObject.AttStringConverter getConverter(Class attClass) {
		return (ICGenObject.AttStringConverter) mapClassCvt.get(attClass);
	}

	public ICGenAttCvtFactory() {
		mapClassCvt.put(String.class, new AttStringConverter() {
			public Object attFromString(String str) {
				return str;
			}

			public String attToString(Object attValue) {
				return (String) attValue;
			}

			public Object deepCopy(Object attValue) {
				return attValue;
			}
		});

		mapClassCvt.put(Boolean.class, new GenericConverter() {
			public Object attFromString(String str) {
				return Boolean.valueOf(str);
			}
		});

		mapClassCvt.put(Long.class, new GenericConverter() {
			public Object attFromString(String str) {
				return Long.valueOf(str);
			}
		});

		mapClassCvt.put(Double.class, new GenericConverter() {
			public Object attFromString(String str) {
				return Double.valueOf(str);
			}
		});
		mapClassCvt.put(Date.class, new DefaultDateConverter());
		mapClassCvt.put(Set.class, new DefaultSetConverter());
	}

	public abstract class GenericConverter implements ICGenObject.AttStringConverter {
		public String attToString(Object attValue) {
			return attValue.toString();
		}
		public Object deepCopy(Object attValue) {
			return attValue;
		}
	}

	public class DefaultDateConverter implements ICGenObject.AttStringConverter {
		SimpleDateFormat DF = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SS", Locale.ENGLISH);

		public String attToString(Object attValue) {
			return DF.format((Date) attValue);
		}

		public Object attFromString(String str) {
			try {
				return DF.parse(str);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		public Object deepCopy(Object attValue) {
			return (null == attValue) ? null : new Date(((Date)attValue).getTime());
		}
	}

	public class DefaultSetConverter implements ICGenObject.AttStringConverter {
		public String attToString(Object attValue) {
			Set s = (Set) attValue;
			StringBuffer b = new StringBuffer("{");

			for (Iterator it = s.iterator(); it.hasNext();) {
				b.append(it.next()).append(it.hasNext() ? "," : "}");
			}
			return b.toString();
		}

		public Object attFromString(String str) {
			str = str.substring(str.indexOf('{'), str.lastIndexOf('}'));
			String[] ss = ICGenUtilsBase.str2arr(str, ',');
			Set ret = new HashSet();
			for (int i = 0; i < ss.length; ++i) {
				ret.add(ss[i]);
			}
			return ret;
		}

		public Object deepCopy(Object attValue) {
			return (null == attValue) ? null : new HashSet(((Set)attValue));
		}
	}
}
