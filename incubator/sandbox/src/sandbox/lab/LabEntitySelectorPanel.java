package sandbox.lab;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dust.api.DustConstants.DustDeclId;

public class LabEntitySelectorPanel extends JPanel implements LabConstants.StatefulComponent {
	private static final long serialVersionUID = 1L;

	DustDeclId typeId;
	LabData data;
	
	ArrayList<LabEntity> alRootEntities = new ArrayList<LabEntity>();

	EntityListModel lmEntities;
	JList lstEntities;
	
	JButton btnAccept;
	JButton btnCancel;
	
	Set<LabEntity> setSelEntities = new HashSet<LabEntity>();
	
	class EntityListModel extends AbstractListModel  {
		private static final long serialVersionUID = 1L;

		@Override
		public int getSize() {
			return alRootEntities.size();
		}

		@Override
		public Object getElementAt(int index) {
			return alRootEntities.get(index);
		}
		
		void reload() {
			fireContentsChanged(this, 0, getSize());
		}

	};

	
	public LabEntitySelectorPanel(DustDeclId typeId, boolean multiple) {
		super(new BorderLayout(4, 4));
		
		this.typeId = typeId;
		
		alRootEntities.addAll(data.findEntities(typeId));

		lmEntities = new EntityListModel();

		lstEntities = new JList(lmEntities);
		ListSelectionModel lm = lstEntities.getSelectionModel();
		
		lm.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateState();
			}
		});
		
		lm.setSelectionMode(multiple ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
		
		lstEntities.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					accept();
				}
				super.mouseClicked(e);
			}
		});
	
		JScrollPane scpList = new JScrollPane(lstEntities);

		add(scpList, BorderLayout.CENTER);
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				accept();
			}
		};
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(al);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(al);

		JPanel pnl1 = new JPanel(null);
		pnl1.setLayout(new BoxLayout(pnl1, BoxLayout.X_AXIS));
		pnl1.add(Box.createHorizontalGlue());
		pnl1.add(btnAccept);
		pnl1.add(Box.createHorizontalGlue());
		pnl1.add(btnCancel);
		pnl1.add(Box.createHorizontalGlue());
		
		add(pnl1, BorderLayout.SOUTH);

	}
	
	void accept() {
		setSelEntities.clear();
		
		for ( Object o : lstEntities.getSelectedValues() ) {
			setSelEntities.add((LabEntity)o);
		}
	}
	
	protected void accepted() {};
	
	@Override
	public void updateState() {
		btnAccept.setEnabled(0 < lstEntities.getSelectedIndices().length);
	}

}
