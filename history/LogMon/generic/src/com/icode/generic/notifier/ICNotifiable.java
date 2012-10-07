/**
 * 
 */
package com.icode.generic.notifier;


public interface ICNotifiable {
	String getListeningTarget(String type, ICNotification notif);
}