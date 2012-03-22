package dust.frame.kernel;

import java.util.*;

import dust.kernel.DustKernel;
import dust.shared.DustRuntimeException;
import dust.shared.DustUtils;

public abstract class DustKernelVariant implements DustKernel.Variant, DustKernelConsts {
	Object data;
	Object key;
	
	public static class Field extends DustKernelVariant {
		DustKernelField field;
		
		Field(DustKernelField field) {
			this.field = field;
			if ( null != field.defValue ) {
				setData(field.defValue, VariantSetMode.set, null);
			}
		}
		
		@Override
		public String getName() {
			return field.name;
		}

		@Override
		public VariantType getType() {
			return field.type;
		}
		
		@Override
		public boolean isNull() {
			return super.isNull() || DustUtils.isEqual(field.defValue, data);
		}
	}


	public static class External extends DustKernelVariant {
		String name;
		VariantType type;
		
		External(String name,	VariantType type) {
			this.name = name;
			this.type = type;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public VariantType getType() {
			return type;
		}
	}


	@Override
	public Object getKey() {
		return key;
	}

	@Override
	public boolean isNull() {
		if (null == data) {
			return true;
		} else {
			switch (getType()) {
			case ARRAY:
			case SET:
				return ((Collection<?>) data).isEmpty();
			default:
				return false;
			}
		}
	}

	@Override
	public void loadData(Variant from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getData() {
		return data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<? extends DustKernel.Variant> getMembers() {
		switch (getType()) {
		case SET:
		case ARRAY:
			return ((Iterable<? extends DustKernel.Variant>) data);
		}

		return null;
	}

	Object getDataAssertType(VariantType reqType) {
		if (getType() != reqType) {
			throw new DustRuntimeException("DustKernelVariant", "required " + reqType + ", actual " + getType());
		}
		return data;
	}

	public void setRef(Object value) {
		getDataAssertType(VariantType.REFERENCE);
//		data = ((DustKernel.DataWrapper) value).getData();
		data = value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setData(Object value, VariantSetMode mode, Object key) {
		VariantType type = getType();
		DustKernelVariant v1;

		switch (type) {
		case SET:
			if (null == data) {
				data = new HashSet<DustKernelVariant>();
			}
			
			v1 = new DustKernelVariant.External(getName(), VariantType.REFERENCE);
			v1.setRef(value);
			((Set<DustKernelVariant>) data).add(v1);

			break;
		case ARRAY:
			if (null == data) {
				data = new ArrayList<DustKernelVariant>();
			}
			v1 = new DustKernelVariant.External(getName(), VariantType.REFERENCE);
			v1.setRef(value);
			((ArrayList<DustKernelVariant>) data).add(v1);

			break;
		case REFERENCE:
			setRef(value);
			break;
		default:
			this.data = value;
		}
	}
}
