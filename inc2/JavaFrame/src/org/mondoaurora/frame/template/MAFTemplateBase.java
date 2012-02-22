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
	
	protected final MAFTemplateSyntax getSyntax() {
		return syntax;
	}

	@Override
	public final Return processEvent(Object event, Object ctx) {
		return processChar((Character)event, ctx);
	}
	
	@Override
	public Object createContextObject(Object msg) {
		return null;
	}
	
	@Override
	public void processRelayReturn(Return ob, Object ctx) {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract Return processChar(char c, Object ctx);

	
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
