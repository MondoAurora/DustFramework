package sandbox.lab.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import dust.api.components.DustVariant;

import sandbox.lab.*;

public class LabVarEditorValSet extends LabVariantEditor {
	JComboBox cbVal;
	Class valClass;
	Map<Integer, Enum<?>> mapValues;
	
	ActionListener alVal = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DustVariant v = getVar();
			
			Enum<?> val = (Enum<?>) cbVal.getSelectedItem();
			v.setValueValSet(val);
			
			updateData();
		}
	};
	
	@Override
	protected JComponent createEditorComp() throws Exception {
		mapValues = new TreeMap<Integer, Enum<?>>();
		Enum<?> es = null;
		for ( Enum<?> e : getFrame().getData().getValEx().getValues(getVar().getId()) ) {
			es = e;
			mapValues.put(e.ordinal(), e);
		}
		
		valClass = es.getDeclaringClass();
		
		cbVal = new JComboBox(mapValues.values().toArray());
		cbVal.addActionListener(alVal);
		return cbVal;
	}

	@Override
	public void updateData() {
		DustVariant v = getVar();
		
		if ( v.isNull() ) {
			cbVal.setSelectedIndex(-1);
		} else {
			cbVal.setSelectedItem(Enum.valueOf(valClass, v.getValueValSet()));
		}

		updateState();
	}
}
