package com.icode.logmonitor;

import java.util.Set;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.app.ICAppMain;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.msg.ICGenMsgDispatcher;
import com.icode.generic.task.ICTask;
import com.icode.generic.task.ICTaskManager;
import com.icode.logmonitor.test.LMTest;

public class LMApp extends ICAppMain.Simple implements ICGenConfigurable, LMConstants {
	
	public int launch() throws Exception {
		ICTaskManager tm = ICAppFrame.getTaskManager();
		
		if (!LMTest.test()) {
			ICAppFrame.shutdown();
			return 0;			
		}
		
		LMConstants.Storage storage = (LMConstants.Storage) ICAppFrame.getComponent(PROP_STORAGE, LMConstants.Storage.class);

		Set admins = storage.getAdmins();
		ICGenMsgDispatcher nd = (ICGenMsgDispatcher) ICAppFrame.getComponent(APP_MSG_DISPATCHER, ICGenMsgDispatcher.class);
		nd.updateListeners(admins);
		
		ICTask inReader = (ICTask) ICAppFrame.getComponent("inReader", null);

		tm.addTask(inReader);
		
		tm.startup();

		tm.waitForGroupFinish(inReader.getGroupName(), null, WAIT_FOREVER);
		ICAppFrame.shutdown();

		return 0;
	}
	
	public static void main(String[] args) {
		ICAppFrame.main("logmonitor", new LMApp(), args);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
	}

}
