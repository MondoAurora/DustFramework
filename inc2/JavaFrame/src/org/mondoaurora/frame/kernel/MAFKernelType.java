package org.mondoaurora.frame.kernel;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.mondoaurora.frame.shared.MAFVariant;
import org.mondoaurora.frame.shared.MAFVariant.VariantSetMode;

public class MAFKernelType extends MAFKernelLogic {
	static String strTypeId = MAFKernelIdentifier.buildPath(KERNEL_PATH, ID_TYPENAME_TYPE);

	static final String[] FIELDS = new String[] { FIELD_REFERRABLE, FIELD_FIELDS, };

	public static final MAFKernelType TYPE = new MAFKernelType(ID_TYPENAME_TYPE, true, new MAFKernelField[] { 
			new MAFKernelField(FIELDS[0], FieldType.BOOLEAN), 
			new MAFKernelField(FIELDS[1], FieldType.ARRAY, MAFKernelField.strTypeId), 
	});

	MAFKernelIdentifier id;
	
	boolean referrable = true;

	// Class<MAFLogic> classLogic; Class.forName is not available in GWT

	Map<String, MAFKernelField> mapFields = new TreeMap<String, MAFKernelField>();
	ArrayList<MAFKernelField> arrFields = new ArrayList<MAFKernelField>();

	Map<String, MAFKernelType> mapMessages = new TreeMap<String, MAFKernelType>();

	public MAFKernelType() {
	}

	MAFKernelType(String id, boolean referrable, MAFKernelField[] fields) {
		this.id = new MAFKernelIdentifier(strTypeId, KERNEL_PATH, id);
		
		this.referrable = referrable;

		for (MAFKernelField f : fields) {
			addField(f);
		}
	}

	public int getFieldCount() {
		return mapFields.size();
	}

	public Iterable<? extends MAFKernelField> getFields() {
		return arrFields;
	}

	public MAFKernelField getField(String fieldName) {
		return mapFields.get(fieldName);
	}

	public void addField(MAFKernelField f) {
		f.idx = mapFields.size();
		mapFields.put(f.getName(), f);
		arrFields.add(f);
	}

	@Override
	MAFKernelConnector export(MAFKernelEnvironment env) {
		MAFKernelConnector c = env.registerEntity(TYPE, id, this, FIELDS);
		
		c.setData(0, referrable);
		
		MAFVariant v = c.getValue(1);

		for (MAFKernelField f : arrFields) {
			v.setData(f.export(env), VariantSetMode.insert, null);
		}

		return c;
	}

	@Override
	public String toString() {
		return id.asPath();
	}
}
