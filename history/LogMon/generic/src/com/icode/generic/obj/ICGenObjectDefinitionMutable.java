package com.icode.generic.obj;

import com.icode.generic.base.ICGenDataManageable;
import com.icode.generic.base.ICGenTreeNode;

public class ICGenObjectDefinitionMutable extends ICGenObjectDefinitionConfigurable implements ICGenDataManageable {
	boolean dirty;
	
	public void storeDataInto(ICGenTreeNode node, Object hint) {
		// TODO Auto-generated method stub
		
	}
	
	protected void edit() {
		if (!dirty) {
			dirty = true;
			++version;
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	public final boolean closeVersion() {
		boolean d = dirty;
		dirty = false;
		return d;
	}
}
