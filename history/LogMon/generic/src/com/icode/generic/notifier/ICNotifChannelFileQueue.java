package com.icode.generic.notifier;

import java.io.*;
import java.util.Iterator;
import java.util.Set;

import com.icode.generic.base.ICGenConstants;
import com.icode.generic.base.ICGenTreeNode;

public class ICNotifChannelFileQueue extends ICNotifChannel implements ICGenConstants {

	protected String tmpDir = "/tmp";
	protected String targetDir = "/queue";

	protected String filePrefix = "queue";
	protected String fileExtension = "item";
	protected int maxCounter = 100;

	protected String charset = "UTF-8";

	protected int counter = 0;

	public ICNotifChannelFileQueue(String type) {
		super(type);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		targetDir = (String) config.getMandatory("targetDir");
		tmpDir = (String) config.getMandatory("tmpDir");
		
		filePrefix = (String) config.getOptional("filePrefix", type + "_queue");
		fileExtension = (String) config.getOptional("fileExtension", type);
		charset = (String) config.getOptional("charset", "UTF-8");

		maxCounter = config.getOptionalInt("maxCounter", 100);

		super.loadDataFrom(config, hint);
	}

	public void addNotification(ICNotification notif, Set targets) {
		for (Iterator it = ((Set) targets).iterator(); it.hasNext();) {
			addChannelNotification(notif, (String) it.next());
		}
	}

	protected void sendInit() throws Exception {

	}

	protected String getFileName() {
		StringBuffer fName = new StringBuffer(filePrefix).append(counter).append(System.currentTimeMillis()).append("conf.").append(fileExtension);

		synchronized (this) {
			++counter;
			if (counter >= maxCounter) {
				counter = 0;
			}
		}
		return fName.toString();
	}

	protected String formatContent(ICNotification notif, Object target) {
		return "";
	}

	protected void sendNotif(ICNotification notif, Object target) throws Exception {
		String fName = getFileName();
		File tmpFile = new File(tmpDir, fName);
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tmpFile), charset);
		writer.write(formatContent(notif, target));
		writer.flush();
		writer.close();

		File finalFile = new File(targetDir, fName);

		if (!tmpFile.renameTo(finalFile)) {
			throw new Exception("Rename failed");
		}
	}

	protected void sendFinished() throws Exception {

	}

}
