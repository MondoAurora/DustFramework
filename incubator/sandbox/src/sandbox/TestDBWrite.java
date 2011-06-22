package sandbox;

import dust.api.DustConstants;
import dust.api.components.*;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common.Identified;
import dust.units.dust.kernel.v0_1.TypeManagement.Field;
import dust.units.dust.kernel.v0_1.TypeManagement.Type;

import sandbox.persistence.db.DBPersistentStorage;

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
		
		DBPersistentStorage dd = new DBPersistentStorage();
		dd.storeEntity(e);
	}
}
