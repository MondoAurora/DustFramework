package sandbox;

import dust.api.components.*;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common.Identified;
import dust.units.dust.kernel.v0_1.TypeManagement.Type;

public class TestBootEntity implements Test.TestItem {
	@Override
	public void test() throws Exception {
		DustWorld world = DustUtils.getWorld();
		
		DustDeclId idType = world.getTypeId(Type.class);
		
		DustEntity e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Identified.Fields.Identifier, "Ahoy!"),
			world.getVar(null, Type.Fields.Unit, "Bambam"),
		});
		
		System.out.print(e.getAspect(idType, false).getField(Identified.Fields.Identifier).getValueString());
	}

	@Override
	public void init(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
