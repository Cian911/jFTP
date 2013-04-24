package com.redpois0n.components;

import it.sauronsoftware.ftp4j.FTPFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import com.redpois0n.Dialogs;
import com.redpois0n.FileChanged;
import com.redpois0n.FileExtensions;
import com.redpois0n.Frame;
import com.redpois0n.PopupUtils;
import com.redpois0n.ftp.Uploader;
import com.redpois0n.models.FileTableModel;
import com.redpois0n.renderers.LocalFileTableRenderer;

@SuppressWarnings("serial")
public class LocalFileJTable extends AbstractFileJTable {

	private File parentFile;

	public LocalFileJTable(FileChanged fc) {
		super(fc);
		super.setDefaultRenderer(Object.class, new LocalFileTableRenderer());
	}

	public LocalFileJTable(FileTableModel model, FileChanged fc) {
		super(model, fc);
		super.setDefaultRenderer(Object.class, new LocalFileTableRenderer());
	}

	public void loadRoots() {
		browse(null, File.listRoots());
	}

	public void browse(String file) {
		browse(new File(file));
	}

	public void browse(File dir) {
		browse(dir, dir.listFiles());
		getModel().insertRow(0, new Object[] { ".." });
	}

	public void browse(File parent, File[] files) {
		clear();

		List<Object[]> foldersList = new ArrayList<Object[]>();
		List<Object[]> filesList = new ArrayList<Object[]>();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];

				if (file.isDirectory()) {
					foldersList.add(new Object[] { file.getAbsolutePath(), "", FileExtensions.get(file).toString(), "" });
				} else {
					filesList.add(new Object[] { file.getAbsolutePath(), (file.length() / 1024L) + " kB", FileExtensions.get(file).toString(), file.lastModified() });
				}
			}
		}

		for (Object[] obj : foldersList) {
			getModel().addRow(obj);
		}

		for (Object[] obj : filesList) {
			getModel().addRow(obj);
		}

		if (parent != null) {
			this.parentFile = parent;
			super.getFileChangedEvent().localFileChanged(parent);
		}
	}

	@Override
	public void onClick() {
		int row = super.getSelectedRow();

		if (row != -1) {
			String value = getModel().getValueAt(row, 0).toString();

			if (value.equals("..") && this.parentFile != null) {
				if (FileExtensions.isRoot(this.parentFile)) {
					loadRoots();
				} else if (this.parentFile != null && this.parentFile.getParentFile() != null) {
					browse(this.parentFile.getParentFile());
				}
			} else if (new File(value).isDirectory()) {
				browse(value);
			}
		}
	}

	@Override
	public JPopupMenu getMenu() {
		JPopupMenu menu = super.getDefaultMenu();
		
		menu.addSeparator();

		menu.add(PopupUtils.getItem("Upload", "upload", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				upload();
			}
		}));

		return menu;
	}

	public void upload() {
		int[] rows = super.getSelectedRows();

		if (rows.length > 0) {
			for (int row : rows) {
				try {
					String str = getModel().getValueAt(row, 0).toString();
					File file = new File(str);

					String remoteloc = file.getName();

					Frame.getInstance();
					Uploader uploader = new Uploader(Frame.getConnection().getClient().currentDirectory(), file);

					FTPFile remote = new FTPFile();
					remote.setName(file.getName());
					remote.setSize(file.length());
					
					Frame.getInstance().getTransferTable().addNew(uploader, remote);

					new Thread(uploader, "Upload: " + remoteloc).start();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}

	@Override
	public void reload() {
		browse(parentFile);
	}

	@Override
	public void makeDir() {
		String dir = Dialogs.showInput("Input name of new folder", "New Folder");

		if (dir == null || dir != null && dir.length() == 0) {
			return;
		}

		File file = new File(parentFile, dir);
		file.mkdirs();
		reload();
	}

	@Override
	public void delete() {
		int[] rows = super.getSelectedRows();
		
		if (rows.length > 0 && Dialogs.yesNo("Are you sure you want to delete " + rows.length + " files", "Delete")) {
			for (int row : rows) {
				String value = getModel().getValueAt(row, 0).toString();

				if (!value.equals("..")) {
					new File(value).delete();
					reload();
				}
			}
		}
		
	}

	@Override
	public void rename() {
		int row = super.getSelectedRow();
		
		if (row != -1) {
			File file = new File(super.getValueAt(row, 0).toString());
			
			String dir = Dialogs.showInput("Input new name of file", "Rename");

			if (dir == null || dir != null && dir.length() == 0) {
				return;
			}

			File newFile = new File(file.getParentFile(), dir);
			file.renameTo(newFile);
			reload();
		}		
	}

}
