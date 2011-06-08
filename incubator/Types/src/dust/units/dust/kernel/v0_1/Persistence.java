package dust.units.dust.kernel.v0_1;

import dust.api.DustDeclarationConstants.MsgDef;
import dust.api.DustDeclarationConstants.SharedDef;
import dust.api.DustDeclarationConstants.TypeDef;

public interface Persistence extends TypeDef {
	public interface Connector extends TypeDef {
		enum Fields {
			LocalModify, // boolean yes/no
			ExternalModify
			// valueset about the refresh methods
		};

		static class Values {
			enum ExternalModify {
				None, // no external change, we own the persistence source
				Passive, // we have to check for modifications "blind" without help
				PassiveDiff, // we have to check but differential update is supported
				Active, // sends events on changes, but no differential update is
								// available
				ActiveDiff
				// sends events and diff available
			};
		}

		enum Messages {
			Load, Save, Commit, Rollback;

			interface LoadMsg extends MsgDef {
				enum Fields {
					TypeId, PersId;
				};
			}

			interface SaveMsg extends MsgDef {
				enum Fields {
					ObjectList, Commit;
				};
			}
		}

		enum Shared implements SharedDef {
			BootConn
		};
	}
}