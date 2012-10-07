package com.icode.generic.shell;

import java.io.InputStream;
import java.io.OutputStream;

import com.icode.generic.ICGenUtils;
import com.icode.generic.base.ICGenTreeNode;

public class ICShellConsole extends ICShell {
	private String prompt;
	private String promptSep;
	
	public ICShellConsole() {
		this("", ">");
	}
	
	public ICShellConsole(String prompt, String promptSep) {
		super(ICSHELL_TO_ERR, ICSHELL_FROM_SINGLE, true);
		// reverse!
		this.prompt = prompt;
		this.promptSep = promptSep;
	}
	
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	protected void endOfAnswerInt(String stream) throws Exception {
		System.out.print( prompt );
		System.out.print( promptSep );
	}

	protected InputStream getFromShellStream(String name) {
		return System.in;
	}

	protected void initShell() throws Exception {
		ICShellCmdProcTrap trap = new ICShellCmdProcTrap.Exit(getProcessor(ICSHELL_FROM));
		setProcessor(ICSHELL_FROM, trap);
		endOfAnswer(ICSHELL_TO);
	}

	protected OutputStream getToShellStream(String name) {
		switch (ICGenUtils.indexOf(ICSHELL_TO_ERR, name)) {
		case 0:
			return System.out;
		case 1:
			return System.err;
		}
		return null;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		prompt = (String) config.getOptional("prompt", "");
		promptSep = (String) config.getOptional("promptSep", ">");
	}
}
