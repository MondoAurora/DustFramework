package com.icode.generic.shell;

import java.io.InputStream;
import java.io.OutputStream;

import com.icode.generic.ICGenUtils;
import com.icode.generic.base.ICGenTreeNode;

public class ICShellOS extends ICShell {

	String shellCmd;
	Process proc;

	public ICShellOS(){
		this(ICGenUtils.getShell());
	}

	public ICShellOS(String shellCmd){
		super(ICSHELL_TO_SINGLE, ICSHELL_FROM_ERR, true);
		this.shellCmd = shellCmd;
	}

	protected void endOfAnswerInt(String stream) throws Exception {
	}

	protected void initShell() throws Exception {
		Runtime rt = Runtime.getRuntime();
		proc = rt.exec(shellCmd);
	}

	public int waitFor() throws Exception {
		int ret = proc.waitFor();
		proc = null;
		endShell();
		return ret;
	}

	protected InputStream getFromShellStream(String name) {
		switch (ICGenUtils.indexOf(ICSHELL_FROM_ERR, name)) {
		case 0:
			return proc.getInputStream();
		case 1:
			return proc.getErrorStream();
		}
		return null;
	}
	
	protected OutputStream getToShellStream(String name) {
		return proc.getOutputStream();
	}

	public synchronized void killShellInt() throws Exception {
		if (null != proc) {
			proc.destroy();
		}
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		shellCmd = (String) config.getMandatory("shellCmd");
	}
}
