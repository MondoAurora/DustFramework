package org.mondoaurora.frame.shared;

import org.mondoaurora.frame.shared.MAFVariant.VariantSetMode;

public interface MAFConnector {
	MAFIdentifier getObTypeID();

	MAFConnector getNeighbor(MAFIdentifier typeId, String[] fields);
	boolean send(MAFConnector data, boolean waitMore) throws Exception;
	
	boolean isNull(int idx);
	
	MAFVariant getValue(int idx);
	void getValues(MAFVariant[] fields);
	
	void setValue(int idx, MAFVariant from);
	void setData(int idx, Object value, VariantSetMode mode, String key);
	void setData(int idx, Object value);
}
