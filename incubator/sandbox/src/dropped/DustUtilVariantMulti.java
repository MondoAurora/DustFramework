package dropped;

import java.util.HashMap;
import java.util.Iterator;

import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.utils.DustUtilInitValue;
import dust.api.wrappers.DustByteBuffer;
import dust.api.wrappers.DustDateImmutable;
import dust.api.wrappers.DustIdentifier;

public abstract class DustUtilVariantMulti implements DustVariant {
	Enum<? extends FieldId> fieldId;
	
	static enum ContentType {
		Unknown, Single, Array, Set, Map
	};
	
	ContentType cType = ContentType.Unknown;

	protected DustUtilVariantMulti(Enum<? extends FieldId> fieldId) {
		this.fieldId = fieldId;
	}

	public Enum<? extends FieldId> getId() {
		return fieldId;
	}

	protected abstract Object getData();

	public abstract void setData(Object value, VariantSetMode mode, String key);

	public DustIdentifier getValueIdentifier() {
		return (DustIdentifier) getData();
	}

	public boolean getValueBoolean() {
		return (Boolean) getData();
	}

	public Long getValueLong() {
		return (Long) getData();
	}

	public Double getValueDouble() {
		return (Double) getData();
	}

	public DustDateImmutable getValueDate() {
		return (DustDateImmutable) getData();
	}

	public String getValueString() {
		return (String) getData();
	}

	public String getValueValSet() {
		return (String) getData();
	}

	public <T extends Enum<T>> T getValueValSet(Class<T> enumType) {
		return Enum.valueOf(enumType, getValueValSet());
	}

	public DustByteBuffer getValueBuffer() {
		return (DustByteBuffer) getData();
	}

	public DustEntity getValueObject() {
		return (DustEntity) getData();
	}

	public void setValueBoolean(boolean val) {
		setData(val, VariantSetMode.set, null);
	}

	public void setValueIdentifier(DustIdentifier val) {
		setData(val, VariantSetMode.set, null);
	}

	public void setValueString(String val) {
		setData(val, VariantSetMode.set, null);
	}

	public void setValueValSet(String val) {
		setData(val, VariantSetMode.set, null);
	}

	public void setValueValSet(Enum<?> val) {
		setValueValSet(val.name());
	}

	public static class Single extends DustUtilVariantMulti {
		Object data;

		public Single(Enum<? extends FieldId> fieldId, Object data) {
			super(fieldId);
			this.data = data;
		}

		public boolean isNull() {
			return (null == data);
		}

		protected Object getData() {
			return data;
		}

		public void setData(Object value, VariantSetMode mode, String key) {
			this.data = value;
		}

		public void loadContent(DustVariant from) {
			this.data = ((DustUtilVariantMulti.Single) from).getData();
		}

		@Override
		public Iterator<DustVariant> iterator() {
			throw new IllegalAccessError("Not a collection!");
		}
	}

	protected static abstract class MultiVal extends DustUtilVariantMulti {
		protected MultiVal(Enum<? extends FieldId> fieldId) {
			super(fieldId);
		}

		@Override
		public boolean isNull() {
			return ((java.util.Collection<?>) getData()).isEmpty();
		}
	}

	protected static class ValMap extends MultiVal {
		private HashMap<String, DustVariant> mapContent = new HashMap<String, DustVariant>();

		protected ValMap(Enum<? extends FieldId> fieldId, DustUtilInitValue[] values) {
			super(fieldId);

			if (null != values) {
				for (DustUtilInitValue iv : values) {
					setData(iv.value, VariantSetMode.insert, iv.key);
				}
			}
		}

		@Override
		public void loadContent(DustVariant from) {
			// TODO Auto-generated method stub

		}

		@Override
		protected Object getData() {
			return mapContent;
		}

		@Override
		public void setData(Object value, VariantSetMode mode, String key) {
			switch (mode) {
			case set:
				mapContent.clear();
				// NO break;
			case insert:
				mapContent.put(key, new Single(fieldId, value));
				break;
			case remove:
				mapContent.remove(key);
				break;
			case clear:
				mapContent.clear();
				break;
			}
		}

		@Override
		public Iterator<DustVariant> iterator() {
			throw new RuntimeException("Not yet implemented!");
		}
	}

}