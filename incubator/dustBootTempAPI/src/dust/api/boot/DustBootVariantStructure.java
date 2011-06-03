package dust.api.boot;

import java.util.HashMap;
import java.util.Map;

import dust.api.components.DustVariant;
import dust.api.components.DustVariantStructure;

public class DustBootVariantStructure implements DustVariantStructure {
	DustDeclId typeId;
	
	Map<Enum<? extends FieldId>, DustVariant> mapFields = new HashMap<Enum<? extends FieldId>, DustVariant>();

	DustBootVariantStructure(DustDeclId typeId, DustVariant[] fields) {
		this.typeId = typeId;
		
		for ( DustVariant v : fields ) {
			setVariant(v);
		}
	}
	
	@Override
	public DustDeclId getTypeId() {
		return typeId;
	}

	@Override
	public DustVariant getField(Enum<? extends FieldId> field) {
		return mapFields.get(field);
	}

	@Override
	public DustVariant getField(Enum<? extends FieldId> field, FieldAccessHint hint) {
		return getField(field);
	}

	void setVariant(DustVariant variant) {
		mapFields.put(variant.getId(), variant);
	}
}
