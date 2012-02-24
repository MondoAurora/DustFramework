package org.mondoaurora.frame.template;




public abstract class MAFTemplateBase implements MAFTemplate, MAFTemplateConsts {
	private MAFTemplateSyntax syntax;
	private String id;
	
	@Override
	public final void init(MAFTemplateSyntax syntax, MAFTemplate parent, String id) {
		this.syntax = syntax;
		this.id = (null == parent) ? id : (parent.getId()+"."+id);
		initInt(syntax);
	}
	
	@Override
	public final String getId() {
		return id;
	}

	public void initInt(MAFTemplateSyntax syntax) {
		
	}
	
	protected final MAFTemplateSyntax getSyntax() {
		return syntax;
	}

	@Override
	public final Return processEvent(Object event, Object ctx) {
		Return ret = processChar((Character)event, ctx);
		
		switch ( ret.getType() ) {
		case Failure:
		case Success:
			syntax.getListener().templateEnd(this, ctx, ret);
			break;
		}
		
		return ret;
	}
	
	@Override
	public final Object createContextObject(Object msg) {
		Object ctx = createContextObjectInt(msg);
		
		syntax.getListener().templateBegin(this, ctx);
		
		return ctx;
	}
	
	protected Object createContextObjectInt(Object msg) {
		return null;
	}
	
	@Override
	public final Return processRelayReturn(Return ob, Object ctx) {
		Return ret = processRelayReturnInt(ob, ctx);
		
		switch ( ret.getType() ) {
		case Failure:
		case Success:
			syntax.getListener().templateEnd(this, ctx, ret);
			break;
		}
		
		return ret;
	}
	
	protected Return processRelayReturnInt(Return ob, Object ctx) {
		return ob;
	}
	
	protected abstract Return processChar(char c, Object ctx);
	
	@Override
	public final String toString() {
		return id + "(" + toStringInt() + ")";
	}
	
	protected abstract String toStringInt() ;

}
