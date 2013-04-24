package com.redpois0n.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;

public final class Separator {

	public static final Component getInvisibleSeparator() {
		return new JLabel("   ");
	}
	
	public static final Dimension getSeparator() {
		return new Dimension(15, 25);
	}

}
