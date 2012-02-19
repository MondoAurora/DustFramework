package org.mondoaurora.frame.kernel;

import java.util.HashMap;
import java.util.Map;

import org.mondoaurora.frame.kernel.MAFKernelDumper.Indent;
import org.mondoaurora.frame.shared.MAFIdentifier;

public class MAFKernelEntity {
	MAFKernelIdentifier id;
	MAFKernelAspect primaryAspect;

	Map<MAFIdentifier, MAFKernelAspect> mapAspects = new HashMap<MAFIdentifier, MAFKernelAspect>();

	public MAFKernelEntity(MAFKernelIdentifier id, MAFKernelAspect primaryAspect) {
		this.id = id;
		this.primaryAspect = primaryAspect;
		addAspect(primaryAspect);
	}

	public void addAspect(MAFKernelAspect aspect) {
		mapAspects.put(aspect.type.id, aspect);
		aspect.entity = this;
	}

	@Override
	public String toString() {
		return "Entity " + id.toString();
	}

	void dump(MAFKernelDumper target, boolean refOnly) {
		target.put("{ \"!ref\" : \"");
		target.put(id.asReference());
		target.put("\" ");

		if (!refOnly) {
			target.put(",");
			target.endLine(Indent.inc);
			for ( Map.Entry<MAFIdentifier, MAFKernelAspect> e : mapAspects.entrySet() ) {
				target.put("\"");
				target.put(e.getKey().asReference());
				target.put("\" : ");
				e.getValue().dump(target);
			}
			target.endLine(Indent.dec);
		} 
		
		target.put("}");
		target.endLine(Indent.keep);
	}

	void dump(MAFKernelDumper target) {
		dump(target, false);
	}
	
	void dump() {
		new MAFKernelDumper().dumpEntity(this);
	}
}
