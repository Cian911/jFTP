package com.redpois0n.renderers;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.redpois0n.Util;

@SuppressWarnings("serial")
public class ProfilesTreeRenderer extends DefaultTreeCellRenderer {
	
	public static final ImageIcon ICON_GROUP = Util.getIcon("group");
	public static final ImageIcon ICON_SERVER = Util.getIcon("server_network");
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel lbl = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		if (value.toString().equalsIgnoreCase("My profiles")) {
			lbl.setIcon(ICON_GROUP);
		} else {
			lbl.setIcon(ICON_SERVER);
		}
		
		return lbl;
	}
}
