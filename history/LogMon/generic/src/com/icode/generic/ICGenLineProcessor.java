package com.icode.generic;

public interface ICGenLineProcessor {
	public Object processLine(String line, boolean partial) throws Exception;
	public boolean isPartialAccepted();
	
	public static abstract class WholeLine implements ICGenLineProcessor {
		public abstract Object processLine(String line) throws Exception;

		public Object processLine(String line, boolean partial) throws Exception {
			return processLine(line);
		}

		public boolean isPartialAccepted() {
			return false;
		}
		
	}

	
	public static class Console extends WholeLine {
		public Object processLine(String line) throws Exception {
			System.out.println(line);
			return null;
		}
		
	}
}
