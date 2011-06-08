package dust.api.components;

import dust.api.DustConstants;

public interface DustEntity extends DustConstants {
	DustDeclId getPrimaryTypeId();
	Iterable<DustDeclId> getTypes();
	
	EntityState getState();
	EntityType getType();

	DustAspect getAspect(DustDeclId type);
}
