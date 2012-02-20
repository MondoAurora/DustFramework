package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFIdentifier;
import org.mondoaurora.frame.shared.MAFVariant;
import org.mondoaurora.frame.shared.MAFVariant.VariantSetMode;

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
			if (null == fld) {
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

	MAFKernelConnector(MAFKernelAspect data, String[] fieldNames) {
		this(data.type, fieldNames);
		this.data = data;
	}

	@Override
	public MAFIdentifier getObTypeID() {
		return type.id;
	}

	@Override
	public boolean isNull(int idx) {
		MAFVariant v = getValue(idx);
		return (null == v) || v.isNull();
	}

	@Override
	public MAFVariant getValue(int idx) {
		return data.content[idxs[idx]];
	}

	@Override
	public void getValues(MAFVariant[] fields) {
		for (int i = 0; i < fields.length; ++i) {
			fields[i] = getValue(i);
		}
	}

	@Override
	public void setData(int idx, Object value, VariantSetMode mode, String key) {
		getValue(idx).setData(value, mode, key);
	}

	@Override
	public void setData(int idx, Object value) {
		getValue(idx).setData(value, VariantSetMode.set, null);
	}

	@Override
	public void setValue(int idx, MAFVariant from) {
		getValue(idx).setData(from);
	}

	@Override
	public MAFConnector getNeighbor(MAFIdentifier typeId, String[] fields) {
		// TODO Auto-generated method stub
		return null;
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
