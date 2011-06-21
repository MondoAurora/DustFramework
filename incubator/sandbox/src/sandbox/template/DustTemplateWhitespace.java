package sandbox.template;

import sandbox.stream.DustStream;
import sandbox.stream.DustStream.Indent;
import dust.api.components.DustEntity;

public class DustTemplateWhitespace implements DustTemplate {
	String wsToWrite;
	DustStream.Indent endLineIndent;
	int blankLines;
	
	public DustTemplateWhitespace(String ws, DustStream.Indent endLineIndent, int blank){
		wsToWrite = ws;
		this.endLineIndent = endLineIndent;
		this.blankLines = blank;
	}

	public DustTemplateWhitespace(){
		this(" ", null, 0);
	}

	public DustTemplateWhitespace(DustStream.Indent endLineIndent){
		this(null, endLineIndent, 1);
	}

	public DustTemplateWhitespace(DustStream.Indent endLineIndent, int elCount){
		this(null, endLineIndent, elCount);
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		if ( null != wsToWrite ) {
			stream.put(wsToWrite);
		}
		if ( null != endLineIndent ) {
			stream.endLine(endLineIndent);
			for ( int i = 0; i < blankLines; ++i ) {
				stream.endLine(Indent.keep);
			}
		}
	}

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

}
