package com.icode.datacube;

import java.text.MessageFormat;
import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.db.ICGenObjPersDB;
import com.icode.generic.db.ICGenObjPersDBConstants;
import com.icode.generic.obj.ICGenObjTranslator;

public class DataCubeBrowserDB implements ICGenConfigurable, DataCubeConstants, ICGenObjPersDBConstants {
	String startField;
	String limitWhere;
	String selectFormat;
	
	ICGenTreeNode dataFilter;
	
	ICGenObject.ObDef cellDef;
	
	ICGenObjPersDB db;
	
	Object timeMin, timeMax;
	
	Map mapCubes = new HashMap();

//	ICGenObject wndData = new ICGenObjectDefault(BROWSER_WND_DEF);
	
	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		selectFormat = node.getOptional("selectFormat", "select min({0}) as timeMin, max({0}) as timeMax from rec_data d, rec_head h where {1}");
		startField = node.getOptional("startField", "str_to_date(data_value, '%Y.%m.%d %H:%i:%s.%f')");
		limitWhere = node.getOptional("limitWhere", "data_key = 'start' and d.rec_id = h.rec_id and h.rec_type = 'Cube'");
		
		cellDef = (ICGenObject.ObDef) ICAppFrame.getComponent(node.getChild("cellDef"), null);

		dataFilter = node.getChild("filter");

		db = (ICGenObjPersDB) ICAppFrame.getComponent("Database", ICGenObjPersDB.class);
	}
	
	public void init() throws Exception {		
		db.connectDefault();
		
		refreshLimits();
	}
	
	public void refreshLimits() throws Exception {
		String select = MessageFormat.format(selectFormat, new String[] {startField, limitWhere});
		
		ICGenTreeNode customSearch = new ICGenTreeNode("search");
		customSearch.addChild(CUSTOM_SELECT, select);
		
		Map limits = db.readCustom(customSearch);
		
		timeMin = limits.get("timeMin");
		timeMax = limits.get("timeMax");
	}
	
	public int loadInterval(ICGenObject wndData) throws Exception {		
		ICGenObjTranslator trSegment = new ICGenObjTranslator(CUBE_DATA_DEF);
		
		mapCubes.clear();
		
		int startIdx = trSegment.getAttIdx(CUBE_SEG_START);

		for ( Enumeration segments = db.find(CUBE_DATA_DEF, null, dataFilter, wndData); segments.hasMoreElements(); ) {
			ICGenObject segOb = (ICGenObject) segments.nextElement();
			
			DataCube cube = new DataCube(cellDef);
			cube.dbLoad(db, segOb);
			
			mapCubes.put(trSegment.getAttByIdx(segOb, startIdx), cube);			
		}
		
		return mapCubes.size();
	}

	public Map getData(ICGenObject.ObDef odCell, Map targetMap) {
		if ( null == targetMap ) {
			targetMap = new TreeMap();
		} else {
			targetMap.clear();
		}
		
		DataCube cube;
		
		for ( Iterator it = mapCubes.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry e = (Map.Entry) it.next();
			
			cube = new DataCube(odCell);
			((DataCube)e.getValue()).flushCube(cube);
			
			targetMap.put(e.getKey(), cube);
		}
		
		return targetMap;
	}

	public ICGenObject.ObDef getCellDef() {
		return cellDef;
	}
}
