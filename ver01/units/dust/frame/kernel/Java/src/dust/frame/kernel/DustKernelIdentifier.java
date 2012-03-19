package dust.frame.kernel;

import dust.shared.DustConsts.DustIdentifier;
import dust.shared.DustUtils;

public class DustKernelIdentifier implements DustIdentifier, Comparable<DustKernelIdentifier>, DustKernelConsts {	
	String typeId;	// the local id
	String ownerId;	// the local id
	String localId;	// the local id
	
	String path;
	String ref;
	
	public static DustKernelIdentifier fromString(String str) {
		int idx = str.indexOf(SEP_TYPE_START);
		str = str.substring(idx + 1);
		
		idx = str.indexOf(SEP_TYPE_END);
		
		String type = str.substring(0, idx);
		str = str.substring(idx + 1);
		
		idx = str.indexOf(SEP_LOCAL_SEP);
		
		String oId = str.substring(0, idx);
		str = str.substring(idx + 1);

		return new DustKernelIdentifier(type, DustUtils.isEmpty(oId) ? null : oId, str);
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
		return DustUtils.arr2str(elements, SEP_PATH_SEP);
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
	
	DustKernelIdentifier(DustIdentifier idType, String ownerId, String localId) {
		this(idType.asPath(), ownerId, localId);
	}
	
	DustKernelIdentifier(String typeId, String ownerId, String localId) {
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
	public String getName() {
		return localId;
	}
	
	@Override
	public int compareTo(DustKernelIdentifier o) {
		return ref.compareTo(o.ref);
	}
	
	@Override
	public String toString() {
		return ref;
	}
}
