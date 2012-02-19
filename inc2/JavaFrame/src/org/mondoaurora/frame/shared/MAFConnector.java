package org.mondoaurora.frame.shared;

public interface MAFConnector {
	MAFIdentifier getObTypeID();
	
	boolean isNull(int idx);
	
	String getString(int idx);
	MAFIdentifier getIdentifier(int idx);
	boolean getBool(int idx, boolean defValue);
	int getInt(int idx, int defValue);
	double getDouble(int idx, double defValue);
	MAFDate getDate(int idx);
	MAFConnector getConnector(int idx);
	Iterable<? extends MAFConnector> getMembers(int idx);
	String getCodeStr(int idx);
	
	void setString(int idx, String val);
	void setIdentifier(int idx, MAFIdentifier val);
	void setBool(int idx, boolean val);
	void setInt(int idx, int val);
	void setDouble(int idx, double val);
	void setDate(int idx, MAFDate val);
	void setConnector(int idx, MAFConnector val);
	void addMember(int idx, MAFConnector member);
	void setCodeStr(int idx, String val);

	boolean send(MAFConnector data, boolean waitMore) throws Exception;
}
