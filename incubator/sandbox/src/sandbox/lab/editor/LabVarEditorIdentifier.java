package sandbox.lab.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import dust.api.components.DustVariant;
import dust.api.wrappers.DustIdentifier;

import sandbox.lab.LabVariantEditor;

public class LabVarEditorIdentifier extends LabVariantEditor {
	JTextField tfVal;
	
	ActionListener alVal = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			getVar().setValueIdentifier(new DustIdentifier(tfVal.getText()));
			
			updateData();
		}
	};
	
	@Override
	protected JComponent createEditorComp() throws Exception {
		tfVal = new JTextField();
		tfVal.addActionListener(alVal);
		return tfVal;
	}

	@Override
	public void updateData() {
		DustVariant v = getVar();
		
		String txt = v.isNull() ? "" : getVar().getValueIdentifier().toString();
		tfVal.setText(txt);
		
		updateState();
	}
}
