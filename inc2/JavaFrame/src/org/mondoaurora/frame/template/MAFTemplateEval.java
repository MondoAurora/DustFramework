package org.mondoaurora.frame.template;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateEval extends MAFTemplateBase {
	MAFEval eval;

	public MAFTemplateEval(MAFEval eval) {
		this.eval = eval;
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) {
		eval.writeContent(stream, currentEntity);
	}
/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		DustVariant var = eval.getVariant(currentEntity);
		return fmt.parseFrom(stream, var);
	}
*/
}
