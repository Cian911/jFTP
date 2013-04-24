package com.redpois0n;

public enum OperatingSystem {

	WIN, UNIX;
	
	public static final OperatingSystem getOS() {
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.contains("win")) {
			return WIN;
		} else {
			return UNIX;
		}
	}
	
}
