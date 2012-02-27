package org.mondoaurora.frame.kernel;

import org.mondoaurora.frame.shared.MAFIdentifier;
import org.mondoaurora.frame.shared.MAFUtils;

public class MAFKernelIdentifier implements MAFIdentifier, Comparable<MAFKernelIdentifier>, MAFKernelConsts {	
	String typeId;	// the local id
	String ownerId;	// the local id
	String localId;	// the local id
	
	String path;
	String ref;
	
	public static MAFKernelIdentifier fromString(String str) {
		int idx = str.indexOf(SEP_TYPE_START);
		str = str.substring(idx + 1);
		
		idx = str.indexOf(SEP_TYPE_END);
		
		String type = str.substring(0, idx);
		str = str.substring(idx + 1);
		
		idx = str.indexOf(SEP_LOCAL_SEP);
		
		String oId = str.substring(0, idx);
		str = str.substring(idx + 1);

		return new MAFKernelIdentifier(type, MAFUtils.isEmpty(oId) ? null : oId, str);
	}
	
	public static String buildRef(String typeId, String ownerId, String localId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(SEP_TYPE_START);
		sb.append(typeId);
		sb.append(SEP_TYPE_END);
		
		if ( null != ownerId ) {
			sb.append(ownerId);
		}
		sb.append(SEP_LOCAL_SEP);

		sb.append(localId);
		return sb.toString();
	}
	
	public static String buildPath(String[] elements) {
		return MAFUtils.arr2str(elements, SEP_PATH_SEP);
	}
	
	public static String buildPath(String ownerId, String localId) {
		StringBuilder sb = new StringBuilder();
		
		if ( null != ownerId ) {
			sb.append(ownerId);
		}
		sb.append(SEP_PATH_SEP);
		sb.append(localId);
		
		return sb.toString();
	}
	
	MAFKernelIdentifier(MAFIdentifier idType, String ownerId, String localId) {
		this(idType.asPath(), ownerId, localId);
	}
	
	MAFKernelIdentifier(String typeId, String ownerId, String localId) {
		this.typeId = typeId;
		this.ownerId = ownerId;
		this.localId = localId;
		
		update();
	}
	
	void update() {
		path = buildPath(ownerId, localId);
		ref = buildRef(typeId, ownerId, localId);
	}
	
	String getType() {
		return typeId;
	}
	
	@Override
	public String asPath() {
		return path;
	}
	
	@Override
	public String asReference() {
		return ref;
	}
	
	@Override
	public int compareTo(MAFKernelIdentifier o) {
		return ref.compareTo(o.ref);
	}
	
	@Override
	public String toString() {
		return ref;
	}
}
