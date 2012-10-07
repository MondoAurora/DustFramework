/**
 * 
 */
package com.icode.logmonitor.test;

import java.util.Date;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenObject;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.db.ICGenObjPersDB;
import com.icode.logmonitor.LMAgent;

public class LMTestDumpAgent extends LMAgent {
	boolean dump;
	ICGenObjPersDB db;

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);
		dump = Boolean.valueOf(node.getOptional("dump", "")).booleanValue();

		db = (ICGenObjPersDB) ICAppFrame.getComponent("Database", ICGenObjPersDB.class);
	}

	public void startWindow(Long start, Long end) {
		if (dump) {
			System.out.println(getName() + " in window " + new Date(start.longValue()) + " - " + new Date(end.longValue()));
		}
	}

	protected void processCell(CollectorLink link, ICGenObject cell) throws Exception {
		if (dump) {
			System.out.println(cell.toFormattedString("{0}"));
		}

		// db.store(cell, null);
	}

	public void endWindow(Long start, Long end) {

	}
}