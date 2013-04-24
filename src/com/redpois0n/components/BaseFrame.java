package com.redpois0n.components;

import javax.swing.JFrame;

import com.redpois0n.OperatingSystem;

@SuppressWarnings("serial")
public abstract class BaseFrame extends JFrame {

	@Override
	public void setVisible(boolean b) {
		if (OperatingSystem.getOS() == OperatingSystem.UNIX) {
			super.pack();
		}
		super.setVisible(b);
	}

}
