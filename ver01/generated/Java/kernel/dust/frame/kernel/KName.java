package dust.frame.kernel;

import dust.kernel.DustKernel;

public class KName implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment
			.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL, "Name");
	public static TypeInfo INFO = new TypeInfo(ID, true, new FieldInfo[] { new FieldInfo("name", VariantType.STRING,
			LEN_LONG), });

	public static class Data extends DustKernel.DataWrapper implements IName {
		@Override
		public String getName() {
			return (String) getFieldVariant(0).getData();
		}

		@Override
		public void setName(String name) {
			getFieldVariant(0).setData(name, VariantSetMode.set, null);
		}
	}
}
