package dust.api.boot;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public class DustBootEntity implements DustEntity {
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
}
