package dust.frame.stream;

import dust.shared.DustObject;

public interface IIndent extends DustObject {
	enum IndentMode { inc, keep, dec };

	IndentMode getIndentMode();
	void setIndentMode(IndentMode indentMode);
	
	int getEmptyLines();
	void setEmptyLines(int emptyLines);
}
