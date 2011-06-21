package sandbox.template;

import sandbox.stream.DustStream;
import dust.api.components.DustEntity;

public class DustTemplateConstant extends DustTemplateBase {
	String constValue;
	char[] parseVal;
	
	public DustTemplateConstant(String constVal){
		constValue = constVal;
		parseVal = constVal.toCharArray();
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		stream.put(constValue);
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( char c : parseVal ) {
			if ( stream.get() != c ) {
				return false;
			}
		}
		
		return true;
	}

}
