package dust.frame.stream;

import dust.shared.DustObject;

public interface IIndenter extends DustObject, IStreamWrite {
	public enum Messages {
		endLine
	};
	
	public enum Fields {
		indentLead
	};
	
	public enum References {
		stream
	};
		
	String getIndentLead();
	void setIndentLead(String indentLead);
	
	IStreamWrite getStream();

}
