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
			Unit, isGlobal, Messages, Shared, Resource
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
				Identifier, String, Integer, Double, ImmutableDate, Boolean, ValueSet, ByteArray, ObType, ObSingle, ObSet, ObArray, ObMap
			};
		}
	}

	public interface Resource extends TypeDef {
	}

	public interface Shared extends TypeDef {
		enum Fields implements FieldId {
			ObType,
		};
	}

	public interface Message extends TypeDef {
	}
}
