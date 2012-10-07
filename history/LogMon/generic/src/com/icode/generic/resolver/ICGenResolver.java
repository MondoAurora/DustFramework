package com.icode.generic.resolver;

import java.util.HashMap;
import java.util.Locale;

import com.icode.generic.base.ICGenObject;

/**
 * The resolver is responsible for loading its properties that lock it to a certain value.
 * The initialized resolver instance must be able to return an Object based on the configuration,
 * the given path (context root) and the current object.
 * 
 * @author Kedves Loránd
 *
 */
public interface ICGenResolver {
	byte SRCTYPE_OBJECT = 0;
	byte SRCTYPE_CONTEXT = 1;
	byte SRCTYPE_GLOBAL = 2;
	byte SRCTYPE_RESOURCE = 3;
	byte SRCTYPE_PERSFIELD = 4;
	
	byte SRCTYPE_CONSTANT_STR = 5;
	byte SRCTYPE_CONSTANT_NUM = 6;

	byte getType();
	
	public interface PathElement {
		Object getPathElement(String name);
	}
	
	public static class PathElementMap extends HashMap implements PathElement {
		private static final long serialVersionUID = 1L;

		public Object getPathElement(String name) {
			return get(name);
		}
	}
	
	public interface Value extends ICGenResolver {
		Object getResolvedValue(ICGenObject param, Object context, Locale locale);
	}

	public interface WriterTarget {
		void write(String str);
	}

	public interface Writer extends ICGenResolver {
		void write(WriterTarget target, ICGenObject param, Object context, Locale locale);
	}
	
	public static class StrWriter implements WriterTarget {
		StringBuffer sb;
		
		public StrWriter() {
			sb = new StringBuffer();
		}
		
		public StrWriter(int initSize) {
			sb = new StringBuffer(initSize);
		}
		
		public void write(String str) {
			sb.append(str);
		}
		
		public String getContent() {
			return sb.toString();
		}
	}

}