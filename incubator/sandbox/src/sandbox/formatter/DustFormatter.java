package sandbox.formatter;

import dust.api.components.DustVariant;

import sandbox.stream.DustStream;

public interface DustFormatter {
	void writeInto(DustStream stream, DustVariant var) throws Exception;
	boolean parseFrom(DustStream stream, DustVariant var) throws Exception;
}
