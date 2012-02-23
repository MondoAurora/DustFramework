package org.mondoaurora.frame.template;

import java.util.ArrayList;

import org.mondoaurora.frame.shared.*;

public class MAFTemplateSequence extends MAFTemplateBase {
	ArrayList<MAFTemplate> content;

	public MAFTemplateSequence(MAFTemplate[] arrContent) {
		content = new ArrayList<MAFTemplate>(arrContent.length);

		for (MAFTemplate t : arrContent) {
			content.add(t);
		}
	}

	@Override
	public void initInt(MAFTemplateSyntax syntax) {
		for (MAFTemplate t : content) {
			t.init(syntax);
		}
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		for (MAFTemplate t : content) {
			t.writeInto(stream, var);
		}
	}

	class Ctx {
		int curr = 0;
	}

	@Override
	public Object createContextObject(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;
		return new Return(ReturnType.Relay, content.get(context.curr), false);
	}

	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		Ctx context = (Ctx) ctx;

		return (ReturnType.Success == ob.getType()) ? (++context.curr == content.size()) ? SUCCESS : CONTINUE : ob;

	}

	@Override
	public String toString() {
		return "{ " + MAFUtils.iter2str(content) + " }";
	}

	/*
	 * @Override protected boolean parseFromInt(DustStream stream, DustEntity
	 * currentEntity) throws Exception { for ( MAFTemplate t : content ) { if ( !
	 * t.parseFrom(stream, currentEntity) ) { return false; } }
	 * 
	 * return true; }
	 */
}
