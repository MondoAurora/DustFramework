package dust.api.components;

import dust.api.DustConstants;

public abstract class DustWorld implements DustConstants {
	public abstract DustDeclId getTypeId(Class<? extends TypeDef> type);
	public abstract DustDeclId getMessageId(Class<? extends MsgDef> type);
	
	public abstract DustVariant getVar(Enum<? extends FieldId> fieldId, FieldType fieldType, Object value);
	
	public abstract DustMessage getMessage(DustDeclId msgId, DustVariant[] fields);
	
	public abstract void invoke(
		InvokeResponseProcessor irProc,
		DustDeclId primaryType, 
		DustVariant[] knownFields,
		boolean createIfMissing,
		Enum<? extends FieldId>[] requiredFields,
		DustEntity filter
	);
	
	public void send(DustAspect to, DustMessage msg) throws Exception {
		send(null, to, msg);
	}
	
	protected abstract void send(DustEntity from, DustAspect to, DustMessage msg) throws Exception;

	protected void initLogic(DustLogic logic, DustDeclId typeId, DustAspect aspect) throws Exception {
		logic.initInt(typeId, aspect);
	}
	
	protected void callProcess(DustLogic logic, DustEntity from, DustMessage msg) throws Exception {
		logic.processMessage(from, msg);
	}
}
