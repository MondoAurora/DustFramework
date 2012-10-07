package com.icode.generic.db;

import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.base.ICGenObject;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.db.ICGenObjPersDB.TableProvider;
import com.icode.generic.filter.ICGenFilterConstants;
import com.icode.generic.obj.ICGenObjTranslator;

public abstract class ICGenObjPersDBFilterBase extends ICGenObjectDefault implements ICGenObjPersDB.FilterBuilder, ICGenFilterConstants {
	private boolean negated = false;
	private byte opCode = OP_INVALID;


	protected ICGenObjPersDBFilterBase(ICGenObject.ObDef obDef) {
		super(obDef);
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);

		ICGenObjTranslator tr;
		tr = new ICGenObjTranslator(FILTER_BASE_NAMES);
		String s;

		s = (String) tr.getAttByIdx(this, BASE_OP);
		opCode = resolveOp(s);

		s = (String) tr.getAttByIdx(this, BASE_NOT);
		negated = Boolean.valueOf(s).booleanValue();
	}

	protected byte getOpCode() {
		return opCode;
	}

	public String getSQL(ICGenObject param, TableProvider tblProvider) {
		StringBuffer sb = new StringBuffer();

		if (negated) {
			sb.append("NOT (");
		}

		buildSQL(sb, param, tblProvider);

		if (negated) {
			sb.append(")");
		}

		return sb.toString();
	}

	protected abstract void buildSQL(StringBuffer sql, ICGenObject param, TableProvider tblProvider);
	protected abstract byte resolveOp(String opString);
}
