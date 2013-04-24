package com.redpois0n.components;

import javax.swing.JDialog;

import com.redpois0n.OperatingSystem;

@SuppressWarnings("serial")
public abstract class BaseDialog extends JDialog {
	
	@Override
	public void setVisible(boolean b) {
		if (OperatingSystem.getOS() == OperatingSystem.UNIX) {
			super.pack();
		}
		super.setVisible(b);
	}

}
