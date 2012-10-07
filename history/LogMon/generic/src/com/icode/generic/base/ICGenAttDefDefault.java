package com.icode.generic.base;

import com.icode.generic.base.ICGenObject.AttStringConverter;


public class ICGenAttDefDefault implements ICGenObject.AttDef {
	String name;
	Class type;
	AttStringConverter cvt;
	
	public ICGenAttDefDefault(String name, Class type, AttStringConverter cvt) {
		this.name = name;
		this.type = type;
		this.cvt = cvt;
	}

	public ICGenAttDefDefault(String name, Class type) {
		this(name, type, ICGenAttCvtFactory.DEFAULT.getConverter(type));
	}

	public ICGenAttDefDefault(String name) {
		this(name, String.class);
	}

	public String getName() {
		return name;
	}

	public Class getType() {
		return type;
	}

	public AttStringConverter getConverter() {
		return cvt;
	}
}
