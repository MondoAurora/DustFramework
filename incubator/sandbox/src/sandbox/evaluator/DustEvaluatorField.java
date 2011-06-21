package sandbox.evaluator;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.utils.DustUtilVariant;
import dust.units.dust.common.v0_1.Common;

public class DustEvaluatorField implements DustEvaluator {
	public enum AccessMode {
		value, existence
	};

	DustVariant varTrue = new DustUtilVariant(Common.Identified.Fields.Identifier, true);
	DustVariant varFalse = new DustUtilVariant(Common.Identified.Fields.Identifier, false);

	DustDeclId typeId;
	Enum<? extends FieldId> field;
	DustEvaluator parent;
	AccessMode mode;

	public DustEvaluatorField(AccessMode mode, DustEvaluator parent, DustDeclId typeId, Enum<? extends FieldId> field) {
		this.mode = mode;
		this.parent = parent;
		this.typeId = typeId;
		this.field = field;
	}

	public DustEvaluatorField(DustEvaluator parent, DustDeclId typeId, Enum<? extends FieldId> field) {
		this(AccessMode.value, parent, typeId, field);
	}

	public DustEvaluatorField(DustDeclId typeId, Enum<? extends FieldId> field) {
		this(AccessMode.value, null, typeId, field);
	}

	@Override
	public DustVariant getVariant(DustEntity currentEntity) throws Exception {
		if (null != parent) {
			currentEntity = parent.getVariant(currentEntity).getValueObject();
		}

		switch (mode) {
		case value:
			return currentEntity.getAspect(typeId, true).getField(field);
		case existence:
			DustAspect a = currentEntity.getAspect(typeId, false);
			return ((null == a) || a.getField(field).isNull()) ? varFalse : varTrue;
		default:
			throw new Exception("DustEvaluatorField AccessMode unset???");
		}
	}

}
