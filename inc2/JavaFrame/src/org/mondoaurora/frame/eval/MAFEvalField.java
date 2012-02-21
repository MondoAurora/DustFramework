package org.mondoaurora.frame.eval;

import org.mondoaurora.frame.kernel.*;
import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.shared.MAFStream.Out;
import org.mondoaurora.frame.tools.MAFToolsVariantWrapper;

public class MAFEvalField implements MAFEval {
	public enum AccessMode {
		value, existence, childExistence
	};

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
	public MAFVariant getVariant(MAFVariant var) {
		/*
		 * if (null != parent) { currentEntity =
		 * parent.getVariant(currentEntity).getValueObject(); }
		 */

//		MAFKernelAspect asp = var.getAspect(typeId);
		if (MAFUtils.isNull(var)) {
			return (AccessMode.existence == mode) ? MAFToolsVariantWrapper.False : null;
		} else {
			if (null == conn) {
				conn = (MAFKernelConnector) var.getReference( new String[] { fieldId });
			} else {
				var.getReference(conn);
			}
			
			switch (mode) {
			case value:
				return (MAFKernelVariant) conn.getValue(0);
			case existence:
				return conn.isNull(0) ? MAFToolsVariantWrapper.False : MAFToolsVariantWrapper.True;
			case childExistence:
				MAFVariant varChild = conn.getValue(0);
				for (MAFVariant v1 : var.getMembers()) {
//					if (child.getVariant(v1.getValueObject()).getValueBoolean()) {
//						return varTrue;
//					}
				}
				return MAFToolsVariantWrapper.False;
			default:
				throw new MAFRuntimeException("Eval", "AccessMode unset");
			}
		}
	}
	
	@Override
	public void writeContent(Out target, MAFVariant var) {
		target.put(getVariant(var).toString());
	}
}
