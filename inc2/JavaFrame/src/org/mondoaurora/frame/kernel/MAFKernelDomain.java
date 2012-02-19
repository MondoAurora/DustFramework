package org.mondoaurora.frame.kernel;


public class MAFKernelDomain extends MAFKernelLogic {	
	static final String[] FIELDS = new String[] {FIELD_UNITS};
	
	static final String strTypeId = MAFKernelIdentifier.buildPath(KERNEL_PATH, ID_TYPENAME_DOMAIN);
	
	static final MAFKernelType TYPE = new MAFKernelType(ID_TYPENAME_DOMAIN, false, new MAFKernelField[] {
			new MAFKernelField(FIELD_UNITS, FieldType.ARRAY, MAFKernelUnit.strTypeId), 
	});

	String vendor;
	String id;
	
	public MAFKernelDomain(String vendor, String id) {
		this.vendor = vendor;
		this.id = id;
	}
	
	@Override
	MAFKernelConnector export(MAFKernelEnvironment env) {
		MAFKernelConnector conn = env.registerEntity(TYPE, vendor, id, this, FIELDS);

		return conn;
	}
}
