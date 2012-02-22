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
		
		public ReturnType getType() {
			return type;
		}
		
		public boolean isEventProcessed() {
			return eventProcessed;
		}
		
		public Object getOb() {
			return ob;
		}
	}
	
	public static final Return CONTINUE = new Return(ReturnType.Continue, null, true);
	
	public static final Return SUCCESS_RETRY = new Return(ReturnType.Success, null, false);
	public static final Return SUCCESS = new Return(ReturnType.Success, null, true);
	
	public static final Return FAILURE = new Return(ReturnType.Failure, null, false);
	
	Object createContextObject(Object msg);
	
	Return processEvent(Object event, Object ctx);
	void processRelayReturn(Return ob, Object ctx);
}
