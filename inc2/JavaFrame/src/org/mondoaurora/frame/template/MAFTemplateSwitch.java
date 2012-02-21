package org.mondoaurora.frame.template;

import java.util.HashMap;
import java.util.Map;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.eval.MAFEvalField;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.kernel.MAFKernelIdentifier;
import org.mondoaurora.frame.kernel.MAFKernelVariant;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateSwitch extends MAFTemplateBase {
	Map<String, MAFTemplate> mapOptions = new HashMap<String, MAFTemplate>();
	MAFEval eval;
		
	public MAFTemplateSwitch(MAFKernelIdentifier typeId, String field) {
		eval = new MAFEvalField(typeId, field);
	}
	
	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) throws Exception {
		
		MAFKernelVariant v = eval.getVariant(currentEntity);
		String key = v.isNull() ? null : v.getString();

		MAFTemplate content = mapOptions.get(key);
		
		content.writeInto(stream, currentEntity);
	}
	
/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( Map.Entry<String, MAFTemplate> entry : mapOptions.entrySet() ) {
			if ( entry.getValue().parseFrom(stream, currentEntity) ) {
				eval.getVariant(currentEntity).setValueValSet(entry.getKey());
				return true;
			}
		}

		return false;
	}
*/
}
