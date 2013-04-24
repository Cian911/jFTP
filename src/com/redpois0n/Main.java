package com.redpois0n;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main {
	
	public static final String getVersion() {
		return "1.0";
	}
	
	public static final File getProfiles() {
		File file = new File("profiles/");
		file.mkdirs();
		return file;
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		Profiles.load();
		
		Frame frame = new Frame();
		frame.setVisible(true);
		
		if (Updater.isAvailable() && Dialogs.yesNo("A new update is available. Do you want to visit redpois0n.com to download it?", "Update available")) {	
			Util.openUrl("http://redpois0n.com");
		}
	}
	
	public static void exit() {
		if (Frame.getConnection() != null && Frame.getConnection().getClient() != null && Frame.getConnection().getClient().isConnected()) {
			int response = JOptionPane.showConfirmDialog(null, "You are still connected to " + Frame.getConnection().getClient().getHost() + ". Do you want to exit?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if (response == JOptionPane.YES_OPTION) {
				try { Frame.getConnection().getClient().disconnect(true); } catch (Exception ex) { };
			} else {
				return;
			}
		}
		
		Profiles.save();
		
		System.exit(0);
	}

	public static String getTitle() {
		String title = "jFTP " + Main.getVersion();
		
		if (Frame.getConnection() != null && Frame.getConnection().getClient() != null && Frame.getConnection().getClient().isConnected()) {
			title += " - " + Frame.getConnection().getClient().getUsername() + "@" + Frame.getConnection().getClient().getHost();
		}
		
		return title;
	}

}
