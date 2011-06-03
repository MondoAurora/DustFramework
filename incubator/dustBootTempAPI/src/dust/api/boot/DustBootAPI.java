package dust.api.boot;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import dust.api.components.DustAPI;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustMessage;
import dust.api.components.DustVariant;
import dust.api.components.DustVariantStructure;

public class DustBootAPI extends DustAPI {
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
	public DustVariantStructure getVarStruct(DustDeclId typeId, DustVariant[] fields) {
		return new DustBootVariantStructure(typeId, fields);
	}

	@Override
	public DustMessage getMessage(DustDeclId msgId, DustVariant[] fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DustEntity getEntity(DustDeclId primaryType, DustInstanceId instId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<DustEntity> getEntities(DustDeclId primaryType, DustVariantStructure[] aspects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void send(DustAspect from, DustAspect to, DustMessage msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
