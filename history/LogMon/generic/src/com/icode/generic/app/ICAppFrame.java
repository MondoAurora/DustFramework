package com.icode.generic.app;

import java.io.*;
import java.util.*;

import com.icode.generic.*;
import com.icode.generic.base.*;
import com.icode.generic.log.ICLogMessage;
import com.icode.generic.log.ICLogger;
import com.icode.generic.shell.ICShell;
import com.icode.generic.shell.ICShellCmdProcessor;
import com.icode.generic.task.*;

public class ICAppFrame implements ICGenConfigurable, ICGenConstants {
	public static final int MAINEXIT_SUCCESS = 0;
	public static final int MAINEXIT_ERR_PARAMS = -1;
	public static final int MAINEXIT_ERR_EXCEPTION = -2;

	static ICAppFrame theFrame;

	private ICGenResourceObsolete appRes;
	private Locale locale = Locale.getDefault();
	private String configFileName = DEFAULT_CONFIGFILE;
	private static boolean diagMode = false;
	private ICGenConfigurationTree config = new ICGenConfigurationTree();

	private Map mapStaticObjects = new HashMap();
	private Map mapDefTypes = new HashMap();

	private ICTaskManager taskMan;
	ICAppMain theApp;

	ICGenCommand cmdLineProc;

	private boolean shuttingDown = false;

	public static class ParamHandler implements ICGenCommand.Executor {
		public Object execute(PrintStream out) throws Exception {
			return null;
		}

		public boolean handleArgument(String arg) throws Exception {
			return false;
		}

		public boolean handleParamValue(int paramIdx, String value) throws Exception {
			switch (paramIdx) {
			case 0:
				theFrame.locale = ICGenUtils.localeFromString(value);
				return true;
			case 1:
				theFrame.configFileName = value;
				return true;
			case 2:
				diagMode = true;
				return true;
			default:
				return false;
			}
		}

		public void init() {
		}
	};

	public static String getString(String key) {
		return theFrame.appRes.getString(key);
	}

	public static void printAppHeader(PrintStream w) {
		w.print(getString(APPINFO_NAME));
		w.print(" ");
		w.println(getString(APPINFO_VERSION));
	}

	protected boolean parseProps(String propName) throws Exception {
		propName = propName.replace('.', '/');
		InputStream is = getClass().getClassLoader().getResourceAsStream(propName + ".properties");
		if (null != is) {
			Properties props = new Properties();
			props.load(is);
			is.close();
			config.parseConfig(props);
			return true;
		}
		return false;
	}

	protected boolean loadConfigs(String mainAppRes, String[] args) throws Exception {
		parseProps(getClass().getPackage().getName() + ".icgen");
		parseProps(mainAppRes);

		ICGenTreeNode cfgFrame = config.getChild(CFG_FRAME);
		if (null != cfgFrame) {
			loadDataFrom(cfgFrame, LOAD_HINT_CONFIG);
		}

		boolean parseSuccess = true;

		ICGenTreeNode cfgCmdLine = config.getChild(CFG_CMDLINEPROCESSOR);
		if (null != cfgCmdLine) {
			ICGenCommand cmd = new ICGenCommand();
			cmd.loadDataFrom(cfgCmdLine, LOAD_HINT_CONFIG);
			parseSuccess = (CMDEXEC_FAILURE != cmd.execute(args, System.out));
		}

		appRes = new ICGenResourceObsolete(mainAppRes, theFrame.locale);

		printAppHeader(System.out);
		if (!parseSuccess) {
			// error exit
			System.out.print(getString(APPINFO_LONG));
			System.exit(MAINEXIT_ERR_PARAMS);
		}

		InputStream is = getConfigFileAsStream(configFileName);
		if (null != is) {
			config.parseConfig(is, true);
		}

		// logger = (ICLogger) getComponent(APP_LOGGER);
		taskMan = (ICTaskManager) getComponent(APP_TASK_MANAGER, ICTaskManager.class);

		if (null == theApp) {
			ICGenTreeNode appNode = config.getChild(CFG_MAINCLASS);
			if (null != appNode) {
				theApp = (ICAppMain) getCachedComponent(mapStaticObjects, CFG_MAINCLASS, appNode, ICAppMain.class);
			} else {
				throw new Exception(CFG_MAINCLASS + " property not found, the application cannot start!");
			}

		} else {
			if (theApp instanceof ICGenConfigurable) {
				ICGenTreeNode mainCfg = config.getChild(CFG_MAINCLASS, true);
				((ICGenConfigurable) theApp).loadDataFrom(mainCfg, LOAD_HINT_CONFIG);
			}
		}

		return parseSuccess;
	}

