package org.mondoaurora.frame.template;

import java.util.ArrayList;

import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFTemplateSequence extends MAFTemplateBase {
	ArrayList<MAFTemplate> content;
	
	public MAFTemplateSequence(MAFTemplate[] arrContent) {
		content = new ArrayList<MAFTemplate>(arrContent.length);
		
		for ( MAFTemplate t : arrContent ) {
			content.add(t);
		}
	}
	
	@Override
	public void initInt(MAFTemplateSyntax syntax) {
		for ( MAFTemplate t : content ) {
			t.init(syntax);
		}
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		for ( MAFTemplate t : content ) {
			t.writeInto(stream, var);
		}
	}
	
	class Ctx {
		int curr = 0;
		boolean go = true;
		Return ret = null;
	}

	@Override
	public Object createContextObject(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;
		
		return (context.go) ? new Return(ReturnType.Relay, content.get(context.curr++), false) : context.ret;
	}
	
	@Override
	public void processRelayReturn(Return ob, Object ctx) {
		Ctx context = (Ctx) ctx;
		context.ret = ob;
		context.go = ((ReturnType.Failure != ob.getType()) && (context.curr < content.size()));
	}

/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( MAFTemplate t : content ) {
			if ( ! t.parseFrom(stream, currentEntity) ) {
				return false;
			}
		}
		
		return true;
	}
*/
}
