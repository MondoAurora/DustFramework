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
		separator.init(syntax);
		content.init(syntax);
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

	/*
	 * @Override protected boolean parseFromInt(DustStream stream, DustEntity
	 * currentEntity) throws Exception { DustAspect asp =
	 * currentEntity.getAspect(typeId, false);
	 * 
	 * DustVariant var = createVar(); DustEntity e = var.getValueObject();
	 * 
	 * while (content.parseFrom(stream, e)) { asp.getField(field).setData(e,
	 * 
	 * VariantSetMode.insert, null); var = createVar(); }
	 * 
	 * return true; }
	 */
}
