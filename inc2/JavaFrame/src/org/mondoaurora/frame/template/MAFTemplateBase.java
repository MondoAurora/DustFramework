package org.mondoaurora.frame.template;



public abstract class MAFTemplateBase implements MAFTemplate {
	private MAFTemplateSyntax syntax;
	
	@Override
	public final void init(MAFTemplateSyntax syntax) {
		this.syntax = syntax;
		initInt(syntax);
	}

	public void initInt(MAFTemplateSyntax syntax) {
		
	}
	
	protected MAFTemplateSyntax getSyntax() {
		return syntax;
	}

	/*
	public final boolean parseFrom(DustStream stream, DustEntity currentEntity) throws Exception {
		int pos = stream.getPosition();
		
		if ( parseFromInt(stream, currentEntity) ) {
			return true;
		} else {
			stream.setPosition(pos);
			return false;
		}
	}
	
	protected abstract boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception;
	*/
}
