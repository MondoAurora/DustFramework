package dust.api.components;

public abstract class DustWorld implements DustContext {
	public abstract DustDeclId getTypeId(Class<? extends TypeDef> type);
	public abstract DustDeclId getMessageId(Class<? extends MsgDef> type);
	
	public abstract DustVariant getVar(DustDeclId typeId, Enum<? extends FieldId> fieldId, Object value);
	
	public abstract DustMessage getMessage(DustDeclId msgId, DustVariant[] fields);
		
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
