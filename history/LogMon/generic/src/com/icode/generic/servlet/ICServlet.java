package com.icode.generic.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.icode.generic.ICGenCommand;
import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConstants;
import com.icode.generic.base.ICGenURLUTF8Encoder;

public class ICServlet extends HttpServlet implements ICGenConstants {
	private static final long serialVersionUID = 1L;
	
	private static final ThreadLocal session = new ThreadLocal();
	
	private ICGenCommand cmd;
	
	ICGenCommand getCmd() {
		if ( null == cmd ) {
			synchronized (this) {
				if ( null == cmd ) {
					String procname = getInitParameter(SERVLET_CMD_PROC);
					cmd = (ICGenCommand) ICAppFrame.getComponent(procname, ICGenCommand.class);
				}
			}
		}
		
		return cmd;
	}

	protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected final boolean processRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		//later: I should know about the services if they require a session, and pre-create it for those
		//now: brute force, create the session
		
		session.set(servletRequest.getSession(true));
		
		String cmd = servletRequest.getParameter(SERVLET_PARAM_CMD);
		
		if ( null != cmd ) {
			cmd = ICGenURLUTF8Encoder.decode(cmd);
		}
		
		String[] ss = ICGenUtils.str2arr(cmd, '/');
		int cmdcnt = ss.length;
		
		ArrayList params = new ArrayList();
		for ( Iterator itPar = servletRequest.getParameterMap().entrySet().iterator(); itPar.hasNext(); ) {
			Map.Entry e = (Map.Entry) itPar.next();
			String pName = (String) e.getKey();
			if ( !SERVLET_PARAM_CMD.equals(pName) ) {
				params.add("-" + pName);
				String value = servletRequest.getParameter(pName);
				if ( !ICGenUtils.isEmpty(value) ) {
					params.add(ICGenURLUTF8Encoder.decode(value));
				}
			}
		}
		
		String[] cmdpars = new String[cmdcnt + params.size()];
		System.arraycopy(ss, 0, cmdpars, 0, cmdcnt);
		for ( int i = 0; i < params.size(); ++i ) {
			cmdpars[cmdcnt+i] = (String) params.get(i);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bos);
		Exception cmdEx = null;
		Object cmdRet = null;
		try {
			cmdRet = getCmd().execute(cmdpars, out);
		} catch (Exception ex) {
			cmdEx = ex;
			ex.printStackTrace(out);
		}
		
		
		ServletHelper.disableHttpCaching(servletResponse);

		formatResult(cmdRet, servletResponse, bos.toString(), cmdEx);
		
		return true;
	}

	protected void formatResult(Object cmdRet, HttpServletResponse response, String out, Exception ex) throws IOException {
	}

	public static HttpSession getSession() {
		return (HttpSession) session.get();
	}

}
