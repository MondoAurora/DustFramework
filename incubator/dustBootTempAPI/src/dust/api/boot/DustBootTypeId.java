package dust.api.boot;

import dust.api.DustConstants.DustDeclId;
import dust.api.wrappers.DustIdentifier;

public class DustBootTypeId implements DustDeclId {
	String className;
	
	public DustBootTypeId(Class<?> c) {
		className = c.getName();
	}

	@Override
	public DustIdentifier getIdentifier() {
		return new DustIdentifier(className);
	}
	
	public String toString() {
		return className;
	}
}
