package sandbox.template;

import sandbox.stream.DustStream;
import dust.api.components.DustEntity;


public abstract class DustTemplateBase implements DustTemplate {
	public final boolean parseFrom(DustStream stream, DustEntity currentEntity) throws Exception {
		int pos = stream.getPosition();
		
		if ( parseFromInt(stream, currentEntity) ) {
			return true;
		} else {
			stream.setPosition(pos);
			return false;
		}
	}
	
	protected abstract boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception;
}
