package sandbox;

import sandbox.persistence.db.DBPersistentStorage;
import sandbox.persistence.stream.StreamDumper;
import dust.api.DustConstants;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;
import dust.units.dust.common.v0_1.Common.Identified;
import dust.units.dust.kernel.v0_1.TypeManagement.Field;
import dust.units.dust.kernel.v0_1.TypeManagement.Type;

public class TestDBRead extends Test.TestItem implements DustConstants {
	StreamDumper sd = new StreamDumper();

	@Override
	public void test() throws Exception {
		DustWorld world = DustUtils.getWorld();

		DustDeclId idType = world.getTypeId(Type.class);
		DustDeclId idField = world.getTypeId(Field.class);


		DustVariant[] knownFields = new DustVariant[] { world.getVar(null, Identified.Fields.Identifier,
			idField.getIdentifier()), };
		
		DustEntity e = DustUtils.getEntity(idType, knownFields);

		System.out.println("\n\nFrom boot objects:");
		sd.dump(e);

		DBPersistentStorage ps = new DBPersistentStorage();
		
		InvokeResponseProcessor rp = new InvokeResponseProcessor() {
			public void searchStarted() {
				System.out.println("\n\nSearch started");
			}
			
			@Override
			public void searchFinished() {
				System.out.println("\n\nSearch finished");
			}
			
			@Override
			public boolean entityFound(DustEntity entity) {
				System.out.println("Found in database:");
				sd.dump(entity);
				return false;
			}
		};
		
		ps.loadEntity(idType, knownFields, rp);
	}
}
