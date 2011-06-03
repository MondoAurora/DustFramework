package dust.units.dust.gui.v0_1;

import dust.api.DustDeclarationConstants;


public interface CommonWidgets extends DustDeclarationConstants {
	public interface Panel extends TypeDef {
	}
	
	public interface TitledWindow extends Panel {
		enum Fields {
			Title
		};
	}
	
	public interface AppWindow extends TitledWindow {
		enum Fields {
			RootPanel, ExitOnClose
		};
	}
	
	public interface ObjectWindow extends TitledWindow {
		enum Fields {
			TargetObject, Editors
		};
	}
	
	public interface FieldEditor extends Panel {
		enum Fields {
			TargetObject, Field, Hint
		};
	}
	
	public interface Renderer extends TypeDef {
		enum Fields {
			AppFrame,
		};
		
		enum Messages {
			ManageObject;
						
			interface ManageObjectMgs extends MsgDef {
				enum Fields {
					TargetObject, ManageGUI, Language;
				};
			}
		};
		
		enum Shared implements SharedDef {
			DefaultRenderer
		};
	}
}
