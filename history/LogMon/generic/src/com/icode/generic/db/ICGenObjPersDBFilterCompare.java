package com.icode.generic.db;

import com.icode.generic.base.*;
import com.icode.generic.db.ICGenObjPersDB.TableProvider;
import com.icode.generic.obj.ICGenObjTranslator;
import com.icode.generic.resolver.ICGenResolver;
import com.icode.generic.resolver.ICGenResolverValue;

public class ICGenObjPersDBFilterCompare extends ICGenObjPersDBFilterBase {
	private static final String[] SQL_COMPARE_OPS =     new String[]{" = ", " <> ", " < ", " <= ", " > ", " >= " };
//	private static final String[] SQL_COMPARE_OPS_REV = new String[]{" = ", " <> ", " >= ", " > ", " <= ", " < " };
	
	
	private ICGenResolver.Value[] cfgObjs;
	
	String value;
	String field;
	String tblAlias;
	
	int fieldIdx;


	public ICGenObjPersDBFilterCompare() {
		super(OBDEF_FILTER_2OP);
	}
	
	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);

		ICGenObjTranslator tr;

		tr = new ICGenObjTranslator(FILTER_2OP_NAMES);
		cfgObjs = new ICGenResolver.Value[FILTER_2OP_NAMES.length];

		for (int i = 0; i < tr.getAttCount(); ++i) {
			cfgObjs[i] = new ICGenResolverValue((String) tr.getAttByIdx(this, i), true);
		}
	}
	
	protected void getCtxValue(ICGenObject param, int valIdx, TableProvider tblProvider) {
		ICGenResolver.Value rv = cfgObjs[valIdx];
		Object value = rv.getResolvedValue(param, null, null);

		switch (rv.getType()) {
		case ICGenResolver.SRCTYPE_CONSTANT_NUM:
			this.value = value.toString();
			break;
		case ICGenResolver.SRCTYPE_PERSFIELD:
			this.field = (String) value;
			if ( null == tblAlias ) {
				tblAlias = tblProvider.getNextAlias() + ".";
			}
			fieldIdx = valIdx;
			break;
		default:
			this.value = new StringBuffer("'").append(value).append("'").toString();
			break;
		}

	}


	protected byte resolveOp(String opString) {
		return (byte) ICGenUtilsBase.indexOf(CMP_OPS, opString);
	}

	protected void buildSQL(StringBuffer sql, ICGenObject param, TableProvider tblProvider) {
		getCtxValue(param, OP2_LVALUE, tblProvider);
		getCtxValue(param, OP2_RVALUE, tblProvider);
		
		sql.append("(").append(tblAlias).append("data_key = '").append(field).append("') and (");
		if ( 0 == fieldIdx ) {
			sql.append(tblAlias).append("data_value").append(SQL_COMPARE_OPS[getOpCode()]).append(value);
		} else {
			sql.append(value).append(SQL_COMPARE_OPS[getOpCode()]).append(tblAlias).append("data_value");			
		}
		sql.append(") ");
	}
}
