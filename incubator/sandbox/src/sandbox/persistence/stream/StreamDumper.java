package sandbox.persistence.stream;

import java.io.PrintStream;

import dust.api.DustConstants.DustDeclId;
import dust.api.components.*;
import dust.api.utils.DustUtils;

import sandbox.persistence.PersistenceValueExtractor;

public class StreamDumper {
	private static String indent = "  ";
	StringBuffer header = new StringBuffer();
	
	PersistenceValueExtractor valReader = new PersistenceValueExtractor();

	public void dump(DustEntity entity) {
		dump(entity, System.out);
	}

	public void dump(DustEntity entity, PrintStream w) {
		DustDeclId idPType = entity.getPrimaryTypeId();

		newLine(w);
		w.print("Entity [");
		w.print(idPType.getIdentifier());
		w.print("] {");

		incIndent();

		try {
			dumpAspect(entity, idPType, w);

			for (DustDeclId idAspType : entity.getTypes()) {
				if (!DustUtils.isEqual(idPType, idAspType)) {
					dumpAspect(entity, idAspType, w);
				}
			}

			decIndent();
			newLine(w);
			w.print("}");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dumpAspect(DustEntity entity, DustDeclId idAspType, PrintStream w) throws Exception {
		DustAspect target = entity.getAspect(idAspType, false);

		newLine(w);
		w.print("Aspect ");
		w.print(idAspType.getIdentifier());
		w.print(" [");

		if ((null != target) && valReader.hasFields(target)) {
			incIndent();
			
			for ( PersistenceValueExtractor.Value val : valReader.getFields(target) ) {
				newLine(w);

				w.print("Value ");
				w.print(val.getId().name());

				DustVariant vTargetFld = val.getValue();

				if ((null == vTargetFld) || vTargetFld.isNull()) {
					w.print(" is null ");
				} else {
					w.print(" = ");

					switch (val.getType()) {
					case Identifier:
						w.print(vTargetFld.getValueIdentifier());
						break;
					case Integer:
						w.print(vTargetFld.getValueInteger());
						break;
					case ObSet:
						w.print("Set of {");
						incIndent();
						for (DustVariant varMem : vTargetFld.getMembers()) {
							dump(varMem.getValueObject(), w);
						}
						decIndent();
						w.println();
						w.print(header);
						w.print("}");
						break;
					}
				}
			}

			decIndent();
			newLine(w);
		} else {
			w.print(" NO FIELDS");
		}

		w.print("]");
	}

	void newLine(PrintStream w) {
		w.println();
		w.print(header);
	}

	void incIndent() {
		header.append(indent);
	}

	void decIndent() {
		// ugly to add to the end and delete from start, but ok for a temp dump
		header.delete(0, indent.length());
	}

}
