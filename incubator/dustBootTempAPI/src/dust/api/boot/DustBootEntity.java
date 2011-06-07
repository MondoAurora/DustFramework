package dust.api.boot;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public class DustBootEntity implements DustEntity, DustAspect {
	DustDeclId typeId;
	
	Map<Enum<? extends FieldId>, DustVariant> mapFields = new HashMap<Enum<? extends FieldId>, DustVariant>();

	DustBootEntity(DustDeclId typeId, DustVariant[] fields) {
		this.typeId = typeId;
		
		for ( DustVariant v : fields ) {
			setVariant(v);
		}
	}
	
	void setVariant(DustVariant variant) {
		mapFields.put(variant.getId(), variant);
	}

	@Override
	public DustDeclId getPrimaryTypeId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<DustDeclId> getTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DustVariant getField(Enum<? extends FieldId> field) {
		return mapFields.get(field);
	}

	@Override
	public DustAspect getAspect(DustDeclId type) {
		return this;
	}

	@Override
	public DustEntity getEntity() {
		return this;
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
