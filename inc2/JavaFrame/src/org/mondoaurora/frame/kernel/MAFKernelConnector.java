package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFDate;
import org.mondoaurora.frame.shared.MAFIdentifier;
import org.mondoaurora.frame.shared.MAFUtils;

public class MAFKernelConnector implements MAFConnector, MAFKernelConsts {
	final MAFKernelType type;

	MAFKernelAspect data;
	int[] idxs;
	MAFKernelField[] fields;

	MAFKernelConnector(MAFKernelType t, String[] fieldNames) {
		idxs = new int[fieldNames.length];
		fields = new MAFKernelField[fieldNames.length];

		type = t;
		int i = 0;
		for (String f : fieldNames) {
			MAFKernelField fld = type.getField(f);
			if ( null == fld ) {
				throw new RuntimeException("Missing field '" + f + "' in type " + t.id);
			}
			idxs[i] = fld.idx;
			fields[i] = fld;
			++i;
		}
	}

	MAFKernelConnector(MAFKernelType t) {
		type = t;

		int l = type.getFieldCount();

		idxs = new int[l];
		fields = new MAFKernelField[l];

		for (MAFKernelField fld : type.getFields()) {
			idxs[fld.idx] = fld.idx;
			fields[fld.idx] = fld;
		}
	}
	
	@Override
	public MAFIdentifier getObTypeID() {
		return type.id;
	}
	
	Object getValue(int idx, FieldType reqType) {
		return data.content[idxs[idx]];
	}

	void setValue(int idx, Object value, FieldType reqType) {
		data.content[idxs[idx]] = value;
	}


	@Override
	public boolean isNull(int idx) {
		return null == getValue(idx, null);
	}

	@Override
	public String getString(int idx) {
		return (String) getValue(idx, FieldType.STRING);
	}

	@Override
	public MAFIdentifier getIdentifier(int idx) {
		return (MAFIdentifier) getValue(idx, FieldType.IDENTIFIER);
	}

	@Override
	public boolean getBool(int idx, boolean defValue) {
		Boolean v = (Boolean) getValue(idx, FieldType.BOOLEAN);
		return (null == v) ? defValue : v;
	}

	@Override
	public int getInt(int idx, int defValue) {
		Integer v = (Integer) getValue(idx, FieldType.INTEGER);
		return (null == v) ? defValue : v;
	}

	@Override
	public double getDouble(int idx, double defValue) {
		Integer v = (Integer) getValue(idx, FieldType.DOUBLE);
		return (null == v) ? defValue : v;
	}

	@Override
	public MAFDate getDate(int idx) {
		return (MAFDate) getValue(idx, FieldType.DATE);
	}

	@Override
	public MAFConnector getConnector(int idx) {
		return (MAFConnector) getValue(idx, FieldType.CONNECTOR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<? extends MAFConnector> getMembers(int idx) {
		Object val = getValue(idx, null);
		return (null == val) ? MAFUtils.NO_MEMBER : (Iterable<? extends MAFConnector>) val;
	}

	@Override
	public String getCodeStr(int idx) {
		return (String) getValue(idx, FieldType.VALUESET);
	}

	@Override
	public void setString(int idx, String val) {
		setValue(idx, val, FieldType.STRING);
	}

	@Override
	public void setIdentifier(int idx, MAFIdentifier val) {
		setValue(idx, val, FieldType.IDENTIFIER);
	}

	@Override
	public void setBool(int idx, boolean val) {
		setValue(idx, val, FieldType.BOOLEAN);
	}

	@Override
	public void setInt(int idx, int val) {
		setValue(idx, val, FieldType.INTEGER);
	}

	@Override
	public void setDouble(int idx, double val) {
		setValue(idx, val, FieldType.DOUBLE);
	}

	@Override
	public void setDate(int idx, MAFDate val) {
		setValue(idx, val, FieldType.DATE);
	}

	@Override
	public void setConnector(int idx, MAFConnector val) {
		setValue(idx, val, FieldType.CONNECTOR);
	}

	@Override
	public void addMember(int idx, MAFConnector member) {
		int i = idxs[idx];
		switch (fields[idx].getType()) {
		case SET:
			data.addMember(i, member);
			break;
		case ARRAY:
			data.addArr(i, member);
			break;
		default:
			throw new RuntimeException("Reader modification exception: " + fields[idx].getName() + " is not a collection!");
		}
	}

	@Override
	public void setCodeStr(int idx, String val) {
		setValue(idx, val, FieldType.VALUESET);
	}
	
	void dump(MAFKernelDumper target) {
		target.dumpEntity(data.entity);
	}


	@Override
	public boolean send(MAFConnector data, boolean waitMore) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
