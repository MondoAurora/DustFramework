package sandbox.template;

import dust.api.components.DustEntity;

import sandbox.stream.DustStream;

public class DustTemplateImport extends DustTemplateBase {
	DustTemplate imported;
	
	public DustTemplateImport(DustTemplate imported) {
		this.imported = imported;
	}

	@Override
	public void writeInto(DustStream stream, DustEntity currentEntity) throws Exception {
		imported.writeInto(stream, currentEntity);
	}

	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		return imported.parseFrom(stream, currentEntity);
	}
}
