/*
 * Created on 2004.06.10.
 */
package com.icode.generic.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Matrix
 */
public class ServletHelper {
	/**
	 * Adds some cache control header entries to the response header, that
	 * disables the caching of html pages. 
	 * @param resp The response
	 */
	public static void disableHttpCaching(HttpServletResponse resp) {
		// Set to expire far in the past.
		resp.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		resp.setHeader("Pragma", "no-cache");
	} 

	public static String getRequestLog(HttpServletRequest request) {
		StringBuffer logMsg = new StringBuffer();
		String method = request.getMethod(); 
		logMsg.append(method);

		if (method.equalsIgnoreCase("GET")) {
			Enumeration reqParNames = request.getParameterNames();
			if (reqParNames != null && reqParNames.hasMoreElements()) {
				String parName;
				String parValue;
				while (reqParNames.hasMoreElements()) {
					parName = (String) reqParNames.nextElement();
					parValue = request.getParameter(parName);
					logMsg.append("\n")
						.append(parName).append("=").append(parValue);
				}
			} else {
				logMsg.append(": Request has no parameters");
			}
		} else if (method.equalsIgnoreCase("POST")) {
			try {
				BufferedReader requestReader = request.getReader();
				try {
					requestReader.mark(16384);
					if (requestReader.ready()) {
						String line;
						do {
							line = requestReader.readLine();
							if (line != null) {
								logMsg.append("\n")
									.append(line);
							}
						} while (line != null);
					} else {
						logMsg.append(": Request has no parameters");
					}
				} finally {
					requestReader.reset();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return logMsg.toString();
	}

}
