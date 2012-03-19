package dust.frame.kernel;

import java.util.Map;

import dust.shared.DustObject;

public interface IUnit extends DustObject {
	IName getName();
	
	IDomain getParent();
	
	int getTypeCount();
	Iterable<Map.Entry<String, IType>> getTypeIter();
	void clearType();

	IType getType(String key);
	void addType(IType type, String key);
	void removeType(String key);
}
