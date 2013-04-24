package com.redpois0n;

import java.awt.Desktop;
import java.net.URI;

import javax.swing.ImageIcon;

public class Util {
	
	public static ImageIcon getIcon(String name) {
		return new ImageIcon(Main.class.getResource("/com/redpois0n/icons/" + name + ".png"));
	}

	public static void openUrl(String string) {
		try {
			Desktop.getDesktop().browse(new URI(string));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
