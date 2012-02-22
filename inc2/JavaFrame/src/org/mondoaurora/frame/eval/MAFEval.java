package org.mondoaurora.frame.eval;

import org.mondoaurora.frame.kernel.MAFKernelConsts;
import org.mondoaurora.frame.process.MAFProcess;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFVariant;

public interface MAFEval extends MAFProcess, MAFKernelConsts {	
	MAFVariant getVariant(MAFVariant var);
	void writeContent(MAFStream.Out target, MAFVariant var);
}
