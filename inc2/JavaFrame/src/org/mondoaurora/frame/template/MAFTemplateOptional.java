package org.mondoaurora.frame.template;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFTemplateOptional extends MAFTemplateBase {
	MAFTemplate content;
	MAFEval eval;

	public MAFTemplateOptional(MAFEval eval, MAFTemplate content) {
		this.eval = eval;
		this.content = content;
	}
	
	@Override
	public void initInt(MAFTemplateSyntax syntax) {
		content.init(syntax);
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		if ( eval.getVariant(var).getBool(false) ) {
			content.writeInto(stream, var);
		}
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
		Return r = ((Ctx) ctx).ret;		
		return (null == r) ? new Return(ReturnType.Relay, content, false) : r.isEventProcessed() ? SUCCESS : SUCCESS_RETRY;
	}
	
	@Override
	public void processRelayReturn(Return ob, Object ctx) {
		((Ctx)ctx).ret = ob;
	}

	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		eval.getVariant(currentEntity).setValueBoolean( content.parseFrom(stream, currentEntity));
		
		return true;
	}
*/
}