	protected InputStream getConfigFileAsStream(String cfgFile) throws Exception {
		File f = new File(cfgFile);
		return (f.exists() && f.isFile()) ? new FileInputStream(cfgFile) : null;
	}

	protected Object getConfiguredComponent(ICGenTreeNode cfg, Class requiredClass) {
		if (null == cfg) {
			return null;
		}

		Object ret;

		String type = (String) cfg.getValue();
		if (null == type) {
			type = "";
		}
		if (type.startsWith(">")) {
			ret = getConfiguredComponent(type.substring(1), requiredClass);
		} else if (type.startsWith("!")) {
			try {
				int ce = type.indexOf('!', 1);
				Class oc;
				oc = Class.forName(type.substring(1, ce));
				String fld = type.substring(ce+1);
				ret = oc.getField(fld).get(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else if (type.startsWith("<")) {
			String[] defss = ICGenUtils.str2arr(type.substring(1), '|');
			ICGenTreeNode defCfg = config.getChild(defss[0]);
			cfg.append(defCfg);
			type = (1 == defss.length) ? (String) defCfg.getValue() : defss[1];
			cfg.setValue(type);
			ret = createConfiguredComponent(cfg, requiredClass);
		} else if (type.startsWith("[")) {
			int ce = type.indexOf(']');
			int ctxId = Integer.parseInt(type.substring(1, ce));
			ret = getCachedComponent(getContexCache(ctxId), cfg.getName(), cfg, requiredClass);
		} else {
			ret = createConfiguredComponent(cfg, requiredClass);
		}

		return ret;
	}

	protected Map getContexCache(int ctx) {
		return mapStaticObjects;
	}

	protected final Object getCachedComponent(Map cache, String name, ICGenTreeNode cfg, Class requiredClass) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		} else {
			synchronized (cache) {
				if (cache.containsKey(name)) {
					return cache.get(name);
				} else {
					Object ret = createConfiguredComponent(cfg, requiredClass);
					cache.put(name, ret);
					return ret;
				}
			}
		}
	}

	protected Object createConfiguredComponent(ICGenTreeNode cfg, Class requiredClass) {
		Object ret = null;
		try {
			String className = (String) cfg.getValue();
			if (ICGenUtils.isEmpty(className)) {
				className = (String) mapDefTypes.get(requiredClass);
			}

			if (!ICGenUtils.isEmpty(className)) {
				int ctxIdx = className.indexOf(']');
				if (-1 != ctxIdx) {
					className = className.substring(ctxIdx + 1);
				}
				ret = Class.forName(className).newInstance();
				if (null != cfg) {
					if (ret instanceof ICGenConfigurable) {
						((ICGenConfigurable) ret).loadDataFrom(cfg, LOAD_HINT_CONFIG);
					} else if (ret instanceof ICGenDataManageable) {
						((ICGenDataManageable) ret).loadDataFrom(cfg, null);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ret;
	}

	public Object getConfiguredComponent(String name, Class requiredClass) {
		return getConfiguredComponent(config.getChild(name), requiredClass);
	}

	public static Object getComponent(String name, Class requiredClass) {
		return theFrame.getConfiguredComponent(name, requiredClass);
	}

	public static Object getComponent(ICGenTreeNode cfg, Class requiredClass) {
		return theFrame.getConfiguredComponent(cfg, requiredClass);
	}

	public static ICTaskManager getTaskManager() {
		return theFrame.taskMan;
	}

	public static ICAppMain getApp() {
		return theFrame.theApp;
	}

	public static ICGenTreeNode getConfig() {
		return theFrame.config;
	}

	public static ICGenTreeNode getConfig(String name) {
		return theFrame.config.getChild(name);
	}

	public static boolean isDiagMode() {
		return diagMode;
	}

	protected void launch() throws Exception {
		theApp.init();
		/*
		 * Runtime.getRuntime().addShutdownHook(new Thread() { public void run() { endApp(); } });
		 */
		ICGenTreeNode cfg;

		cfg = config.getChild(APP_MONITOR);
		if (null != cfg) {
			ICTask monitor = (ICTask) getComponent(cfg, ICTask.class);
			getTaskManager().addTask(monitor);
			getTaskManager().startGroup(monitor.getGroupName(), null, WAIT_FOREVER);
			if (!monitor.stateCheck(ICTaskConstants.STATECHECK_RUNNING)) {
				throw new Exception("Failed to start app monitor " + monitor.getException().getMessage());
			}
		}

		cfg = config.getChild(APP_SHELL);
		if (null != cfg) {
			ICShell shell = (ICShell) getComponent(APP_SHELL, ICShell.class);
			ICGenCommand appCmd = (ICGenCommand) getComponent(APP_COMMANDS, ICGenCommand.class);
			ICShellCmdProcessor cp = new ICShellCmdProcessor(appCmd);
			shell.setProcessor(cp);
			shell.startShell();
		}

		theApp.launch();
	}

	public static void shutdown() {
		theFrame.endApp();
	}

	public synchronized void endApp() {
		if (!shuttingDown) {
			shuttingDown = true;
			try {
				theApp.shutdown();

				if (null != taskMan) {
					taskMan.shutdown();
				}

				theApp.shutdownAfterTasksStopped();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void logInfoMsg(String from, String message) {
		((ICLogger) getComponent(APP_LOGGER, ICLogger.class)).log(EVENT_LEVEL_INFO, from, message, null);
	}

	public static void log(byte level, String from, String message, Object param) {
		((ICLogger) getComponent(APP_LOGGER, ICLogger.class)).log(level, from, message, param);
	}

	public static void log(ICLogMessage logMsg) {
		((ICLogger) getComponent(APP_LOGGER, ICLogger.class)).log(logMsg);
	}

	public static void main(String mainAppRes, String[] args) {
		main(mainAppRes, "config.properties", null, null, args);
	}

	public static void main(String mainAppRes, ICAppMain main, String[] args) {
		main(mainAppRes, "config.properties", main, null, args);
	}

	public static void main(String mainAppRes, String configFName, ICAppMain main, String[] args) {
		main(mainAppRes, configFName, main, null, args);
	}

	public static void main(String mainAppRes, String configFName, ICAppMain main, ICAppFrame frame, String[] args) {
		try {
			ResourceBundle res = ResourceBundle.getBundle(mainAppRes);
			String frmClass = ICGenUtils.getResValue(res, CFG_FRAME, null);

			if (null == frame) {
				theFrame = ICGenUtils.isEmpty(frmClass) ? new ICAppFrame() : (ICAppFrame) Class.forName(frmClass).newInstance();
				theFrame.configFileName = configFName;
				theFrame.theApp = main;
			} else {
				theFrame = frame;
			}

			if (theFrame.loadConfigs(mainAppRes, args)) {
				theFrame.launch();
			} else {
				System.exit(MAINEXIT_ERR_PARAMS);
			}
		} catch (Exception e) {
			System.out.println("FATAL: " + e.getMessage());
			e.printStackTrace(System.err);
			theFrame.endApp();
			// System.exit(MAINEXIT_ERR_EXCEPTION);
		}
	}

	public static void addDefClass(Class reqClass, Class defaultClass) throws Exception {
		theFrame.mapDefTypes.put(reqClass, defaultClass.getName());
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		ICGenTreeNode node;

		ICGenTreeNode defTypes = config.getChild(FRAME_DEFTYPES);
		if (null != defTypes) {
			for (Iterator it = defTypes.getChildren(); it.hasNext();) {
				node = (ICGenTreeNode) it.next();
				mapDefTypes.put(Class.forName(node.getName()), node.getValue());
			}
		}
	}

}
