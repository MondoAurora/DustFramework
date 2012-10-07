package com.icode.logmonitor;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.resolver.ICGenResolver;

public class LMAgentFilter extends LMAgent {
	ICGenFilter[] filters;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);

		ICGenTreeNode n = node.getChild(CFG_AGENT_FILTERS);
		if (null != n) {
			ArrayList af = new ArrayList();

			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				af.add(ICAppFrame.getComponent(n, ICGenFilter.class));
			}

			filters = (ICGenFilter[]) af.toArray(new ICGenFilter[af.size()]);
		}
	}

	ICGenResolver.PathElementMap pm = new ICGenResolver.PathElementMap();

	public void startWindow(Long start, Long end) {
		super.startWindow(start, end);
		
		pm.put("twStart", new Date(start.longValue()));
		pm.put("twEnd", new Date(end.longValue()));
		pm.put("name", name);
	}

	protected void processCell(CollectorLink link, ICGenObject cell) throws Exception {
		ICGenFilter f;

		for (int i = 0; i < filters.length; ++i) {
			f = filters[i];
			if (f.isMatching(cell)) {
				pm.put("name", name);
				sendMsg("A", cell, pm);
			}
		}
	}
}
