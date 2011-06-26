package sandbox.lab.editor;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import dust.api.components.DustVariant;

import sandbox.lab.LabVariantEditor;

public class LabVarEditorDummy extends LabVariantEditor {
	JLabel lbl;
	
	@Override
	protected JComponent createEditorComp() throws Exception {
		lbl = new JLabel();
		lbl.setMaximumSize(new Dimension(30, 30));		
		return lbl;
	}

	@Override
	public void updateData() {
		DustVariant v = getVar();
		
		String txt = v.isNull() ? VAL_UNSET : getVar().toString();
		
		if ( 50 < txt.length()) {
			txt = txt.substring(0, 50) + "...";
		}
		lbl.setText(txt);
		
		updateState();
	}
}
