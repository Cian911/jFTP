package com.redpois0n.components;

import it.sauronsoftware.ftp4j.FTPFile;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.redpois0n.Direction;
import com.redpois0n.ftp.Transfer;
import com.redpois0n.renderers.TransferTableRenderer;

@SuppressWarnings("serial")
public class TransferJTable extends JTable {
	
	public TransferJTable() {
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], new Object[] { "Local file", "Direction", "Remote file", "Progress", "Size"});
		super.setModel(model);
		super.setDefaultRenderer(Object.class, new TransferTableRenderer());
	}
	
	public DefaultTableModel getModel() {
		return (DefaultTableModel) super.getModel();
	}
	
	public void addNew(Transfer transferer, FTPFile file) {
		getModel().insertRow(0, new Object[] { transferer.getLocalFile().getAbsoluteFile(), transferer.getDirection() == Direction.DOWNLOAD ? "Downloading" : "Uploading", transferer.getRemoteFile(), "0", (file.getSize() / 1024L) + " kB"});
	}
	
	public void setValueAt(Transfer dl, Object value, int column) {
		int row = -1;
		
		for (int i = 0; i < getModel().getRowCount(); i++) {
			if (getModel().getValueAt(i, 0).toString().equalsIgnoreCase(dl.getLocalFile().getAbsolutePath()) && getModel().getValueAt(i, 2).toString().equalsIgnoreCase(dl.getRemoteFile())) {
				row = i;
				break;
			}
		}
		
		super.setValueAt(value, row, column);
		
	}
	
	public void clear() {
		while (getModel().getRowCount() > 0) {
			getModel().removeRow(0);
		}
	}

}
