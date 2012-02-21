package org.mondoaurora.frame.kernel;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.mondoaurora.frame.tools.MAFToolsStreamOut;

public class MAFKernelDumper extends MAFToolsStreamOut {
	Set<MAFKernelEntity> setEntities = new HashSet<MAFKernelEntity>();

	public MAFKernelDumper() {
		super();
	}

	public MAFKernelDumper(PrintStream targetStream, String indent) {
		super(targetStream, indent);
	}
	
	public void dumpConnector(MAFKernelConnector conn) {
		dumpEntity(conn.data.entity);
	}
	
	public void dumpEntity(MAFKernelEntity e) {
		if ( setEntities.contains(e) ) {
			e.dump(this, true);
		} else {
			setEntities.add(e);
			e.dump(this);
		}
	}
}
