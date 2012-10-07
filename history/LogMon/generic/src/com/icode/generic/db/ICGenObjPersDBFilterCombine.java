package com.icode.generic.db;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.db.ICGenObjPersDB.TableProvider;

public class ICGenObjPersDBFilterCombine extends ICGenObjPersDBFilterBase {
	private static final String[] SQL_COMBINE_OPS = new String[]{" OR ", " AND " };

	ICGenObjPersDBFilterBase[] members;

	public ICGenObjPersDBFilterCombine() {
		super(OBDEF_FILTER_COMBINE);
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);

		ICGenTreeNode n = node.getChild(CFG_MEMBERS);
		if (null != n) {
			Map mm = new TreeMap();

			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				mm.put(n.getName(), ICAppFrame.getComponent(n, ICGenObjPersDBFilterBase.class));
			}

			members = (ICGenObjPersDBFilterBase[]) mm.values().toArray(new ICGenObjPersDBFilterBase[mm.size()]);
		}
	}

	protected void buildSQL(StringBuffer sql, ICGenObject param, TableProvider tblProvider) {
		for ( int i = 0; i < members.length; ++i ) {			
			if ( 0 != i ) {
				sql.append(SQL_COMBINE_OPS[getOpCode()]);
			}
			sql.append("( ").append(members[i].getSQL(param, tblProvider)).append(" )");
		}
	}
	
	protected byte resolveOp(String opString) {
		return (byte) ICGenUtilsBase.indexOf(COMBINE_OPS, opString);
	}

}
