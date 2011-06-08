package dust.api.boot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dust.api.DustConstants;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustMessage;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtilVariant;

public class DustBootWorld extends DustWorld {
	Map<Class<?>, DustBootTypeId> mapTypes = new HashMap<Class<?>, DustBootTypeId>();
	Map<DustDeclId, Set<DustEntity>> mapEntities = new HashMap<DustConstants.DustDeclId, Set<DustEntity>>();

	DustBootTypeId getId(Class<?> cc) {
		DustBootTypeId ti = mapTypes.get(cc);
		if ( null == ti ) {
			ti = new DustBootTypeId(cc);
			mapTypes.put(cc, ti);
		}
		return ti;
	}
	
	@Override
	public DustDeclId getTypeId(Class<? extends TypeDef> type) {
		return getId(type);
	}

	@Override
	public DustDeclId getMessageId(Class<? extends MsgDef> type) {
		return getId(type);
	}

	@Override
	public DustVariant getVar(DustDeclId typeId, Enum<? extends FieldId> fieldId, Object value) {
		return new DustUtilVariant(fieldId, value);
	}

	@Override
	public DustMessage getMessage(DustDeclId msgId, DustVariant[] fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoke(InvokeResponseProcessor irProc, DustDeclId primaryType, DustVariant[] knownFields,
		boolean createIfMissing, Enum<? extends FieldId>[] requiredFields, DustEntity filter) {
		irProc.searchStarted();
		
		boolean create = createIfMissing;
		Set<DustEntity> setInstances = mapEntities.get(primaryType);
		if ( null != setInstances ) {
			for ( DustEntity e : setInstances ) {
				boolean match = true;
				
				for ( DustVariant v : knownFields ) {
					DustAspect asp = e.getAspect(v.getTypeId());
					if ( (null == asp ) || !v.equals(asp.getField(v.getId())) ) {
						match = false;
						break;
					}
				}
				
				if ( match ) {
					create = false;
					irProc.entityFound(e);
				}
			}
		}
		
		if ( create ) {
			irProc.entityFound(new DustBootEntity(primaryType, knownFields));			
		}
		
		irProc.searchFinished();
	}
	
	void addEntity(DustDeclId id, DustEntity e) {
		Set<DustEntity> setInstances = mapEntities.get(id);
		if ( null == setInstances ) {
			setInstances = new HashSet<DustEntity>();
			mapEntities.put(id, setInstances);
		} 
		
		setInstances.add(e);
	}
	
	void dropEntity(DustEntity e) {
		for ( DustDeclId id : e.getTypes() ) {
			Set<DustEntity> setInstances = mapEntities.get(id);
			if ( null != setInstances ) {
				setInstances.remove(e);
			} 
		}
	}
	
	@Override
	protected void send(DustEntity from, DustAspect to, DustMessage msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
