package org.mondoaurora.frame.process;

import org.mondoaurora.frame.process.MAFProcess.Return;

public class MAFProcessManager {
	class StackItem {
		Object context;
		MAFProcess process;

		MAFProcessEventSource src;
		Object mark;

		StackItem previous;

		public StackItem(MAFProcess process, MAFProcessEventSource src, Object event) {
			this.process = process;
			context = process.createContextObject(event);
			this.src = src;
			mark = src.mark();

			previous = callStack;
			callStack = this;
		}

		void processRelayReturn(Return ret, Object event) {
			process.processRelayReturn(ret, context);
		}

		void process(Object event) {
			Return r = process.processEvent(event, context);

			switch (r.type) {
			case Success:
				src.releaseMark(mark);
				callStack = previous;
				if (null != callStack ) {
					callStack.processRelayReturn(r, event);
				}
				if ( !r.eventProcessed ) {
					callStack.process(event);
				}
				break;
			case Failure:
				src.rollback(mark);
				
				callStack = previous;
				if (null != callStack ) {
					callStack.processRelayReturn(r, event);
				}
				break;
			case Relay:
				StackItem si = new StackItem((MAFProcess) r.ob, src, event);
				if ( !r.eventProcessed ) {
					si.process(event);
				}
				break;
			}

		}
	};

	MAFProcess root;
	StackItem callStack;

	public void processEvent(MAFProcessEventSource src, Object event) {
		if (null == callStack) {
			new StackItem(root, src, event);
		}

		callStack.process(event);
	}

	public void process(MAFProcess root, MAFProcessEventSource src) {
		this.root = root;
		src.start(this);
	}
}
