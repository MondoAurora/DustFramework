package dust.shared;


public interface DustLogic <Data, MsgType extends Enum<?>> extends DustConsts {
	
	enum ReturnType {	Continue, Success, Failure, Relay, Pass	} ;
	
	public static class Return {
		ReturnType type;
		DustObject ob;
		boolean eventProcessed;

		public Return(ReturnType type, DustObject ob, boolean eventProcessed) {
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
		
		@Override
		public String toString() {
			return type.toString() + " " + (eventProcessed ? "processed " : "retry ") + ob;
		}
	}

	public void init(Data config);	
	
	Object processContextCreated(MsgType msg);
	Return processRelayReturn(Return ob, Object ctx);	
	void processContextClosed(Object ctx);

	public abstract static class Simple <Data> implements DustLogic <Data, Enum<?>> {
		@Override
		public Object processContextCreated(Enum<?> msg) {
			return null;
		}

		@Override
		public dust.shared.DustLogic.Return processRelayReturn(dust.shared.DustLogic.Return ob, Object ctx) {
			return null;
		}

		@Override
		public void processContextClosed(Object ctx) {
		}
	}
}
