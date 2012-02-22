package org.mondoaurora.frame.test;

import org.mondoaurora.frame.kernel.*;
import org.mondoaurora.frame.process.*;
import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.tools.MAFToolsJsonRelay;
import org.mondoaurora.frame.tools.MAFToolsStreamOut;

public class MAFTest {
	
	/**
	 * @param args
	 */
	public static void test(String[] args) {
		testDump();
		
		System.out.println("\n------------");
		
		testJSONExport();

		System.out.println("\n------------");
		
		testJSONImport();
	}

	static MAFKernelConnector getAnObject() {
		MAFIdentifier id = MAFEnvironment.getId("[mondoaurora.frame.kernel.Vendor]:mondoaurora");
		return (MAFKernelConnector) MAFEnvironment.getInstance(id, MAFKernelVendor.FIELDS);
	}

	static void testDump() {
		MAFKernelConnector conn = getAnObject();

		MAFKernelDumper d = new MAFKernelDumper();
		d.dumpConnector(conn);
		d.dumpConnector(conn);
	}

	static void testJSONExport() {
		MAFConnector conn = getAnObject();

		MAFToolsJsonRelay jr = new MAFToolsJsonRelay();
		MAFToolsStreamOut stream = new MAFToolsStreamOut();

		jr.write(stream, conn);
		jr.write(stream, conn);
	}

	static void testJSONImport() {
		MAFProcessEventSource src = new MAFTestEventSourceFile("temp/json.template.txt");
		MAFToolsJsonRelay jr = new MAFToolsJsonRelay();
		
		jr.read(src);
	}

	static void testImport() {
		MAFProcessEventSource src = new MAFTestEventSourceFile("temp/json.template.txt");
		MAFProcess proc = new MAFTestProcessor();
		
		MAFProcessManager mgr = new MAFProcessManager();
		
		mgr.process(proc, src);
		
	}

	
}
