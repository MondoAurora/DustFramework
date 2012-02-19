package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFConnector;


public class MAFKernelUnit extends MAFKernelLogic {
	static String strTypeId = MAFKernelIdentifier.buildPath(KERNEL_PATH, ID_TYPENAME_UNIT);

	static final String[] FIELDS = new String[] {FIELD_TYPES};

	public static final MAFKernelType TYPE = new MAFKernelType(ID_TYPENAME_UNIT, true, new MAFKernelField[] {
			new MAFKernelField(FIELDS[0], FieldType.ARRAY, MAFKernelType.strTypeId), 
	});
	
	String id;
	
	public MAFKernelUnit(String id) {
		this.id = id;
	}
	
	@Override
	MAFKernelConnector export(MAFKernelEnvironment env) {
		MAFKernelConnector target = env.registerEntity(TYPE, new MAFKernelIdentifier(strTypeId, FRAME_PATH, ID_UNIT_KERNEL), null, FIELDS);
		
		target.addMember(0, MAFKernelVendor.TYPE.export(env));
		target.addMember(0, MAFKernelDomain.TYPE.export(env));
		target.addMember(0, TYPE.export(env));
		target.addMember(0, MAFKernelType.TYPE.export(env));
		target.addMember(0, MAFKernelField.TYPE.export(env));

		return target;	
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processMessage(MAFConnector connData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelClosed() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
