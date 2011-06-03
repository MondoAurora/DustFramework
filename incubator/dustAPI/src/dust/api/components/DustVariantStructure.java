package dust.api.components;

import dust.api.DustConstants;

public interface DustVariantStructure extends DustConstants {
	DustDeclId getTypeId();
	
	DustVariant getField(Enum<? extends FieldId> field);
	DustVariant getField(Enum<? extends FieldId> field, FieldAccessHint hint);
}
