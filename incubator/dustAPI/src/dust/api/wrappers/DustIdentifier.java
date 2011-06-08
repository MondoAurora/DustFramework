package dust.api.wrappers;

public class DustIdentifier {
	private final String id;

	public DustIdentifier(String id) {
		this.id = id;
	}
	
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DustIdentifier) {
			return id.equals(((DustIdentifier) obj).id);
		}
		return super.equals(obj);
	}
	
	
}
