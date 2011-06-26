package sandbox.lab;

import java.util.*;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustConstants.InvokeResponseProcessor;
import dust.api.components.*;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;

import sandbox.persistence.PersistenceValueExtractor;

public class LabData {

	class EntitySearcher implements InvokeResponseProcessor {
		Set<LabEntity> found = new TreeSet<LabEntity>();
		
		@Override
		public void searchStarted() {
		}

		@Override
		public void searchFinished() {
		}

		@Override
		public boolean entityFound(DustEntity entity) {
			found.add(getLabEntity(entity));
			return true;
		}
	};

	DustWorld world;
	DustDeclId idType;
	DustDeclId idUnit;
	DustDeclId idIdentified;
	DustDeclId idNamed;

	Map<DustEntity, LabEntity> mapEntities;

	public String srcTypes;
	public String srcBoot;

	PersistenceValueExtractor vx = new PersistenceValueExtractor();

	public LabData() {
		world = DustUtils.getWorld();

		idIdentified = world.getTypeId(Common.Identified.class);
		idNamed = world.getTypeId(Common.Named.class);
		idType = world.getTypeId(TypeManagement.Type.class);
		idUnit = world.getTypeId(TypeManagement.Unit.class);

		mapEntities = new HashMap<DustEntity, LabEntity>();
	}
	
	public LabEntity getLabEntity(DustEntity e) {
		LabEntity le = mapEntities.get(e);
		
		if ( null == le ) {
			le = new LabEntity(e);
			mapEntities.put(e, le);
		}
		
		return le;
	}

	public void reset() {
		mapEntities.clear();
	}

	public Set<LabEntity> findEntities(DustDeclId type) {
		return findEntities(new DustDeclId[]{type});
	}

	public Set<LabEntity> findRootEntities() {
		return findEntities(new DustDeclId[]{idUnit, idType});
	}

	public Set<LabEntity> findEntities(DustDeclId[] types) {
		EntitySearcher irProc = new EntitySearcher();

		for ( DustDeclId typeId : types ) {
			world.invoke(irProc, typeId, null, false, null, null);
			world.invoke(irProc, idUnit, null, false, null, null);
		world.invoke(irProc, idType, null, false, null, null);
		}
		
		return irProc.found; 
	}

	public LabEntity addEntity() throws Exception {
		return getLabEntity(DustUtils.getEntity(null, null));
	}

	public DustAspect addAspect(DustEntity e, DustEntity type) {
		DustAspect ret = e.getAspect(idType, false);

		if (null == ret) {
			ret = e.getAspect(idType, true);
		}

		return ret;
	}

	public void delAspect(DustEntity e, DustEntity type) {
	}

	public void delEntity(DustEntity e) {
		mapEntities.remove(e);
	}

	public PersistenceValueExtractor getValEx() {
		return vx;
	}
}
