package org.mondoaurora.frame.test;

import org.mondoaurora.frame.process.MAFProcess;

public class MAFTestProcessor implements MAFProcess {

	@Override
	public Object createContextObject(Object msg) {
		return null;
	}

	@Override
	public Return processEvent(Object event, Object ctx) {
		System.out.print(event);
		return CONTINUE;
	}

	@Override
	public void processRelayReturn(Return ob, Object ctx) {
		
	}

}
