package sandbox.lab;

import java.util.*;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustConstants.InvokeResponseProcessor;
import dust.api.DustDeclarationConstants.TypeDef;
import dust.api.components.*;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;

public class LabData {

	public class LabEntity {
		DustEntity e;
		String name;
		final String typeName;

		LabEntity(DustEntity e) {
			this("?", e);
		}

		LabEntity(String typeName, DustEntity e) {
			this.typeName = typeName;
			this.e = e;
			name = e.getAspect(idNamed, true).getField(Common.Named.Fields.Name).getValueString();
		}

		public String toString() {
			return typeName + ": " + name;
		}
	}

	public class LabUnit extends LabEntity {
		LabUnit(DustEntity e) {
			super("Unit", e);

			DustAspect aUnit = e.getAspect(idUnit, true);
			name = aUnit.getField(TypeManagement.Unit.Fields.Domain).getValueIdentifier().toString() + "." + name;
		}
	}

	public class LabType extends LabEntity {
		LabType(DustEntity e) {
			super("Type", e);

			String cName = e.getAspect(idIdentified, false).getField(Common.Identified.Fields.Identifier)
				.getValueIdentifier().toString();
			try {
				typeId = world.getTypeId((Class<? extends TypeDef>) Class.forName(cName));
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			DustEntity eUnit = e.getAspect(idType, false).getField(TypeManagement.Type.Fields.Unit).getValueObject();
			unit = mapUnits.get(eUnit);

			name = unit.name + "." + name;
		}

		DustDeclId typeId;
		LabUnit unit;
	}

	InvokeResponseProcessor irProc = new InvokeResponseProcessor() {
		@Override
		public void searchStarted() {
		}

		@Override
		public void searchFinished() {
		}

		@Override
		public boolean entityFound(DustEntity entity) {
			LabEntity le = null;

			if (null != entity.getAspect(idType, false)) {
				LabType lt = new LabType(entity);
				mapTypes.put(entity, lt);
				le = lt;
			} else if (null != entity.getAspect(idUnit, false)) {
				LabUnit lu = new LabUnit(entity);
				le = mapUnits.put(entity, lu);
				le = lu;
			} else {
				le = new LabEntity(entity);
			}
			alEntities.add(le);

			return true;
		}
	};

	DustWorld world;
	DustDeclId idType;
	DustDeclId idUnit;
	DustDeclId idIdentified;
	DustDeclId idNamed;

	ArrayList<LabEntity> alEntities;
	Map<DustEntity, LabUnit> mapUnits;
	Map<DustEntity, LabType> mapTypes;

	public String srcTypes;
	public String srcBoot;

	public LabData() {
		world = DustUtils.getWorld();

		idIdentified = world.getTypeId(Common.Identified.class);
		idNamed = world.getTypeId(Common.Named.class);
		idType = world.getTypeId(TypeManagement.Type.class);
		idUnit = world.getTypeId(TypeManagement.Unit.class);

		alEntities = new ArrayList<LabEntity>();
		mapUnits = new HashMap<DustEntity, LabUnit>();
		mapTypes = new HashMap<DustEntity, LabType>();

		refresh();
	}

	public void reset() {
		alEntities.clear();
		mapUnits.clear();
		mapTypes.clear();
	}

	public void refresh() {
		reset();

		world.invoke(irProc, idUnit, null, false, null, null);
		world.invoke(irProc, idType, null, false, null, null);
	}

	public LabEntity addEntity() throws Exception {
		LabEntity e = new LabEntity(DustUtils.getEntity(null, null));

		alEntities.add(e);

		return e;
	}

	public DustAspect addAspect(DustEntity e, LabType t) {
		DustAspect ret = e.getAspect(idType, false);

		if (null == ret) {
			if (t.typeId == idUnit) {
				mapUnits.put(e, new LabUnit(e));
			} else if (t.typeId == idType) {
				mapTypes.put(e, new LabType(e));
			}
			ret = e.getAspect(idType, true);
		}

		return ret;
	}

	public void delAspect(DustEntity e, LabType t) {
		if (null != e.getAspect(idType, false)) {
			if (t.typeId == idUnit) {
				mapUnits.remove(e);
			} else if (t.typeId == idType) {
				mapTypes.remove(e);
			}
			e.removeAspect(t.typeId);
		}
	}

	public void delEntity(DustEntity e) {
		alEntities.remove(e);
		mapUnits.remove(e);
		mapTypes.remove(e);
	}

}
