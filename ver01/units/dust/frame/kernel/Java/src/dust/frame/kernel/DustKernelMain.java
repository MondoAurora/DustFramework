package dust.frame.kernel;

import test.DustTest;

public class DustKernelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DustKernelEnvironment.init();
		
		DustTest.test(args);
		
	}

}
