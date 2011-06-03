package dust.units.dust.kernel.v0_1;

import dust.api.DustDeclarationConstants;

public interface KernelToolkit extends DustDeclarationConstants {

	public interface StrConverter extends TypeDef {
		enum Shared implements SharedDef {
			Identifier, Long, Double, ImmutableDate, Boolean, ValueSet, ByteArray
		};
	}

	public interface Encoding extends TypeDef {
		enum Fields {
			Code
		};
		
		enum Shared implements SharedDef {
			ASCII, UTF8, ISO_8859_1, ISO_8859_2, Default
		};
	}
	
	public interface LocaleInfo extends TypeDef {
		enum Fields {
			Language, Country, Variant
		};
	}

	public interface StringProcessor extends TypeDef {
		enum Fields {
			RequestTemporal
		};
		
		enum Messages {
			Process;
			
			public interface ProcessMsg extends MsgDef {
				enum Fields {
					Content, EOL, Process;
				};
			}
		}
	}

	public interface CommandProcessor extends StringProcessor {
		enum Fields {
			User
		};
	}

	public interface Template extends StringProcessor {
		enum Fields {
			TemplateString, TargetType
		};
	}

	public interface TextBuffer extends WriteTarget.Text {
		enum Fields {
			LineLength
		};
	}

	public interface PersistenceConnector extends TypeDef {
		enum Fields {
			LocalModify, // boolean yes/no
			ExternalModify // valueset about the refresh methods
		};
		
		static class Values {
			enum ExternalModify {
				None, // no external change, we own the persistence source
				Passive, // we have to check for modifications "blind" without help
				PassiveDiff, // we have to check but differential update is supported
				Active, // sends events on changes, but no differential update is available
				ActiveDiff // sends events and diff available
			};
		}

		enum Messages {
			Load, Save, Commit, Rollback;
			
			interface LoadMsg extends MsgDef {
				enum Fields {
					PersId;
				};
			}
			
			interface SaveMsg extends MsgDef {
				enum Fields {
					ObjectList, Immediate;
				};
			}
		}
		
		enum Shared implements SharedDef {
			BootConn
		};
	}
	
	public interface Filter extends TypeDef {
		enum Messages {
			isAccepted;
			
			public interface MsgIsAccepted extends MsgDef {
				enum Fields {
					toCheck;
				};
			}
		}
	}
	
	public interface API extends TypeDef {
		enum Messages {
			getObject, createObject, findObject, getMember, send;
			
			public interface MsgGetObject extends MsgDef {
				enum Fields {
					type, persId, getObject;
				};
			}
			
			public interface MsgCreateObject extends MsgDef {
				enum Fields {
					type, values, createObject;
				};
			}
			
			public interface MsgFindObject extends MsgDef {
				enum Fields {
					type, filter, findObject;
				};
			}
			
			public interface MsgGetMember extends MsgDef {
				enum Fields {
					parent, path, getMember;
				};
			}
			
			public interface MsgSend extends MsgDef {
				enum Fields {
					from, to, data;
				};
			}
		}
	}
}
