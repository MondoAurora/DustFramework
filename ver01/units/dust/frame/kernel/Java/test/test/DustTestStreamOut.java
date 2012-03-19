package test;


import java.io.PrintStream;

import dust.shared.DustConsts;

public class DustTestStreamOut implements DustConsts, DustConsts.DustStream.Out {
	PrintStream targetStream;

	StringBuilder sbIndent = new StringBuilder();
	String indent;
	boolean newLine;
	boolean putting;
	
	public DustTestStreamOut() {
		this(System.out, "\t");
	}

	public DustTestStreamOut(PrintStream targetStream, String indent) {
		this.targetStream = targetStream;
		this.indent = indent;
		putting = false;
	}
		
	public void put(String str) {
		putting();
		targetStream.print(str);
	}
	
	public void endLine(Indent indent) {
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
