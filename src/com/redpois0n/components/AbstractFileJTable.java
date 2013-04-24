package com.redpois0n.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.FileChanged;
import com.redpois0n.PopupUtils;
import com.redpois0n.models.FileTableModel;

@SuppressWarnings("serial")
public abstract class AbstractFileJTable extends JTable implements FileChanged {
	
	private FileChanged fc;

	public AbstractFileJTable(FileChanged fc) {
		super(new FileTableModel(new Object[] { "File", "File size", "File type", "Last modified" }));
		setup(fc);
	}
	
	public AbstractFileJTable(FileTableModel model, FileChanged fc) {
		super(model);
		setup(fc);
	}
	
	public abstract void onClick();
	
	public abstract JPopupMenu getMenu();
	
	public void setup(FileChanged fc) {
		super.setRowHeight(20);
		super.getTableHeader().setReorderingAllowed(false);
		this.fc = fc;
		PopupUtils.addPopup(this, getMenu());
		
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					onClick();
				}
			}
		});
	}
	
	public FileChanged getFileChangedEvent() {
		return fc;
	}
	
	public DefaultTableModel getModel() {
		return (DefaultTableModel) super.getModel();
	}
	
	public void clear() {
		while (getModel().getRowCount() > 0) {
			getModel().removeRow(0);
		}
	}

	@Override
	public void localFileChanged(File file) {
		if (fc != null) {
			fc.localFileChanged(file);
		}
	}

	@Override
	public void remoteFileChanged(String file) {
		if (fc != null) {
			fc.remoteFileChanged(file);
		}
	}
	
	public abstract void reload();
	
	public abstract void makeDir();
	
	public abstract void delete();
	
	public abstract void rename();
	
	public Component[] getToolbar() {
		JPopupMenu menu = getMenu();
		
		List<Component> components = new ArrayList<Component>();
		
		for (Component component : menu.getComponents()) {
			if (component instanceof JMenuItem) {
				JMenuItem item = (JMenuItem)component;
				
				JButton button = new JButton();
				button.setToolTipText(item.getText());
				button.setIcon(item.getIcon());
				button.addActionListener(item.getActionListeners()[0]);
				
				components.add(button);
			}
		}
		
		return components.toArray(new Component[0]);
	}
	
	public JPopupMenu getDefaultMenu() {
		JPopupMenu menu = new JPopupMenu();	
		
		menu.add(PopupUtils.getItem("Update", "update", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}	
		}));
		
		menu.add(PopupUtils.getItem("New folder", "new_folder", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeDir();
			}	
		}));
		
		menu.add(PopupUtils.getItem("Delete", "delete", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}	
		}));
		
		menu.add(PopupUtils.getItem("Rename", "rename", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rename();
			}	
		}));
		
		return menu;
	}
	

}
