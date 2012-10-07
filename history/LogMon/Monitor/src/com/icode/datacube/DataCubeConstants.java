package com.icode.datacube;

import java.util.Date;

import com.icode.generic.base.*;

public interface DataCubeConstants extends ICGenConstants {
	String CFG_COLLECTOR_DATA = "data";
	String CFG_COLLECTOR_ATT_LOADER = "loader";

	String CUBE_SEG_START = "start";
	String CUBE_SEG_END = "end";
	
	ICGenObject.ObDef CUBE_DATA_DEF = new ICGenObjectDefDefault("Cube", 1, new ICGenAttDefDefault[] {
			new ICGenAttDefDefault(CFG_GEN_NAME), 
			new ICGenAttDefDefault(CUBE_SEG_START, Date.class), 
			new ICGenAttDefDefault(CUBE_SEG_END, Date.class)}
	);

	ICGenObject.ObDef BROWSER_WND_DEF = new ICGenObjectDefDefault("BrowserWndDef", 1, new ICGenAttDefDefault[] {
			new ICGenAttDefDefault(CUBE_SEG_START, Date.class), 
			new ICGenAttDefDefault(CUBE_SEG_END, Date.class)}
	);

}
