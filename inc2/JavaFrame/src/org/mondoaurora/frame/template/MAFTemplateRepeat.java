package org.mondoaurora.frame.template;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFTemplateRepeat extends MAFTemplateBase {
	MAFEval evalRepeat;
	MAFTemplate separator;
	MAFTemplate content;

	public MAFTemplateRepeat(MAFEval evalRepeat, MAFTemplate content, MAFTemplate separator) {
		this.evalRepeat = evalRepeat;
		this.content = content;
		this.separator = separator;
	}

	@Override
	public void initInt(MAFTemplateSyntax syntax) {
		separator.init(syntax, this, MEMBER_REP_SEP);
		content.init(syntax, this, MEMBER_REP_CONT);
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var_) {
		MAFVariant var = (null == evalRepeat) ? var_ : evalRepeat.getVariant(var_);

		if (!var.isNull()) {
			boolean concat = false;
			for (MAFVariant repFld : var.getMembers()) {
				if (!repFld.isNull()) {
					if (concat) {
						if (null != separator) {
							separator.writeInto(stream, repFld);
						}
					} else {
						concat = true;
					}
					content.writeInto(stream, repFld);
				}
			}
		}
	}

	class Ctx {
		boolean readContent = true;
	}

	@Override
	protected Object createContextObjectInt(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;

		return new Return(ReturnType.Relay, context.readContent ? content : separator, false);
	}

	@Override
	protected Return processRelayReturnInt(Return ob, Object ctx) {
		Ctx context = (Ctx) ctx;

		if (null != separator) {
			context.readContent = !context.readContent;
		}

		return (ReturnType.Failure == ob.getType()) ? SUCCESS : CONTINUE;
	}
	
	@Override
	protected String toStringInt() {
		return "(" + content + " | " + separator + ")*";
	}
}
