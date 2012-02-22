package org.mondoaurora.frame.process;

public interface MAFProcess {
	enum ReturnType {	Continue, Success, Relay, Failure	} ;
	
	class Return {
		ReturnType type;
		Object ob;
		boolean eventProcessed;

		public Return(ReturnType type, Object ob, boolean eventProcessed) {
			this.type = type;
			this.ob = ob;
			this.eventProcessed = eventProcessed;
		}
	}
	
	public static final Return CONTINUE = new Return(ReturnType.Continue, null, true);
	
	Object createContextObject(Object msg);
	
	Return processEvent(Object event, Object ctx);
	void processRelayReturn(Return ob, Object ctx);
}
