package com.icode.generic.filter;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;

public class ICGenFilterCombine extends ICGenFilterBase {
	ICGenFilter[] members;

	public ICGenFilterCombine() {
		super(ICGenUtilsBase.EMPTYARR);
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);
		
		ICGenTreeNode n = node.getChild(CFG_MEMBERS);
		if ( null != n ) {
			Map mm = new TreeMap();
			
			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				mm.put(n.getName(), ICAppFrame.getComponent(n, ICGenFilter.class));
			}

			members = (ICGenFilter[]) mm.values().toArray(new ICGenFilter[mm.size()]);
		}
	}

	public boolean isMatchingInt(ICGenObject object) {
		boolean ret = COMBINE_OP_AND == getOpCode();
		
		boolean val;
		for ( int i = 0; i < members.length; ++i ) {
			val = members[i].isMatching(object);
			
			if ( val != ret ) {
				return val;
			}
		}
		
		return ret;
	}

	protected byte resolveOp(String opString) {
		return (byte) ICGenUtilsBase.indexOf(COMBINE_OPS, opString);
	}
}
