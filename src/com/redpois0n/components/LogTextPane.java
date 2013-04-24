package com.redpois0n.components;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.redpois0n.LogType;

@SuppressWarnings("serial")
public class LogTextPane extends JTextPane {

	public void command(String s) {
		this.log(LogType.COMMAND, s, new Color(0, 0, 150));
	}
	
	public void reply(String s) {
		this.log(LogType.REPLY, s, new Color(34, 139, 34));
	}

	public void error(String s) {
		this.log(LogType.ERROR, s, Color.RED.darker());
	}
	
	public void status(String s) {
		this.log(LogType.STATUS, s, Color.BLACK);
	}
	
	public void log(LogType log, String s, Color c) {
		try {
			Document doc = super.getDocument();
			
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet set = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
			
			doc.insertString(doc.getLength(), log.toString() + "\t" + s + "\n\r", set);
			
			super.setSelectionEnd(super.getText().length());
			super.setSelectionStart(super.getText().length());		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
