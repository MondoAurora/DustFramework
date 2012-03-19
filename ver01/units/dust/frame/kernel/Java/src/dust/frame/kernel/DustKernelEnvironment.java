package dust.frame.kernel;

import java.util.*;

import dust.kernel.DustKernel;
import dust.kernel.DustKernel.DataWrapper.Factory;
import dust.shared.DustLogic;
import dust.shared.DustObject;


public class DustKernelEnvironment extends DustKernel.Environment implements DustKernelConsts {
	public static String ID_TYPE = DustKernelIdentifier.buildPath(KERNEL_PATH, ID_TYPE_TYPE);
	
	Set<DustKernelEntity> setEntities = new HashSet<DustKernelEntity>();
	Map<DustIdentifier, DustKernelData> mapAspById = new HashMap<DustIdentifier, DustKernelData>();
	
	Map<String, DustIdentifier> mapIds = new TreeMap<String, DustIdentifier>();
	static DustKernelIdentifier idKernelUnit;
	
	LogicFactory factLogic;
	DataWrapper.Factory factDataWrapper;
	LogicWrapper.Factory factLogicWrapper;
	


	public void init(LogicFactory factLogic, Factory factDataWrapper, LogicWrapper.Factory factLogicWrapper) {
		this.factLogic = factLogic;
		this.factDataWrapper = factDataWrapper;
		this.factLogicWrapper = factLogicWrapper;
	}

	@Override
	protected DustObject getInstanceI(DustIdentifier id) {
		DustKernelData a = mapAspById.get(id);
		
		if ( null == a ) {
			a = registerEntity(id).primaryAspect;
		}
		
		return ( null != a ) ? factDataWrapper.newWrapper(a.type.id, a) : null;
	}
	
	@Override
	protected DustIdentifier getIdI(String id) {
		DustIdentifier ii = mapIds.get(id);
		return (null == ii) ? registerId(id) : ii;
	}
	
	synchronized DustIdentifier registerId(String id) {
		DustIdentifier newId = mapIds.get(id);
		
		if ( null == newId ) {
			newId = DustKernelIdentifier.fromString(id);
			mapIds.put(id, newId);
		}
		
		return newId;
	}
	
	@Override
	protected DustIdentifier getTypeIdI(String vendor, String domain, String unit, String name) {
		String ref = DustKernelIdentifier.buildRef(ID_TYPE, DustKernelIdentifier.buildPath(new String[]{vendor, domain, unit}), name);
			
		return getIdI(ref);
	}
		
	public void dump() {
		System.out.println("Identifiers:");
		for ( DustIdentifier i : mapIds.values() ) {
			System.out.println(i);
		}
	}

	public static void init() {
		DustKernelEnvironment env = new DustKernelEnvironment();
		DustKernel.Environment.env = env;
		
		DustKernelBoot.init(env);
		
		env.dump();
	}
	
	DustObject wrapData(DustKernelData data) {
		return factDataWrapper.newWrapper(data.type.id, data);
	}
	
	DustKernelData createData(DustKernelType type, DustLogic<?, Enum<?>> logic) {
		if ( null == logic ) {
			logic = factLogic.newLogic(type.id);
		}
		
		@SuppressWarnings("rawtypes")
		LogicWrapper lw = (null == logic) ? null : factLogicWrapper.newWrapper(type.id, logic);
		return new DustKernelData(type, lw);		
	}
	
	DustKernelEntity registerEntity(DustIdentifier id) {
		DustKernelType t = DustKernelType.getType(((DustKernelIdentifier)id).getType());
		
		return registerEntity(t, id, null);
	}
	
	DustKernelEntity registerEntity(DustKernelType type, String ownerId, String aspId, DustLogic<?, Enum<?>> logic) {
		return registerEntity(type, getIdI(DustKernelIdentifier.buildRef(type.id.asPath(), ownerId, aspId)), logic);
	}
	
	DustKernelEntity registerEntity(DustKernelType type, DustIdentifier id, DustLogic<?, Enum<?>> logic) {
		DustKernelData data = createData(type, logic);		
		DustKernelEntity e = new DustKernelEntity(id, data);
		
		setEntities.add(e);
		
		if ( type.referrable ) {
			mapAspById.put(id, data);
			mapIds.put(id.asReference(), id);
		}
						
		return e;
	}
}
