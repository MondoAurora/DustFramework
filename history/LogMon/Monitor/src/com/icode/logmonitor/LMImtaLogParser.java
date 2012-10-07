package com.icode.logmonitor;

import java.text.*;
import java.util.Date;
import java.util.Locale;

import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.ICGenUtils;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;

public class LMImtaLogParser implements ICGenObject.Parser, ICGenConfigurable, LMConstants {
	public static final String MTA_DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss.SS";
	static final int DATE_PART_LENGTH = 23;
	static final int MINIMUM_LENGTH = 30;

	protected static final String[] NO_DEST_CHANNEL_STATUSES = new String[] { "R", "D", "J", "V", "K", "Q", "JA", "B", "VA", "Z", "DA", "BA" };

	static char separator = ' ';

	static String RFC_PREFIX = "rfc822;";

	SimpleDateFormat LOGDATEFORMAT = getDateFormat();

	String serverName;
	ICGenObject.ObDef targetDef;
	ICGenObjTranslator transTarget = new ICGenObjTranslator(MTA_FIELDS);

//	ICGenObjStringCvt exporter;

	protected static String removeUid(String address) throws ParseException {
		if (address != null) {
			// Some to addresses looks like this ...:uid@domain
			// The prefix must be cut off in these cases
			int sepIdx = address.indexOf(':');
			if (sepIdx >= 0) {
				address = address.substring(sepIdx + 1);
			}
		}

		return address;
	}

	private String getWord(String s, ParsePosition pos) throws ParseException {
		return ICGenUtils.getWord(s, separator, pos);
	}

	private void loadAtt(ICGenObject evt, int attIdx, String s, ParsePosition pos) throws ParseException {
		transTarget.setAttByIdx(evt, attIdx, getWord(s, pos));
	}

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(MTA_DATE_FORMAT, Locale.ENGLISH);
	}

	public ICGenObject parseObject(String s) throws Exception {
		if (MINIMUM_LENGTH > s.length()) {
			throw new ParseException(LINETOOSHORT, s.length());
		}

		Date d = LOGDATEFORMAT.parse(s.substring(0, DATE_PART_LENGTH));

		ICGenObject evt = new ICGenObjectDefault(targetDef);

		transTarget.setAttByIdx(evt, ATT_TIME, d);
		transTarget.setAttByIdx(evt, ATT_SRV, serverName);

		String str;
		ParsePosition position = new ParsePosition(DATE_PART_LENGTH);
		loadAtt(evt, ATT_CH_FROM, s, position);

		str = getWord(s, position);

		if (-1 == ICGenUtils.indexOf(NO_DEST_CHANNEL_STATUSES, str)) {
			transTarget.setAttByIdx(evt, ATT_CH_TO, str);
			loadAtt(evt, ATT_STATUS, s, position);
		} else {
			transTarget.setAttByIdx(evt, ATT_CH_TO, null);
			transTarget.setAttByIdx(evt, ATT_STATUS, str);
		}

		str = getWord(s, position);
		transTarget.setAttByIdx(evt, ATT_SIZE, new Long(str));

		// from
		String from = getWord(s, position);
		// to
		String to = getWord(s, position);
		String forwardedTo;

		// The original destination starts with "rfc...;"
		int sepIdx = to.indexOf(';');
		if (sepIdx > 0) {
			to = to.substring(sepIdx + 1);
			forwardedTo = getWord(s, position);
		} else {
			sepIdx = from.indexOf(';');
			if (sepIdx > 0) {
				// The log entry doesn't contains the origin of the
				// message, so the variable "from" contains the
				// destination now.
				forwardedTo = to;
				to = from.substring(sepIdx + 1);
				from = "";
			} else {
				forwardedTo = getWord(s, position);
			}
		}

		transTarget.setAttByIdx(evt, ATT_FROM, from);
		transTarget.setAttByIdx(evt, ATT_TO, removeUid(to));
		transTarget.setAttByIdx(evt, ATT_FWD_TO, removeUid(forwardedTo));

		loadAtt(evt, ATT_MSG_ID, s, position);

		int restStart = position.getIndex();

		str = getWord(s, position);

		if (str.startsWith("*")) {
			transTarget.setAttByIdx(evt, ATT_AUTH_USER, str.substring(1));
			restStart = position.getIndex();
		}

		str = s.substring(restStart).trim();
		transTarget.setAttByIdx(evt, ATT_REST, str);

		int IPstart = str.indexOf('[');
		if (IPstart > 0) {
			int IPend = str.indexOf(']', IPstart);
			if (IPend > IPstart) {
				transTarget.setAttByIdx(evt, ATT_IP, str.substring(IPstart + 1, IPend));
			}
		}

		return evt;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		serverName = config.getMandatory("serverName");

		ICGenTreeNode n = config.getChild("eventDef");
		targetDef = (ICGenObject.ObDef) ICAppFrame.getComponent(n, ICGenObject.ObDef.class);

//		n = config.getChild("exporter");
//		exporter = (ICGenObjStringCvt) ICAppFrame.getComponent(n, ICGenObjStringCvt.class);
	}
}
