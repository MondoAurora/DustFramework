package sandbox.stream;

import sandbox.stream.DustStream.Indent;

public class DustStreamPrefixIndenter {
	DustStream targetStream;

	StringBuilder sbIndent = new StringBuilder();
	String indent;
	boolean newLine;
	boolean putting;

	protected DustStreamPrefixIndenter(DustStream targetStream, String indent) {
		this.targetStream = targetStream;
		this.indent = indent;
		putting = false;
	}

	public void putting() throws Exception {
		if (!putting) {
			try {
				putting = true;
				if (newLine) {
					if (0 < sbIndent.length()) {
						targetStream.put(sbIndent.toString());
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
