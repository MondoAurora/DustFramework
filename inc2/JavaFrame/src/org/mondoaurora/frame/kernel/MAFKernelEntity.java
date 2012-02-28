package org.mondoaurora.frame.kernel;

import java.util.*;

import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.tools.MAFToolsVariantWrapper;

public class MAFKernelEntity implements MAFKernelConsts {
	EntityState state;
	
	MAFKernelIdentifier id;
	MAFKernelAspect primaryAspect;

	Map<MAFKernelIdentifier, MAFKernelAspect> mapAspects = new TreeMap<MAFKernelIdentifier, MAFKernelAspect>();

	protected MAFKernelEntity() {
		state = EntityState.NEW;
	}

	public MAFKernelEntity(MAFKernelIdentifier id, MAFKernelAspect primaryAspect) {
		this.id = id;
		this.primaryAspect = primaryAspect;
		state = EntityState.NORMAL;
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
			for (Map.Entry<MAFKernelIdentifier, MAFKernelAspect> e : mapAspects.entrySet()) {
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

		MAFVariant varParent;
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

		public Variant(MAFKernelAspect.Variant parentAspect) {
			// System.out.println("Starting entity");
			// creates with NO entity
			this.varParent = (null == parentAspect) ? null : parentAspect.getVar(); // get the current field of that
																							// aspect variant
		}

		public MAFConnector getConnector() {
			return new MAFKernelConnector(entity.primaryAspect);
		}

		public void setRef(String ref) {
			MAFKernelIdentifier id = (MAFKernelIdentifier) MAFEnvironment.getId(ref);
			MAFKernelEntity ke = null;

			if (null != varParent) {
				for (MAFVariant varRef : varParent.getMembers()) {
					MAFKernelConnector kc = (MAFKernelConnector) varRef.getReference(MAFUtils.EMPTYARR);
					ke = kc.getEntity();
					if (ke.getRef().equals(ref)) {
						entity = ke;
						break;
					}
				}
			}

			if (null == entity) {
				if (null == id) {
					id = MAFKernelIdentifier.fromString(ref);
				} else {
					entity = ((MAFKernelConnector) MAFEnvironment.getInstance(id, null)).getEntity();
				}
			}

			if (null == entity) {
				entity = new MAFKernelEntity();
				entity.id = id;
			}
		}

		public MAFKernelAspect.Variant getCurrAspect() {
			return currAspect;
		}

		public void startAspect(String aspRef) {
			System.out.println("Starting aspect " + aspRef);

			MAFIdentifier id = MAFEnvironment.getId(aspRef);
			MAFKernelAspect asp = entity.mapAspects.get(id);

			currAspect = (null == asp) ? new MAFKernelAspect.Variant(aspRef) : new MAFKernelAspect.Variant(aspRef, asp);
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
