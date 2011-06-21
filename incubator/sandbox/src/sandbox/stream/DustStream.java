package sandbox.stream;

public interface DustStream {
	char get() throws Exception;
	void unget(char c) throws Exception;
	
	int getPosition();
	void setPosition(int pos);
	
	boolean hasRemaining();

	void put(String str) throws Exception;
	
	public static enum Indent { inc, keep, dec };
	void endLine(Indent indent);
}
