package com.icode.generic.shell;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.icode.generic.base.ICGenTreeNode;

public class ICShellSocket extends ICShell {
	public static final String ID_SEND = "IC-identify";
	public static final String ID_ACK = "IC-ShellSocket";

	public static final int SOCKET_DEFPORT = 4444;
	
	/** Server socket port number. */
	protected String host;

	/** Server socket port number. */
	protected int port;

	/** Socket timeout in milliseconds. */
	protected int clientTimeout = (int)TIME_MINUTE;

	Socket client;

	public ICShellSocket() {
		this(null);
	}
	
	public ICShellSocket(String host, int port) {
		this(null);
		
		this.host = host;
		this.port = port;
	}

	public ICShellSocket(Socket client) {
		super(ICSHELL_TO_SINGLE, ICSHELL_FROM_SINGLE, false);
		this.client = client;
	}

	protected void endOfAnswerInt(String stream) throws Exception {
	}

	protected OutputStream getToShellStream(String name) throws Exception {
		return client.getOutputStream();
	}

	protected InputStream getFromShellStream(String name) throws Exception {
		return client.getInputStream();
	}

	protected void initShell() throws Exception {
		if (null == client) {
			client = new Socket( host, port );
			client.setSoTimeout( clientTimeout );
		}
	}

	public void setSocketTimeout(int timeout) {
		clientTimeout = timeout;
	}

	protected void initConversation() throws Exception {
		
	}

	protected void streamClosed(String streamName) throws Exception {
		endShell();
	}

	public synchronized void killShellInt() throws Exception {
		if (null != client) {
			if (client.isConnected()) {
				client.shutdownInput();
				client.shutdownOutput();
				client.close();				
			}
			client = null;
		}
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		port = config.getOptionalInt("port", SOCKET_DEFPORT);
		host = (String) config.getOptional("host", "localhost");
		clientTimeout = config.getOptionalInt("clientTimeout", (int)TIME_MINUTE);
	}
}
