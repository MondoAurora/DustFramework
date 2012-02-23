package org.mondoaurora.frame.process;

import org.mondoaurora.frame.process.MAFProcess.Return;
import org.mondoaurora.frame.process.MAFProcess.ReturnType;

public class MAFProcessManager {
	private static final Object NOCONTEXT = new Object();
	
	class StackItem {
		Object context;
		MAFProcess process;

		MAFProcessEventSource src;
		Object mark;

		StackItem previous;

		private StackItem(MAFProcess process, MAFProcessEventSource src) {
			this.process = process;
			context = NOCONTEXT;
			this.src = src;
			mark = src.mark();
		}

		void end(Return r, Object event) {
			if (ReturnType.Success == r.type) {
				src.releaseMark(mark);
			} else {
				src.rollback(mark);
			}
		}

		Return processRelayReturn(Return ret, Object event) {
			return process.processRelayReturn(ret, context);
		}
		
		Return process(Object event) {
			if ( NOCONTEXT == context ) {
				context = process.createContextObject(event);
			}
			return process.processEvent(event, context);
		}
	};

	StackItem callStack;

	public void processEvent(MAFProcessEventSource src, Object event) {
		Return r = callStack.process(event);

		switch (r.type) {
		case Failure:
			end(r, event);
			return; //RETURN, NOT BREAK!
		case Success:
			end(r, event);
			break;
		case Relay:
			begin((MAFProcess) r.ob, src);
			break;
		}

		if ( !r.eventProcessed ) {
			processEvent(src, event);
		}
	}

	public void process(MAFProcess root, MAFProcessEventSource src) {
		begin(root, src);
		
		src.start(this);
	}
	
	void begin(MAFProcess process, MAFProcessEventSource src) {
//		System.out.println("ProcMan starting " + process);
		
		StackItem si = new StackItem(process, src);
		si.previous = callStack;
		callStack = si;			
	}

	void end(Return r, Object event) {
//		System.out.println("ProcMan releasing " + callStack.process);
		
		callStack.end(r, event);
		
		callStack = callStack.previous;
		
		if (null != callStack ) {
			r = callStack.processRelayReturn(r, event);
			switch (r.type) {
			case Failure:
			case Success:
				end(r, event);
				break;
			}
		}
	}

}
