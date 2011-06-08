package dust.api.components;

import dust.api.DustConstants;

public interface DustAspect extends DustConstants {
	DustDeclId getType();
	DustVariant getField(Enum<? extends FieldId> field);
	DustEntity getEntity();
}
