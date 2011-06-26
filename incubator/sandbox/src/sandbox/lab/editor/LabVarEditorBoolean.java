package sandbox.lab.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import dust.api.components.DustVariant;

import sandbox.lab.LabVariantEditor;

public class LabVarEditorBoolean extends LabVariantEditor {
	JButton btVal;
	
	ActionListener alVal = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DustVariant v = getVar();
			
			v.setValueBoolean( v.isNull() || !v.getValueBoolean() );
			
			updateData();
		}
	};
	
	@Override
	protected JComponent createEditorComp() throws Exception {
		btVal = new JButton();
		btVal.addActionListener(alVal);
		return btVal;
	}

	@Override
	public void updateData() {
		DustVariant v = getVar();
		
		String val = v.isNull() ? VAL_UNSET : v.getValueBoolean() ? "+" : "-";
		btVal.setText(val);
		
		updateState();
	}
}
