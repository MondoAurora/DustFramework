package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.test.MAFTest;

public class MAFKernelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MAFKernelEnvironment.init();
		
		MAFTest.test(args);
		
	}

}
