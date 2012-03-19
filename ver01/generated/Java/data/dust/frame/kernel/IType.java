package dust.frame.kernel;

import dust.shared.DustObject;

public interface IType extends DustObject {
	IName getName();
	
	IUnit getParent();
	
	boolean getReferrable();
	void setReferrable(boolean referrable);
	
	int getFieldCount();
	Iterable<IField> getFieldIter();
	void clearFieldArray();

	void addField(IField field, int idx);
	void removeField(int idx);
}
