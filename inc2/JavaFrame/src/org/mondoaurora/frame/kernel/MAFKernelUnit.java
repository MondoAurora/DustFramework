package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFVariant;
import org.mondoaurora.frame.shared.MAFVariant.VariantSetMode;


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
		
		MAFVariant v = target.getValue(0);

		v.setData(MAFKernelVendor.TYPE.export(env), VariantSetMode.insert, null);
		v.setData(MAFKernelDomain.TYPE.export(env), VariantSetMode.insert, null);
		v.setData(TYPE.export(env), VariantSetMode.insert, null);
		v.setData(MAFKernelType.TYPE.export(env), VariantSetMode.insert, null);
		v.setData(MAFKernelField.TYPE.export(env), VariantSetMode.insert, null);

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
