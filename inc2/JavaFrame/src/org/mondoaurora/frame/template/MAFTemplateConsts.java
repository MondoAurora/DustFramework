package org.mondoaurora.frame.template;

public interface MAFTemplateConsts {
	public static final class Initer {
		public final String id;
		public final MAFTemplate template;
		
		public Initer(String id, MAFTemplate template) {
			super();
			this.id = id;
			this.template = template;
		}
	}
	
	String MEMBER_OPT_CONT = "optContent";
	String MEMBER_REP_CONT = "repContent";
	String MEMBER_REP_SEP = "repSeparator";
	String MEMBER_OPT = "optional";
}
