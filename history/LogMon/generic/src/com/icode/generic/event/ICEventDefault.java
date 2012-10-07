package com.icode.generic.event;

import com.icode.generic.ICGenObjectDefault;

public class ICEventDefault extends ICGenObjectDefault implements ICEvent {
	final long timestamp;
	final byte level;
	final String type;

	protected ICEventDefault(ObDef def, long timestamp, byte level, String type) {
		super(def);		
		this.timestamp = timestamp;
		this.level = level;
		this.type = type;
	}

	public long getTimeMsec() {
		return timestamp;
	}

	public byte getLevel() {
		return level;
	}

	public String getType() {
		return type;
	}

}
