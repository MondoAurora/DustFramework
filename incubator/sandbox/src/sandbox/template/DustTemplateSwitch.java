package sandbox.template;

import java.util.HashMap;
import java.util.Map;

import dust.api.components.*;

import sandbox.evaluator.DustEvaluator;
import sandbox.stream.DustStream;

public class DustTemplateSwitch extends DustTemplateBase {
	Map<String, DustTemplate> mapOptions = new HashMap<String, DustTemplate>();
	DustEvaluator eval;
	
	DustDeclId typeId;
	Enum<? extends FieldId> field;

	
	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		DustAspect asp = currentEntity.getAspect(typeId, false);
		
		if ( null != asp ) {
			DustVariant var = asp.getField(field);
			String val = var.isNull() ? null : var.getValueValSet();
			DustTemplate content = mapOptions.get(val);
			
			content.writeInto(stream, currentEntity);
		}
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( Map.Entry<String, DustTemplate> entry : mapOptions.entrySet() ) {
			if ( entry.getValue().parseFrom(stream, currentEntity) ) {
				eval.getVariant(currentEntity).setValueValSet(entry.getKey());
				return true;
			}
		}

		return false;
	}

}
