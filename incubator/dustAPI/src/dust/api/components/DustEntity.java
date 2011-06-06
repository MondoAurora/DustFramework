package dust.api.components;

import java.util.Enumeration;

import dust.api.DustConstants;

public interface DustEntity extends DustConstants {
	DustDeclId getPrimaryTypeId();
	Enumeration<DustDeclId> getTypes();
	
	EntityState getState();
	EntityType getType();

	DustAspect getAspect(DustDeclId type);
}
