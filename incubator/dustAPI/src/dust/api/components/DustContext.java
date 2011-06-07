package dust.api.components;

import dust.api.DustConstants;

public interface DustContext extends DustConstants {
	void invoke(
		InvokeResponseProcessor irProc,
		DustDeclId primaryType, 
		DustVariant[] knownFields,
		boolean createIfMissing,
		Enum<? extends FieldId>[] requiredFields,
		DustEntity filter
	);
	
	void send(DustAspect to, DustMessage msg) throws Exception;	
}
