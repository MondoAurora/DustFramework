package sandbox.lab;

import javax.swing.JComponent;
import javax.swing.JLabel;

import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.DustAspect;
import dust.api.components.DustVariant;

import sandbox.persistence.PersistenceValueExtractor;

public class LabVariantEditor {
	Enum<? extends FieldId> fieldId;
	String fieldName;
	DustVariant myVar;
	
	JLabel lblField;
	JComponent compEditor;
	
	public LabVariantEditor(){};
	
	public void init(DustAspect asp, Enum<? extends FieldId> fieldId) {
		fieldName = fieldId.name();
		myVar = asp.getField(fieldId);
		
		lblField = new JLabel(fieldName);
		compEditor = new JLabel(asp.getField(fieldId).toString());
	}
	
	public static Class<? extends LabVariantEditor> getEditorClass(PersistenceValueExtractor.Value fldVal) {
		return LabVariantEditor.class;
	}
}
