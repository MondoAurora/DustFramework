package com.icode.generic.event;

import com.icode.generic.base.ICGenObject;

public interface ICEvent extends ICGenObject {
	public static interface Source {
		long getLastProcessedMsec();
		void setLastProcessedMsec(long ts);
	};

	public static interface Collector {
		public void addSource(Source source);
		public void removeSource(Source source);
			
		public boolean processEvent(Source source, ICEvent info) throws Exception;
	}

	public static interface Parser {
		public ICEvent parseEvent(String str) throws Exception;
	}

	long getTimeMsec();
	String getType();
	byte getLevel();
	
	public static class Util {
		private static final String[] TEMP_LEVEL_NAME_RESOLVER = new String[] {
			"Fatal", "Error", "Alert", "Warning", "Info", "Debug", "DebugFinest"
		};

		public static String levelToString(byte level) {
			return TEMP_LEVEL_NAME_RESOLVER[level];
		}
	}
}
