package org.mondoaurora.frame.shared;


public interface MAFStream {	
	enum Indent { inc, keep, dec };

	interface Out extends MAFStream {
		void put(String str);
		void endLine(Indent indent);		
	}
	
}
