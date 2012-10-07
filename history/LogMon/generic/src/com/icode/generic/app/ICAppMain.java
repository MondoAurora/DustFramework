package com.icode.generic.app;

import java.io.PrintStream;

import com.icode.generic.base.ICGenConstants;

public interface ICAppMain extends ICGenConstants {
	void printAppStatus(PrintStream w);
	
	int init() throws Exception;
	
	int launch() throws Exception;
	int shutdown() throws Exception;
	
	int shutdownAfterTasksStopped() throws Exception;

	public static abstract class Simple implements ICAppMain {
		public void printAppStatus(PrintStream w) {
			w.println("OK.");
		}
		
		public int init() throws Exception {
			return RETURN_SUCCESS;
		}
		
		public int shutdown() throws Exception {
			return RETURN_SUCCESS;
		}
		
		public int shutdownAfterTasksStopped() throws Exception {
			return RETURN_SUCCESS;
		}
		
	}
}
