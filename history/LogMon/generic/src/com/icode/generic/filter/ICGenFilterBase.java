package com.icode.generic.filter;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;
import com.icode.generic.resolver.*;

public abstract class ICGenFilterBase implements ICGenConfigurable, ICGenFilter, ICGenFilterConstants {
	private String name;
	
	private String[] cfgNames;
	private boolean negated = false;
	private byte opCode = OP_INVALID;
	
	private ICGenResolver.Value[] cfgObjs;

	protected ICGenFilterBase(String[] names) {
		cfgNames = names;
	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		name = node.getNameAtt(CFG_GEN_NAME);

		ICGenTreeNode n = node.getChild(CFG_FILTER_OPTS);
		ICGenObject opts = (ICGenObject) ICAppFrame.getComponent(n, ICGenObject.class);

		ICGenObjTranslator tr;
		tr = new ICGenObjTranslator(FILTER_BASE_NAMES);
		String s;
		
		s = (String) tr.getAttByIdx(opts, BASE_OP);
		opCode = resolveOp(s);
		
		s = (String) tr.getAttByIdx(opts, BASE_NOT);
		negated = Boolean.valueOf(s).booleanValue();
		
		tr = new ICGenObjTranslator(cfgNames);
		cfgObjs = new ICGenResolver.Value[cfgNames.length];

		for (int i = 0; i < tr.getAttCount(); ++i) {
			cfgObjs[i] = new ICGenResolverValue((String)tr.getAttByIdx(opts, i));
		}
	}

	public String getName() {
		return name;
	}

	public final boolean isMatching(ICGenObject object) {
		return negated ^ isMatchingInt(object);
	}
	
	protected byte getOpCode() {
		return opCode;
	}

	protected Object getCtxValue(ICGenObject param, int valIdx) {
		return cfgObjs[valIdx].getResolvedValue(param, null, null);
	}

	protected abstract byte resolveOp(String opString);
	protected abstract boolean isMatchingInt(ICGenObject object);
}
