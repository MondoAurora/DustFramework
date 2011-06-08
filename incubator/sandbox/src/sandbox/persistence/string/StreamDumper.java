package sandbox.persistence.string;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import dust.api.DustConstants;
import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtilVariant;
import dust.api.utils.DustUtils;
import dust.api.wrappers.DustIdentifier;
import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;
import dust.units.dust.kernel.v0_1.TypeManagement.Field;

public class StreamDumper {
	private static String indent = "  ";
	StringBuffer header = new StringBuffer();

	DustWorld world = DustUtils.getWorld();

	DustDeclId idIdentified = world.getTypeId(Common.Identified.class);
	DustDeclId idFieldContainer = world.getTypeId(Common.FieldContainer.class);

	DustDeclId idType = world.getTypeId(TypeManagement.Type.class);
	DustDeclId idField = world.getTypeId(TypeManagement.Field.class);

	Map<DustDeclId, Map<Enum<? extends FieldId>, Field.Values.FieldType>> mapIdToFields = new HashMap<DustConstants.DustDeclId, Map<Enum<? extends FieldId>, Field.Values.FieldType>>();

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
		DustAspect target = entity.getAspect(idAspType);

		newLine(w);
		w.print("Aspect ");
		w.print(idAspType.getIdentifier());
		w.print(" [");

		Map<Enum<? extends FieldId>, Field.Values.FieldType> mapFields = getMapFields(idAspType);
		if (null == mapFields) {
			w.print(" NO FIELDS");
		} else {
			incIndent();

			for (Map.Entry<Enum<? extends FieldId>, Field.Values.FieldType> eFld : mapFields.entrySet()) {
				newLine(w);

				w.print("Value ");
				w.print(eFld.getKey().name());

				DustVariant vTargetFld = target.getField(eFld.getKey());

				if ((null == vTargetFld) || vTargetFld.isNull()) {
					w.print(" is null ");
				} else {
					w.print(" = ");

					switch (eFld.getValue()) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	Map<Enum<? extends FieldId>, Field.Values.FieldType> getMapFields(DustDeclId id) throws Exception {
		Map<Enum<? extends FieldId>, Field.Values.FieldType> ret = mapIdToFields.get(id);

		if (null == ret) {
			DustEntity eType = DustUtils.getEntity(idType, new DustVariant[] { new DustUtilVariant(
				Common.Identified.Fields.Identifier, new DustIdentifier(id.toString())) });
			DustAspect aspFields = eType.getAspect(idFieldContainer);
			if (null != aspFields) {
				DustVariant varFields = aspFields.getField(Common.FieldContainer.Fields.Fields);

				if (!varFields.isNull()) {
					ret = new HashMap<Enum<? extends FieldId>, TypeManagement.Field.Values.FieldType>();

					for (DustVariant vFld : varFields.getMembers()) {
						DustEntity eField = vFld.getValueObject();
						DustIdentifier fldId = eField.getAspect(idIdentified).getField(Common.Identified.Fields.Identifier)
							.getValueIdentifier();

						Class c = Class.forName(id.getIdentifier().toString() + "$Fields");
						Enum e = Enum.valueOf(c, fldId.toString());

						DustAspect fld = vFld.getValueObject().getAspect(idField);
						Field.Values.FieldType ft = fld.getField(TypeManagement.Field.Fields.FieldType).getValueValSet(
							Field.Values.FieldType.class);

						ret.put(e, ft);
					}
				}
			}

			mapIdToFields.put(id, ret);
		}

		return ret;
	}
}
