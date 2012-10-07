package com.icode.generic.base;

import java.util.*;

import com.icode.generic.resolver.ICGenResolver;

/**
 * A node of a tree. A node can have a name and a value, and
 * may contains child nodes too.
 * @author Matrix
 */
public class ICGenTreeNode implements ICGenResolver.PathElement {
  protected String name;
  protected String value;
  protected Map children;
  protected ICGenTreeNode parent;
  
  private static final Iterator NO_CHILDREN = new Iterator() {
		public boolean hasNext() {
			return false;
		}

		public Object next() {
			return null;
		}

		public void remove() {
		}
  	
  };

  protected ICGenTreeNode(String name, String value, ICGenTreeNode parent) {
    this.name = name;
    this.value = value;
    this.parent = parent;
  }

  public ICGenTreeNode(String name, String value) {
    this(name, value, null);
  }

  public ICGenTreeNode(String name) {
    this(name, null, null);
  }

  /**
   * Returns the name.
   * @return String
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   * @param name The name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the full name of the node including the name of the ancestors too.
   * @return
   */
  public String getFullName() {
  	ICGenTreeNode parent = getParent();
  	if (parent != null) {
  		return parent.getFullName() + "." + getName();
  	} else {
  		return getName();
  	}
  }
  
  /**
   * Returns the value.
   * @return Object
   */
  public String getValue() {
    return value;
  }

  public ArrayList getValueArray() {
 		String[] ss = ICGenUtilsBase.str2arr(value, ',');
 		
  	ArrayList retVal = new ArrayList(ss.length);
  	for ( int i = 0; i < ss.length; ++i ) {
  		retVal.add(ss[i]);
  	}
  	return retVal;
  }
  
  /**
   * Sets the value.
   * @param value The value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the parent.
   * @return ICGenTreeNode
   */
  public ICGenTreeNode getParent() {
    return parent;
  }

  /**
   * Sets the parent.
   * @param parent The parent to set
   */
  public void setParent(ICGenTreeNode parent) {
    this.parent = parent;
  }

  // child related methods

  public int getChildCount() {
    if (children != null) {
      return children.size();
    } else {
      return 0;
    }
	}

  public boolean hasChild() {
    return (getChildCount() > 0);
  }

  public Iterator getChildren() {
    return (null == children) ? NO_CHILDREN : children.values().iterator();
  }

  public Set getChildNames() {
    return (null == children) ? null : children.keySet();
  }

  public ICGenTreeNode getChild(String name) {
  	return getChild(name, false);
  }

  public ICGenTreeNode getChild(String name, boolean createMissing) {
    if (children == null || !children.containsKey(name)) {
      return createMissing ? addChild(name) : null;
    }
    return (ICGenTreeNode) children.get(name);
  }

  public ICGenTreeNode addChild(String name) {
    return addChild(name, null);
  }

  public ICGenTreeNode addChild(String name, String value) {
    if (children == null) {
        children = new TreeMap();
    }
    ICGenTreeNode child = new ICGenTreeNode(name, value, this);
    children.put(name, child);
    return child;
  }

  public ICGenTreeNode addChild(ICGenTreeNode node) {
    if (children == null) {
      children = new TreeMap();
    }
    ICGenTreeNode oldParent = node.getParent();
    if (oldParent != null) {
      oldParent.removeChild(node);
    }
    node.setParent(this);
    children.put(node.name, node);
    return node;
  }

  public ICGenTreeNode removeChild(String name) {
    if (children == null) {
      return null;
    }
    ICGenTreeNode oldChild = (ICGenTreeNode)children.remove(name);
    if (oldChild != null) {
      oldChild.setParent(null);
    }
    return oldChild;
  }

  public ICGenTreeNode removeChild(ICGenTreeNode node) {
    return removeChild(node.name);
  }
  
  public void reset() {
		for ( Iterator itCh = getChildren(); itCh.hasNext(); ) {
			((ICGenTreeNode)itCh.next()).setParent(null);
		}
		children = null;
  }
  
  public void append(ICGenTreeNode node) {
  	if ( null != node.children ) {
  		for ( Iterator itCh = node.children.entrySet().iterator(); itCh.hasNext(); ) {
  			Map.Entry eCh = (Map.Entry) itCh.next();
  			String name = (String)eCh.getKey();
  			ICGenTreeNode otherChild = (ICGenTreeNode) eCh.getValue();
  			ICGenTreeNode child = getChild(name, false);
  			if ( null == child ) {
  				child = addChild(name, otherChild.getValue());
  			} else if (null == child.getValue() ) {
  				child.setValue(otherChild.getValue());
  			}
  			if ( otherChild.hasChild() ) {
  				child.append(otherChild);
  			}
  		}
  	}
  }

  public String getMandatory(String childName) {
  	
  	ICGenTreeNode child = getChild(childName);
  	if (child != null) {
  		return child.getValue(); 
  	} else {
  		throw new RuntimeException(getFullName() + " doesn't have child " + childName);
  	}
  }
  
