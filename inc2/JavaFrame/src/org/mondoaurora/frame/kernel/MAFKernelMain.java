package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFEnvironment;
import org.mondoaurora.frame.shared.MAFIdentifier;

public class MAFKernelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MAFKernelEnvironment.init();
		
		MAFIdentifier id = MAFEnvironment.getId("[mondoaurora.frame.kernel.Vendor]:mondoaurora");
		
		MAFKernelConnector conn = (MAFKernelConnector) MAFEnvironment.getInstance(id, MAFKernelVendor.FIELDS);
		
		MAFKernelDumper d = new MAFKernelDumper();

		d.dumpConnector(conn);
//		MAFConnector c1 = conn.getMembers(0).iterator().next();
	}

}
