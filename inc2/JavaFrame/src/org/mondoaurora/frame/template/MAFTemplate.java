package org.mondoaurora.frame.template;

import org.mondoaurora.frame.process.MAFProcess;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public interface MAFTemplate extends MAFProcess {
	void writeInto(MAFStream.Out stream, MAFVariant var);
	
	void init(MAFTemplateSyntax syntax, MAFTemplate parent, String id);

	String getId();
	
	public interface Connector {
		void templateBegin(MAFTemplate template, Object context);
		void templateEnd(MAFTemplate template, Object context, Return ret);
	}
}