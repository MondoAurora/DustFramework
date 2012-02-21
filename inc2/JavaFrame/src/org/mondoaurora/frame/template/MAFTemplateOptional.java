package org.mondoaurora.frame.template;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateOptional extends MAFTemplateBase {
	MAFTemplate content;
	MAFEval eval;

	public MAFTemplateOptional(MAFEval eval, MAFTemplate content) {
		this.eval = eval;
		this.content = content;
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) throws Exception {
		if ( eval.getVariant(currentEntity).getBool(false) ) {
			content.writeInto(stream, currentEntity);
		}
	}

	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		eval.getVariant(currentEntity).setValueBoolean( content.parseFrom(stream, currentEntity));
		
		return true;
	}
*/
}
