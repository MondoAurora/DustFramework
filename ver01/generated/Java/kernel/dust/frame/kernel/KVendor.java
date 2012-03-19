package dust.frame.kernel;

import java.util.Map.Entry;

import dust.kernel.DustKernel;

public class KVendor implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL,
			"Vendor");
	
	public static TypeInfo INFO = new TypeInfo(ID, true, new FieldInfo[] { new FieldInfo("domains", VariantType.ARRAY,
			KDomain.INFO), });

	public static class Data extends DustKernel.DataWrapper implements IVendor {

		@Override
		public IName getName() {
			return (IName) getNeighbor(KName.INFO.id);
		}

		@Override
		public int getDomainCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Iterable<Entry<String, IDomain>> getDomainIter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearDomain() {
			// TODO Auto-generated method stub

		}

		@Override
		public IDomain getDomain(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addDomain(IDomain domain, String key) {
			getFieldVariant(0).setData(domain, VariantSetMode.addFirst, key);
		}

		@Override
		public void removeDomain(String key) {
			// TODO Auto-generated method stub

		}
	}

}
