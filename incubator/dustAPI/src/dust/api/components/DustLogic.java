package dust.api.components;

import dust.api.DustConstants.DustDeclId;
import dust.api.utils.DustUtils;

public abstract class DustLogic {
	DustEntity myEntity;
	DustDeclId myTypeId;
	
	final void initInt(DustDeclId typeId, DustEntity ob) throws Exception {
		myTypeId = typeId;
		myEntity = ob;
		init(myEntity);
	}
	
	final void processMessage(DustEntity from, DustMessage msg) throws Exception {
		processMessage(myEntity, from, msg);
	}
	
	protected final void send(DustEntity target, DustMessage msg) throws Exception {
		DustUtils.getWorld().send(myEntity, target, msg);
	}
	
	protected abstract void init(DustEntity myAspect) throws Exception;
	protected abstract void processMessage(DustEntity myObject, DustEntity from, DustMessage msg) throws Exception;	
}
