package sandbox.template;

import dust.api.components.DustEntity;

import sandbox.evaluator.DustEvaluator;
import sandbox.stream.DustStream;

public class DustTemplateOptional extends DustTemplateBase {
	DustTemplate content;
	DustEvaluator eval;

	public DustTemplateOptional(DustEvaluator eval, DustTemplate content) {
		this.eval = eval;
		this.content = content;
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		if ( eval.getVariant(currentEntity).getValueBoolean() ) {
			content.writeInto(stream, currentEntity);
		}
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		eval.getVariant(currentEntity).setValueBoolean( content.parseFrom(stream, currentEntity));
		
		return true;
	}

}
