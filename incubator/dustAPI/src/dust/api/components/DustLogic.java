package dust.api.components;

import dust.api.DustConstants.DustDeclId;
import dust.api.utils.DustUtils;

public abstract class DustLogic {
	DustAspect myAspect;
	DustDeclId myTypeId;
	
	final void initInt(DustDeclId typeId, DustAspect ob) throws Exception {
		myTypeId = typeId;
		myAspect = ob;
		init(myAspect);
	}
	
	final void processMessage(DustEntity from, DustMessage msg) throws Exception {
		processMessage(myAspect, from, msg);
	}
	
	protected final void send(DustAspect target, DustMessage msg) throws Exception {
		DustUtils.getWorld().send(myAspect.getEntity(), target, msg);
	}
	
	protected abstract void init(DustAspect myAspect) throws Exception;
	protected abstract void processMessage(DustAspect myAspect, DustEntity from, DustMessage msg) throws Exception;	
}
