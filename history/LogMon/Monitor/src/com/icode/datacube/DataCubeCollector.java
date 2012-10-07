package com.icode.datacube;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.conv.ICGenConverter;
import com.icode.generic.obj.ICGenObjTranslator;
import com.icode.generic.obj.ICGenObjectDefinitionConfigurable;

public class DataCubeCollector implements ICGenDataManageable, DataCubeConstants {
	public static class ConvAttDef extends ICGenObjectDefinitionConfigurable.DefAttConfig {
		ICGenConverter loader;

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			super.loadDataFrom(node, hint);

			ICGenTreeNode n = node.getChild(CFG_COLLECTOR_ATT_LOADER);
			loader = (null == n) ? new ICGenConverter.LoaderCopy(getName()) : (ICGenConverter) ICAppFrame.getComponent(n, ICGenConverter.class);
		}
	}

	String name;

	long segmentSizeMsec;
	
	ICGenObject.ObDef odCell;
	ICGenObjTranslator trTime;

	TreeMap mapCubes = new TreeMap();

	Long currSegment;
	Long oldestSegment;

	public boolean processObject(ICGenObject obj) {
		boolean newSegment = false;

		Date val = (Date) trTime.getAttByIdx(obj, 0);
		Long timeSeg = new Long(ICGenUtilsBase.getTimeSegment(val.getTime(), segmentSizeMsec));
		
		DataCube cube = (DataCube) mapCubes.get(timeSeg);
		if (null == cube) {
			newSegment = true;
			currSegment = timeSeg;
			if (null == oldestSegment) {
				oldestSegment = currSegment;
			}
			cube = new DataCube(odCell);
			mapCubes.put(timeSeg, cube);
		}

		cube.addCell(obj);

		return newSegment;
	}

	public void dropSegmentBelow(Long segId) {
		if (0 < segId.compareTo(oldestSegment)) {
			Map head = mapCubes.headMap(segId);
			
/*			
			ICGenPersistentStorage db = (ICGenPersistentStorage) ICAppFrame.getComponent("Database", ICGenPersistentStorage.class);
			
			for ( Iterator it = head.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry e = (Map.Entry) it.next();
				try {
					((DataCube)e.getValue()).dump(db, name, (Long)e.getKey(), segmentSizeMsec);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
*/
			head.clear();
			oldestSegment = (Long) mapCubes.firstKey();
		}
	}

	public void storeDataInto(ICGenTreeNode node, Object hint) {
		// TODO Auto-generated method stub

	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		name = node.getNameAtt(CFG_GEN_NAME);
		segmentSizeMsec = node.getOptionalLong(CFG_SEGMENT_SEC, 60) * 1000;

		ICGenTreeNode n;
		n = node.getChild(CFG_COLLECTOR_DATA);
		odCell = (ICGenObject.ObDef) ICAppFrame.getComponent(n, ICGenObject.ObDef.class);

		Set sFlds = new HashSet();

		sFlds.add(ICGenConstants.EVT_TIME);

		String[] ss;
		ICGenConverter loader;

		for (int i = odCell.getAttCount(); i-- > 0;) {
			ConvAttDef ad = (ConvAttDef) odCell.getAtt(i);
			loader = ad.loader;
			if (null == loader) {
				loader = ad.loader = new ICGenConverter.LoaderCopy(ad.getName());
			}
			
			ss = loader.getFldInput();
			for (int j = 0; j < ss.length; ++j) {
				sFlds.add(ss[j]);
			}
		}

		ss = (String[]) sFlds.toArray(new String[sFlds.size()]);
		
		trTime = new ICGenObjTranslator(new String[]{ICGenConstants.EVT_TIME});
	}

	public void dump(Long start, Long end, DataCube targetCube) {
		targetCube.clear();

		Map toProcess = mapCubes.subMap(start, end);
		for (Iterator it = toProcess.values().iterator(); it.hasNext();) {
			((DataCube) it.next()).flushCube(targetCube);
		}
	}

	public Long getCurrSegment() {
		return currSegment;
	}

	public String getName() {
		return name;
	}

	public ICGenObject.ObDef getOdCell() {
		return odCell;
	}
}