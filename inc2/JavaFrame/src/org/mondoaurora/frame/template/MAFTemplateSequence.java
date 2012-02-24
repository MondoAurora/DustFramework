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
		int i = 0;
		for (MAFTemplate t : content) {
			t.init(syntax, this, String.valueOf(++i));
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
	protected Object createContextObjectInt(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;
		return new Return(ReturnType.Relay, content.get(context.curr), false);
	}

	@Override
	protected Return processRelayReturnInt(Return ob, Object ctx) {
		Ctx context = (Ctx) ctx;

		return (ReturnType.Success == ob.getType()) ? (++context.curr == content.size()) ? SUCCESS : CONTINUE : ob;

	}

	@Override
	protected String toStringInt() {
		return "{ " + MAFUtils.iter2str(content) + " }";
	}

}
