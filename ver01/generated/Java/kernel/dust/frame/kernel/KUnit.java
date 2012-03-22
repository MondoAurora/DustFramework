package dust.frame.kernel;

import java.util.Map.Entry;

import dust.kernel.DustKernel;

public class KUnit implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment
			.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL, "Unit");
	public static TypeInfo INFO = new TypeInfo(ID, true, new FieldInfo[] { new FieldInfo("types", VariantType.ARRAY,
			KType.INFO), }, null);

	public static class Data extends DustKernel.DataWrapper implements IUnit {

		@Override
		public IName getName() {
			return (IName) getNeighbor(KName.INFO.id);
		}

		@Override
		public IDomain getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getTypeCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Iterable<Entry<String, IType>> getTypeIter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearType() {
			// TODO Auto-generated method stub

		}

		@Override
		public IType getType(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addType(IType type, String key) {
			getFieldVariant(0).setData(type, VariantSetMode.addFirst, key);
		}

		@Override
		public void removeType(String key) {
			// TODO Auto-generated method stub

		}
	}
}
