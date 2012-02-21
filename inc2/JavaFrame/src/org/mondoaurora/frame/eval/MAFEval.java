package org.mondoaurora.frame.eval;

import org.mondoaurora.frame.kernel.MAFKernelConsts;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.kernel.MAFKernelVariant;
import org.mondoaurora.frame.shared.MAFStream;

public interface MAFEval extends MAFKernelConsts {	
	MAFKernelVariant getVariant(MAFKernelEntity currentEntity) throws Exception;
	void writeContent(MAFStream.Out target, MAFKernelEntity currentEntity) throws Exception;
}
