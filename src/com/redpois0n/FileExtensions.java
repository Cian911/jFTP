package com.redpois0n;

import java.io.File;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class FileExtensions {
	
	private static final HashMap<String, Icon> icons = new HashMap<String, Icon>();
	
	public static Icon get(String file) {
		return get(new File(file));
	}
	
	public static Icon get(File file) {
		if (file == null) {
			throw new NullPointerException();
		}
		
		Icon icon = null;
		
		String path = file.getAbsolutePath();
		String extension = null;
	
		if (file.getName().contains(".")) {
			extension = path.substring(path.lastIndexOf("."), path.length());
		} else {
			extension = "";
		}
		
		if (icons.containsKey(file.isDirectory() ? path : extension)) {
			return icons.get(file.isDirectory() ? path : extension);
		}
		
		try {
			if (file.isDirectory()) {
				icon = FileSystemView.getFileSystemView().getSystemIcon(file);
				
				icons.put(path, icon);
			} else {
				File temp = File.createTempFile("icon", extension);
				icon = FileSystemView.getFileSystemView().getSystemIcon(temp);
				temp.delete();
				
				icons.put(extension, icon);
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return icon;
	}
	
	public static Icon getFolder() {
		Icon icon = null;
		
		try {
			if (icons.containsKey("folder")) {
				icon = icons.get("folder");
			} else {
				File temp = new File(System.getProperty("java.io.tmpdir") + File.separator + "folder");
				temp.mkdirs();
				icon = FileSystemView.getFileSystemView().getSystemIcon(temp);
				temp.delete();
				icons.put("folder", icon);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return icon;
	}
	
	public static boolean isRoot(String str) {
		return isRoot(new File(str));
	}
	
	public static boolean isRoot(File root) {
		for (File file : File.listRoots()) {
			if (root.getAbsolutePath().equals(file.getAbsolutePath())) {
				return true;
			}
		}
		
		return false;
	}

	public static Icon getRoot(String string) {
		if (icons.containsKey(string)) {
			return icons.get(string);
		} else {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(new File(string));
			icons.put(string, icon);
			return icon;
		}
	}

}
