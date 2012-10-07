package com.icode.generic.log;

public interface ICLogSender {
	void log(byte level, String message, Object param);
}
