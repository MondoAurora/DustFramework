/**
 * 
 */
package com.icode.generic.msg;

import java.util.Locale;


public interface ICGenMsgListener {
	boolean isAccept(String channel, ICGenMsgDef msgDef);
	String getChannelAddress(String channel);
	Locale getPreferredLocale();
}