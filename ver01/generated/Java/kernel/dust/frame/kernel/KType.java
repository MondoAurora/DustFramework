package dust.frame.kernel;

import java.util.Collection;

import dust.kernel.DustKernel;
import dust.shared.*;
import dust.shared.DustLogic.Return;

public class KType implements DustKernel, Consts {
	public static final DustIdentifier ID = Environment
			.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_KERNEL, "Type");
	public static TypeInfo INFO = new TypeInfo(ID, true,
			new FieldInfo[] { new FieldInfo("referrable", VariantType.BOOLEAN, false),
					new FieldInfo("fields", VariantType.ARRAY, KField.INFO), }, null);

	public static class Data extends DustKernel.DataWrapper implements IType {

		@Override
		public IName getName() {
			return (IName) getNeighbor(KName.INFO.id);
		}

		@Override
		public IUnit getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean getReferrable() {
			return (Boolean) getFieldVariant(0).getData();
		}

		@Override
		public void setReferrable(boolean referrable) {
			getFieldVariant(0).setData(referrable, VariantSetMode.set, null);
		}

		@Override
		public int getFieldCount() {
			return isNull(1) ? 0 : ((Collection<?>) getFieldVariant(1).getData()).size();
		}

		@Override
		public Iterable<IField> getFieldIter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearFieldArray() {
			// TODO Auto-generated method stub

		}

		@Override
		public void addField(IField field, int idx) {
			getFieldVariant(1).setData(field, VariantSetMode.addFirst, null);
		}

		@Override
		public void removeField(int idx) {
			// TODO Auto-generated method stub

		}
	}

	public static class Logic extends DustKernel.LogicWrapper<SType, Enum<?>> {
		@SuppressWarnings("unchecked")
		@Override
		public void init(DustObject ob) {
			((DustLogic<IType, Enum<?>>)getLogic()).init((IType)ob);
		}
		@Override
		public Return processMessage(Enum<?> msgId, DustObject msgOb, Object ctx) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
