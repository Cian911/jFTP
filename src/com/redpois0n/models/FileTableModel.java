package com.redpois0n.models;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class FileTableModel extends DefaultTableModel {

	public FileTableModel(Object[] columns) {
		super(new Object[0][0], columns);
	}
	
	@Override
	public boolean isCellEditable(int i, int i1) {
		return false;
	}
}
