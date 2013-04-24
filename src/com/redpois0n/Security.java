package com.redpois0n;

public enum Security {

	FTP, FTPS, FTPES;
	
	public static final Security fromString(String string) {
		if (string.equalsIgnoreCase("normal")) {
			return FTP;
		} else if (string.equalsIgnoreCase("ftps")) {
			return FTPS;
		} else if (string.equalsIgnoreCase("ftpes")) {
			return FTPES;
		} else {
			return null;
		}
	}
}
