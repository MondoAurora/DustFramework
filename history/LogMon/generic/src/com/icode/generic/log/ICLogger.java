package com.icode.generic.log;

import java.util.*;

import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenConfigurable;
import com.icode.generic.base.ICGenTreeNode;

public class ICLogger implements ICGenConfigurable {
	private static final String CHANNELS = "channels";

	Set channels = new HashSet();

	public void log(byte level, String from, String message, Object param) {
		log(new ICLogMessage(level, from, message, param));
	}

	public void log(ICLogMessage logMsg) {
		synchronized (channels) {
			for (Iterator it = channels.iterator(); it.hasNext();) {
				((ICLogChannel) it.next()).processLog(logMsg);
			}
		}
	}

	public void addChannel(ICLogChannel channel) {
		synchronized (channels) {
			channels.add(channel);
		}
	}

	public void removeChannel(ICLogChannel channel) {
		synchronized (channels) {
			channels.remove(channel);
		}
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		ICGenTreeNode chnNode = config.getChild(CHANNELS);
		if (null != chnNode) {
			for (Iterator it = chnNode.getChildren(); it.hasNext();) {
				channels.add(ICAppFrame.getComponent((ICGenTreeNode) it.next(), ICLogChannel.class));
			}
		}
	}
}