  public String getOptional(String childName, String defaultValue) {
  	ICGenTreeNode child = getChild(childName);
  	if (child != null) {
  		return child.getValue(); 
  	} else {
  		return defaultValue;
  	}
  }
  
  public String getNameAtt(String childName) {
  	return getOptional(childName, getName());
  }
  
  public int getOptionalInt(String childName, int defaultValue) {
  	ICGenTreeNode child = getChild(childName);
  	if (child != null) {
  		return Integer.parseInt(child.getValue()); 
  	} else {
  		return defaultValue;
  	}
  }
  
  public long getOptionalLong(String childName, long defaultValue) {
  	ICGenTreeNode child = getChild(childName);
  	if (child != null) {
  		return Long.parseLong( child.getValue()); 
  	} else {
  		return defaultValue;
  	}
  }

	public void toTransferString(StringBuffer content) {
		content.append(ICGenURLUTF8Encoder.encode(name));
		content.append(":");
		if ( null != value ) {
			content.append(ICGenURLUTF8Encoder.encode(value));
		}
		content.append("\n");

		if (hasChild()) {
			content.append("+\n");
			Iterator it = getChildren();
			while (it.hasNext()) {
				((ICGenTreeNode) it.next()).toTransferString(content);
			}
			content.append("-\n");
		}
	}

	public static ICGenTreeNode fromTransferString(String str) {
		ICGenTreeNode root, current;
		String line;
		boolean goDown = false;
		root = current = null;

		int pos = 0;
		int idx;

		do {
			idx = str.indexOf('\n', pos);

			line = str.substring(pos, (-1 == idx) ? str.length() : idx).trim();

			if ("+".equals(line)) {
				goDown = true;
			} else if ("-".equals(line)) {
				current = current.getParent();
			} else {
				int cm = line.indexOf(":");

				if (-1 != cm) {
					String name = ICGenURLUTF8Encoder.decode(line.substring(0, cm)).trim();
					String value = line.substring(cm + 1).trim();
					if ( 0 == value.length() ) {
						value = null;
					} else {
						value = ICGenURLUTF8Encoder.decode(value);
					}

					if (null == current) {
						root = current = new ICGenTreeNode(name, value);
					} else {
						if (goDown) {
							current = current.addChild(name, value);
							goDown = false;
						} else {
							current = current.parent.addChild(name, value);
						}
					}
				}
			}

			pos = idx + 1;
		} while (-1 != idx);

		return root;
	}


	public interface NodeFormatter {
		void endNode(StringBuffer into);
		void startNode(StringBuffer into, String header);
		void printValue(StringBuffer into, String header, String value);
	}
	
	public static class PlainFormatter implements NodeFormatter {
		String indent = "  ";
		StringBuffer currIndent = new StringBuffer();
		
		public void endNode(StringBuffer into) {
			int len = currIndent.length();
			currIndent.delete(len - indent.length(), len);
		}

		public void startNode(StringBuffer into, String header) {
			into.append(currIndent).append(header).append(": ");
			currIndent.append(indent);
		}

		public void printValue(StringBuffer into, String header, String value) {
			if ( null != value ) {
				into.append(value);
			}
			into.append("\n");
		}
	}
	
	public static class HTMLFormatter implements NodeFormatter {
		String startNodeWith;
		String endNodeWith;
		String indent = "&nbsp;&nbsp;";
		StringBuffer currIndent = new StringBuffer();
		
		public HTMLFormatter() {
			this("<p>", "</p>\n");
		}
		
		protected HTMLFormatter(String startNodeWith, String endNodeWith) {
			this.startNodeWith = startNodeWith;
			this.endNodeWith = endNodeWith;
		}
		
		public void startNode(StringBuffer into, String header) {
			into.append(startNodeWith).append(currIndent).append(header).append(": ").append(currIndent);
			currIndent.append(indent);
		}

		public void endNode(StringBuffer into) {
			int len = currIndent.length();
			currIndent.delete(len - indent.length(), len);
			into.append(endNodeWith);
		}

		public void printValue(StringBuffer into, String header, String value) {
			if ( null != value ) {
				into.append(value.replaceAll("\n", "<br/>\n" + currIndent));
			}
		}
	}
	
	public void toStringBufferFormatted(StringBuffer into, NodeFormatter formatter) {
		formatter.startNode(into, name);
		formatter.printValue(into, name, value);
		try {
			for ( Iterator it = getChildren(); it.hasNext(); ) {
				((ICGenTreeNode)it.next()).toStringBufferFormatted(into, formatter);
			}
		} finally {
			formatter.endNode(into);
		}
	}
/*
	public String toString() {
		return toString(new PlainFormatter());
	}
*/
	public String toStringHTML() {
		return toString(new HTMLFormatter());
	}

	public String toString(NodeFormatter formatter) {
		StringBuffer ret = new StringBuffer();
		toStringBufferFormatted(ret, formatter);
		return ret.toString();
	}

	public String toString() {
		return value;
	}

	public Object getPathElement(String name) {
		return getChild(name);
	}
}
