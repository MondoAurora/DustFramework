package org.mondoaurora.frame.template;

import java.util.HashMap;
import java.util.Map;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;
import org.mondoaurora.frame.template.MAFTemplateConsts.Initer;

public class MAFTemplateSwitch extends MAFTemplateBase {
	Map<String, MAFTemplate> mapOptions = new HashMap<String, MAFTemplate>();
	MAFEval eval;
	
	MAFEval transform;
		
	public MAFTemplateSwitch() {
	}
	
	public MAFTemplateSwitch(MAFEval eval, MAFEval transform, Initer[] rules) {
		this.eval = eval;
		this.transform = transform;
		
		for ( Initer rule : rules ) {
			mapOptions.put(rule.id, rule.template);
		}
	}

	@Override
	public void initInt(MAFTemplateSyntax syntax) {
		for ( MAFTemplate t : mapOptions.values() ) {
			t.init(syntax);
		}
	}
	
	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		
		MAFVariant v = eval.getVariant(var);
		String key = v.isNull() ? null : v.getString();

		MAFTemplate content = mapOptions.get(key);
		
		v = (null == transform) ? var : transform.getVariant(var);
		
		content.writeInto(stream, v);
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
