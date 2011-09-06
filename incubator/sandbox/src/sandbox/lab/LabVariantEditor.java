package sandbox.lab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dust.api.DustConstants;
import dust.api.components.DustAspect;
import dust.api.components.DustVariant;

import sandbox.lab.LabConstants.DataAwareComponent;
import sandbox.lab.editor.*;
import sandbox.persistence.PersistenceValueExtractor;

public abstract class LabVariantEditor implements DataAwareComponent, DustConstants {
	public static final String VAL_UNSET = "null";
	
	LabAspectPanel aspPanel;
	
	Enum<? extends FieldId> fieldId;
	String fieldName;
	DustVariant myVar;
	
	JLabel lblField;
	JComponent compEditor;
	JButton btClear;
	
	ActionListener clearListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent action) {
			if ( btClear == action.getSource() ){
				myVar.setData(null, VariantSetMode.clear, null);
				updateData();
			}
		}
	};
	
	public LabVariantEditor(){};
	
	public void init(LabAspectPanel aspPanel, DustAspect asp, Enum<? extends FieldId> fieldId) throws Exception {
		this.aspPanel = aspPanel;
		
		fieldName = fieldId.name();
		myVar = asp.getField(fieldId);
		
		lblField = new JLabel(fieldName);
		compEditor = createEditorComp();
		
		btClear = new JButton("X");
		btClear.addActionListener(clearListener);
		
		updateData();
	}
	
	public static Class<? extends LabVariantEditor> getEditorClass(PersistenceValueExtractor.Value fldVal) {
		switch ( fldVal.getType() ) {
		case Boolean:
			return LabVarEditorBoolean.class;
		case Identifier:
			return LabVarEditorIdentifier.class;
		case String:
			return LabVarEditorString.class;
		case ValueSet:
			return LabVarEditorValSet.class;
		case ObSingle:
			return LabVarEditorObSingle.class;
		case ObSet:
			return LabVarEditorObSet.class;
		case ObType:
		case Integer:
		case Double:
		case ImmutableDate:
		case ObArray:
		case ObMap:
		case ByteArray:
		default:
			return LabVarEditorDummy.class;				
		}
	}

	@Override
	public void updateState() {
		btClear.setEnabled( !myVar.isNull() );
	}
	
	protected DustVariant getVar() {
		return myVar;
	}
	
	protected abstract JComponent createEditorComp() throws Exception;
	
	protected LabFrame getFrame() {
		return aspPanel.entPanel.frame;
	}

}
