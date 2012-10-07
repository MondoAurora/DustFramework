/**
 * 
 */
package com.icode.generic.base;

public interface ICGenFilter {
	String getName();
	boolean isMatching(ICGenObject object);
}