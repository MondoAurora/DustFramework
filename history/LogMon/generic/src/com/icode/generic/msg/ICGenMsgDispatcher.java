package com.icode.generic.msg;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;

public class ICGenMsgDispatcher implements ICGenConfigurable {
	private static final String CHANNELS = "channels";

	Set listeners = new HashSet();
	Map channels = new HashMap();

	Map mapListenedLevels = new HashMap();

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		for (Iterator it = config.getChild(CHANNELS).getChildren(); it.hasNext();) {
			ICGenTreeNode chCfg = (ICGenTreeNode) it.next();
			String name = chCfg.getName();
			ICGenMsgChannel ch = (ICGenMsgChannel) ICAppFrame.getComponent(chCfg, ICGenMsgChannel.class);
			channels.put(name, ch);

			ICAppFrame.getTaskManager().addTask(ch);
		}
	}

	public boolean isMsgListened(ICGenMsgDef msgDef) {

		synchronized (mapListenedLevels) {
			Boolean listened = (Boolean) mapListenedLevels.get(msgDef);

			if (null == listened) {
				for (Iterator itL = listeners.iterator(); itL.hasNext();) {
					ICGenMsgListener ml = (ICGenMsgListener) itL.next();
					for (Iterator it = channels.values().iterator(); it.hasNext();) {
						ICGenMsgChannel channel = (ICGenMsgChannel) it.next();
						if (ml.isAccept(channel.type, msgDef)) {
							mapListenedLevels.put(msgDef, Boolean.TRUE);
							return true;
						}
					}
				}

				mapListenedLevels.put(msgDef, listened = Boolean.FALSE);
			}

			return listened.booleanValue();
		}
	}

	public synchronized void dispatchMsg(ICGenMsg msg) {
		if ((0 < listeners.size()) && (0 < channels.size())) {
			Set targets = new HashSet();
			for (Iterator it = channels.values().iterator(); it.hasNext();) {
				ICGenMsgChannel channel = (ICGenMsgChannel) it.next();

				targets.clear();
				for (Iterator itL = listeners.iterator(); itL.hasNext();) {
					ICGenMsgListener ml = (ICGenMsgListener) itL.next();
					if (ml.isAccept(channel.type, msg.def)) {
						targets.add(ml);
					}
				}

				if (!targets.isEmpty()) {
					channel.transmitMsg(msg, targets);
				}
			}
		}
	}

	public synchronized void updateListeners(Set updatedListeners) {
		synchronized (mapListenedLevels) {
			listeners.clear();
			if (null != updatedListeners) {
				listeners.addAll(updatedListeners);
			}
			
			mapListenedLevels.clear();
		}
	}
}
