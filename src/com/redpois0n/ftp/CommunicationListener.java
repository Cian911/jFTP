package com.redpois0n.ftp;

import it.sauronsoftware.ftp4j.FTPCommunicationListener;

import com.redpois0n.Frame;

public class CommunicationListener implements FTPCommunicationListener {

	public CommunicationListener(Connection connection) {
		
	}

	@Override
	public void received(String arg0) {
		if (arg0.toLowerCase().contains("zzz")) {
			return;
		}
		
		Frame.getInstance().getLog().reply(arg0);
	}

	@Override
	public void sent(String arg0) {
		if (arg0.toLowerCase().contains("noop")) {
			return;
		}
		
		if (arg0.startsWith("PASS")) {
			int pwdlen = arg0.length() - 5;
			if (pwdlen > 0) {
				String chars = "";
				for (int i = 0; i < pwdlen; i++) {
					chars += "*";
				}
				arg0 = "PASS " + chars;
			} else {
				arg0 = "PASS";
			}
		}
		
		Frame.getInstance().getLog().command(arg0);
	}

}
