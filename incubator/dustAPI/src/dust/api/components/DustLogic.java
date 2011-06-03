package dust.api.components;

import dust.api.utils.DustUtils;

public abstract class DustLogic {
	DustAspect myAspect;
	
	final void initInt(DustAspect ob) throws Exception {
		myAspect = ob;
		init(myAspect);
	}
	
	final void processMessage(DustAspect from, DustMessage msg) throws Exception {
		processMessage(myAspect, from, msg);
	}
	
	protected final void send(DustAspect target, DustMessage msg) throws Exception {
		DustUtils.getApi().send(myAspect, target, msg);
	}
	
	protected abstract void init(DustAspect myAspect) throws Exception;
	protected abstract void processMessage(DustAspect myObject, DustAspect from, DustMessage msg) throws Exception;	
}
