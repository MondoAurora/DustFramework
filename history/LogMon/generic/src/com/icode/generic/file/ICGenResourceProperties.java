package com.icode.generic.file;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;

import com.icode.generic.ICGenIndexer;
import com.icode.generic.resource.ICGenResource;

public class ICGenResourceProperties extends ICGenResource {
	
	protected void fillModule(ModuleRes modRes, ICGenIndexer.Index idxLocale, String resName) {
		try {
			InputStream is;
			is = ClassLoader.getSystemClassLoader().getResourceAsStream(resName + ".properties");
			if (null != is) {
				PropertyResourceBundle rb = new PropertyResourceBundle(is);
				is.close();

				for (Enumeration en = rb.getKeys(); en.hasMoreElements();) {
					String key = (String) en.nextElement();
					modRes.addStr(key, idxLocale, rb.getString(key));
				}
			}
		} catch (Exception e) {
		}
	}

}
