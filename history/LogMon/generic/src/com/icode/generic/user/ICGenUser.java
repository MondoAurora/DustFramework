package com.icode.generic.user;

import java.util.Locale;

import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.ICGenUtils;
import com.icode.generic.base.*;
import com.icode.generic.msg.ICGenMsgDef;
import com.icode.generic.msg.ICGenMsgListener;

public class ICGenUser extends ICGenObjectDefault implements ICGenMsgListener, ICGenConstants {
	private static final String[] NOTIF_TYPES = new String[] { NOTIF_TYPE_MAIL, NOTIF_TYPE_SMS, NOTIF_TYPE_SKYPE };
	int[] notifLevels;
	Locale prefLocale;

	public ICGenUser() {
		super(USER_DEF);
	}
		
	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		super.loadDataFrom(node, hint);
		prefLocale = ICGenUtils.localeFromString(node.getOptional("prefLocale", null));
		
		String[] notLvl = ICGenUtils.str2arr(getAttrib(USER_REQ_NOTIF_LEVELS), ',');
		notifLevels = new int[NOTIF_TYPES.length];
		for (int i = NOTIF_TYPES.length; i-- > 0;) {
			notifLevels[i] = -1;
		}
		for (int i = notLvl.length; i-- > 0;) {
			notifLevels[i] = Byte.parseByte(notLvl[i]);
		}
	}

	public Locale getPreferredLocale() {
		return prefLocale;
	}

	public boolean isAccept(String channel, ICGenMsgDef msgDef) {
		boolean ret = false;
		int typeIdx = ICGenUtilsBase.indexOf(NOTIF_TYPES, channel);

		if (!ICGenUtils.isEmpty(getChannelAddress(channel))) {
			if (msgDef.getLevel() <= notifLevels[typeIdx]) {
				ret = true;
			}
		}
		
		return ret;
	}

	public String getChannelAddress(String channel) {
		int typeIdx = getDefinition().getAttIdx(channel);
		return getAttrib(typeIdx);
	}
}
