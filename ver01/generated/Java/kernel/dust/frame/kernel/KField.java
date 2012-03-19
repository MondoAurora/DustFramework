package dust.frame.kernel;

import java.util.EnumSet;

import dust.kernel.DustKernel;
import dust.shared.*;
import dust.shared.DustLogic.Return;

public class KField implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL,
			"Field");
	public static TypeInfo INFO = new TypeInfo(ID, false, new FieldInfo[] {
			new FieldInfo("type", EnumSet.allOf(VariantType.class)), new FieldInfo("length", VariantType.INTEGER),
			new FieldInfo("obType", VariantType.STRING, LEN_LONG), new FieldInfo("values", VariantType.STRING, LEN_LONG),
			new FieldInfo("defValue", VariantType.STRING, LEN_LONG), });

	public static class Data extends DustKernel.DataWrapper implements IField {
		@Override
		public IName getName() {
			return (IName) getNeighbor(KName.INFO.id);
		}

		@Override
		public String getType() {
			return (String) getFieldVariant(0).getData();
		}

		@Override
		public void setType(String type) {
			getFieldVariant(0).setData(type, VariantSetMode.set, null);
		}

		@Override
		public int getLength() {
			return (Integer) getFieldVariant(1).getData();
		}

		@Override
		public void setLength(int length) {
			getFieldVariant(1).setData(length, VariantSetMode.set, null);
		}

		@Override
		public String getObType() {
			return (String) getFieldVariant(2).getData();
		}

		@Override
		public void setObType(String obType) {
			getFieldVariant(2).setData(obType, VariantSetMode.set, null);
		}

		@Override
		public String getEnumNames() {
			return (String) getFieldVariant(3).getData();
		}

		@Override
		public void setEnumNames(String enumNames) {
			getFieldVariant(3).setData(enumNames, VariantSetMode.set, null);
		}

		@Override
		public String getDefValue() {
			return (String) getFieldVariant(4).getData();
		}

		@Override
		public void setDefValue(String defValue) {
			getFieldVariant(4).setData(defValue, VariantSetMode.set, null);
		}
	}

	public static class Logic extends DustKernel.LogicWrapper<SField, Enum<?>> {
		@SuppressWarnings("unchecked")
		@Override
		public void init(DustObject ob) {
			((DustLogic<IField, Enum<?>>)getLogic()).init((IField)ob);
		}
		@Override
		public Return processMessage(Enum<?> msgId, DustObject msgOb, Object ctx) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
