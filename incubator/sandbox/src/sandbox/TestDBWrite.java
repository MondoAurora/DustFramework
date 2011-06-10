package sandbox;

import sandbox.persistence.db.DBDumper;
import dust.api.DustConstants;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;
import dust.units.dust.common.v0_1.Common.Identified;
import dust.units.dust.kernel.v0_1.TypeManagement.Field;
import dust.units.dust.kernel.v0_1.TypeManagement.Type;

public class TestDBWrite extends Test.TestItem implements DustConstants {
	@Override
	public void test() throws Exception {
		DustWorld world = DustUtils.getWorld();
		
		DustDeclId idType = world.getTypeId(Type.class);
		DustDeclId idField = world.getTypeId(Field.class);

		DustVariant[] knownFields = new DustVariant[] {
			world.getVar(null, Identified.Fields.Identifier, idField.getIdentifier()),
		};
		
		DustEntity e = DustUtils.getEntity(idType, knownFields);

//		StreamDumper sd = new StreamDumper();
//		sd.dump(e);
		
		DBDumper dd = new DBDumper();
		dd.dumpEntity(e);
	}
}
