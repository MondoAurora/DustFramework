package sandbox.evaluator;

import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public interface DustEvaluator {	
	DustVariant getVariant(DustEntity currentEntity) throws Exception;
}
