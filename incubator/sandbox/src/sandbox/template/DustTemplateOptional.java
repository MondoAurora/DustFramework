package sandbox.template;

import sandbox.evaluator.DustEvaluator;
import sandbox.stream.DustStream;
import dust.api.components.DustEntity;

public class DustTemplateOptional extends DustTemplateBase {
	DustTemplate content;
	DustEvaluator eval;

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
