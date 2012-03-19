package dust.frame.kernel;

import java.util.Map;

import dust.shared.DustObject;

public interface IDomain extends DustObject {
	IName getName();
	
	IVendor getParent();

	int getUnitCount();
	Iterable<Map.Entry<String, IUnit>> getUnitIter();
	void clearUnit();

	IUnit getUnit(String key);
	void addUnit(IUnit unit, String key);
	void removeUnit(String key);
}
