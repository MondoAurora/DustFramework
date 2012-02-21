package org.mondoaurora.frame.template;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateConstant extends MAFTemplateBase {
	String constValue;
	char[] parseVal;
	
	public MAFTemplateConstant(String constVal){
		constValue = constVal;
		parseVal = constVal.toCharArray();
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) throws Exception {
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
