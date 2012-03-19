package dust.frame.kernel;

import dust.frame.stream.*;


public class DustKernelBoot implements DustKernelConsts {

	static void init(DustKernelEnvironment env) {
		DataWrapper.Factory factDataWrapper = new ReflectFactoryWrapper.Data();
		LogicWrapper.Factory factLogicWrapper = new ReflectFactoryWrapper.Logic();
		
		ReflectFactoryLogic factLogic = new ReflectFactoryLogic();
		factLogic.registerClass(KStreamWrite.ID, DustStreamWriter.class.getName());
		factLogic.registerClass(KIndenter.ID, DustStreamIndenter.class.getName());

		env.init(factLogic, factDataWrapper, factLogicWrapper);
		
		DustKernelEntity e;
		DustKernelType tVendor = new DustKernelType(KVendor.INFO);
		DustKernelType tDomain = new DustKernelType(KDomain.INFO);
		DustKernelType tUnit = new DustKernelType(KUnit.INFO);

		e = env.registerEntity(tVendor, null, ID_VENDOR_ROOT, null);
		IVendor vRoot = (IVendor) e.getAspect(KVendor.ID);

		e = env.registerEntity(tDomain, ID_VENDOR_ROOT, ID_DOMAIN_FRAME, null);
		IDomain dFrame = (IDomain) e.getAspect(KDomain.ID);

		vRoot.addDomain(dFrame, ID_DOMAIN_FRAME);

		e = env.registerEntity(tUnit, FRAME_PATH, ID_UNIT_KERNEL, null);
		IUnit uKernel = (IUnit) e.getAspect(KUnit.ID);

		dFrame.addUnit(uKernel, ID_UNIT_KERNEL);

		regType(uKernel, DustKernelType.TYPE_NAME, env);
		regType(uKernel, tVendor, env);
		regType(uKernel, tDomain, env);
		regType(uKernel, tUnit, env);
		regType(uKernel, DustKernelType.TYPE_TYPE, env);
		regType(uKernel, DustKernelField.TYPE_FIELD, env);
		
		vRoot.getName().setName(ID_VENDOR_ROOT);
		dFrame.getName().setName(ID_DOMAIN_FRAME);
		uKernel.getName().setName(ID_UNIT_KERNEL);
		
		
		
		regType(uKernel, new DustKernelType(KStreamWrite.INFO), env);
		regType(uKernel, new DustKernelType(KIndenter.INFO), env);
		
//		e = env.registerEntity(env.getIdI("[dust.frame.stream.StreamWrite]:dump"));
//		e.addAspect(KIndenter.ID);		
	}

	static void regType(IUnit unit, DustKernelType t, DustKernelEnvironment env) {
		unit.addType((IType) t.export(env), t.name);
	}
}
