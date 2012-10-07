package com.icode.generic.shell;

import java.io.*;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import com.icode.generic.ICGenUtils;
import com.icode.generic.base.ICGenTreeNode;

public class ICShellSSH extends ICShell {
	String host;

	String user;
	String pass;
	String keyFileName;

	Connection conn;
	Session sess;
	OutputStream cmdline;

	boolean isAuthenticated = false;

	public ICShellSSH() {
		this(null);
	}

	public ICShellSSH(String host) {
		super(ICSHELL_TO_SINGLE, ICSHELL_FROM_ERR, true);
		this.host = host;
	}

	public void setAuthInfo(String user, String pass, String keyFileName) throws Exception {
		this.user = user;
		this.pass = pass;
		this.keyFileName = keyFileName;
	}

	protected void endOfAnswerInt(String stream) throws Exception {
	}

	protected Object doSend(String stream, String message) throws Exception {
		cmdline.write((message + " && echo .-.-.\n").getBytes());
		cmdline.flush();
		return null;
	}

	protected void initShell() throws Exception {
		conn = new Connection(host);
		conn.connect();
		
//		String[] ss = conn.getRemainingAuthMethods(user);
		
//		System.out.println("ICShell auth methods: " + ICGenUtils.arr2str(ss, ','));
		
//		getProcessor(ICSHELL_FROM).processLine("Auth methods: " + ICGenUtils.arr2str(ss, ','));

		if (null != keyFileName) {
			isAuthenticated = conn.authenticateWithPublicKey(user, new File(keyFileName), pass);
		} else {
			isAuthenticated = conn.authenticateWithPassword(user, pass);
		}
		
		/*
		 * However, in rare cases you may encounter servers that need several steps.
		 * I.e., if one of the Connection.authenticateWithXXX() methods returns
		 * false and Connection.isAuthenticationPartialSuccess() returns true, then
		 * further authentication is needed. For each step, to find out which
		 * authentication methods may proceed, you can use either the
		 * Connection.getRemainingAuthMethods() or the
		 * Connection.isAuthMethodAvailable() method. Again, please have a look into
		 * the SwingShell.java example.
		 */
		
		if (isAuthenticated == false) {
			throw new IOException("Authentication failed.");
		}

		sess = conn.openSession();

		sess.startShell();
		cmdline = sess.getStdin();
	}

	public int waitFor() throws Exception {
		if (0 != super.waitFor()) {
			sess.close();
			conn.close();

			return 0;
		} else {
			return -1;
		}
	}

	protected InputStream getFromShellStream(String name) {
		switch (ICGenUtils.indexOf(ICSHELL_FROM_ERR, name)) {
		case 0:
			return sess.getStdout();
		case 1:
			return sess.getStderr();
		}
		return null;
	}

	protected OutputStream getToShellStream(String name) {
		return sess.getStdin();
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		host = (String) config.getMandatory("host");
		user = (String) config.getOptional("user", null);
		pass = (String) config.getOptional("pass", null);
		keyFileName = (String) config.getOptional("keyFileName", null);
	}

}
