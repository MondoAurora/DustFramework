package dust.frame.generic;

import dust.kernel.DustKernel;

public class KText implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_GENERIC,
			"Text");
	public static TypeInfo INFO = new TypeInfo(ID, true, new FieldInfo[] { new FieldInfo("name", VariantType.STRING,
			LEN_LONG), });

	public static class Data extends DustKernel.DataWrapper implements IText {
		@Override
		public String getText() {
			return (String) getFieldVariant(0).getData();
		}

		@Override
		public void setText(String text) {
			getFieldVariant(0).setData(text, VariantSetMode.set, null);
		}
	}
}
