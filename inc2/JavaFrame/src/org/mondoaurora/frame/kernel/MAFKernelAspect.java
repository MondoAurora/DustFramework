package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFLogic;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFKernelAspect {
	MAFKernelEntity entity;

	MAFKernelType type;
	MAFKernelVariant[] content;

	MAFLogic logic;

	public MAFKernelAspect(MAFKernelType type) {
		this.type = type;

		content = new MAFKernelVariant[type.getFieldCount()];
		
		for (MAFKernelField fld : type.arrFields) {
			content[fld.idx] = new MAFKernelVariant(fld.type);
		}
	}

	public MAFKernelAspect(MAFKernelType type, MAFKernelLogic logic) {
		this(type);

		this.logic = logic;
	}

	void dump(MAFKernelDumper target) {
		target.put("{");

		target.endLine(MAFStream.Indent.inc);
		
		boolean add = false;

		for (MAFKernelField fld : type.arrFields) {
			MAFKernelVariant o = content[fld.idx];

			if ((null != o) && !o.isNull()) {
				if ( add ) {
					target.put(",");
					target.endLine(MAFStream.Indent.keep);
				} else {
					add = true;
				}
				fld.dump(target, o);
			}
		}

		target.endLine(MAFStream.Indent.dec);

		target.put("}");
	}

}
