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
	
	class Ctx {
		Return ret = null;
	}

	@Override
	public Object createContextObject(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;
		
		return (null == context.ret) ? new Return(ReturnType.Relay, imported, false) : context.ret;
	}
	
	@Override
	public void processRelayReturn(Return ob, Object ctx) {
		((Ctx)ctx).ret = ob;
	}
	
	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		return imported.parseFrom(stream, currentEntity);
	}
	*/
}
