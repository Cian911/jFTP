package com.redpois0n;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Updater {
	
	public static final String url = "http://redpois0n.com/misc/version/getversion.php?proj=jftp";
	
	public static final boolean isAvailable() {		
		System.out.println("Checking for updates: " + url);
		try {
			String ver = new BufferedReader(new InputStreamReader(new URL(url).openStream())).readLine();
			
			return !ver.equals(Main.getVersion());
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
