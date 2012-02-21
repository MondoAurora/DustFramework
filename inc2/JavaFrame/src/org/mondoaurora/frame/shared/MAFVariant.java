package org.mondoaurora.frame.shared;


public interface MAFVariant {
	enum VariantSetMode { set, addFirst, addLast, insert, remove, clear };

	String getKey();
	boolean isNull();
	
	String getString();
	MAFIdentifier getIdentifier();
	boolean getBool(boolean defValue);
	int getInt(int defValue);
	double getDouble(double defValue);
	MAFDate getDate();
	String getCodeStr();
	
	void setString(String val);
	void setIdentifier(MAFIdentifier val);
	void setBool(boolean val);
	void setInt(int val);
	void setDouble( double val);
	void setDate(MAFDate val);
	void setReference(MAFConnector val);
	void setCodeStr(String val);
	
	MAFConnector getReference(String[] fields);
	void getReference(MAFConnector conn);
	Iterable<? extends MAFVariant> getMembers();

	void setData(MAFVariant from);
	void setData(Object value, VariantSetMode mode, String key);
}
