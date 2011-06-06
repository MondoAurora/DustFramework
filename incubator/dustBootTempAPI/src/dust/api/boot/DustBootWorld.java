package dust.api.boot;

import java.util.HashMap;
import java.util.Map;

import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustMessage;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;

public class DustBootWorld extends DustWorld {
	Map<Class<?>, DustBootTypeId> mapTypes = new HashMap<Class<?>, DustBootTypeId>();

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
	public DustVariant getVar(Enum<? extends FieldId> fieldId, FieldType fieldType, Object value) {
		DustBootVariant bv = new DustBootVariant(fieldId, fieldType);
		bv.setData(value, null, null);
		
		return bv;
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
		irProc.entityFound(new DustBootEntity(primaryType, knownFields));
		irProc.searchFinished();
	}
	
	@Override
	protected void send(DustEntity from, DustAspect to, DustMessage msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
