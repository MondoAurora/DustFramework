package sandbox;

import dust.api.DustConstants;
import dust.api.boot.DustBootWorld;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;

public class Test implements DustConstants {
	public static abstract class TestItem implements DustConstants {
		public void init(String[] args) {
			
		}
		public abstract void test() throws Exception;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DustWorld world = new DustBootWorld();
		DustUtils.setWorld(world, true);

//		runTest(TestBootEntity.class, args);
		runTest(TestInitTypeManagement.class, args);
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
