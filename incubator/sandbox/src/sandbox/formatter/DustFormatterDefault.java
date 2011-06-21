package sandbox.formatter;

import sandbox.stream.DustStream;
import dust.api.components.DustVariant;

public class DustFormatterDefault implements DustFormatter {

	@Override
	public void writeInto(DustStream stream, DustVariant var) throws Exception {
		stream.put(var.toString());
	}

	@Override
	public boolean parseFrom(DustStream stream, DustVariant var) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
