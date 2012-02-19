package org.mondoaurora.frame.kernel;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class MAFKernelDumper {
	public static enum Indent { inc, keep, dec };
	
	PrintStream targetStream;

	StringBuilder sbIndent = new StringBuilder();
	String indent;
	boolean newLine;
	boolean putting;
	
	Set<MAFKernelEntity> setEntities = new HashSet<MAFKernelEntity>();
	
	public MAFKernelDumper() {
		this(System.out, "\t");
	}

	public MAFKernelDumper(PrintStream targetStream, String indent) {
		this.targetStream = targetStream;
		this.indent = indent;
		putting = false;
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
	
	void put(String str) {
		putting();
		targetStream.print(str);
	}
	
	void endLine(Indent indent) {
		endingLine(indent);
		targetStream.println();
	}
	
	public void putting() {
		if (!putting) {
			try {
				putting = true;
				if (newLine) {
					if (0 < sbIndent.length()) {
						targetStream.print(sbIndent.toString());
						newLine = false;
					}
				}
			} finally {
				putting = false;
			}
		}
	}

	public void endingLine(Indent i) {
		newLine = true;
		switch (i) {
		case inc:
			sbIndent.append(indent);
			break;
		case dec:
			sbIndent.delete(0, indent.length());
			break;
		}
	}

}
