package org.mondoaurora.frame.template;

import java.util.HashMap;
import java.util.Map;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFRuntimeException;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateSyntax implements MAFTemplateConsts {
	
	Map<String, MAFTemplate> mapRules = new HashMap<String, MAFTemplate>();
	MAFTemplate startRule;
	
	boolean initialized = false;
	
	public MAFTemplateSyntax() {
		// TODO Auto-generated constructor stub
	}
	
	public MAFTemplateSyntax(String startRuleId, Initer[] rules) {
		for ( Initer rule : rules ) {
			mapRules.put(rule.id, rule.template);
		}
		
		init();
		
		startRule = getRule(startRuleId);
		
		if ( null == startRule ) {
			throw new MAFRuntimeException("TemplateSyntax", "Missing start rule " + startRuleId, null);
		}
	}
	
	public void init() {
		if ( !initialized ) {
			initialized = true;
			
			MAFTemplate target;
			
			for ( MAFTemplate t : mapRules.values() ) {
				if (t instanceof MAFTemplateRef) {
					MAFTemplateRef ti = (MAFTemplateRef) t;
					target = mapRules.get(ti.target);
					if ( null == target ) {
						throw new MAFRuntimeException("TemplateSyntax", "Missing rule " + ti.target, null);
					} else if ( ti == target ) {
						throw new MAFRuntimeException("TemplateSyntax", "Self importing rule " + ti.target, null);
					}
					ti.imported = target;
				}
			}
		}
	}
	
	public MAFTemplate getRule(String ruleId) {
		return mapRules.get(ruleId);
	}
	
	
	public void write(MAFKernelEntity e, MAFStream.Out stream) throws Exception {
		startRule.writeInto(stream, e);
	}
}
