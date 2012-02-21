package org.mondoaurora.frame.template;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateWhitespace extends MAFTemplateBase {
	String wsToWrite;
	MAFStream.Out.Indent endLineIndent;
	int blankLines;
	
	public MAFTemplateWhitespace(String ws, MAFStream.Out.Indent endLineIndent, int blank){
		wsToWrite = ws;
		this.endLineIndent = endLineIndent;
		this.blankLines = blank;
	}

	public MAFTemplateWhitespace(){
		this(" ", null, 0);
	}

	public MAFTemplateWhitespace(MAFStream.Indent endLineIndent){
		this(null, endLineIndent, 0);
	}

	public MAFTemplateWhitespace(MAFStream.Indent endLineIndent, int elCount){
		this(null, endLineIndent, elCount);
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) {
		if ( null != wsToWrite ) {
			stream.put(wsToWrite);
		}
		if ( null != endLineIndent ) {
			stream.endLine(endLineIndent);
			for ( int i = 0; i < blankLines; ++i ) {
				stream.endLine(MAFStream.Out.Indent.keep);
			}
		}
	}

	/*
	@Override
	public boolean parseFrom(DustStream stream, DustEntity currentEntity) throws Exception {
		char c;
		boolean wsFound = false;
		boolean ws;
		
		do { 
			c = stream.get();
			ws = Character.isWhitespace(c);
			wsFound |= ws;
		} while ( ws );
		
		stream.unget(c);
		
		return wsFound;
	}
*/
}
