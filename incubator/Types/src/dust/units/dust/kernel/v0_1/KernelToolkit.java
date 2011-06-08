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
