package dust.frame.stream;

import dust.shared.DustObject;

public interface IMark extends DustObject {
	String getIndentMode();
	void setIndentMode(String indentMode);
	
	int getEmptyLines();
	void setEmptyLines(int emptyLines);
}
