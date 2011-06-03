package dust.api.boot;

import dust.api.DustConstants.DustDeclId;

public class DustBootTypeId implements DustDeclId {
	String className;
	
	public DustBootTypeId(Class<?> c) {
		className = c.getName();
	}
}
