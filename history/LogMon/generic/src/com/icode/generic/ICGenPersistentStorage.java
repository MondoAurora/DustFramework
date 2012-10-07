package com.icode.generic;

import java.util.Enumeration;
import java.util.Map;

import com.icode.generic.base.ICGenObject;
import com.icode.generic.base.ICGenTreeNode;

public interface ICGenPersistentStorage {
	void connectDefault() throws Exception;
	void connect(Map credentials) throws Exception;
	
	Map readCustom(ICGenTreeNode customSearch) throws Exception;
	
	public Enumeration find(ICGenObject.ObDef type, ICGenObject parent, ICGenTreeNode filter, ICGenObject filterParam) throws Exception;
	
	public void store(ICGenObject ob, ICGenObject master) throws Exception;
}
