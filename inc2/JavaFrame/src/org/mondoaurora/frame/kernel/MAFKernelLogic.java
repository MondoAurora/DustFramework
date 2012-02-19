package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFLogic;

public abstract class MAFKernelLogic implements MAFLogic, MAFKernelConsts {

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processMessage(MAFConnector connData) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelClosed() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	abstract MAFKernelConnector export(MAFKernelEnvironment env); 

}
