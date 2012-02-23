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

	@Override
	protected Return processChar(char c, Object ctx) {
		return new Return(ReturnType.Relay, content, false);
	}
	
	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		return (ReturnType.Success == ob.getType()) ? ob : SUCCESS_RETRY;
	}

	@Override
	public String toString() {
		return "TemplOptional: " + content;
	}
	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		eval.getVariant(currentEntity).setValueBoolean( content.parseFrom(stream, currentEntity));
		
		return true;
	}
*/
}
