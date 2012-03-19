package dust.frame.kernel;

import dust.kernel.*;
import dust.kernel.DustKernel.DataInstance;
import dust.kernel.DustKernel.DataWrapper;
import dust.kernel.DustKernel.LogicWrapper;
import dust.shared.DustConsts.DustIdentifier;
import dust.shared.*;

public class ReflectFactoryWrapper<Type> {

	String postfix;

	ReflectFactoryWrapper(String postfix) {
		this.postfix = postfix;
	}

	@SuppressWarnings("unchecked")
	Type forID(DustIdentifier typeID) {
		StringBuffer className = new StringBuffer(typeID.asPath());

		try {
			int lastDot = className.lastIndexOf(".");
			className.insert(lastDot + 1, "K");
			className.append("$").append(postfix);

			return ((Class<Type>) Class.forName(className.toString())).newInstance();
		} catch (Exception e) {
			throw new DustRuntimeException("ReflectFactoryWrapper", "Failed to create " + postfix + " wrapper " + className
					+ "for type " + typeID, e);
		}
	}

	public static class Data extends DustKernel.DataWrapper.Factory {
		ReflectFactoryWrapper<DataWrapper> f = new ReflectFactoryWrapper<>("Data");

		@Override
		public DataWrapper newWrapper(DustIdentifier typeID, DataInstance instance) {
			DataWrapper dw = f.forID(typeID);
			setData(dw, instance);
			return dw;
		}
	}

	public static class Logic extends DustKernel.LogicWrapper.Factory {
		@SuppressWarnings("rawtypes")
		ReflectFactoryWrapper<LogicWrapper> f = new ReflectFactoryWrapper<>("Logic");

		@SuppressWarnings("rawtypes")
		@Override
		public LogicWrapper newWrapper(DustIdentifier typeID, DustLogic<?, Enum<?>> logic) {
			LogicWrapper lw = f.forID(typeID);
			setLogic(lw, logic);
			return lw;
		}
	}

}
