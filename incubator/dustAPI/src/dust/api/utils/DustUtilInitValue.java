package dust.api.utils;

import dust.api.DustConstants;
import dust.api.components.DustEntity;

public class DustUtilInitValue implements DustConstants {
	public final String key;
	public final DustEntity value;
	
	public DustUtilInitValue(String key, DustEntity value) {
		this.key = key;
		this.value = value;
	}
	
	public DustUtilInitValue(DustEntity value) {
		this.key = null;
		this.value = value;
	}
}
