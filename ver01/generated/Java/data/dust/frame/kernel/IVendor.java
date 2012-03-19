package dust.frame.kernel;

import java.util.Map;

import dust.shared.DustObject;

public interface IVendor extends DustObject {
	IName getName();
	
	int getDomainCount();
	Iterable<Map.Entry<String, IDomain>> getDomainIter();
	void clearDomain();

	IDomain getDomain(String key);
	void addDomain(IDomain domain, String key);
	void removeDomain(String key);
}
