package com.icode.generic.obj;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.base.ICGenObject.*;

public class ICGenObjectDefinitionConfigurable implements ICGenObject.ObDef, ICGenConfigurable, ICGenObjConstants {
	protected ICGenObject.ObDef parent;
	protected String name;
	protected long version;

	protected Map mapAtts = new HashMap();
	protected AttDef[] arrDefs;

	public static class DefAttConfig implements ICGenObject.AttDef, ICGenConfigurable {
		protected String name;
		protected Class type;
		protected AttStringConverter cvt;

		public String getName() {
			return name;
		}

		public Class getType() {
			return type;
		}

		public AttStringConverter getConverter() {
			return cvt;
		}

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			name = node.getNameAtt(CFG_NAME);

			String s = node.getOptional(CFG_ATTDEF_CLASS, null);
			type = (null == s) ? String.class : Class.forName(s);

			ICGenTreeNode nCvt = node.getChild(CFG_ATTDEF_STRCVT);
			cvt = (null == nCvt) ? ICGenAttCvtFactory.DEFAULT.getConverter(type) : (AttStringConverter) ICAppFrame.getComponent(nCvt, AttStringConverter.class);
		}
	}

	public AttDef getAtt(int attIdx) {
		return arrDefs[attIdx];
	}

	public int getAttCount() {
		return arrDefs.length;
	}

	public int getAttIdx(String attName) {
		return ((Integer) mapAtts.get(attName)).intValue();
	}

	public String getName() {
		return name;
	}

	public long getVersion() {
		return version;
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		name = node.getNameAtt(CFG_NAME);
		version = node.getOptionalLong(CFG_OBJDEF_VERSION, -1);
		
		ICGenTreeNode n = node.getChild(CFG_OBJDEF_PARENT);

		parent = (ICGenObject.ObDef) ICAppFrame.getComponent(n, ICGenObject.ObDef.class);

		ICGenObject.AttDef ad;
		if (null != parent) {
			for (int i = 0; i < parent.getAttCount(); ++i) {
				ad = parent.getAtt(i);
				mapAtts.put(ad.getName(), ad);
			}
		}

		n = node.getChild(CFG_OBJDEF_ATTRIBUTES);
		if (null != n) {
			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				ad = (ICGenObject.AttDef) ICAppFrame.getComponent(n, ICGenObject.AttDef.class);
				mapAtts.put(ad.getName(), ad);
			}
		}
		
		int count = mapAtts.size();
		arrDefs = new AttDef[count];

		int idx = 0;
		for (Iterator it = mapAtts.values().iterator(); it.hasNext(); ++idx) {
			ad = (ICGenObject.AttDef) it.next();
			arrDefs[idx] = ad;
			mapAtts.put(ad.getName(), new Integer(idx));
		}
	}

	public ObDef getParent() {
		return parent;
	}
}