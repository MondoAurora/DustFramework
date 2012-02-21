package org.mondoaurora.frame.shared;

public class MAFRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MAFRuntimeException(String component, String message, Exception source) {
		super("MAF Exception at " + component + ": " + message, source);
	}

	public static class InvalidFieldType extends MAFRuntimeException {
		private static final long serialVersionUID = 1L;

		public InvalidFieldType(String info) {
			super("KernelDataAccess", "Invalid field type " + info, null);
		}
	};
	
}
