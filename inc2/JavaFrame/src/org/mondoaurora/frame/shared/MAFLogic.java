package org.mondoaurora.frame.shared;

public interface MAFLogic {
	public void init() throws Exception;
	public void processMessage(MAFConnector connData) throws Exception;
	public void channelClosed() throws Exception;
}
