package sandbox.persistence;

import java.util.*;

import dust.api.*;
import dust.api.DustConstants.DustDeclId;
import dust.api.DustDeclarationConstants.FieldId;
import dust.api.components.*;
import dust.api.utils.DustUtilVariant;
import dust.api.utils.DustUtils;
import dust.api.wrappers.DustIdentifier;

import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;
import dust.units.dust.kernel.v0_1.TypeManagement.Field;

public class PersistenceValueExtractor {
	public class Value {
		Enum<? extends FieldId> id;
		Field.Values.FieldType type;
		DustVariant value;
		
		public Enum<? extends FieldId> getId() {
			return id;
		}
		public Field.Values.FieldType getType() {
			return type;
		}
		public DustVariant getValue() {
			return value;
		}
	}
	
	public class ValueIterator implements Iterable<Value>, Iterator<Value> {
		Value v = new Value();
		DustAspect target;
		
		Iterator<Map.Entry<Enum<? extends FieldId>, Field.Values.FieldType>> mapIt;
		
		public ValueIterator(DustAspect target) throws Exception {
			this.target = target;
			
			Map<Enum<? extends FieldId>, Field.Values.FieldType> mapFields = getMapFields(target.getType());
			
			mapIt = (null == mapFields) ? null : mapFields.entrySet().iterator();
		}
		
		@Override
		public Iterator<Value> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return (null != mapIt) && mapIt.hasNext();
		}

		@Override
		public Value next() {
			Map.Entry<Enum<? extends FieldId>, Field.Values.FieldType> e = mapIt.next();
			
			v.id = e.getKey();
			v.type = e.getValue();
			v.value = target.getField(v.id);
			
			return v;
		}

		@Override
		public void remove() {
			throw new RuntimeException("This iterator is read-only!");
		}
		
	}
	DustWorld world = DustUtils.getWorld();

	DustDeclId idIdentified = world.getTypeId(Common.Identified.class);
	DustDeclId idFieldContainer = world.getTypeId(Common.FieldContainer.class);

	DustDeclId idType = world.getTypeId(TypeManagement.Type.class);
	DustDeclId idField = world.getTypeId(TypeManagement.Field.class);

	Map<DustDeclId, Map<Enum<? extends FieldId>, Field.Values.FieldType>> mapIdToFields = new HashMap<DustConstants.DustDeclId, Map<Enum<? extends FieldId>, Field.Values.FieldType>>();
	
	public boolean hasFields(DustAspect aspect) throws Exception {
		return null != getMapFields(aspect.getType());
	}

	public Value getField(DustAspect aspect, String fieldId, Value value) throws Exception {
		if ( null == value ) {
			value = new Value();
		}
		
		DustDeclId typeId = aspect.getType();
		Map<Enum<? extends FieldId>, Field.Values.FieldType> typeFields = getMapFields(typeId);
		
		value.id = getEnumForField(typeId.toString(), fieldId);
		value.type = typeFields.get(value.id);
		value.value = aspect.getField(value.id);

		return value;
	}

	public Iterable<Value> getFields(DustAspect aspect) throws Exception {
		return new ValueIterator(aspect);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	Enum<? extends FieldId> getEnumForField(String typeName, String valueName) throws Exception {
		Class c = Class.forName(typeName + "$Fields");
		return Enum.valueOf(c, valueName);
	}

	Map<Enum<? extends FieldId>, Field.Values.FieldType> getMapFields(DustDeclId id) throws Exception {
		Map<Enum<? extends FieldId>, Field.Values.FieldType> ret = mapIdToFields.get(id);

		if (null == ret) {
			DustEntity eType = DustUtils.getEntity(idType, new DustVariant[] { new DustUtilVariant(
				Common.Identified.Fields.Identifier, new DustIdentifier(id.toString())) });
			DustAspect aspFields = eType.getAspect(idFieldContainer, false);
			if (null != aspFields) {
				DustVariant varFields = aspFields.getField(Common.FieldContainer.Fields.Fields);

				if (!varFields.isNull()) {
					ret = new HashMap<Enum<? extends FieldId>, TypeManagement.Field.Values.FieldType>();

					for (DustVariant vFld : varFields.getMembers()) {
						DustEntity eField = vFld.getValueObject();
						DustIdentifier fldId = eField.getAspect(idIdentified, false).getField(Common.Identified.Fields.Identifier)
							.getValueIdentifier();

						Enum<? extends FieldId> e = getEnumForField(id.getIdentifier().toString(), fldId.toString());

						DustAspect fld = vFld.getValueObject().getAspect(idField, false);
						Field.Values.FieldType ft = fld.getField(TypeManagement.Field.Fields.FieldType).getValueValSet(
							Field.Values.FieldType.class);

						ret.put(e, ft);
					}
				}
			}

			mapIdToFields.put(id, ret);
		}

		return ret;
	}
}
