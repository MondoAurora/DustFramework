package dust.api.components;

import dust.api.DustConstants;

public interface DustEntity extends DustConstants {
	DustDeclId getPrimaryTypeId();
	Iterable<DustDeclId> getTypes();
	
	EntityState getState();
	EntityType getType();

	DustAspect getAspect(DustDeclId type, boolean createMissing);
	void removeAspect(DustDeclId type);
	
	Object getPersistentId();
	
	void setPersistentId(Object id);
	void setState(EntityState state);
	void setType(EntityType type);
}
