package com.icode.generic.file;

import java.io.*;
import java.nio.channels.FileChannel;

import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.shell.ICShell.LineProcessor;
import com.icode.generic.task.ICTaskStreamReader;

public class ICFileReader extends ICTaskStreamReader {
	String fileName;
	FileInputStream fis;

	long lastFilePos = -1;

	public ICFileReader() {
		this(null, null, null, null);
	}

	public ICFileReader(Object owner, String group, String name, LineProcessor processor) {
		super(owner, group, name, null, processor);
		this.fileName = name;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		fileName = (String) config.getOptional("fileName", fileName);
		super.loadDataFrom(config, hint);
	}

	protected boolean init() throws Exception {
		return super.init();
	}

	protected void startReading() throws Exception {
		if (!new File(fileName).isFile()) {
			requestStop(WAIT_NONE);
		} else {
			fis = new FileInputStream(fileName);
			if (-1 != lastFilePos) {
				fis.getChannel().position(lastFilePos);
			}
			setIs(fis);
			super.startReading();
		}
	}

	protected void goingToSleep() {
		try {
			FileChannel ch = fis.getChannel();
			if (null != ch) {
				lastFilePos = ch.position();
			}

			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.goingToSleep();
	};

}
