package sandbox.lab.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dust.api.components.DustVariant;

import sandbox.lab.LabVariantEditor;

public class LabVarEditorObSingle extends LabVariantEditor {
	JButton btEdit;
	JButton btSelect;
	JLabel lblObInfo;
	
	ActionListener alVal = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DustVariant v = getVar();
			
			if ( btEdit == arg0.getSource() ) {
				getFrame().selectEntityFrame(v.getValueObject(), true);
			} else {
				
			}
						
			updateData();
		}
	};
	
	@Override
	protected JComponent createEditorComp() throws Exception {
		btEdit = new JButton("Edit");
		btSelect = new JButton("Select");
		
		btEdit.addActionListener(alVal);
		btSelect.addActionListener(alVal);
		
		lblObInfo = new JLabel();
		
		JPanel p1 = new JPanel( null );
		p1.setLayout(new BoxLayout(p1, BoxLayout.LINE_AXIS));
		p1.add(btEdit);
		p1.add(btSelect);
		
		JPanel p2 = new JPanel( new BorderLayout() );
		p2.add(lblObInfo, BorderLayout.CENTER);
		p2.add(p1, BorderLayout.EAST);
		
		return p2;
	}

	@Override
	public void updateData() {
		DustVariant v = getVar();
		
		String val = v.isNull() ? VAL_UNSET : v.getValueObject().toString();
		lblObInfo.setText(val);
		
		updateState();
	}
	
	@Override
	public void updateState() {
		btEdit.setEnabled(!getVar().isNull());
		super.updateState();
	}
}
