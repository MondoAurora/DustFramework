package com.icode.logmonitor.test;

import java.util.*;

import com.icode.datacube.*;
import com.icode.datacube.DataCube.ContentEnumerator;
import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.ICGenPersistentStorage;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;
import com.icode.generic.resolver.ICGenResolver;
import com.icode.generic.resource.ICGenResource;
import com.icode.generic.shell.ICShellTalker;
import com.icode.logmonitor.LMConstants;
import com.skype.Chat;
import com.skype.Skype;

public class LMTest implements LMConstants, DataCubeConstants {
	void resTest(String id, Locale l) {
		final ICGenResource res = (ICGenResource) ICAppFrame.getComponent(APP_RESOURCES, ICGenResource.class);
		System.out.println("Resource string " + id + " from locale " + l + " is: " + res.getResStr("LogMon", id, l));
	}

	public void testResource() throws Exception {
		Locale l = Locale.getDefault();
		System.out.println("Default locale: " + l);
		
		String[] msgs = new String[]{"testmsg", "testmsg1", "testmsg2"};
		Locale[] ls = new Locale[]{Locale.US, Locale.ENGLISH, Locale.FRENCH, Locale.CANADA_FRENCH, Locale.FRANCE, Locale.CANADA};
		
		ICGenResource res = (ICGenResource) ICAppFrame.getComponent(APP_RESOURCES, ICGenResource.class);
		int cnt = 10000000;
		long s = System.currentTimeMillis();
		for ( int i = 0; i < cnt; ++i ) {
			res.getResStr("LogMon", msgs[i%msgs.length], ls[i%ls.length]);
		}
		s = System.currentTimeMillis() - s;
		System.out.println("Searching res " + cnt + " times took " + s + " msecs.");
		
		resTest("testmsg", l);
		resTest("testmsg", Locale.US);
		resTest("testmsg", Locale.ENGLISH);
		resTest("testmsg", Locale.FRENCH);
		resTest("testmsg", Locale.CANADA_FRENCH);
		resTest("testmsg1", Locale.US);
		resTest("testmsg1", Locale.ENGLISH);
		resTest("testmsg2", Locale.US);
		resTest("testmsg2", Locale.ENGLISH);
	}

	public boolean testModem() throws Exception {
		ICShellTalker m = (ICShellTalker) ICAppFrame.getComponent("GSMModem", null);
		
		ICGenResolver.PathElementMap info = new ICGenResolver.PathElementMap();
		info.put("phoneNumber", "+36703120668");
		info.put("smsText", "This is a test message!");
		
		m.sendCommand("sms", null, info);
		
		info.put("smsText", "This is a test message TWO!");
		
		m.sendCommand("sms", null, info);
		
		return false;
	}

	public boolean testSkype() throws Exception {
		Chat c = Skype.chat("kedves.lorand.test");
		c.send("test message...");
		
		return false;
	}

	public boolean testCubeBrowser() throws Exception {
		DataCubeBrowserDB cubeBrowser = (DataCubeBrowserDB) ICAppFrame.getComponent("CubeBrowser", null);
		
		cubeBrowser.init();
		
		ICGenObject wndData = new ICGenObjectDefault(BROWSER_WND_DEF);

		wndData.setAttrib(0, "2010.01.12 02:57:00.00");
		wndData.setAttrib(1, "2010.01.12 03:17:00.00");
		
		cubeBrowser.loadInterval(wndData);
		
		String[] names = new String[]{"status", "sumSize", "count"};
		ICGenObject.ObDef odCell = new ICGenObjectDefDefault(cubeBrowser.getCellDef(), names);
		ICGenObjTranslator trCell = new ICGenObjTranslator(odCell);

		Map result = cubeBrowser.getData(odCell, null);

		for ( Iterator it = result.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry e = (Map.Entry) it.next();
			System.out.println(e.getKey() + " ------------------------------- ");
			
			DataCube cube = ((DataCube)e.getValue());
			
			for ( Enumeration content = cube.enumContent(); content.hasMoreElements(); ) {
				System.out.println("   " + trCell.toString((ICGenObject) content.nextElement(), ',', '=', '\\'));
			}
		}

		return false;
	}

	public boolean testDB() throws Exception {
		ICGenTreeNode wnd = ICAppFrame.getConfig("testTimeWindow");
		ICGenObject.ObDef cellDef = (ICGenObject.ObDef) ICAppFrame.getComponent(wnd.getChild("cellDef"), null);
		
		ICGenObjTranslator trCell = new ICGenObjTranslator(cellDef);
		ICGenObjTranslator trSegment = new ICGenObjTranslator(CUBE_DATA_DEF);
		
		ICGenTreeNode filter = wnd.getChild("filter");
		
		ICGenPersistentStorage db = (ICGenPersistentStorage) ICAppFrame.getComponent("Database", ICGenPersistentStorage.class);

		for ( Enumeration segments = db.find(CUBE_DATA_DEF, null, filter, null); segments.hasMoreElements(); ) {
			ICGenObject segOb = (ICGenObject) segments.nextElement();
			System.out.println(trSegment.toString(segOb, ',', '=', '\\'));
			
			for ( Enumeration content = db.find(cellDef, segOb, null, null); content.hasMoreElements(); ) {
				ICGenObject cell = (ICGenObject) content.nextElement();
				
				System.out.println("    " + trCell.toString(cell, ',', '=', '\\'));
			}
			
			System.out.println("-------");
			
		}
		
		return false;
	}
	

	
	public static boolean test() throws Exception {
		if ( false ) {
			return true;
		}
		
		return new LMTest().testCubeBrowser();
	}
}
