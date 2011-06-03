package dust.units.dust.kernel.v0_1;

import dust.api.DustDeclarationConstants;

public interface ContextManagement extends DustDeclarationConstants {
	public interface Context extends TypeDef {
		enum Fields {
			Parent, DirtyMembers, StatusCollector
		};
		
		enum Messages {
			Check, Commit, Reset, Done, Cancel;
		}

		enum Shared implements SharedDef {
			Current
		};
	}

	public interface StatusCollector extends TypeDef {
		enum Fields {
			StatMsgs
		};
	}

}
