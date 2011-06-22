package sandbox.template;

import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

import sandbox.evaluator.DustEvaluator;
import sandbox.formatter.DustFormatter;
import sandbox.stream.DustStream;

public class DustTemplateEval extends DustTemplateBase {
	DustEvaluator eval;
	DustFormatter fmt;

	public DustTemplateEval(DustEvaluator eval, DustFormatter fmt) {
		this.eval = eval;
		this.fmt = fmt;
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		fmt.writeInto(stream, eval.getVariant(currentEntity));
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		DustVariant var = eval.getVariant(currentEntity);
		return fmt.parseFrom(stream, var);
	}
}
