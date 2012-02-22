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
	
	class Ctx {
		int curr = 0;
	}
	
	@Override
	public Object createContextObject(Object msg) {
		return new Ctx();
	}
	
	@Override
	protected Return processChar(char c, Object ctx) {
		Ctx context = (Ctx) ctx;
		
		Return ret = (c == parseVal[context.curr++]) ? (parseVal.length == context.curr) ? SUCCESS : CONTINUE : FAILURE;
		
		if ( ReturnType.Success == ret.getType() ) {
			System.out.println("Const: \"" + constValue + "\"");
		}
		
		return ret;
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
