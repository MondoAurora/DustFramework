/*
 * Created on 2004.12.23.
 *
 */
package com.icode.generic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.icode.generic.base.ICGenTreeNode;


/**
 * based on 
 * @author KL
 */
public class ICGenConfigurationTree extends ICGenTreeNode {
	/** the parameter name segments are separated by this character */
	public static final char ATTRIB_SEPARATOR = '$';
	/** the parameter name segments are separated by this character */
	public static final char MEMBER_SEPARATOR = '.';

	public static final String ROOT_NAME = "/";

	public ICGenConfigurationTree() {
		super(ROOT_NAME);
	}
	
	public void parseConfig(String propFileName) throws Exception {
		FileInputStream in = new FileInputStream(propFileName);
		parseConfig(in, true);
	}
	
	public void parseConfig(InputStream configStream, boolean closeWhenDone) throws Exception {
		Properties configuration = new Properties();
		configuration.load(configStream);
		parseConfig(configuration);
		if ( closeWhenDone ) {
			configStream.close();
		}
	}
	
	/**
	 * It parses the specified <code>Properties</code> object, and sorts
	 * the parameters by its types.
	 * @param configuration   the <code>Properties</code> oject to parse
	 */
	public void parseConfig(Properties configuration) {
		Enumeration keys = configuration.keys();
		String key;
		String value;

		while (keys.hasMoreElements()) {
			key = (String) keys.nextElement();
			value = configuration.getProperty(key);			
			insertEntry(key, value);
		}
	}

	public ICGenTreeNode findChild(String key) {
		return findChild(key, false);
	}

	public ICGenTreeNode findChild(String key, boolean createMissing) {
		ICGenTreeNode child = this;
		int sepIdx = key.indexOf(ATTRIB_SEPARATOR);
		String nodeName;
		String attName;
		if ( -1 != sepIdx ) {
			nodeName = key.substring(0, sepIdx);
			attName = key.substring(sepIdx+1);
		} else {
			nodeName = key;
			attName = null;			
		}
		
		String name;

		for ( boolean readOn = true; readOn; ) {
			sepIdx = nodeName.indexOf(MEMBER_SEPARATOR);
			if ( -1 != sepIdx ) {
				name = nodeName.substring(0, sepIdx);
				nodeName = nodeName.substring(sepIdx + 1);
			} else {
				name = nodeName;
				readOn = false;
			}
			child = child.getChild(name, createMissing);
		}
		
		if ( null != attName ) {
			child = child.getChild(attName, createMissing);
		}

/*		
		for ( boolean readOn = true; readOn; ) {
			sepIdx = key.indexOf(MEMBER_SEPARATOR);
			if ( -1 == sepIdx ) {
				sepIdx = key.indexOf(ATTRIB_SEPARATOR);
			}
			if (sepIdx > 0) {
				name = key.substring(0, sepIdx);
				key = key.substring(sepIdx + 1);
			} else {
				name = key;
				readOn = false;
			}
			child = child.getChild(name, createMissing);
		}
*/
		return child;
	}

	private void insertEntry(String key, String value) {
		ICGenTreeNode target = findChild(key, true);
		target.setValue(value);
	}

}
