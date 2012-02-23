package org.mondoaurora.frame.test;

import org.mondoaurora.frame.process.MAFProcess;

public class MAFTestProcessor implements MAFProcess {
	int count = 0;
	
	@Override
	public Object createContextObject(Object msg) {
		Object ctx = ++count;
		System.out.print(" relay start " + ctx);
		return ctx;
	}

	@Override
	public Return processEvent(Object event, Object ctx) {
		System.out.print(event);
		Return ret;
		char c = (Character) event;

		switch (c) {
		case '{':
			ret = new Return(ReturnType.Relay, this, true);
			break;
		case '}':
			System.out.print(" end " + ctx);
			ret = new Return(ReturnType.Success, this, true);
			break;
		default:
			ret = CONTINUE;
			break;
		}

		return ret;
	}

	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		return CONTINUE;
	}

}
