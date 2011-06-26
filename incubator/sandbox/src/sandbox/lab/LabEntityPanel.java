package sandbox.lab;

import javax.swing.*;

import dust.api.DustConstants.DustDeclId;
import dust.api.components.DustEntity;

public class LabEntityPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	LabFrame frame;
	DustEntity myEntity;

	public LabEntityPanel(LabFrame frame, DustEntity e) {
		super( null );
		setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) );
		
		this.frame = frame;
		myEntity = e;
		
		for ( DustDeclId aspId : e.getTypes() ) {
			try {
				add( new LabAspectPanel(this, e, aspId));
			} catch (Exception e1) {
				add( new JLabel(aspId + " failed with " + e1.toString()));
				e1.printStackTrace();
			}
		}
	}
}
