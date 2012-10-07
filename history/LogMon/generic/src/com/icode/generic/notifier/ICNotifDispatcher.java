package com.icode.generic.notifier;

import java.util.*;

import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;

public class ICNotifDispatcher implements ICGenConfigurable {
	private static final String CHANNELS = "channels";
	
	Set listeners = new HashSet();
	Map channels = new HashMap();

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		for ( Iterator it = config.getChild(CHANNELS).getChildren(); it.hasNext(); ) {
			ICGenTreeNode chCfg = (ICGenTreeNode) it.next();
			String name = chCfg.getName();
			ICNotifChannel ch = (ICNotifChannel)ICAppFrame.getComponent(chCfg, ICNotifChannel.class);
			channels.put(name, ch);
			
			ICAppFrame.getTaskManager().addTask(ch);
		}
	}

	public synchronized void dispatchNotification(ICNotification notif) {
		if ((0 < listeners.size()) && (0 < channels.size())) {
			Set targets = new HashSet();
			String addr;
			for (Iterator it = channels.values().iterator(); it.hasNext();) {
				ICNotifChannel channel = (ICNotifChannel) it.next();

				targets.clear();
				for (Iterator itL = listeners.iterator(); itL.hasNext();) {
					addr = ((ICNotifiable) itL.next()).getListeningTarget(channel.type, notif);
					if (!ICGenUtils.isEmpty(addr)) {
						targets.add(addr);
					}
				}

				if (!targets.isEmpty()) {
					channel.addNotification(notif, targets);
				}
			}
		}
	}

	public synchronized void updateListeners(Set updatedListeners) {
		listeners.clear();
		if ( null != updatedListeners ) {
			listeners.addAll(updatedListeners);
		}
	}
}
