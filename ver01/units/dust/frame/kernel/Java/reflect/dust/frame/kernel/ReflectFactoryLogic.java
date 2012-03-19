package dust.frame.kernel;

import java.util.HashMap;
import java.util.Map;

import dust.kernel.DustKernel;
import dust.shared.*;
import dust.shared.DustConsts.DustIdentifier;

public class ReflectFactoryLogic implements DustKernel.LogicFactory {
	Map<DustIdentifier, Class<DustLogic<?, Enum<?>>>> mapLogicClasses = new HashMap<DustConsts.DustIdentifier, Class<DustLogic<?,Enum<?>>>>();

	@Override
	public DustLogic<?, Enum<?>> newLogic(DustIdentifier typeId) {
		try {
			Class<DustLogic<?, Enum<?>>> cc = mapLogicClasses.get(typeId);
			
			return (null == cc) ? null : cc.newInstance();
		} catch (Exception e) {
			throw new DustRuntimeException("ReflectFactoryLogic", "Failed to create logic wrapper for type " + typeId, e);
		}
	}

	@SuppressWarnings("unchecked")
	public void registerClass(DustIdentifier typeId, String className) {
		try {
			registerClass(typeId, (Class<DustLogic<?, Enum<?>>>)Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new DustRuntimeException("ReflectFactoryLogic", "Failed to register class " + className + " for type " + typeId, e);
		}
	}

	public void registerClass(DustIdentifier typeId, Class<DustLogic<?, Enum<?>>> clazz) {
		mapLogicClasses.put(typeId, clazz);
	}
}
