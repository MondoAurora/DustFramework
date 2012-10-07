package com.icode.generic.obj;

import com.icode.generic.base.*;

public class ICGenObjTranslator implements ICGenConfigurable {
	String[] attNames;
	int[] attIndexes;
	int count;
	
	ICGenObject.ObDef lastDef;

	public ICGenObjTranslator() {
	}
	
	public ICGenObjTranslator(ICGenObject.ObDef targetDef) {
		count = targetDef.getAttCount();
		this.attNames = new String[count];
		for ( int i = 0; i < count; ++i ) {
			attNames[i] = targetDef.getAtt(i).getName();
		}
		this.attIndexes = new int[count];
	}
	
	public ICGenObjTranslator(String attName) {
		this(new String[] {attName} );
	}
	
	public ICGenObjTranslator(String[] attNames_) {
		setFields(attNames_);
	}
	
	void setFields(String[] attNames_) {
		count = attNames_.length;
		this.attNames = new String[count];		
		System.arraycopy(attNames_, 0, attNames, 0, count);
		this.attIndexes = new int[count];
	}
	
	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		setFields(ICGenUtilsBase.str2arr(node.getMandatory("fields"), ','));
	}
	
	public void setMapping(ICGenObject.ObDef od) {
		if ( lastDef != od ) {
			for ( int i = 0; i < attNames.length; ++i ) {
				attIndexes[i] = od.getAttIdx(attNames[i]);
			}
			lastDef = od;
		}
	}
	
	private void testMapping(ICGenObject obj) {
		setMapping(obj.getDefinition());
	}
	
	public int getAttCount() {
		return count;
	}
	
	public String getAttName(int idx) {
		return attNames[idx];
	}
	
	public int getRemoteIdx(int idx) {
		return attIndexes[idx];
	}
	
	public int getAttIdx(String name) {
		return ICGenUtilsBase.indexOf(attNames, name);
	}
	
	public void setAttByIdx(ICGenObject obj, int idx, Object value) {
		testMapping(obj);
		obj.setAttribObj(attIndexes[idx], value);
	}
	
	public Object getAttByIdx(ICGenObject obj, int idx) {
		testMapping(obj);
		return obj.getAttribObj(attIndexes[idx]);
	}
	
	public void setAttStrByIdx(ICGenObject obj, int idx, String value) {
		testMapping(obj);
		obj.setAttrib(attIndexes[idx], value);
	}
	
	public String getAttStrByIdx(ICGenObject obj, int idx) {
		testMapping(obj);
		return obj.getAttrib(attIndexes[idx]);
	}
	
	public void loadString(ICGenObject obj, String data, char chField, char chKey, char chEsc) {
		testMapping(obj);
		
		// very simple!!
		String[] fields = ICGenUtilsBase.str2arr(data, chField);
		
		for ( int i = 0; i < count; ++i ) {
			obj.setAttrib(attIndexes[i], fields[i]);
		}
		
	}
	
	public String toString(ICGenObject obj, char chField, char chKey, char chEsc) {
		testMapping(obj);

		StringBuffer buf = new StringBuffer();
		String s;
		for ( int i = 0; i < count; ++i ) {
			if ( 0 < i ) {
				buf.append(chField);
			}
			if ( 0 != chKey ) {
				buf.append(attNames[i]).append(chKey);
			}
			s = obj.getAttrib(attIndexes[i]);
			// escaping...
			buf.append(s);
		}
		
		return buf.toString();
	}
}
