package sandbox.evaluator;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public class DustEvaluatorField implements DustEvaluator {
	DustDeclId typeId;
	Enum<? extends FieldId> field;
	DustEvaluator parent;

	public DustEvaluatorField(DustEvaluator parent, DustDeclId typeId, Enum<? extends FieldId> field) {
		this.parent = parent;
		this.typeId = typeId;
		this.field = field;
	}

	public DustEvaluatorField(DustDeclId typeId, Enum<? extends FieldId> field) {
		this.typeId = typeId;
		this.field = field;
	}

	//
	@Override
	public DustVariant getVariant(DustEntity currentEntity) throws Exception {
		if ( null != parent ) {
			currentEntity = parent.getVariant(currentEntity).getValueObject();
		}
		return currentEntity.getAspect(typeId, true).getField(field);
	}

}
