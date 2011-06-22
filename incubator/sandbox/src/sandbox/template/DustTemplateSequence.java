package sandbox.template;

import java.util.ArrayList;

import dust.api.components.DustEntity;

import sandbox.stream.DustStream;

public class DustTemplateSequence extends DustTemplateBase {
	ArrayList<DustTemplate> content;
	
	public DustTemplateSequence(DustTemplate[] arrContent) {
		content = new ArrayList<DustTemplate>(arrContent.length);
		
		for ( DustTemplate t : arrContent ) {
			content.add(t);
		}
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( DustTemplate t : content ) {
			t.writeInto(stream, currentEntity);
		}
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		for ( DustTemplate t : content ) {
			if ( ! t.parseFrom(stream, currentEntity) ) {
				return false;
			}
		}
		
		return true;
	}

}
