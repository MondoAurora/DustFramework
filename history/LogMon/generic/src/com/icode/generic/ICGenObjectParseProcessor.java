package com.icode.generic;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.base.ICGenObject.Parser;
import com.icode.generic.base.ICGenObject.Processor;

public class ICGenObjectParseProcessor implements ICGenLineProcessor, ICGenObject.Parser, ICGenConfigurable {

	ICGenObject.Parser parser;
	ICGenObject.Processor proc;

	public ICGenObjectParseProcessor() {
	}

	public ICGenObjectParseProcessor(Parser parser, Processor proc) {
		this.parser = parser;
		this.proc = proc;
	}

	public ICGenObject parseObject(String line) throws Exception {
		try {
			ICGenObject ob = parser.parseObject(line);
			if (null != ob) {
				proc.processObject(ob);
			}
			return ob;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		parser = (ICGenObject.Parser) ICAppFrame.getComponent(node.getChild(CFG_GEN_PARSER), null);
		proc = (ICGenObject.Processor) ICAppFrame.getComponent(node.getChild(CFG_GEN_PROCESSOR), null);
	}

	public boolean isPartialAccepted() {
		return false;
	}

	public Object processLine(String line, boolean partial) throws Exception {
		return parseObject(line);
	}
}
