package org.mondoaurora.frame.template;

import org.mondoaurora.frame.shared.MAFStream;

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
	
	MAFTemplate TEMPL_WS_SPACE = new MAFTemplateWhitespace();
	MAFTemplate TEMPL_WS_LINEFEED = new MAFTemplateWhitespace(MAFStream.Indent.keep);
	MAFTemplate TEMPL_WS_INDENT_INC = new MAFTemplateWhitespace(MAFStream.Indent.inc);
	MAFTemplate TEMPL_WS_INDENT_DEC = new MAFTemplateWhitespace(MAFStream.Indent.dec);

	
	String MEMBER_OPT_CONT = "optContent";
	String MEMBER_REP_CONT = "repContent";
	String MEMBER_REP_SEP = "repSeparator";
	String MEMBER_OPT = "optional";
}
