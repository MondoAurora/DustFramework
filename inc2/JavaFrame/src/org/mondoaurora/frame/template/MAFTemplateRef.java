package org.mondoaurora.frame.template;

import org.mondoaurora.frame.shared.*;

public class MAFTemplateRef extends MAFTemplateBase {
	String target;
	MAFTemplate imported;
	
	public MAFTemplateRef(String target) {
		this.target = target;
	}
	
	@Override
	public void initInt(MAFTemplateSyntax syntax) {
		MAFTemplate t = syntax.getRule(target);
		if ( null == target ) {
			throw new MAFRuntimeException("TemplateSyntax", "Missing rule " + target, null);
		} else if ( t == this ) {
			throw new MAFRuntimeException("TemplateSyntax", "Self importing rule " + target, null);
		}
		imported = t;
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		imported.writeInto(stream, var);
	}
	
	@Override
	protected Return processChar(char c, Object ctx) {
		return new Return(ReturnType.Relay, imported, false);
	}
	
	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		return ob;
	}
	
	@Override
	public String toString() {
		return "<" + target + ">";
	}

	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		return imported.parseFrom(stream, currentEntity);
	}
	*/
}
