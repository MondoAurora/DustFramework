package org.mondoaurora.frame.template;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFRuntimeException;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateRef extends MAFTemplateBase {
	String target;
	MAFTemplate imported;
	
	public MAFTemplateRef(String target) {
		this.target = target;
	}
	
	@Override
	public void init(MAFTemplateSyntax syntax) {
		MAFTemplate t = syntax.getRule(target);
		if ( null == target ) {
			throw new MAFRuntimeException("TemplateSyntax", "Missing rule " + target, null);
		} else if ( t == this ) {
			throw new MAFRuntimeException("TemplateSyntax", "Self importing rule " + target, null);
		}
		imported = t;
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) {
		imported.writeInto(stream, currentEntity);
	}

	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		return imported.parseFrom(stream, currentEntity);
	}
	*/
}
