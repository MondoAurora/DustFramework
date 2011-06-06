package dust.api.components;

import dust.api.DustConstants;


public interface DustMessage extends DustConstants {
	DustDeclId getTypeId();
	DustDeclId getMessageId();
	
	DustVariant getField(Enum<? extends FieldId> field);
}
