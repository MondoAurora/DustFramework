package com.icode.logmonitor;

import java.util.*;

import com.icode.datacube.DataCubeCollector;
import com.icode.datacube.DataCube;
import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.msg.ICGenMsgSender;
import com.icode.generic.obj.ICGenObjTranslator;

public abstract class LMAgent implements ICGenDataManageable, LMConstants {
	public static class CollectorLink implements ICGenDataManageable, LMConstants {
		String collName;
		DataCubeCollector coll;

		ICGenObjTranslator trCell;
		String[] names;
		
		boolean checkPartial;

		DataCube collCube;

		public void storeDataInto(ICGenTreeNode node, Object hint) {
			// TODO Auto-generated method stub

		}
		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			collName = node.getNameAtt(CFG_GEN_NAME);
			names = ICGenUtilsBase.str2arr(node.getMandatory(CFG_GEN_FIELDS), ',');
			trCell = new ICGenObjTranslator(names);
			checkPartial = Boolean.valueOf(node.getOptional("checkPartial", "")).booleanValue();
		}

		final void init(DataCubeCollector coll) {
			this.coll = coll;

			ICGenObject.ObDef odCell = new ICGenObjectDefDefault(coll.getOdCell(), names);
			collCube = new DataCube(odCell);
		}

		public String getCollName() {
			return collName;
		}
		public ICGenObjTranslator getTrCell() {
			return trCell;
		}
		public boolean isCheckPartial() {
			return checkPartial;
		}
	}

	String name;

	private long segmentMSec;
	private long windowLengthMSec;
	private CollectorLink[] links;

	
	ICGenMsgSender sender;
	
	private ICGenObject status, log;

	public long getWindowLengthMSec() {
		return windowLengthMSec;
	}

	public long getSegmentMSec() {
		return segmentMSec;
	}

	public int getLinkCount() {
		return links.length;
	}

	public CollectorLink getLink(int idx) {
		return links[idx];
	}

	public void storeDataInto(ICGenTreeNode node, Object hint) {
		// TODO Auto-generated method stub
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		name = node.getNameAtt(CFG_GEN_NAME);
		segmentMSec = node.getOptionalLong(CFG_SEGMENT_SEC, 60) * 1000;
		windowLengthMSec = segmentMSec * node.getOptionalLong(CFG_AGENT_WND_SEG_COUNT, 15);


		ICGenTreeNode n = node.getChild(CFG_AGENT_LINKS);

		ArrayList arr = new ArrayList();
		if (null != n) {
			CollectorLink cl;

			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				cl = (CollectorLink) ICAppFrame.getComponent(n, CollectorLink.class);
				arr.add(cl);
			}
		}
		links = (CollectorLink[]) arr.toArray(new CollectorLink[arr.size()]);

		n = node.getChild(CFG_GEN_MESSAGES);
		
		if ( null != n ) {
			sender = new ICGenMsgSender();
			sender.loadDataFrom(n, hint);
		}
		
		ICGenObject.ObDef od = (ICGenObject.ObDef) ICAppFrame.getComponent(node.getChild("odStatus"), ICGenObject.ObDef.class);
		status = (null == od) ? null : new ICGenObjectDefault(od);
		
		od = (ICGenObject.ObDef) ICAppFrame.getComponent(node.getChild("odLog"), ICGenObject.ObDef.class);
		log = (null == od) ? null : new ICGenObjectDefault(od);
	}

	protected ICGenObject getStatus() {
		return status;
	}

	protected ICGenObject getLog() {
		return log;
	}

	final void process(Long start, Long end) throws Exception {
		startWindow(start, end);

		CollectorLink cLink;

		for (int i = 0; i < getLinkCount(); ++i) {
			cLink = getLink(i);
			cLink.coll.dump(start, end, cLink.collCube);
			startCollector(cLink);

			for (Enumeration enCells = cLink.collCube.enumContent(); enCells.hasMoreElements();) {
				processCell(cLink, (ICGenObject) enCells.nextElement());
			}

			endCollector(cLink);
		}

		endWindow(start, end);
	}

	public void startWindow(Long start, Long end) {
	}
	public void startCollector(CollectorLink link) {
	}
	public void endCollector(CollectorLink link) {
	}

	final protected void sendMsg(String msgId, ICGenObject param, Object context) {
		sender.sendMsg(msgId, param, context);
	}

	protected abstract void processCell(CollectorLink link, ICGenObject cell) throws Exception;

	public void endWindow(Long start, Long end) {
	}

	public String getName() {
		return name;
	}
}
