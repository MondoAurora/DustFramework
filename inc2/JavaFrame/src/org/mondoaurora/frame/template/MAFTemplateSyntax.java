package org.mondoaurora.frame.template;

import java.util.HashMap;
import java.util.Map;

import org.mondoaurora.frame.process.MAFProcessEventSource;
import org.mondoaurora.frame.process.MAFProcessManager;
import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.template.MAFTemplate.Connector;

public class MAFTemplateSyntax implements MAFTemplateConsts {
	
	private Map<String, MAFTemplate> mapRules = new HashMap<String, MAFTemplate>();
	private MAFTemplate startRule;
	
	private boolean initialized = false;
	private Connector listener;
	
	public MAFTemplateSyntax() {
	}
	
	public MAFTemplateSyntax(String startRuleId, Initer[] rules, Connector listener) {
		this.listener = listener;

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
			
			for ( Map.Entry<String, MAFTemplate> e : mapRules.entrySet() ) {
				e.getValue().init(this, null, e.getKey());
			}
		}
	}
	
	MAFTemplate getRule(String ruleId) {
		return mapRules.get(ruleId);
	}
	
	MAFTemplate getStartRule() {
		return startRule;
	}
	
	Connector getListener() {
		return listener;
	}
	
	public void write(MAFVariant var, MAFStream.Out stream) {
		startRule.writeInto(stream, var);
	}
	
	public void read(MAFProcessEventSource src) {
		MAFProcessManager mgr = new MAFProcessManager();
		mgr.process(startRule, src);
	}
}
