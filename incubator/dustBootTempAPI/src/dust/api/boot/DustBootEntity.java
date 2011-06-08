package dust.api.boot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
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
			return mapFields.get(field);
		}

		@Override
		public DustEntity getEntity() {
			return DustBootEntity.this;
		}	
	}
	
	DustDeclId primaryTypeId;
	
	Map<Enum<? extends FieldId>, DustVariant> mapFields = new HashMap<Enum<? extends FieldId>, DustVariant>();
	Map<DustDeclId, MyAspect> mapTypes = new HashMap<DustDeclId, MyAspect>();

	DustBootEntity(DustDeclId typeId, DustVariant[] fields) {
		this.primaryTypeId = typeId;
		addType(primaryTypeId);
		
		for ( DustVariant v : fields ) {
			setVariant(v);
		}
	}
	
	void addType(DustDeclId typeId) {
		if ( !mapTypes.containsKey(typeId) ) {
			mapTypes.put(typeId, new MyAspect(typeId));
			((DustBootWorld)DustUtils.getWorld()).addEntity(typeId, this);
		}
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
		return EntityState.Creating;
	}

	@Override
	public EntityType getType() {
		return EntityType.Temporal;
	}

	@Override
	public DustAspect getAspect(DustDeclId type) {
		return mapTypes.get(type);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder("[");
		
		for ( Iterator<Map.Entry<Enum<? extends FieldId>, DustVariant>> it = mapFields.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<Enum<? extends FieldId>, DustVariant> e = it.next();
			b.append(e.getKey()).append(": ").append(e.getValue()).append("; ");
		}
		return b.append("]").toString();
	}
}
