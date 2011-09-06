package sandbox.lab.editor;

import java.util.ArrayList;

import javax.swing.*;

import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

import sandbox.lab.LabVariantEditor;

public class LabVarEditorObSet extends LabVariantEditor {
	
	class MemberListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;
		@Override
		public int getSize() {
			return alMembers.size();
		}

		@Override
		public Object getElementAt(int index) {
			return alMembers.get(index);
		}
		
		void updated() {
			fireContentsChanged(this, 0, getSize());
		}
	};
	
	ArrayList<DustEntity> alMembers = new ArrayList<DustEntity>();

	JList lstVal;
	MemberListModel lmMembers;
		
	@Override
	protected JComponent createEditorComp() throws Exception {
		lmMembers = new MemberListModel();
		lstVal = new JList(lmMembers);
		JScrollPane scp = new JScrollPane(lstVal);
		return scp;
	}

	@Override
	public void updateData() {
		DustVariant v = getVar();
		
		alMembers.clear();
		
		if ( ! v.isNull() ) {
			for ( DustVariant vv : v.getMembers()) {
				alMembers.add(vv.getValueObject());
			}
		}
		
		updateState();
	}
}
