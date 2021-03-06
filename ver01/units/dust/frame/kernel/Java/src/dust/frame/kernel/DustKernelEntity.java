package dust.frame.kernel;

import java.util.Map;
import java.util.TreeMap;

import dust.kernel.DustKernel;
import dust.shared.DustObject;

public class DustKernelEntity implements DustKernelConsts, DustKernel.Entity {
	EntityState state;
	
	DustIdentifier id;
	DustKernelData primaryAspect;

	Map<DustIdentifier, DustKernelData> mapAspects = new TreeMap<DustIdentifier, DustKernelData>();

	protected DustKernelEntity() {
		state = EntityState.NEW;
	}

	public DustKernelEntity(DustIdentifier id, DustKernelData primaryAspect) {
		this.id = id;
		this.primaryAspect = primaryAspect;
		state = EntityState.NORMAL;
		addAspect(primaryAspect);
	}

	public void addAspect(DustKernelData aspect) {
		mapAspects.put(aspect.type.getID(), aspect);
		aspect.entity = this;
		
		initAspect(aspect);
	}

	synchronized DustKernelData addAspect(DustIdentifier id) {
		DustKernelData d = mapAspects.get(id);
		
		if ( null == d ){
			d = ((DustKernelEnvironment)DustKernel.Environment.getEnv()).createData(DustKernelType.getType(id), null);
			addAspect(d);
		}
		
		return d;
	}

	public void initAspect(DustKernelData d) {
		DustKernelData dOR;
		
		for ( DustIdentifier id : d.type.setOverrides ) {
			dOR = getAspectI(id);
			dOR.logicWrapper.overload = d.logicWrapper;
		}
	}

	public DustObject getAspect(DustIdentifier id) {
		return ((DustKernelEnvironment)DustKernel.Environment.getEnv()).wrapData(getAspectI(id));
	}

	DustKernelData getAspectI(DustIdentifier id) {
		DustKernelData d = mapAspects.get(id);
		return (null == d) ? addAspect(id) : d;
	}

	public Iterable<Map.Entry<DustIdentifier, DustKernelData>> getAspects() {
		return mapAspects.entrySet();
	}

	public String getRef() {
		return id.asReference();
	}

	public DustIdentifier getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Entity " + id.toString();
	}
}
