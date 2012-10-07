package com.icode.generic.net;

import java.io.IOException;
import java.net.*;

import com.icode.generic.ICGenCommand;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.shell.*;
import com.icode.generic.shell.ICShell.LineProcessor;
import com.icode.generic.task.ICTask;

public class ICGenNetSrvSocket extends ICTask {
	protected int port;
	protected int soTimeout = 5000;

	protected ServerSocket server = null;
	
	ICGenTreeNode procNode;

	public ICGenNetSrvSocket() {
		this(null);
	}

	public ICGenNetSrvSocket(Object owner) {
		super(owner, SOCKET_SERVER, null);
	}

	protected void doTask() throws Exception {
		while (stateCheck(STATECHECK_RUNNING)) {
			try {
				Socket client = server.accept();
				if (null != client) {
					ICShell clientShell = new ICShellSocket(client);
					
					LineProcessor processor;
					if (null != procNode) {
						processor = (ICShell.LineProcessor) ICAppFrame.getComponent(procNode, ICShell.LineProcessor.class);
					} else {
						processor = new ICShellCmdProcessor((ICGenCommand) ICAppFrame.getComponent(APP_COMMANDS, ICGenCommand.class));
					}

					clientShell.setProcessor(processor);
					SocketAddress sa = client.getRemoteSocketAddress();
					clientShell.setName(getName() + " client connected from " + sa);
					clientShell.startShell();
				}
			} catch (SocketException ex) {
				// if the accept failed because stopRequested(), we should not throw exception
				if (stateCheck(STATECHECK_RUNNING)) {
					throw ex;
				}
			}
		}
	}

	protected boolean init() throws Exception {
		String ownerName = null;
		boolean portAvailable = true;

		try {
			Socket socket = new Socket("localhost", port);
			socket.setSoTimeout(soTimeout);

			ICShell testShell = new ICShellSocket(socket);

			ICShell.LineProcessor rp = new ICShell.LineProcessor(false) {
				public Object processLine(String line) throws Exception {
					return null;
				}
			};
			testShell.setProcessor(rp);
			testShell.startShell();
			// if it starts without problems, it means that the port is already served
			ownerName = testShell.getName();

			testShell.endShell();
			portAvailable = false;

		} catch (Exception ex) {
			// do nothing, this is the good way now
		}

		if (null != ownerName) {
			throw new Exception("The port " + port + " is owned by another application: " + ownerName);
		}
		
		if ( !portAvailable ) {
			throw new Exception("The port " + port + " is not available!");
		}
		server = new ServerSocket(port);

		return true;
	};

	protected void stopRequested() {
		try {
			if (!server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {
			// A least I have tried...
			// This is only to kick server.accept() out of its block - if there is a
			// problem, the accept has already failed
		}
	};

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);

		port = config.getOptionalInt("port", ICShellSocket.SOCKET_DEFPORT);
		soTimeout = config.getOptionalInt("soTimeout", 5000);
		procNode = config.getChild("processor");
	}
}
