package dust.api.components;

import java.util.Enumeration;

import dust.api.DustConstants;

public abstract class DustAPI implements DustConstants {
	public abstract DustDeclId getTypeId(Class<? extends TypeDef> type);
	public abstract DustDeclId getMessageId(Class<? extends MsgDef> type);
	
	public abstract DustVariant getVar(Enum<? extends FieldId> fieldId, FieldType fieldType, Object value);
	
	public abstract DustVariantStructure getVarStruct(DustDeclId typeId, DustVariant[] fields);
	public abstract DustMessage getMessage(DustDeclId msgId, DustVariant[] fields);
	
	public abstract DustEntity getEntity(DustDeclId primaryType, DustInstanceId instId);
	public abstract Enumeration<DustEntity> getEntities(DustDeclId primaryType, DustVariantStructure[] aspects);
	
	public void send(DustAspect to, DustMessage msg) throws Exception {
		send(null, to, msg);
	}
	
	protected abstract void send(DustAspect from, DustAspect to, DustMessage msg) throws Exception;

	protected void initLogic(DustLogic logic, DustAspect asp) throws Exception {
		logic.initInt(asp);
	}
	
	protected void callProcess(DustLogic logic, DustAspect from, DustMessage msg) throws Exception {
		logic.processMessage(from, msg);
	}
}
