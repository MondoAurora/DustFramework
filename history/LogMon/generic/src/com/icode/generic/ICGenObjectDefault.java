package com.icode.generic;

import java.text.MessageFormat;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICGenObjectDefault implements ICGenObject, ICGenDataManageable, Cloneable, ICGenConstants {
	long persKey = -1;
	
	private ObDef def;
	
	private Object[] atts;
	
	public ICGenObjectDefault() {
	}

	public ICGenObjectDefault(ObDef def) {
		setDef(def);
	}

	void setDef(ObDef def) {
		this.def = def;
		atts = new Object[def.getAttCount()];
	}

	public ObDef getDefinition() {
		return def;
	}
	
	protected Object[] getAtts() {
		return atts;
	}

	public Object getAttribObj(int attIdx) {
		return atts[attIdx];
	}

	public String getAttrib(int attIdx) {
		Object o = atts[attIdx];
		if ((null == o) || (o instanceof String)) {
			return (String) o;
		} else {
			return getDefinition().getAtt(attIdx).getConverter().attToString(o);
		}
	}

	public void setAttribObj(int attIdx, Object value) {
		atts[attIdx] = value;
	}

	public void setAttrib(int attIdx, String value) {
		if ( null != value ) {
			AttDef ad = getDefinition().getAtt(attIdx);
			if ( String.class != ad.getType() ) {
				atts[attIdx] = ad.getConverter().attFromString(value);
				return;
			}
		}
		atts[attIdx] = value;
	}

	public void reset() {
		for (int i = atts.length; i-- > 0;) {
			atts[i] = null;
		}
	}

	public void set(String[] values) {
		int c = (atts.length > values.length) ? values.length : atts.length;
		
		for (int i = c; i-- > 0;) {
			atts[i] = values[i];
		}
	}

	public String toFormattedString(String pattern) {
		return MessageFormat.format(pattern, atts);
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		String val;
		AttDef ad;
		
		ICGenTreeNode n = node.getChild(CFG_OB_DEF);
		if ( null != n ) {
			setDef((ICGenObject.ObDef) ICAppFrame.getComponent(n, ICGenObject.ObDef.class));
		}
		
		for ( int i = def.getAttCount(); i-->0; ) {
			ad = def.getAtt(i);
			val = node.getOptional(ad.getName(), null);
			if ( null != val ) {
				atts[i] = ad.getConverter().attFromString(val);
			}
		}
	}

	public void storeDataInto(ICGenTreeNode node, Object hint) {
		AttDef ad;
		
		for ( int i = def.getAttCount(); i-->0; ) {
			if ( null != atts[i] ) {
				ad = def.getAtt(i);
				node.addChild(ad.getName(), ad.getConverter().attToString(atts[i]));
			}
		}		
	}

	public long getPersKey() {
		return persKey;
	}

	public void setPersKey(long key) {
		this.persKey = key;
	}

	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		ICGenObjectDefault ret = (ICGenObjectDefault) super.clone();
		ret.atts = new Object[def.getAttCount()];
		for ( int i = 0; i < def.getAttCount(); ++i ) {
			ret.atts[i] = def.getAtt(i).getConverter().deepCopy(atts[i]);
		}
		return ret;
	}

	public ICGenObject replicate(){
		try {
			return (ICGenObject) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
}
