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
/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		DustVariant var = eval.getVariant(currentEntity);
		return fmt.parseFrom(stream, var);
	}
*/
}
