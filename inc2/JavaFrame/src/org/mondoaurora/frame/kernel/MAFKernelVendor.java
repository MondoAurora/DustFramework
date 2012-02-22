package org.mondoaurora.frame.kernel;


public class MAFKernelVendor extends MAFKernelLogic {
	public static final String[] FIELDS = new String[] {FIELD_DOMAINS};
	
	static final  String strTypeId = MAFKernelIdentifier.buildPath(KERNEL_PATH, ID_TYPENAME_VENDOR);
	
	static final MAFKernelType TYPE = new MAFKernelType(ID_TYPENAME_VENDOR, true, new MAFKernelField[] {
			new MAFKernelField(FIELD_DOMAINS, FieldType.ARRAY, MAFKernelDomain.strTypeId), 
	});

	String id;
	
	public MAFKernelVendor(String id) {
		this.id = id;
	}
	
	@Override
	MAFKernelConnector export(MAFKernelEnvironment env) {
		MAFKernelConnector conn = env.registerEntity(TYPE, null, id, this, FIELDS);

		return conn;
	}
}
