package org.mondoaurora.frame.kernel;

import java.util.*;

import org.mondoaurora.frame.shared.*;

public class MAFKernelEnvironment extends MAFEnvironment implements MAFKernelHelpers {

	Set<MAFKernelEntity> setEntities = new HashSet<MAFKernelEntity>();
	Map<MAFIdentifier, MAFKernelAspect> mapAspById = new HashMap<MAFIdentifier, MAFKernelAspect>();
	
	Map<String, MAFKernelIdentifier> mapIds = new HashMap<String, MAFKernelIdentifier>();
	
	static MAFKernelIdentifier idKernelUnit;

	@Override
	protected MAFConnector getThisI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MAFConnector getCallContextI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MAFConnector getInstanceI(MAFIdentifier id, String[] names) {
		MAFKernelAspect a = mapAspById.get(id);
		MAFKernelConnector ret = null;
		
		if ( null != a ) {
			ret = (null == names) ? new MAFKernelConnector(a.type) : new MAFKernelConnector(a.type, names);
			ret.data = a;
		}
		
		return ret;
	}
	
	@Override
	protected MAFIdentifier getIdI(String id) {
		return mapIds.get(id);
	}
	
	
	public MAFKernelEnvironment() {
		MAFKernelVendor vendorRoot = new MAFKernelVendor(ID_VENDOR_ROOT);
		MAFKernelDomain domainFrame = new MAFKernelDomain(ID_VENDOR_ROOT, ID_DOMAIN_FRAME);
		MAFKernelUnit unitKernel = new MAFKernelUnit(ID_UNIT_KERNEL);
		
		MAFKernelConnector connKernel = unitKernel.export(this);
		MAFKernelConnector connFrame = domainFrame.export(this);
		MAFKernelConnector connRoot = vendorRoot.export(this);

		connRoot.setData(0, connFrame);
		connFrame.setData(0, connKernel);
		
//		dump();
	}
	
	public void dump() {
		System.out.println("Identifiers:");
		for ( MAFIdentifier i : mapIds.values() ) {
			System.out.println(i);
		}
/*		
		MAFKernelDumper d = new MAFKernelDumper();
		
		System.out.println("Entities:");
		for ( MAFKernelEntity e : setEntities ) {
			d.dumpEntity(e);
		}
		*/
	}

	public static void init() {
		MAFEnvironment.env = new MAFKernelEnvironment();
	}
	
	MAFKernelConnector registerEntity(MAFKernelType type, String ownerId, String aspId, MAFKernelLogic logic, String[] fields) {
		return registerEntity(type, new MAFKernelIdentifier(type.id.asPath(), ownerId, aspId), logic, fields);
	}
	
	MAFKernelConnector registerEntity(MAFKernelType type, MAFKernelIdentifier id, MAFKernelLogic logic, String[] fields) {
		MAFKernelAspect a = new MAFKernelAspect(type, logic);		
		MAFKernelEntity e = new MAFKernelEntity(id, a);
		
		setEntities.add(e);
		
		if ( type.referrable ) {
			mapAspById.put(id, a);
			mapIds.put(id.asReference(), id);
		}
		
		MAFKernelConnector conn = new MAFKernelConnector(type, fields);
		conn.data = a;
				
		return conn;
	}
}
