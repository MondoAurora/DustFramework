package dust.api.wrappers;

import java.util.Date;

public final class DustDateImmutable {
	public final long content;
	
	public DustDateImmutable(long date) {
		this.content = date;
	}
	
	public DustDateImmutable(Date date) {
		this.content = date.getTime();
	}
	
	public Date getDate() {
		return new Date(content);
	}
}
