package dust.frame.generic;

import dust.kernel.DustKernel;

public class KChar implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_GENERIC,
			"Char");
	public static TypeInfo INFO = new TypeInfo(ID, true, new FieldInfo[] { new FieldInfo("name", VariantType.STRING,
			LEN_LONG), });

	public static class Data extends DustKernel.DataWrapper implements IChar {
		@Override
		public char getChar() {
			return (char) getFieldVariant(0).getData();
		}

		@Override
		public void setChar(char char_) {
			getFieldVariant(0).setData(char_, VariantSetMode.set, null);
		}
	}
}
