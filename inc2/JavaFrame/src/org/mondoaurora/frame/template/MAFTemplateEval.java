package org.mondoaurora.frame.template;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFTemplateEval extends MAFTemplateBase {
	MAFEval eval;

	public MAFTemplateEval(MAFEval eval) {
		this.eval = eval;
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		eval.writeContent(stream, var);
	}
	
	@Override
	protected Return processChar(char c, Object ctx) {
		return new Return(ReturnType.Relay, eval, false);
	}
	
	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		return ob;
	}

	@Override
	public String toString() {
		return "TemplEval: " ;
	}

/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		DustVariant var = eval.getVariant(currentEntity);
		return fmt.parseFrom(stream, var);
	}
*/
}
