package sandbox.lab;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;

import sandbox.lab.LabConstants.StatefulComponent;
import sandbox.lab.SwingUtils.GridBagUtil;
import sandbox.persistence.PersistenceValueExtractor;

public class LabAspectPanel extends JPanel implements StatefulComponent {
	private static final long serialVersionUID = 1L;
	
	static class AspectInfo {
		DustDeclId typeId;
		
		String name;
		Map<Enum<? extends FieldId>, Class<? extends LabVariantEditor>> mapFields;
		
		AspectInfo(DustDeclId typeId, PersistenceValueExtractor vx) throws Exception {
			name = typeId.toString();
			mapFields = new TreeMap<Enum<? extends FieldId>, Class<? extends LabVariantEditor>>();
			
			for (PersistenceValueExtractor.Value val : vx.getFields(typeId)) {
				mapFields.put(val.getId(), LabVariantEditor.getEditorClass(val));
			}
		}
	}
	
	AspectInfo myAspInfo;
	DustAspect myAspect;
	GridBagUtil gbu;
	
	LabEntityPanel entPanel;
	
	Map<Enum<? extends FieldId>, LabVariantEditor> mapFieldEditors = new HashMap<Enum<? extends FieldId>, LabVariantEditor>();
	PersistenceValueExtractor vx;
	
	public LabAspectPanel(LabEntityPanel entPanel, DustEntity e, DustDeclId id) throws Exception {
		super(new BorderLayout(4, 4));
		
		this.entPanel = entPanel;
		
		myAspect = e.getAspect(id, true);
		vx = entPanel.frame.getData().vx;
		myAspInfo = getAspInfo(id, vx);
		
		add(new JLabel(myAspInfo.name), BorderLayout.NORTH);
		
		JPanel pnlContent = new JPanel(null);
		gbu = new GridBagUtil(pnlContent, new Insets(5, 5, 5, 5));
		
		for ( Map.Entry<Enum<? extends FieldId>, Class<? extends LabVariantEditor>> eFld : myAspInfo.mapFields.entrySet()){
			LabVariantEditor lve = eFld.getValue().newInstance();
			
			Enum<? extends FieldId> fldId = eFld.getKey();
			mapFieldEditors.put(fldId, lve);
			lve.init(this, myAspect, fldId);
			
			gbu.addComp(lve.lblField);
			gbu.cons.weightx = 1.0;
			gbu.addComp(lve.compEditor);
			gbu.addComp(lve.btClear);
			
			gbu.nextRow();
		}
		
		add(pnlContent, BorderLayout.CENTER);
		
		updateState();
	}
	
	static synchronized AspectInfo getAspInfo(DustDeclId aspId, PersistenceValueExtractor vx) throws Exception {
		final Map<DustDeclId, AspectInfo> mapAspInfo = new HashMap<DustDeclId, AspectInfo>();

		AspectInfo ret = mapAspInfo.get(aspId);
		
		if ( null == ret ) {
			ret = new AspectInfo(aspId, vx);
			mapAspInfo.put(aspId, ret);
		}
		
		return ret;
	}

	@Override
	public void updateState() {
		for ( LabVariantEditor lve : mapFieldEditors.values() )	{
			lve.updateState();
		}
	}	
}
