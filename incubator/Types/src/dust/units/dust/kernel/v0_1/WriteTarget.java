package dust.units.dust.kernel.v0_1;

import dust.api.DustDeclarationConstants;

public interface WriteTarget extends DustDeclarationConstants {

	public interface Raw extends TypeDef {
		enum Messages {
			Open, Write, Flush, Close;
			
			public interface OpenMsg extends MsgDef {
				enum Fields {
					Append, CreateMissing, AutoFlush;
				};
			}
			
			public interface WriteMsg extends MsgDef {
				enum Fields {
					Content, Offset, Length;
				};
			}
		}
	}

	public interface Text extends Raw {
		enum Fields {
			Encoding
		};

		enum Messages {
			EndLine, IndentInc, IndentDec;
		}
		
		enum Shared implements SharedDef {
			DefaultOut, DefaultErr
		};

	}

}
