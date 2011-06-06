package dust.api.components;

import dust.api.DustConstants;

public interface DustAspect extends DustConstants {
	DustVariant getField(Enum<? extends FieldId> field);
	DustEntity getEntity();
}
