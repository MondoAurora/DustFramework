package org.mondoaurora.frame.template;

import java.util.*;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.shared.*;
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
	
	
	class Ctx {
		Iterator<Map.Entry<String, MAFTemplate>> it = mapOptions.entrySet().iterator(); 
	}

	@Override
	public Object createContextObject(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;	
		return new Return(ReturnType.Relay, context.it.next().getValue(), false);
	}
	
	@Override
	public Return processRelayReturn(Return ob, Object ctx) {
		Ctx context = (Ctx) ctx;
		return ( ReturnType.Success == ob.getType() ) ? ob : context.it.hasNext() ? CONTINUE : FAILURE;
	}

	@Override
	public String toString() {
		return "[" + MAFUtils.iter2str(mapOptions.entrySet(), "|") + "]";
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
