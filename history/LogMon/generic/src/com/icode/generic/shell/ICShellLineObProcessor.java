package com.icode.generic.shell;

import com.icode.generic.ICGenObjectParseProcessor;
import com.icode.generic.base.ICGenTreeNode;

public class ICShellLineObProcessor extends ICShell.LineProcessor {
	ICGenObjectParseProcessor pp;
	
	public ICShellLineObProcessor() {
		super(false);
		pp = new ICGenObjectParseProcessor();
	}

	public Object processLine(String line) throws Exception {
		return pp.processLine(line, false);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		pp.loadDataFrom(config, hint);
	}

}
