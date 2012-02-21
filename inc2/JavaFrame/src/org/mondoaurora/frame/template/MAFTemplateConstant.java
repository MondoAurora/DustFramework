package org.mondoaurora.frame.template;

import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFTemplateConstant extends MAFTemplateBase {
	String constValue;
	char[] parseVal;
	
	public MAFTemplateConstant(String constVal){
		constValue = constVal;
		parseVal = constVal.toCharArray();
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		stream.put(constValue);
	}

	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( char c : parseVal ) {
			if ( stream.get() != c ) {
				return false;
			}
		}
		
		return true;
	}
*/
}
