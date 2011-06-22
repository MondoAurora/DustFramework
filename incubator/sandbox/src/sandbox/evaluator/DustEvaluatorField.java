package sandbox.evaluator;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.*;
import dust.api.utils.DustUtilVariant;

import dust.units.dust.common.v0_1.Common;

public class DustEvaluatorField implements DustEvaluator {
	public enum AccessMode {
		value, existence, childExistence
	};

	DustVariant varTrue = new DustUtilVariant(Common.Identified.Fields.Identifier, true);
	DustVariant varFalse = new DustUtilVariant(Common.Identified.Fields.Identifier, false);

	DustDeclId typeId;
	Enum<? extends FieldId> field;
	DustEvaluator parent;
	DustEvaluator child;
	
	AccessMode mode;

	public DustEvaluatorField(AccessMode mode, DustEvaluator parent, DustEvaluator child, DustDeclId typeId, Enum<? extends FieldId> field) {
		this.mode = mode;
		this.parent = parent;
		this.child = child;
		this.typeId = typeId;
		this.field = field;
	}

	public DustEvaluatorField(DustEvaluator parent, DustDeclId typeId, Enum<? extends FieldId> field) {
		this(AccessMode.value, parent, null, typeId, field);
	}

	public DustEvaluatorField(AccessMode mode, DustDeclId typeId, Enum<? extends FieldId> field) {
		this(mode, null, null, typeId, field);
	}

	public DustEvaluatorField(DustDeclId typeId, Enum<? extends FieldId> field) {
		this(AccessMode.value, null, null, typeId, field);
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
		case childExistence:
			DustVariant var = currentEntity.getAspect(typeId, true).getField(field);
			for ( DustVariant v1 : var.getMembers() ) {
				if ( child.getVariant(v1.getValueObject()).getValueBoolean() ) {
					return varTrue;
				}
			}
			return varFalse;
		default:
			throw new Exception("DustEvaluatorField AccessMode unset???");
		}
	}

}
