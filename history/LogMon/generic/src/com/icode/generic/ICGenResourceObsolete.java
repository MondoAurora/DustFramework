package com.icode.generic;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class ICGenResourceObsolete extends ResourceBundle {
	String name;

	ArrayList propResources = new ArrayList();
	TreeSet keys = null;
	Map values = new HashMap();

	public ICGenResourceObsolete(String name) throws IOException {
		this(name, Locale.getDefault());
	}

	boolean addResPostfix(StringBuffer name, String postfix, ClassLoader classLoader) throws IOException {
		if (ICGenUtils.isEmpty(postfix)) {
			return false;
		} else {
			name.append("_").append(postfix);
			addRes(name.toString(), classLoader);
			return true;
		}
	}

	public ICGenResourceObsolete(String name, Locale locale) throws IOException {
		StringBuffer n = new StringBuffer(name);

		this.name = name;
		ClassLoader classLoader = getClass().getClassLoader();
		addRes(name, classLoader);

		if (null != locale) {
			if (addResPostfix(n, locale.getLanguage(), classLoader)) {
				if (addResPostfix(n, locale.getCountry(), classLoader)) {
					addResPostfix(n, locale.getVariant(), classLoader);
				}
			}
		}
	}

	private void addRes(String resName, ClassLoader classLoader) throws IOException {
		InputStream is;
		is = classLoader.getResourceAsStream(resName + ".properties");
		if (null != is) {
			propResources.add(new PropertyResourceBundle(is));
			is.close();
		}
	}

	public synchronized Enumeration getKeys() {
		if (null == keys) {
			keys = new TreeSet();
			Enumeration e;
			for (int i = propResources.size(); i-- > 0;) {
				e = ((ResourceBundle) propResources.get(i)).getKeys();
				while (e.hasMoreElements()) {
					keys.add(e.nextElement());
				}
			}
		}
		return Collections.enumeration(keys);
	}

	protected Object handleGetObject(String key) {
		if (values.containsKey(key)) {
			return values.get(key);
		} else {
			synchronized (values) {
				if (values.containsKey(key)) {
					return values.get(key);
				} else {
					Object ret = null;
					for (int i = propResources.size(); (null == ret) && (i-- > 0);) {
						try {
							ret = ((ResourceBundle) propResources.get(i)).getObject(key);
						} catch (MissingResourceException me) {
							// do nothing
						}
					}
					values.put(key, ret);
					return ret;
				}
			}
		}
	}
}
