package dust.units.dust.testone.v0_1;

import dust.api.DustDeclarationConstants;

public interface GraphPrimitives extends DustDeclarationConstants {
	public interface Point extends TypeDef {
		enum Fields {
			X, Y
		};
		
		enum Messages {
			Move;
			
			interface MoveMsg extends MsgDef {
				enum Fields {
					X, Y;
				};
			}			
		};
	}

	public interface Circle extends Point {
		enum Fields {
			Radius
		};
	}

	public interface Label extends Point {
		enum Fields {
			Text, Color, Style;
			
			public static interface Values {
				public enum Color {
					Red, Green, Blue
				};
				
				public enum Style {
					Normal, Italic, Bold, BoldItalic
				};		
			}
		};
		
		enum SharedInstances implements SharedDef {
			TopLabel, BottomLabel;
		}
	
		enum Messages {
			Enlarge;
						
			interface EnlargeMgs extends MsgDef {
				enum Fields {
					X, Y;
				};
			}
		};
	}
}
