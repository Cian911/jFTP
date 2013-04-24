package com.redpois0n.ftp;

@SuppressWarnings("serial")
public class FailedToConnectException extends Exception {

	public FailedToConnectException() {

	}

	public FailedToConnectException(String arg0) {
		super(arg0);
	}

	public FailedToConnectException(Throwable arg0) {
		super(arg0);
	}

	public FailedToConnectException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FailedToConnectException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
