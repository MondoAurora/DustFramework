package com.icode.generic.file;

import java.io.PrintStream;
import java.text.ParsePosition;
import java.util.Iterator;
import java.util.Map;

import com.icode.generic.ICGenUtils;
import com.icode.generic.shell.ICShell;
import com.icode.generic.shell.ICShellOS;

public interface ICFileIdentifier {
	void getIDForFiles(Map files) throws Exception;

	public static class Default implements ICFileIdentifier {

		public void getIDForFiles(Map files) throws Exception {
			for (Iterator it = files.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				e.setValue(e.getKey());
			}
		}

	}

	public static class Unix implements ICFileIdentifier {
		private static final String ENDSTRING = "-.-.-";

		class InodeReader extends ICShell.LineProcessor {
			String prompt = null;
			Map files;

			InodeReader(Map files) {
				super(false);
				this.files = files;
			}

			public Object processLine(String line) throws Exception {
				if (ENDSTRING.equals(line)) {
					getShell().getToShellPrintStream().println("exit");
				} else if ( line.length() > 0 ){
					char ch = line.charAt(0);
					if (Character.isDigit(ch) || Character.isWhitespace(ch)) {
						try {
							ParsePosition pos = new ParsePosition(0);
							ICGenUtils.skipSeparators(line, ' ', pos);
							String inode = ICGenUtils.getWord(line, ' ', pos);
							String filename = line.substring(pos.getIndex()).trim();

							if (files.containsKey(filename)) {
								files.put(filename, inode);
							}
						} catch (Exception ex) {

						}
					}
				}
				return null;
			}

		};

		public void getIDForFiles(Map files) throws Exception {
			System.out.println("Starting shell");
			StringBuffer command = new StringBuffer("ls -1i ");
			for (Iterator it = files.keySet().iterator(); it.hasNext();) {
				command.append("\"").append(it.next()).append("\" ");
			}
			command.append(" && echo " + ENDSTRING);

			ICShellOS shell = new ICShellOS();
			InodeReader ir = new InodeReader(files);
			shell.setProcessor(ir);
			shell.startShell();

			PrintStream ps = shell.getToShellPrintStream();
			ps.println(command);
			shell.waitFor();

			System.out.println("Shell done");
		}
	}
}
