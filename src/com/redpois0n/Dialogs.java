package com.redpois0n;

import javax.swing.JOptionPane;

public class Dialogs {
	
	public static boolean yesNo(String msg, String title) {
		return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}
	
	public static String showInput(String msg, String title) {
		return JOptionPane.showInputDialog(null, msg, title, JOptionPane.QUESTION_MESSAGE);
	}

}
