package dust.api.utils;

import java.util.Set;

import dust.api.DustConstants;
import dust.api.DustConstants.InvokeResponseProcessor;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public class DustUtilInvokeEntityProcessor implements DustConstants, InvokeResponseProcessor {
	boolean lazyCreate;
	boolean forceSingle;
	
	boolean allowMore;
	
	DustEntity entity;
	Set<DustEntity> setEntities;
	Exception ex;
	
	public DustUtilInvokeEntityProcessor(boolean lazyCreate, boolean forceSingle) {
		this.lazyCreate = lazyCreate;
		this.forceSingle = forceSingle;
	}
	
	public DustEntity searchOrCreate(DustDeclId primaryType, DustVariant[] knownFields, boolean forceSingle) throws Exception {
		this.forceSingle = forceSingle;
		allowMore = forceSingle;
		setEntities = null;
		
		DustUtils.getWorld().invoke(this, primaryType, knownFields, true, null, null);
		
		if ( null != ex ) {
			throw ex;
		}
		
		return entity;
	}
	
	@Override
	public void searchStarted() {
		entity = null;
		ex = null;
		if ( null != setEntities ) {
			setEntities.clear();
		}
	}

	@Override
	public boolean entityFound(DustEntity entity_) {
		if ( forceSingle && (null != entity) ) {
			ex = new Exception("Multiple instances found!");
			return false;
		}
		if ( null == setEntities ) {
			entity = entity_;
		} else {
			setEntities.add(entity_);
		}
		
		return allowMore;
	}

	@Override
	public void searchFinished() {
		// TODO Auto-generated method stub
		
	}

}
