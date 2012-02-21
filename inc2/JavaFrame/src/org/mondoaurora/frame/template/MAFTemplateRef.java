package org.mondoaurora.frame.template;

import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.shared.MAFStream;

public class MAFTemplateRef extends MAFTemplateBase {
	String target;
	MAFTemplate imported;
	
	public MAFTemplateRef(String target) {
		this.target = target;
	}

	@Override
	public void writeInto(MAFStream.Out stream, MAFKernelEntity currentEntity) throws Exception {
		imported.writeInto(stream, currentEntity);
	}

	/*
	@Override
	protected boolean parseFromInt(DustStream stream, DustEntity currentEntity) throws Exception {
		return imported.parseFrom(stream, currentEntity);
	}
	*/
}
