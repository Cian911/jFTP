package com.redpois0n.renderers;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.redpois0n.FileExtensions;

@SuppressWarnings("serial")
public class LocalFileTableRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (column == 0 && value != null) {
			File file = new File(value.toString());
			
			if (!FileExtensions.isRoot(file)) {
				lbl.setIcon(FileExtensions.get(value.toString()));
				lbl.setText(file.getName());
			} else {
				lbl.setIcon(FileExtensions.getRoot(value.toString()));
			}
		} else {
			lbl.setIcon(null);
		}
		
		return lbl;
	}

}
