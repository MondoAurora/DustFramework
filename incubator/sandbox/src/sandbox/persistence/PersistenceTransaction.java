package sandbox.persistence;

import java.util.HashMap;
import java.util.Map;

import dust.api.DustConstants;
import dust.api.components.DustEntity;

public abstract class PersistenceTransaction implements DustConstants {
	Map<Object, DustEntity> entities = new HashMap<Object, DustEntity>();

	public boolean add(DustEntity e) {
		Object key = e.getPersistentId();

		if (entities.containsKey(key)) {
			return false;
		} else {
			entities.put(key, e);
			return true;
		}
	}
	
	public Iterable<DustEntity> getEntities() {
		return entities.values();
	}
	
	public final void commit() throws Exception {
		commitInt();
		
		for ( Map.Entry<Object, DustEntity> e : entities.entrySet() ) {
			DustEntity entity = e.getValue();
			entity.setState(EntityState.Steady);
			entity.setPersistentId(e.getKey());
		}
	}
	
	public abstract void commitInt() throws Exception;
}
