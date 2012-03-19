package dust.frame.kernel;

import java.util.Map.Entry;

import dust.kernel.DustKernel;

public class KDomain implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL,
			"Domain");
	public static TypeInfo INFO = new TypeInfo(ID, true, new FieldInfo[] { new FieldInfo("units", VariantType.ARRAY,
			KUnit.INFO), });

	public static class Data extends DustKernel.DataWrapper implements IDomain {
		@Override
		public IName getName() {
			return (IName) getNeighbor(KName.INFO.id);
		}

		@Override
		public IVendor getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getUnitCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Iterable<Entry<String, IUnit>> getUnitIter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearUnit() {
			// TODO Auto-generated method stub

		}

		@Override
		public IUnit getUnit(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addUnit(IUnit unit, String key) {
			getFieldVariant(0).setData(unit, VariantSetMode.addFirst, key);
		}

		@Override
		public void removeUnit(String key) {
			// TODO Auto-generated method stub

		}
	}
}
