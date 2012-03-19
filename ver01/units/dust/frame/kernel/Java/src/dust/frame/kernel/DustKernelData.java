package dust.frame.kernel;

import dust.kernel.DustKernel;
import dust.shared.DustObject;

public class DustKernelData implements DustKernel.DataInstance, DustKernelConsts {
	DustKernelEntity entity;

	DustKernelType type;
	DustKernelVariant[] content;

	LogicWrapper<?, ?> logicWrapper;
	boolean logicInit = false;

	DustKernelData(DustKernelType type) {
		this.type = type;

		content = new DustKernelVariant[type.getFieldCount()];

		for (DustKernelField fld : type.arrFields) {
			content[fld.idx] = new DustKernelVariant.Field(fld);
		}
	}

	DustKernelData(DustKernelType type, LogicWrapper<?, ?> logic) {
		this(type);

		this.logicWrapper = logic;
	}
	
	@Override
	public Entity getEntity() {
		return entity;
	}

	@Override
	public DustIdentifier getObTypeID() {
		return type.getID();
	}

	@Override
	public void send(DataWrapper w, Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc) {
		if ( null != logicWrapper ) {
			if ( !logicInit ) {
				logicInit = true;
				logicWrapper.initI(((DustKernelEnvironment)DustKernel.Environment.getEnv()).wrapData(this));
			}
			logicWrapper.processMessageI(msgId, msgOb, null);
		}
	}

	@Override
	public int getFieldCount() {
		return content.length;
	}

	@Override
	public Variant getFieldVariant(int idx) {
		return content[idx];
	}

	@Override
	public DustObject getNeighbor(DustIdentifier typeId) {
		return entity.getAspect(typeId);
	}

	@Override
	public boolean isNull(int idx) {
		return content[idx].isNull();
	}

	@Override
	public String toString() {
		return "Data: " + type.toString();
	}
}
