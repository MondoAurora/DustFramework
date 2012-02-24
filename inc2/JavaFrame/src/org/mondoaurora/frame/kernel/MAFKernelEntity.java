package org.mondoaurora.frame.kernel;

import java.util.*;

import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;
import org.mondoaurora.frame.tools.MAFToolsVariantWrapper;

public class MAFKernelEntity {
	MAFKernelIdentifier id;
	MAFKernelAspect primaryAspect;

	Map<MAFKernelIdentifier, MAFKernelAspect> mapAspects = new TreeMap<MAFKernelIdentifier, MAFKernelAspect>();

	protected MAFKernelEntity() {
		
	}
	
	public MAFKernelEntity(MAFKernelIdentifier id, MAFKernelAspect primaryAspect) {
		this.id = id;
		this.primaryAspect = primaryAspect;
		addAspect(primaryAspect);
	}

	public void addAspect(MAFKernelAspect aspect) {
		mapAspects.put(aspect.type.id, aspect);
		aspect.entity = this;
	}

	public MAFKernelAspect getAspect(MAFKernelIdentifier id) {
		return mapAspects.get(id);
	}
	
	public Iterable<Map.Entry<MAFKernelIdentifier, MAFKernelAspect>> getAspects() {
		return mapAspects.entrySet();
	}
	
	public String getRef() {
		return id.asReference();
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
			target.endLine(MAFStream.Indent.inc);
			for ( Map.Entry<MAFKernelIdentifier, MAFKernelAspect> e : mapAspects.entrySet() ) {
				target.put("\"");
				target.put(e.getKey().asReference());
				target.put("\" : ");
				e.getValue().dump(target);
			}
			target.endLine(MAFStream.Indent.dec);
		} 
		
		target.put("}");
	}

	void dump(MAFKernelDumper target) {
		dump(target, false);
	}
	
	void dump() {
		new MAFKernelDumper().dumpEntity(this);
	}
	
	public static class Variant extends MAFToolsVariantWrapper {
		MAFKernelEntity entity;
		MAFVariant varRef;
		boolean refOnly;

		public Variant(MAFKernelEntity entity, String refKey, boolean refOnly) {
			this.entity = entity;
			this.refOnly = refOnly;

			if (null != refKey) {
				varRef = new MAFToolsVariantWrapper.ConstString(refKey, entity.getRef());
			}
		}

		public Variant(MAFKernelEntity entity) {
			this.entity = entity;
		}

		@Override
		public Iterable<? extends MAFVariant> getMembers() {
			ArrayList<MAFVariant> alContent = new ArrayList<MAFVariant>();

			if (null != varRef) {
				alContent.add(varRef);
			}

			if (!refOnly) {
				for (Map.Entry<MAFKernelIdentifier, MAFKernelAspect> e : entity.getAspects()) {
					alContent.add(new MAFKernelAspect.Variant(e.getKey().asReference(), e.getValue()));
				}
			}

			return alContent;
		}
	}

}

