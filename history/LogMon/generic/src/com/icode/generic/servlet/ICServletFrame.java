package com.icode.generic.servlet;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpSession;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConstants;

public class ICServletFrame extends ICAppFrame implements ServletContextListener, ICGenConstants {
	
	public static final String SESSION_CACHE = "ICServletFrame.Cache";
	private static final ThreadLocal context = new ThreadLocal();


	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext ctx = arg0.getServletContext();
		context.set(ctx);

		String appRes = (String) ctx.getInitParameter(DEFAULT_APPRES);
		if (null == appRes) {
			appRes = DEFAULT_APPRES;
		}

		String configFName = (String) ctx.getAttribute(DEFAULT_CONFIGFILE);
		if (null == configFName) {
			configFName = DEFAULT_CONFIGFILE;
		}

		ICAppFrame.main(appRes, configFName, null, this, new String[] {});
	}

	protected Map getContexCache(int ctx) {
		if (0 == ctx) {
			return super.getContexCache(ctx);
		} else {
			HttpSession session = ICServlet.getSession();
			Map mapSession = (Map) session.getAttribute(SESSION_CACHE);
			
			if ( null == mapSession ) {
				mapSession = new HashMap();
				session.setAttribute(SESSION_CACHE, mapSession);
			}
			
			return mapSession;
		}
	}

	protected InputStream getConfigFileAsStream(String cfgFile) throws Exception {
		return ((ServletContext)context.get()).getResourceAsStream("/WEB-INF/" + cfgFile);
	}

}
