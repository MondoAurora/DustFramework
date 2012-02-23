package org.mondoaurora.frame.test;

import java.io.*;

import org.mondoaurora.frame.process.MAFProcessEventSource;
import org.mondoaurora.frame.shared.MAFRuntimeException;

public class MAFTestEventSourceFile extends MAFProcessEventSource {
	String content;
	int pos;

	public MAFTestEventSourceFile(String fileName) {
		byte[] buffer = new byte[(int) new File(fileName).length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(fileName));
			f.read(buffer);
		} catch (Exception e) {
			throw new MAFRuntimeException("MAFTestEventSourceFile", "Read failed", e);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ignored) {
				}
		}
		
		content = new String(buffer);
		pos = 0;
	}

	@Override
	public void start() {
		for ( pos = 0; pos < content.length(); ++pos ) {
			char c = content.charAt(pos);
			if (',' == c) {
				c = ',';
			}
			sendEvent(c);
		}
	}

	@Override
	public Object mark() {
		return pos;
	}

	@Override
	public void rollback(Object mark) {
		pos = ((Integer) mark) - 1; // the for cycle will step one forward
/*		
		int p1 = pos+30;
		if ( p1 >= content.length() ) {
			p1 = content.length()-1;
		}
		System.out.println("Stream rolled back to " + pos + ": " + content.substring(pos, p1));
*/
	}

	@Override
	public void releaseMark(Object mark) {
		
	}
	
	@Override
	public String toString() {
		return content.substring(pos);
	}
}
