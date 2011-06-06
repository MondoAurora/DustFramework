package sandbox;

import dust.api.DustConstants;

public class Test implements DustConstants {
	public static abstract class TestItem {
		public void init(String[] args) {
			
		}
		public abstract void test() throws Exception;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runTest(TestBootEntity.class, args);
	}
	
	public static void runTest(Class<? extends TestItem> testClass, String[] args) {
		try {
			TestItem ti = testClass.newInstance();
			ti.init(args);
			ti.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
