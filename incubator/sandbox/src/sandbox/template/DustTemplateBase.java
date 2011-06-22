package sandbox.template;

import dust.api.components.DustEntity;

import sandbox.stream.DustStream;


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
