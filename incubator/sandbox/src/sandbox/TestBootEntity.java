package sandbox;

import dust.api.DustConstants;
import dust.api.boot.DustBootWorld;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;
import dust.units.dust.common.v0_1.Common.Identified;
import dust.units.dust.kernel.v0_1.TypeManagement.Type;

public class TestBootEntity extends Test.TestItem implements DustConstants {
	@Override
	public void test() throws Exception {
		DustWorld world = new DustBootWorld();
		
		DustUtils.setWorld(world, true);
		
		DustDeclId idType = world.getTypeId(Type.class);
		
		DustEntity e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(Identified.Fields.Identifier, FieldType.String, "Ahoy!"),
			world.getVar(Type.Fields.AutoInit, FieldType.Boolean, false),
		});
		
		System.out.print(e.getAspect(idType).getField(Identified.Fields.Identifier).getValueString());
	}
}
