package org.mondoaurora.frame.eval;

import org.mondoaurora.frame.shared.MAFStream.Out;
import org.mondoaurora.frame.shared.MAFVariant;

public abstract class MAFEvalBase implements MAFEval {

	@Override
	public Object createContextObject(Object msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Return processEvent(Object event, Object ctx) {
		return processChar((Character)event, ctx);
	}
	
	protected abstract Return processChar(char c, Object ctx);


	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		return FAILURE;
	}

	@Override
	public MAFVariant getVariant(MAFVariant var) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeContent(Out target, MAFVariant var) {
		// TODO Auto-generated method stub
		
	}

}
