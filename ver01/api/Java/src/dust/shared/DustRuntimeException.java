package dust.shared;

public class DustRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DustRuntimeException(String component, String message, Exception source) {
		super("Dust exception at " + component + ": " + message, source);
	}

	public DustRuntimeException(String component, String message) {
		this(component, message, null);
	}	
}
