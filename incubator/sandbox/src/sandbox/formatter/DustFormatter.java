package sandbox.formatter;

import sandbox.stream.DustStream;
import dust.api.components.DustVariant;

public interface DustFormatter {
	void writeInto(DustStream stream, DustVariant var) throws Exception;
	boolean parseFrom(DustStream stream, DustVariant var) throws Exception;
}
