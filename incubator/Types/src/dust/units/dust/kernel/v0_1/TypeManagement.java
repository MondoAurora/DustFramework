package dust.units.dust.kernel.v0_1;

import dust.api.DustDeclarationConstants;

public interface TypeManagement extends DustDeclarationConstants {
	public interface Module extends TypeDef {
		enum Fields implements FieldId {
			Vendor, Domain, Version
		};

		enum Shared implements SharedDef {
			Kernel
		};
	}

	public interface Type extends TypeDef {
		enum Fields implements FieldId {
			Unit, Fields, Messages, Shared, Resource
		};

		enum Shared implements SharedDef {
			Self
		};
	}

	public interface FieldValue extends TypeDef {
		enum Fields implements FieldId {
			Ordinal
		};
	}

	public interface Field extends TypeDef {
		enum Fields implements FieldId {
			FieldType, ObType, Values
		};

		public static interface Values {
			enum FieldType {
				Identifier, String, Long, Double, ImmutableDate, Boolean, ValueSet, ByteArray, ObType, ObSingle, ObSet, ObArray, ObMap
			};
		}
	}

	public interface Resource extends TypeDef {
		enum Fields implements FieldId {
			Fields,
		};
	}

	public interface Shared extends TypeDef {
		enum Fields implements FieldId {
			ObType,
		};
	}

	public interface Message extends TypeDef {
		enum Fields implements FieldId {
			Fields,
		};

		// enum MsgSynch { InSync, Parallel };
		// enum MsgAccess { QueryOnly, ModifiesOthers, ModifiesThis };

	}

	public interface TypeLoader extends TypeDef {
		enum Fields {
			RootDir, Ext, Template
		};
	}
}
