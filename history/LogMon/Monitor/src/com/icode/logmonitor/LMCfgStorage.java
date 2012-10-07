package com.icode.logmonitor;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.user.ICGenUser;

public class LMCfgStorage implements LMConstants.Storage {
	Set admins = new HashSet();
	
	public long authenticate(String user, String passwd) throws Exception {
		return 0;
	}

	public Set getAdmins() throws Exception {
		return admins;
	}

	public String getAuthUser() {
		return null;
	}

	public ArrayList getKnownSpammers() throws Exception {
		return null;
	}

	public ICGenTreeNode getStatus() throws Exception {
		return null;
	}

	public void logout() {
	}

	public void storeStatus(ICGenTreeNode status) throws Exception {
		System.out.println(status);
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		ICGenTreeNode n = config.getChild("admins");
		
		if (null != n) {
			for (Iterator it = n.getChildren(); it.hasNext();) {
				n = (ICGenTreeNode) it.next();
				admins.add(ICAppFrame.getComponent(n, ICGenUser.class));
			}
		}
	}
}
