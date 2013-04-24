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
import com.redpois0n.ftp.Connection;
import com.redpois0n.ftp.Downloader;
import com.redpois0n.models.FileTableModel;
import com.redpois0n.renderers.RemoteFileTableRenderer;

@SuppressWarnings("serial")
public class RemoteFileJTable extends AbstractFileJTable {

	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	public RemoteFileJTable(FileChanged fc) {
		super(fc);
		super.setDefaultRenderer(Object.class, new RemoteFileTableRenderer());
	}

	public RemoteFileJTable(FileTableModel model, FileChanged fc) {
		super(model, fc);
		super.setDefaultRenderer(Object.class, new RemoteFileTableRenderer());
	}

	@Override
	public void onClick() {
		if (con == null) {
			return;
		}

		int row = super.getSelectedRow();

		if (row != -1) {
			Object obj = getModel().getValueAt(row, 0);

			if (obj instanceof FTPFile) {
				FTPFile value = (FTPFile) obj;

				if (value.getType() == FTPFile.TYPE_DIRECTORY) {
					try {
						con.list(value.getName(), super.getFileChangedEvent());
						con.changeDir(value.getName(), super.getFileChangedEvent());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (obj.toString().equals("..")) {			
				
				con.changeDir("..", super.getFileChangedEvent());
				con.list("..", super.getFileChangedEvent());
			}

		}
	}

	public void browse(FTPFile[] files, String parent) {
		clear();

		getModel().addRow(new Object[] { ".." });

		List<Object[]> foldersList = new ArrayList<Object[]>();
		List<Object[]> filesList = new ArrayList<Object[]>();

		for (int i = 0; i < files.length; i++) {
			FTPFile file = files[i];

			boolean isDirectory = file.getType() == FTPFile.TYPE_DIRECTORY;

			if (isDirectory) {
				foldersList.add(new Object[] { file, "", FileExtensions.get(file.getName()).toString(), "" });
			} else {
				filesList.add(new Object[] { file, (file.getSize() / 1024L) + " kB", FileExtensions.get(file.getName()).toString(), file.getModifiedDate() });
			}
		}

		for (Object[] obj : foldersList) {
			getModel().addRow(obj);
		}

		for (Object[] obj : filesList) {
			getModel().addRow(obj);
		}

	}

	@Override
	public JPopupMenu getMenu() {
		JPopupMenu menu = super.getDefaultMenu();

		menu.addSeparator();

		menu.add(PopupUtils.getItem("Download", "download", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				download();
			}
		}));

		return menu;
	}

	@Override
	public void reload() {
		con.list(null, super.getFileChangedEvent());
	}

	@Override
	public void makeDir() {
		String dir = Dialogs.showInput("Input name of new folder", "New Folder");

		if (dir == null || dir != null && dir.length() == 0) {
			return;
		}

		con.mkdir(dir);

		reload();
	}

	@Override
	public void delete() {
		int rows[] = super.getSelectedRows();

		if (rows.length > 0 && Dialogs.yesNo("Are you sure you want to delete " + rows.length + " files", "Delete")) {
			for (int row : rows) {
				Object obj = getModel().getValueAt(row, 0);

				if (obj instanceof FTPFile) {
					FTPFile file = (FTPFile) obj;
					con.delete(file.getName(), file.getType() == FTPFile.TYPE_DIRECTORY);
					reload();
				}
			}
		}
	}

	public void download() {
		int[] rows = super.getSelectedRows();

		if (rows.length > 0) {
			for (int row : rows) {
				try {
					Object obj = getModel().getValueAt(row, 0);

					if (obj instanceof FTPFile) {
						FTPFile file = (FTPFile) obj;

						File loc = new File(Frame.getInstance().getLocalPath() + File.separator + file.getName());

						String remoteloc = file.getName();

						Downloader downloader = new Downloader(Frame.getConnection().getClient().currentDirectory(), remoteloc, loc);

						Frame.getInstance().getTransferTable().addNew(downloader, file);

						new Thread(downloader, "Download: " + remoteloc).start();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public void rename() {
		int row = super.getSelectedRow();

		if (row != -1) {
			Object obj = getModel().getValueAt(row, 0);

			if (obj instanceof FTPFile) {
				FTPFile file = (FTPFile) obj;

				String remote = file.getName();

				String dir = Dialogs.showInput("Input new name of file", "Rename");

				if (dir == null || dir != null && dir.length() == 0) {
					return;
				}

				con.rename(remote, dir);
			}
		}
	}

}
