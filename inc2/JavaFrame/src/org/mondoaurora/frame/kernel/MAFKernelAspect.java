package org.mondoaurora.frame.kernel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mondoaurora.frame.kernel.MAFKernelDumper.Indent;
import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFIdentifier;
import org.mondoaurora.frame.shared.MAFLogic;

public class MAFKernelAspect {
	MAFKernelEntity entity;

	MAFKernelType type;
	Object[] content;

	MAFLogic logic;

	public MAFKernelAspect(MAFKernelType type) {
		this.type = type;

		content = new Object[type.getFieldCount()];
	}

	public MAFKernelAspect(MAFKernelType type, MAFKernelLogic logic) {
		this(type);

		this.logic = logic;
	}

	void addMember(int idx, MAFConnector member) {
		@SuppressWarnings("unchecked")
		Set<MAFConnector> c = (Set<MAFConnector>) content[idx];

		if (null == c) {
			c = new HashSet<MAFConnector>();
			content[idx] = c;
		}
		c.add(member);
	}

	void addArr(int idx, MAFConnector member) {
		@SuppressWarnings("unchecked")
		ArrayList<MAFConnector> c = (ArrayList<MAFConnector>) content[idx];

		if (null == c) {
			c = new ArrayList<MAFConnector>();
			content[idx] = c;
		}
		c.add(member);
	}

	void dump(MAFKernelDumper target) {
		target.put("{");
//		target.put("{ \"!type\" : \"");
//		target.put(type.id.asReference());
//		target.put("\", ");

		target.endLine(Indent.inc);
		
		boolean add = false;

		for (MAFKernelField fld : type.arrFields) {
			Object o = content[fld.idx];

			if (null != o) {
				if ( add ) {
					target.put(",");
					target.endLine(Indent.keep);
				} else {
					add = true;
				}
				fld.dump(target, o);
			}
		}

		target.endLine(Indent.dec);

		target.put("}");
		target.endLine(Indent.keep);
	}

}
