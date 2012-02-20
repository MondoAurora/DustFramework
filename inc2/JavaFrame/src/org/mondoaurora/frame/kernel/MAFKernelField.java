package org.mondoaurora.frame.kernel;

import java.util.EnumSet;

import org.mondoaurora.frame.shared.MAFDate;
import org.mondoaurora.frame.shared.MAFUtils;

public class MAFKernelField extends MAFKernelLogic {
	static String strTypeId = MAFKernelIdentifier.buildPath(KERNEL_PATH, ID_TYPENAME_FIELD);

	static final String[] FIELDS = new String[] { FIELD_ID, FIELD_TYPE, FIELD_LENGTH, FIELD_OBTYPE, FIELD_VALUES };

	public static final MAFKernelType TYPE = new MAFKernelType(ID_TYPENAME_FIELD, false, new MAFKernelField[] {
			new MAFKernelField(FIELDS[0], FieldType.STRING, ID_LENGTH),
			new MAFKernelField(FIELDS[1], EnumSet.allOf(FieldType.class)), new MAFKernelField(FIELDS[2], FieldType.INTEGER),
			new MAFKernelField(FIELDS[3], FieldType.STRING, ID_LENGTH),
			new MAFKernelField(FIELDS[4], FieldType.STRING, LONG_LENGTH), });

	String name;
	int idx;

	FieldType type;
	int length;
	String obType;

	String[] valsetValues;

	public MAFKernelField(String name, FieldType type) {
		this(name, type, 0, null);
	}

	public <T extends Enum<T>> MAFKernelField(String name, EnumSet<T> enumSet) {
		this(name, FieldType.VALUESET, 0, null);

		valsetValues = new String[enumSet.size()];
		int idx = 0;
		for (Enum<T> e : enumSet) {
			valsetValues[idx++] = e.name();
		}
	}

	public MAFKernelField(String name, FieldType type, int length) {
		this(name, type, length, null);
	}

	public MAFKernelField(String name, FieldType type, String obType) {
		this(name, type, -1, obType);
	}

	public MAFKernelField(String name, FieldType type, int length, String obType) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.obType = obType;
	}

	public String getName() {
		return name;
	}

	public FieldType getType() {
		return type;
	}

	public Class<?> getJavaClass() {
		switch (type) {
		case BOOLEAN:
			return Boolean.class;
		case VALUESET:
			return String.class;
		case INTEGER:
			return Integer.class;
		case DOUBLE:
			return Double.class;
		case IDENTIFIER:
		case STRING:
			return String.class;
		case DATE:
			return MAFDate.class;
		case ARRAY:
		case SET:
			return String.class;
		}
		return null;
	}

	public int getLength() {
		return length;
	}

	public int getIndex() {
		return idx;
	}

	@Override
	MAFKernelConnector export(MAFKernelEnvironment env) {
		MAFKernelConnector conn = env.registerEntity(TYPE, null, name, this, FIELDS);

		conn.setData(0, name);
		conn.setData(1, type.name());
		conn.setData(2, length);
		conn.setData(3, obType);
		conn.setData(4, (null == valsetValues) ? null : MAFUtils.arr2str(valsetValues, SEP_VALSET));

		return conn;
	}

	void dump(MAFKernelDumper target, MAFKernelVariant o) {
		target.put("\"");
		target.put(name);
		target.put("\" : ");
		o.dump(target);
	}

}
