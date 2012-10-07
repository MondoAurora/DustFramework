/**
 * 
 */
package com.icode.generic.base;


public interface ICGenConfigurable extends ICGenConstants {
	String LOAD_HINT_CONFIG = "loadConfig";
	
	void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception;
}