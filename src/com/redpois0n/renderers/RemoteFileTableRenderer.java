package com.redpois0n.renderers;

import it.sauronsoftware.ftp4j.FTPFile;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.redpois0n.FileExtensions;

@SuppressWarnings("serial")
public class RemoteFileTableRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (column == 0 && value != null && value instanceof FTPFile) {
			FTPFile file = (FTPFile)value;
			
			if (file.getType() == FTPFile.TYPE_DIRECTORY) {
				lbl.setIcon(FileExtensions.getFolder());
			} else {
				lbl.setIcon(FileExtensions.get(file.getName()));
			}
			lbl.setText(file.getName());
			
		} else {
			lbl.setIcon(null);
		}
		
		return lbl;
	}

}

