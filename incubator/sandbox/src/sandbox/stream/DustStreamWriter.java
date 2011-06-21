package sandbox.stream;

import java.io.PrintStream;

public class DustStreamWriter implements DustStream {
	DustStreamPrefixIndenter indenter;
	PrintStream streamOut;
	
	public DustStreamWriter(PrintStream out, String indent) {
		this.streamOut = out;
		indenter = new DustStreamPrefixIndenter(this, indent);
	}
	
	public DustStreamWriter() {
		this(System.out, "  ");
	}

	@Override
	public char get() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void unget(char c) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosition(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasRemaining() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void put(String str) throws Exception {
		indenter.putting();
		streamOut.print(str);
	}

	@Override
	public void endLine(Indent indent) {
		indenter.endingLine(indent);
		streamOut.println();
	}

}
