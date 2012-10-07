package com.icode.generic.file;

import java.io.*;

import com.icode.generic.base.*;

public class ICFileQueue implements ICGenConfigurable, ICGenConstants {
	protected String tmpDir = "/tmp";
	protected String targetDir = "/queue";

	protected String filePrefix = "queue";
	protected String fileExtension = "item";
	protected int maxCounter = 100;

	protected String charset = "UTF-8";

	protected int counter = 0;

	public ICFileQueue() {
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		targetDir = (String) config.getMandatory("targetDir");
		tmpDir = (String) config.getMandatory("tmpDir");
		
		filePrefix = (String) config.getMandatory("filePrefix");
		fileExtension = (String) config.getMandatory("fileExtension");
		charset = (String) config.getOptional("charset", "UTF-8");

		maxCounter = config.getOptionalInt("maxCounter", 100);
	}

	protected String getFileName() {
		synchronized (this) {
			StringBuffer fName = new StringBuffer(filePrefix).append(counter).append(System.currentTimeMillis()).append("conf.").append(fileExtension);
			++counter;
			if (counter >= maxCounter) {
				counter = 0;
			}
			return fName.toString();
		}
	}

	public void toQueue(String content) throws Exception {
		String fName = getFileName();
		File tmpFile = new File(tmpDir, fName);
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tmpFile), charset);
		writer.write(content);
		writer.flush();
		writer.close();

		File finalFile = new File(targetDir, fName);

		if (!tmpFile.renameTo(finalFile)) {
			throw new Exception("Rename failed");
		}
	}
}
