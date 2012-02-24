package org.mondoaurora.frame.template;

import java.util.*;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.shared.*;

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
		for ( Map.Entry<String, MAFTemplate> e : mapOptions.entrySet() ) {
			e.getValue().init(syntax, this, e.getKey());
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
	protected Object createContextObjectInt(Object msg) {
		return new Ctx();
	}

	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;	
		return new Return(ReturnType.Relay, context.it.next().getValue(), false);
	}
	
	@Override
	protected Return processRelayReturnInt(Return ob, Object ctx) {
		Ctx context = (Ctx) ctx;
		return ( ReturnType.Success == ob.getType() ) ? ob : context.it.hasNext() ? CONTINUE : FAILURE;
	}

	@Override
	protected String toStringInt() {
		return "[" + MAFUtils.iter2str(mapOptions.entrySet(), "|") + "]";
	}
}
