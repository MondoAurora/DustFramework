package sandbox.template;

import sandbox.evaluator.DustEvaluator;
import sandbox.stream.DustStream;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public class DustTemplateRepeat extends DustTemplateBase {
	DustEvaluator evalRepeat;
	DustTemplate separator;
	DustTemplate content;

	DustVariant lastVar;

	public DustTemplateRepeat(DustEvaluator evalRepeat, DustTemplate content, DustTemplate separator) {
		this.evalRepeat = evalRepeat;
		this.content = content;
		this.separator = separator;
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		DustVariant var = evalRepeat.getVariant(currentEntity);

		if (!var.isNull()) {
			boolean concat = false;
			for (DustVariant repFld : var.getMembers()) {
				if (concat) {
					separator.writeInto(stream, repFld.getValueObject());
				} else {
					concat = true;
				}
				content.writeInto(stream, repFld.getValueObject());
			}
		}
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		/*
		 * DustAspect asp = currentEntity.getAspect(typeId, false);
		 * 
		 * DustVariant var = createVar(); DustEntity e = var.getValueObject();
		 * 
		 * while (content.parseFrom(stream, e)) { asp.getField(field).setData(e,
		 * VariantSetMode.insert, null); var = createVar(); }
		 */
		return true;

	}

	DustVariant createVar() {
		if (null == lastVar) {
			lastVar = null; // here I have to generate a Variant according to the new
											// variant's type
		}
		return lastVar;
	}
}
