package org.mondoaurora.frame.kernel;

import java.util.*;

import org.mondoaurora.frame.shared.*;
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
		
		MAFKernelAspect.Variant currAspect;

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
		
		public Variant() {
			System.out.println("Starting entity");
			// creates with NO entity
		}
		
		public MAFConnector getConnector() {
			return new MAFKernelConnector(entity.primaryAspect);
		}
		
		public void setRef(String ref) {
			// here I can get the referred entity or create one if missing. Now just create it
			entity = new MAFKernelEntity();
			entity.id = MAFKernelIdentifier.fromString(ref);
			
			System.out.println("Setting entity ref to: " + ref);

		}
		
		public MAFKernelAspect.Variant getCurrAspect() {
			return currAspect;
		}
		
		public void startAspect(String aspRef) {
			System.out.println("Starting aspect " + aspRef);

			currAspect = new MAFKernelAspect.Variant(aspRef);
		}
		
		public void endCurrentAspect() {
			System.out.println("Ending aspect " + currAspect.aspect.type.id);
			entity.addAspect(currAspect.aspect);
			
			if (entity.id.getType().equals(currAspect.aspect.type.id.asPath())) {
				entity.primaryAspect = currAspect.aspect;
			}
			currAspect = null;
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

