package sandbox.lab;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.*;

import javax.swing.*;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;

import sandbox.persistence.PersistenceValueExtractor;

public class LabAspectPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	static class AspectInfo {
		DustDeclId typeId;
		
		String name;
		Map<Enum<? extends FieldId>, Class<? extends LabVariantEditor>> mapFields;
		
		AspectInfo(DustDeclId typeId) throws Exception {
			name = typeId.toString();
			mapFields = new TreeMap<Enum<? extends FieldId>, Class<? extends LabVariantEditor>>();
			
			for (PersistenceValueExtractor.Value val : vx.getFields(typeId)) {
				mapFields.put(val.getId(), LabVariantEditor.getEditorClass(val));
			}
		}
	}
	
	static PersistenceValueExtractor vx = new PersistenceValueExtractor();
		
	AspectInfo myAspInfo;
	DustAspect myAspect;
	
	public LabAspectPanel(DustEntity e, DustDeclId id) throws Exception {
		super(new BorderLayout(4, 4));
		myAspect = e.getAspect(id, true);
		
		myAspInfo = getAspInfo(id);
		
		add(new JLabel(myAspInfo.name), BorderLayout.NORTH);
		
		JPanel pnlContent = new JPanel(new GridLayout(myAspInfo.mapFields.size(), 2));
		
		for ( Map.Entry<Enum<? extends FieldId>, Class<? extends LabVariantEditor>> eFld : myAspInfo.mapFields.entrySet()){
			LabVariantEditor lve = eFld.getValue().newInstance();
			lve.init(myAspect, eFld.getKey());
			
			pnlContent.add(lve.lblField);
			pnlContent.add(lve.compEditor);
		}
		
		add(pnlContent, BorderLayout.CENTER);
	}
	
	static synchronized AspectInfo getAspInfo(DustDeclId aspId) throws Exception {
		final Map<DustDeclId, AspectInfo> mapAspInfo = new HashMap<DustDeclId, AspectInfo>();

		AspectInfo ret = mapAspInfo.get(aspId);
		
		if ( null == ret ) {
			ret = new AspectInfo(aspId);
			mapAspInfo.put(aspId, ret);
		}
		
		return ret;
	}
}
