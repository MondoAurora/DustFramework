package dust.frame.kernel;

import dust.shared.DustObject;

public interface IField extends DustObject {
	IName getName();

	String getType();
	void setType(String type);
	
	String getObType();
	void setObType(String obType);
	
	String getDefValue();
	void setDefValue(String defValue);
	
	int getLength();
	void setLength(int length);
	
	String getEnumNames();
	void setEnumNames(String enumNames);
}
