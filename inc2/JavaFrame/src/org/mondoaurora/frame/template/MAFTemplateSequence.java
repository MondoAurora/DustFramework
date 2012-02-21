package org.mondoaurora.frame.template;

import java.util.ArrayList;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

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
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) {
		for ( MAFTemplate t : content ) {
			t.writeInto(stream, currentEntity);
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
