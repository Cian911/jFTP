package com.redpois0n;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopupUtils {

	public static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public static JMenuItem getItem(String name, String icon, ActionListener listener) {
		JMenuItem item = new JMenuItem();
		
		item.setText(name);
		item.setIcon(Util.getIcon(icon));
		item.addActionListener(listener);
		
		return item;
	}

}
