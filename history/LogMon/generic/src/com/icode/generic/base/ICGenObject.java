package com.icode.generic.base;

public interface ICGenObject {
	public interface AttStringConverter {
		String attToString(Object attValue);
		Object attFromString(String str);
		Object deepCopy(Object attValue);
	}
		
	public interface AttDef {
		String getName();
		Class getType();
		AttStringConverter getConverter();
	}
	
	public interface ObDef {
		ObDef getParent();
		
		String getName();
		long getVersion();
		
		int getAttCount();
		AttDef getAtt(int attIdx);
		int getAttIdx(String attName);
	}
	
	ObDef getDefinition();
	
	Object getAttribObj(int attIdx);
	String getAttrib(int attIdx);
	void setAttribObj(int attIdx, Object value);
	void setAttrib(int attIdx, String value);
	void reset();
	
	void setPersKey(long key);
	long getPersKey();
	
	String toFormattedString(String pattern);
	
	ICGenObject replicate();	
	
	public interface Processor {
		Object processObject(ICGenObject ob) throws Exception;
	}

	public interface Parser {
		ICGenObject parseObject(String line) throws Exception;
	}

}
