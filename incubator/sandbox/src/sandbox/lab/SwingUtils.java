package sandbox.lab;

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class SwingUtils {
	public static class GridBagUtil {
		JPanel pnl;
		GridBagConstraints cons;
		Insets defaultInsets;
		
		
		
		public GridBagUtil(JPanel p) {
			this(p, new Insets(0, 0, 0, 0));
		}
		
		public GridBagUtil(JPanel p, Insets defInsets) {
			this.pnl = p;
			defaultInsets = defInsets;
			
			pnl.setLayout(new GridBagLayout());
			cons = new GridBagConstraints();
			reset();
			
			cons.gridx = cons.gridy = 0;
		}
		
		public void addComp(JComponent comp) {
			pnl.add(comp, cons);
			reset();
			cons.gridx += 1;
		}
		
		public void reset() {
			cons.anchor = GridBagConstraints.CENTER;
			
			cons.fill = GridBagConstraints.BOTH;
			cons.gridheight = cons.gridwidth = 1;
			cons.ipadx = cons.ipady = 0;
			cons.weightx = cons.weighty = 0;
			cons.insets = defaultInsets;
		}
		
		public void nextRow() {
			cons.gridx = 0;
			cons.gridy += 1;
		}
	}
}
