package org.mondoaurora.frame.eval;

import org.mondoaurora.frame.kernel.MAFKernelAspect;
import org.mondoaurora.frame.kernel.MAFKernelConnector;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.kernel.MAFKernelIdentifier;
import org.mondoaurora.frame.kernel.MAFKernelVariant;
import org.mondoaurora.frame.shared.MAFRuntimeException;
import org.mondoaurora.frame.shared.MAFVariant;
import org.mondoaurora.frame.shared.MAFStream.Out;

public class MAFEvalField implements MAFEval {
	public enum AccessMode {
		value, existence, childExistence
	};

	MAFKernelVariant varTrue = new MAFKernelVariant(FieldType.BOOLEAN, true);
	MAFKernelVariant varFalse = new MAFKernelVariant(FieldType.BOOLEAN, false);

	MAFEval parent;
	MAFEval child;

	MAFKernelIdentifier typeId;
	String fieldId;

	MAFKernelConnector conn;
	AccessMode mode;

	public MAFEvalField(AccessMode mode, MAFEval parent, MAFEval child, MAFKernelIdentifier typeId, String fieldId) {
		this.mode = mode;
		this.parent = parent;
		this.child = child;

		this.typeId = typeId;
		this.fieldId = fieldId;
	}

	public MAFEvalField(MAFEval parent, MAFKernelIdentifier typeId, String field) {
		this(AccessMode.value, parent, null, typeId, field);
	}

	public MAFEvalField(AccessMode mode, MAFKernelIdentifier typeId, String field) {
		this(mode, null, null, typeId, field);
	}

	public MAFEvalField(MAFKernelIdentifier typeId, String field) {
		this(AccessMode.value, null, null, typeId, field);
	}

	@Override
	public MAFKernelVariant getVariant(MAFKernelEntity currentEntity) {
		/*
		 * if (null != parent) { currentEntity =
		 * parent.getVariant(currentEntity).getValueObject(); }
		 */

		MAFKernelAspect asp = currentEntity.getAspect(typeId);

		if (null == asp) {
			return (AccessMode.existence == mode) ? varFalse : null;
		} else {
			if (null == conn) {
				conn = new MAFKernelConnector(asp, new String[] { fieldId });
			} else {
				conn.setData(asp);
			}
			
			switch (mode) {
			case value:
				return (MAFKernelVariant) conn.getValue(0);
			case existence:
				return conn.isNull(0) ? varFalse : varTrue;
			case childExistence:
				MAFVariant var = conn.getValue(0);
				for (MAFVariant v1 : var.getMembers()) {
//					if (child.getVariant(v1.getValueObject()).getValueBoolean()) {
//						return varTrue;
//					}
				}
				return varFalse;
			default:
				throw new MAFRuntimeException("Eval", "AccessMode unset");
			}
		}
	}
	
	@Override
	public void writeContent(Out target, MAFKernelEntity currentEntity) {
		target.put(getVariant(currentEntity).toString());
	}
}
