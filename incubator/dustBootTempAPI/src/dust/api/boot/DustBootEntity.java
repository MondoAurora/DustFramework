package dust.api.boot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.utils.DustUtilVariant;
import dust.api.utils.DustUtils;

public class DustBootEntity implements DustEntity {
	class MyAspect implements DustAspect {
		DustDeclId myId;

		MyAspect(DustDeclId id) {
			myId = id;
		}

		@Override
		public DustDeclId getType() {
			return myId;
		}

		@Override
		public DustVariant getField(Enum<? extends FieldId> field) {
			DustVariant v = mapFields.get(field);

			if (null == v) {
				v = new DustUtilVariant(field, null);
				setVariant(v);
			}
			return v;
		}

		@Override
		public DustEntity getEntity() {
			return DustBootEntity.this;
		}
	}

	DustDeclId primaryTypeId;

	Map<Enum<? extends FieldId>, DustVariant> mapFields = new HashMap<Enum<? extends FieldId>, DustVariant>();
	Map<DustDeclId, MyAspect> mapTypes = new HashMap<DustDeclId, MyAspect>();
	
	EntityState state;

	DustBootEntity(DustDeclId typeId, DustVariant[] fields) {
		this.primaryTypeId = typeId;
		addType(primaryTypeId);

		if (null != fields) {
			for (DustVariant v : fields) {
				setVariant(v);
			}
			state = EntityState.Steady;
		} else {
			state = EntityState.Creating;
		}
	}

	DustAspect addType(DustDeclId typeId) {
		MyAspect asp = mapTypes.get(typeId);

		if (null == asp) {
			asp = new MyAspect(typeId);
			mapTypes.put(typeId, asp);
			((DustBootWorld) DustUtils.getWorld()).addEntity(typeId, this);
		}

		return asp;
	}

	void setVariant(DustVariant variant) {
		mapFields.put(variant.getId(), variant);
		addType(variant.getTypeId());
	}

	@Override
	public DustDeclId getPrimaryTypeId() {
		return primaryTypeId;
	}

	@Override
	public Iterable<DustDeclId> getTypes() {
		return mapTypes.keySet();
	}

	@Override
	public EntityState getState() {
		return state;
	}

	@Override
	public EntityType getType() {
		return EntityType.Temporal;
	}

	@Override
	public DustAspect getAspect(DustDeclId typeId) {
		return addType(typeId);
	}

	public String toString() {
		StringBuilder b = new StringBuilder("[");

		for (Iterator<Map.Entry<Enum<? extends FieldId>, DustVariant>> it = mapFields.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Enum<? extends FieldId>, DustVariant> e = it.next();
			b.append(e.getKey()).append(": ").append(e.getValue()).append("; ");
		}
		return b.append("]").toString();
	}
}
