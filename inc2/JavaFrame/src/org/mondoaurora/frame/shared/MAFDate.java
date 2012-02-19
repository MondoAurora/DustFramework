package org.mondoaurora.frame.shared;

import java.util.Date;

public class MAFDate {
	protected Date date;
	
	public MAFDate(Date date_) {
		this.date = new Date(date_.getTime());
	}

	public Date getDate(Date into) {
		if ( null == into ) {
			return new Date(date.getTime());
		} else {
			into.setTime(date.getTime());
			return into;
		}
	}
}
