package sandbox.lab;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.*;

import sandbox.Test;
import sandbox.lab.LabData.LabEntity;

public class LabFrame extends JFrame implements Test.TestItem {
	private static final long serialVersionUID = 1L;

	static String title = "dustLab - sandbox version";

	enum MenuCmds {
		Open, Close, Exit, Help, NewEntity, DelEntity, NewAspect, DelAspect
	};

	class MenuElement {
		MenuCmds cmd;

		String name;
		MenuElement[] submenu;

		public MenuElement(MenuCmds cmd) {
			this.cmd = cmd;
		}

		public MenuElement(String name, MenuElement[] submenu) {
			this.cmd = null;

			this.name = name;
			this.submenu = submenu;
		}

		JMenuItem getItem() {
			JMenuItem i;

			if (null == cmd) {
				i = new JMenu(name);
				for (MenuElement e : submenu) {
					i.add(e.getItem());
				}
			} else {
				i = new JMenuItem(cmd.name());
				i.setActionCommand(cmd.name());
				i.addActionListener(menuListener);
			}

			return i;
		}
	}

	MenuElement[] menus = new MenuElement[] {
		new MenuElement("File", new MenuElement[] { new MenuElement(MenuCmds.Open), new MenuElement(MenuCmds.Close),
			new MenuElement(MenuCmds.Exit), }),
		new MenuElement("Help", new MenuElement[] { new MenuElement(MenuCmds.Help), }), };

	ActionListener menuListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			processCommand(MenuCmds.valueOf(e.getActionCommand()));
		}
	};

	class EntityListModel extends AbstractListModel implements ListSelectionListener {
		@Override
		public int getSize() {
			return data.alEntities.size();
		}

		@Override
		public Object getElementAt(int index) {
			return data.alEntities.get(index);
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				Object sel = lstEntities.getSelectedValue();
				selectEntityFrame(sel, false);
				updateState();
			}
		}
	};

	MouseAdapter dblClickListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				doubleClicked((JComponent) e.getSource());
			}
			super.mouseClicked(e);
		}
	};

	class FrameAction extends AbstractAction {
		MenuCmds cmd;

		public FrameAction(String name, MenuCmds cmd) {
			super(name);
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			processCommand(cmd);
		}
	}
	
	class EntityFrame extends JInternalFrame {
		Object content;
		
		EntityFrame(Object content) {
			super(content.toString(), true, true, true, true);
			this.content = content;
			
			addInternalFrameListener(il);
			getContentPane().add(new LabEntityPanel(((LabEntity)content).e), BorderLayout.CENTER);
			pack();
			show();
		}
	}

	LabData data;

	JDesktopPane desktop;
	JSplitPane splMain;

	EntityListModel lmEntities;
	JList lstEntities;

	InternalFrameListener il;

	Action actDelEntity = new FrameAction("Delete", MenuCmds.DelEntity);
	Action actNewEntity = new FrameAction("Create", MenuCmds.NewEntity);

	Action actDelAspect;
	Action actNewAspect;

	Map<Object, EntityFrame> mapEntityFrames = new HashMap<Object, EntityFrame>();

	public LabFrame() throws HeadlessException {
		super(title);
		JPanel pnl, pnl1;

		data = new LabData();

		JMenuBar mb = new JMenuBar();

		for (MenuElement e : menus) {
			mb.add(e.getItem());
		}
		setJMenuBar(mb);

		splMain = new JSplitPane();
		getContentPane().add(splMain);

		desktop = new JDesktopPane();
		desktop.setPreferredSize(new Dimension(400, 200));

		splMain.setRightComponent(desktop);

		lmEntities = new EntityListModel();

		lstEntities = new JList(lmEntities);
		lstEntities.getSelectionModel().addListSelectionListener(lmEntities);
		lstEntities.addMouseListener(dblClickListener);

		JScrollPane scpList = new JScrollPane(lstEntities);
		pnl = new JPanel(new BorderLayout(4, 4));

		pnl.add(scpList, BorderLayout.CENTER);

		pnl1 = new JPanel(null);
		pnl1.setLayout(new BoxLayout(pnl1, BoxLayout.X_AXIS));
		pnl1.add(Box.createHorizontalGlue());
		pnl1.add(new JButton(actNewEntity));
		pnl1.add(Box.createHorizontalGlue());
		pnl1.add(new JButton(actDelEntity));
		pnl1.add(Box.createHorizontalGlue());
		pnl.add(pnl1, BorderLayout.SOUTH);

		splMain.setLeftComponent(pnl);

		il = new InternalFrameAdapter() {

			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				Object sel = ((EntityFrame)e.getInternalFrame()).content;
				mapEntityFrames.remove(sel);
			}

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				setTitle(title + " - " + e.getInternalFrame().getTitle());
				Object sel = ((EntityFrame)e.getInternalFrame()).content;
				lstEntities.setSelectedValue(sel, true);
			}
		};
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);

		updateState();
	}

	void updateState() {
		actDelEntity.setEnabled(-1 != lstEntities.getSelectedIndex());
	}

	void selectEntityFrame(Object labE, boolean createIfMissing) {
		EntityFrame iFrm = mapEntityFrames.get(labE);

		if (null == iFrm) {
			if (createIfMissing) {
				iFrm = new EntityFrame(labE);

				mapEntityFrames.put(labE, iFrm);
				desktop.add(iFrm);
			}
		} 

		if (null != iFrm) {
			iFrm.show();
			iFrm.toFront();
			try {
				iFrm.setSelected(true);
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void doubleClicked(JComponent src) {
		if (src == lstEntities) {
			Object sel = lstEntities.getSelectedValue();

			if (!mapEntityFrames.containsKey(sel)) {
				selectEntityFrame(sel, true);
			}
		}
	}

	public void processCommand(MenuCmds cmd) {
		System.out.println("Command: " + cmd.name());
	}

	@Override
	public void init(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void test() throws Exception {
	}

}
