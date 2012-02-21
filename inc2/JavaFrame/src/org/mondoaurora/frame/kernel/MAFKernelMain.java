package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFEnvironment;
import org.mondoaurora.frame.shared.MAFIdentifier;
import org.mondoaurora.frame.tools.MAFToolsJsonRelay;
import org.mondoaurora.frame.tools.MAFToolsStreamOut;

public class MAFKernelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MAFKernelEnvironment.init();
		
		MAFIdentifier id = MAFEnvironment.getId("[mondoaurora.frame.kernel.Vendor]:mondoaurora");
		
		MAFKernelConnector conn = (MAFKernelConnector) MAFEnvironment.getInstance(id, MAFKernelVendor.FIELDS);
		
//		MAFKernelDumper d = new MAFKernelDumper();
//		d.dumpConnector(conn);
//		d.dumpConnector(conn);
		
		MAFToolsJsonRelay jr = new MAFToolsJsonRelay();
		MAFToolsStreamOut stream = new MAFToolsStreamOut();
		
		jr.write(stream, conn);
		jr.write(stream, conn);
	}

}
