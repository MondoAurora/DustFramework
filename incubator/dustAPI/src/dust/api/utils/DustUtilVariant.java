package dust.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.wrappers.DustByteBuffer;
import dust.api.wrappers.DustDateImmutable;
import dust.api.wrappers.DustIdentifier;

public class DustUtilVariant implements DustVariant {
	final Enum<? extends FieldId> fieldId;
	final DustDeclId typeId;

	static enum ContentType {
		Unknown, Single, Array, Set, Map
	};

	ContentType cType = ContentType.Unknown;

	Object data;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DustDeclId typeIDFromFieldId(Enum<? extends FieldId> fieldId) {
		for (Class cc = fieldId.getDeclaringClass(); null != cc; cc = cc.getDeclaringClass()) {
			if (TypeDef.class.isAssignableFrom(cc)) {
				return DustUtils.getWorld().getTypeId(cc);
			}
		}

		return null;
	}

	protected DustUtilVariant(Enum<? extends FieldId> fieldId) {
		this.fieldId = fieldId;
		typeId = typeIDFromFieldId(fieldId);
	}

	public DustUtilVariant(Enum<? extends FieldId> fieldId, Object value) {
		this(fieldId);

		if (value instanceof DustUtilInitValue[]) {
			for (DustUtilInitValue iv : (DustUtilInitValue[]) value) {
				setData(iv.value, VariantSetMode.insert, iv.key);
			}
		} else if (null != value) {
			setData(value, VariantSetMode.set, null);
		}
	}

	public Enum<? extends FieldId> getId() {
		return fieldId;
	}

	public DustIdentifier getValueIdentifier() {
		return (DustIdentifier) getData();
	}

	public boolean getValueBoolean() {
		return (Boolean) getData();
	}

	public Integer getValueInteger() {
		return (Integer) getData();
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

	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> T getValueValSet(Class<T> enumType) {
		return (T) getData();
	}
	
	@Override
	public String getValueValSet() {
		return ((Enum<?>) data).name();
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

	public void setValueInteger(Integer val) {
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

	public boolean isNull() {
		if (null == data) {
			return true;
		} else {
			switch (cType) {
			case Map:
			case Set:
			case Array:
				return ((Collection<?>) data).isEmpty();
			default:
				return false;
			}
		}
	}

	protected Object getData() {
		return data;
	}

	@SuppressWarnings("unchecked")
	public void setData(Object value, VariantSetMode mode, String key) {
		if (ContentType.Unknown == cType) {
			if (VariantSetMode.set == mode) {
				cType = ContentType.Single;
			} else {
				switch (mode) {
				case addFirst:
				case addLast:
					cType = ContentType.Array;
					data = new ArrayList<DustVariant>();
					break;
				case insert:
					if (null == key) {
						cType = ContentType.Set;
						data = new HashSet<DustVariant>();
					} else {
						cType = ContentType.Map;
						data = new HashMap<String, DustVariant>();
					}
					break;
				}
			}
		}

		switch (cType) {
		case Single:
			this.data = value;
			break;
		case Map:
			switch (mode) {
			case insert:
				((Map<String, DustVariant>) data).put(key, new DustUtilVariant(fieldId, (DustEntity) value));
				break;
			}
		case Set:
			switch (mode) {
			case insert:
				((Set<DustVariant>) data).add(new DustUtilVariant(fieldId, (DustEntity) value));
				break;
			}
		}
	}

	public void loadContent(DustVariant from) {
		this.data = ((DustUtilVariant) from).getData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<DustVariant> getMembers() {
		switch (cType) {
		case Set:
			return ((Set<DustVariant>) data);
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String toString() {
		if (ContentType.Single == cType) {
			return String.valueOf(data);
		} else {
			StringBuilder b = new StringBuilder("{ ");
			switch (cType) {
			case Map:
				for (Iterator<Map.Entry<String, DustVariant>> it = ((Map) data).entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, DustVariant> e = it.next();
					b.append(e.getKey()).append(" = ").append(e.getValue()).append(";");
				}
				break;

			case Set:
				for (Iterator<DustVariant> it = ((Set) data).iterator(); it.hasNext();) {
					b.append(it.next()).append(";");
				}
				break;

			default:
				break;
			}
			b.append("}");

			return b.toString();
		}
	}

	@Override
	public DustDeclId getTypeId() {
		return typeId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DustUtilVariant) {
			DustUtilVariant v = (DustUtilVariant) obj;
			if (v.fieldId.equals(fieldId)) {
				switch (cType) {
				case Single:
					return DustUtils.isEqual(v.data, data);
				case Set:
					// TODO Temporal!!!
					return ((Set<DustVariant>) data).size() == ((Set<DustVariant>) v.data).size();
				}
			}
		}
		return super.equals(obj);
	}

}
