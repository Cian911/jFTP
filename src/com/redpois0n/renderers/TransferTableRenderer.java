package com.redpois0n.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class TransferTableRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (column == 3) {
			JProgressBar bar = new JProgressBar();
			if (value.toString().equals("100%")) {
				bar.setValue(100);	
			} else {
				bar.setMaximum(Integer.parseInt(table.getValueAt(row, 4).toString().split(" ")[0]) * 1024);
				bar.setValue(Integer.parseInt(value.toString()));	
			}
			return bar;
		} else if (column == 1) {
			String status = value.toString();
			
			if (status.equals("Aborted") || status.equals("Failed") || status.startsWith("Error: ")) {
				lbl.setForeground(Color.red.darker());
			} else if (status.equals("Completed")) {
				lbl.setForeground(Color.green.darker());
			} else {
				lbl.setForeground(Color.black);
			}
		} else {
			lbl.setForeground(Color.black);
		}
		
		return lbl;
	}

}
