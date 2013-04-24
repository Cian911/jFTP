package com.redpois0n.ftp;

import it.sauronsoftware.ftp4j.FTPClient;

public abstract class FTPAction {

	public abstract void perform(FTPClient client) throws Exception;
	
}
