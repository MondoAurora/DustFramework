package sandbox.template;

import sandbox.stream.DustStream;
import dust.api.components.DustAspect;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;

public class DustTemplateRepeat extends DustTemplateBase {
	DustTemplate separator;
	DustTemplate content;
	
	DustDeclId typeId;
	Enum<? extends FieldId> field;
	
	DustVariant lastVar;

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		DustAspect asp = currentEntity.getAspect(typeId, false);
		if ( null != asp ) {
			DustVariant var = asp.getField(field);
			
			if ( !var.isNull() ) {
				boolean concat = false;
				for ( DustVariant repFld : var.getMembers() ) {
					if ( concat ) {
						separator.writeInto(stream, repFld.getValueObject());
					} else {
						concat = true;
					}
					content.writeInto(stream, repFld.getValueObject());
				}
			}
		}
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		DustAspect asp = currentEntity.getAspect(typeId, false);
		
		DustVariant var = createVar();
		DustEntity e = var.getValueObject();
		
		while ( content.parseFrom(stream, e) ) {
			asp.getField(field).setData(e, VariantSetMode.insert, null);
			var = createVar();
		}
		
		return true;

	}

	DustVariant createVar() {
		if ( null == lastVar ) {
			lastVar = null; // here I have to generate a Variant according to the new variant's type
		}
		return lastVar;
	}
}
