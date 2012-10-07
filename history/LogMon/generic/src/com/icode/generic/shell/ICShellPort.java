package com.icode.generic.shell;

import java.io.*;
import java.util.Enumeration;

import javax.comm.*;

import com.icode.generic.base.ICGenTreeNode;

public class ICShellPort extends ICShell {
	public static final byte TYPE_SERIAL = 0;
	public static final byte TYPE_PARALLEL = 1;
	public static final String[] TYPE_NAMES = new String[] { "Serial", "Parallel" };

	String id;
	byte type;

	int serBaud, serDataBits, serStopBits, serParity;

	private static CommPortIdentifier portID;
	CommPort port;
	ParallelPort parallelPort;
	SerialPort serialPort;


	public ICShellPort() {
		super(ICSHELL_TO_SINGLE, ICSHELL_FROM_SINGLE, true);
	}

	public ICShellPort(String name, byte type) {
		this();

		setName(name);
		this.type = type;
	}

	protected void endOfAnswerInt(String stream) throws Exception {
	}

	protected OutputStream getToShellStream(String name) throws Exception {
		return port.getOutputStream();
	}

	protected InputStream getFromShellStream(String name) throws Exception {
		return port.getInputStream();
	}

	protected void initShell() throws Exception {
		if (null == portID) {
			System.out.println("Listing visible ports...");

			for (Enumeration ports = CommPortIdentifier.getPortIdentifiers(); ports.hasMoreElements();) {
				CommPortIdentifier pid = (CommPortIdentifier) ports.nextElement();
				System.out.println(pid.getName());
			}

			portID = CommPortIdentifier.getPortIdentifier(id);

			if (null != portID) {
				System.out.println("Opening port: " + id);
				port = portID.open("ICShellPort", 2000);
				if (TYPE_SERIAL == type) {
					serialPort = ((SerialPort) port);
					serialPort.setSerialPortParams(serBaud, serDataBits, serStopBits, serParity);
				} else {

				}
			} else {
				throw new Exception("Port not found: " + id);
			}
		}
	}

	protected void initConversation() throws Exception {
	}

	protected void streamClosed(String streamName) throws Exception {
		endShell();
	}

	public synchronized void killShellInt() throws Exception {
		serialPort.removeEventListener();
		port.getInputStream().close();
		port.getOutputStream().close();
		port.close();
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);

		id = config.getMandatory("id").toString();
		type = (byte) config.getOptionalInt("type", ICShellPort.TYPE_SERIAL);

		if (TYPE_SERIAL == type) {
			serBaud = config.getOptionalInt("baud", 19200);
			serDataBits = config.getOptionalInt("databits", SerialPort.DATABITS_8);
			serStopBits = config.getOptionalInt("stopbits", SerialPort.STOPBITS_1);
			serParity = config.getOptionalInt("parity", SerialPort.PARITY_NONE);
		}

		setName("Port " + id);
	}
}
