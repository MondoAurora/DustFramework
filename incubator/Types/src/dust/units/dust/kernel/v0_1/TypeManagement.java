package dust.units.dust.kernel.v0_1;

import dust.api.DustDeclarationConstants;

public interface TypeManagement extends DustDeclarationConstants {
	public interface Module extends TypeDef {
		enum Fields {
			Vendor, Domain, Version
		};
		
		enum Shared implements SharedDef {
			Kernel
		};
	}

	public interface Type extends TypeDef {
		enum Fields implements FieldId {
			Unit, Parent, Fields, AutoInit, Messages, Shared, Resource
		};
		enum Shared implements SharedDef {
			Self
		};
	}

	public interface FieldValue extends TypeDef {
		enum Fields {
			Ordinal
		};
	}

	public interface Field extends TypeDef {
		enum Fields {
			Type, Mode, ObType, Values
		};
	}

	public interface Resource extends TypeDef {
		enum Fields {
			Fields, 
		};
	}

	public interface Shared extends TypeDef {
		enum Fields {
			ObType, 
		};
	}

	public interface Message extends MsgDef {
		enum Fields {
			Fields, 
		};
		
//	enum MsgSynch { InSync, Parallel };
//	enum MsgAccess { QueryOnly, ModifiesOthers, ModifiesThis };

	}

	public interface TypeLoader extends TypeDef {
		enum Fields {
			RootDir, Ext, Template
		};
	}
}
