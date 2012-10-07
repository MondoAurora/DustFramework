package com.icode.generic.ldap;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;

import com.icode.generic.ICGenUtils;
import com.icode.generic.base.*;

public class ICGenLDAP implements ICGenConfigurable, ICGenConstants {
	String ldapHost;
	String ldapPort;

	String ldapUserFormat;

	protected Hashtable env = null;
	protected DirContext ldapContext = null;

	abstract class RepeatableLDAPAction {
		protected abstract Object doAction(boolean repeated) throws Exception;

		public Object doUserLDAPAction() throws Exception {
			try {
				return doAction(false);
			} catch (NamingException e) {
				initLdapContext();
				return doAction(true);
			}
		}
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		env = new Hashtable();

		ldapHost = (String) config.getOptional("ldapHost", "localhost");
		ldapPort = (String) config.getOptional("ldapPort", "389");
		ldapUserFormat = (String) config.getOptional("ldapUserFormat", getUserFormat());

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + ldapHost + ":" + ldapPort + "/");
		env.put(Context.BATCHSIZE, "500");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
	}

	public void authenticate(String user, String passwd) throws Exception {
		env.put(Context.SECURITY_PRINCIPAL, getAdminUserDN(user));
		env.put(Context.SECURITY_CREDENTIALS, passwd);

		initLdapContext();
	}

	public String getUserFormat() {
		return null;
	}

	public void logout() {
		try {
			ldapContext.close();
		} catch (NamingException e) {
			// I think this is not a problem here...
			e.printStackTrace();
		}
	}

	public String getAdminUserDN(String user) {
		return MessageFormat.format(ldapUserFormat, new String[] { user });
	}

	protected void initLdapContext() throws Exception {
		ldapContext = new InitialDirContext(env);
	}

	public static void loadAtts(ICGenObject obj, String[] names, Attributes atts) {
		String name, val;

		for (int i = names.length; i-- > 0;) {
			name = names[i];
			val = null;

			if (!ICGenUtils.isEmpty(name)) {
				Attribute att = atts.get(name);

				if (null != att) {
					try {
						val = att.get().toString();
					} catch (NamingException e) {
					}
				}
			}
			obj.setAttribObj(i, val);
		}
	}

	public static String safeGet(Attributes atts, String name) {
		Attribute att = atts.get(name);
		try {
			return (null == att) ? null : (String) att.get();
		} catch (NamingException e) {
			return null;
		}
	}

	public static interface MultiLoader {
		boolean isMulti(String attID);
		String[] getNameValuePair(String attID, String content);
		void init();
	}

	public static class NullMultiLoader implements MultiLoader {
		public String[] getNameValuePair(String attID, String content) {
			return new String[] { attID, content };
		}

		public void init() {}

		public boolean isMulti(String attID) {
			return false;
		}
	}
	
	public static final MultiLoader NULL_MULTI_LOADER = new NullMultiLoader();

	public static class DefaultMultiLoader implements MultiLoader {
		int[] counts;
		String[] multiAtts;
		
		public DefaultMultiLoader(String[] multiAtts) {
			this.multiAtts = multiAtts;
			counts = new int[multiAtts.length];
		}
		
		public String[] getNameValuePair(String attID, String content) {
			return new String[] { attID + inc(attID), content };
		}

		protected int inc(String attID) {
			return ++(counts[ICGenUtils.indexOf(multiAtts, attID)]);
		}

		public void init() {
			for ( int i = counts.length; i-->0; ) {
				counts[i] = 0;
			}
		}

		public boolean isMulti(String attID) {
			return -1 != ICGenUtils.indexOf(multiAtts, attID);
		}
	}

	public static class HeaderMultiLoader extends DefaultMultiLoader {
		String headerSep;

		public HeaderMultiLoader(String[] multiAtts) {
			this(multiAtts, "\n");
		}

		public HeaderMultiLoader(String[] multiAtts, String headerSep) {
			super(multiAtts);
			this.headerSep = headerSep;
		}

		public String[] getNameValuePair(String attID, String content) {
			int count = inc(attID);
			String[] ret = new String[2];
			
			int split = content.indexOf(headerSep);
			if (-1 == split) {
				ret[0] = String.valueOf(count);
				ret[1] = content;
			} else {
				ret[0] = content.substring(0, split);
				ret[1] = content.substring(split+1);
			}
			return ret;
		}
	}

	public static void loadAtts(ICGenTreeNode into, Attributes attributes, MultiLoader mLoader) throws Exception {
		for (Enumeration atts = attributes.getAll(); atts.hasMoreElements();) {
			Attribute a = (Attribute) atts.nextElement();
			String attID = a.getID();
			String value;

			for (Enumeration vals = a.getAll(); vals.hasMoreElements();) {
				value = String.valueOf(vals.nextElement());
				
				if (mLoader.isMulti(attID)) {
					String[] c = mLoader.getNameValuePair(attID, value);
					into.addChild(c[0], c[1]);
				} else {
					into.addChild(a.getID(), value);
				}
			}
		}
	}
}