package org.mondoaurora.frame.template;

import java.util.ArrayList;

import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFTemplateSequence extends MAFTemplateBase {
	ArrayList<MAFTemplate> content;
	
	public MAFTemplateSequence(MAFTemplate[] arrContent) {
		content = new ArrayList<MAFTemplate>(arrContent.length);
		
		for ( MAFTemplate t : arrContent ) {
			content.add(t);
		}
	}
	
	@Override
	public void init(MAFTemplateSyntax syntax) {
		for ( MAFTemplate t : content ) {
			t.init(syntax);
		}
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFVariant var) {
		for ( MAFTemplate t : content ) {
			t.writeInto(stream, var);
		}
	}
/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( MAFTemplate t : content ) {
			if ( ! t.parseFrom(stream, currentEntity) ) {
				return false;
			}
		}
		
		return true;
	}
*/
}
