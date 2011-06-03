package dust.api.components;

import java.util.Iterator;

import dust.api.DustConstants;
import dust.api.wrappers.DustByteBuffer;
import dust.api.wrappers.DustDateImmutable;
import dust.api.wrappers.DustIdentifier;

public abstract class DustVariant implements Iterable<DustVariant>, DustConstants {
	FieldType fldType;
	Enum<? extends FieldId> fieldId;

	protected DustVariant(Enum<? extends FieldId> fieldId, FieldType fldType) {
		this.fieldId = fieldId;
		this.fldType = fldType;
	}

	public FieldType getType() {
		return fldType;
	}

	public Enum<? extends FieldId> getId() {
		return fieldId;
	}

	public void testType(FieldType target) {
		if (target != fldType) {
			throw new IllegalAccessError("Invalid type " + target.name() + " requested for a value of " + fldType.name());
		}
	}

	public abstract boolean isNull();

	public abstract void loadContent(DustVariant from);

	public DustIdentifier getValueIdentifier() {
		testType(FieldType.Identifier);
		return (DustIdentifier) getData();
	}

	public Boolean getValueBoolean() {
		testType(FieldType.Boolean);
		return (Boolean) getData();
	}

	public Class<?> getValueClass() {
		testType(FieldType.JavaClass);
		return (Class<?>) getData();
	}

	public Long getValueLong() {
		testType(FieldType.Long);
		return (Long) getData();
	}

	public Double getValueDouble() {
		testType(FieldType.Double);
		return (Double) getData();
	}

	public DustDateImmutable getValueDate() {
		testType(FieldType.ImmutableDate);
		return (DustDateImmutable) getData();
	}

	public String getValueString() {
		testType(FieldType.String);
		return (String) getData();
	}

	public String getValueValSet() {
		testType(FieldType.ValueSet);
		return (String) getData();
	}

	public <T extends Enum<T>> T getValueValSet(Class<T> enumType) {
		return Enum.valueOf(enumType, getValueValSet());
	}

	public DustByteBuffer getValueBuffer() {
		testType(FieldType.ByteArray);
		return (DustByteBuffer) getData();
	}

	public DustAspect getValueObject() {
		testType(FieldType.ObSingle);
		return (DustAspect) getData();
	}

	public abstract void setData(Object value, VariantSetMode mode, String key);

	public void setValueBoolean(Boolean val) {
		testType(FieldType.Boolean);
		setData(val, VariantSetMode.set, null);
	}

	public void setValueIdentifier(String val) {
		testType(FieldType.Identifier);
		setData(val, VariantSetMode.set, null);
	}

	public void setValueString(String val) {
		testType(FieldType.String);
		setData(val, VariantSetMode.set, null);
	}

	public void setValueValSet(String val) {
		testType(FieldType.ValueSet);
		setData(val, VariantSetMode.set, null);
	}

	public void setValueValSet(Enum<?> val) {
		setValueValSet(val.name());
	}

	@Override
	public Iterator<DustVariant> iterator() {
		throw new IllegalAccessError("Not a collection!");
	}

	protected Object getData() {
		// TODO Auto-generated method stub
		return null;
	}
}