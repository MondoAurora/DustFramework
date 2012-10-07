package com.icode.generic.base;

import com.icode.generic.base.ICGenObject.AttDef;
import com.icode.generic.base.ICGenObject.ObDef;


public class ICGenObjectDefDefault implements ICGenObject.ObDef {
	public final ObDef parent;
	
	public final String name;
	public final long version;
	
	private final AttDef[] attDefs;
	private final String[] attNames;

	public ICGenObjectDefDefault(ICGenObject.ObDef src, String[] attNames_) {
		this.parent = src.getParent();
		this.name = src.getName();
		this.version = src.getVersion();
		
		int count = attNames_.length;
		this.attNames = new String[count];
		System.arraycopy(attNames_, 0, this.attNames, 0, count);
		
		this.attDefs = new AttDef[count];
		for ( int i = 0; i < count; ++i ) {
			attDefs[i] = src.getAtt(src.getAttIdx(attNames[i]));
		}
	}
	
	public ICGenObjectDefDefault(String name, long version, String[] attNames_) {
		this(null, name, version, attNames_);
	}
	
	public ICGenObjectDefDefault(ObDef parent, String name, long version, String[] attNames_) {
		this.parent = parent;
		this.name = name;
		this.version = version;
		
		int pCount = (null == parent) ? 0 : parent.getAttCount();
		int lCount = attNames_.length;
		int count = lCount + pCount;
		
		this.attNames = new String[count];
		for ( int i = 0; i < pCount; ++i ) {
			attNames[i] = parent.getAtt(i).getName();
		}
		System.arraycopy(attNames_, 0, this.attNames, pCount, lCount);
		
		this.attDefs = new AttDef[count];
		for ( int i = 0; i < count; ++i ) {
			attDefs[i] = new ICGenAttDefDefault(attNames[i]);
		}
	}

	public ICGenObjectDefDefault(String name, long version, AttDef[] attDefs_) {
		this(null, name, version, attDefs_);
	}

	public ICGenObjectDefDefault(ObDef parent, String name, long version, AttDef[] attDefs_) {
		this.parent = parent;
		this.name = name;
		this.version = version;

		int pCount = (null == parent) ? 0 : parent.getAttCount();
		int lCount = attDefs_.length;
		int count = lCount + pCount;

		this.attDefs = new AttDef[count];
		for ( int i = 0; i < pCount; ++i ) {
			attDefs[i] = parent.getAtt(i);
		}
		System.arraycopy(attDefs_, 0, this.attDefs, pCount, lCount);
		
		this.attNames = new String[count];
		for ( int i = 0; i < count; ++i ) {
			attNames[i] = attDefs[i].getName();
		}
	}

	public int getAttIdx(String attName) {
		return ICGenUtilsBase.indexOf(attNames, attName);
	}

	public AttDef getAtt(int attIdx) {
		return attDefs[attIdx];
	}

	public int getAttCount() {
		return attDefs.length;
	}

	public String getName() {
		return name;
	}

	public long getVersion() {
		return version;
	}

	public ObDef getParent() {
		return parent;
	}
}
