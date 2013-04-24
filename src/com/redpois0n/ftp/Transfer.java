package com.redpois0n.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.redpois0n.Direction;

public abstract class Transfer {
	
	private static final List<Transfer> list = new ArrayList<Transfer>();
	
	public static final Transfer[] toArray() {
		return list.toArray(new Transfer[0]);
	}
	
	public static final int size() {
		return list.size();
	}
	
	public static final void add(Transfer transfer) {
		list.add(transfer);
	}
	
	public static final Transfer get(int i) {
		return list.get(i);
	}
	
	public static final void remove(Transfer transfer) {
		list.remove(transfer);
	}
	
	public abstract String getRemoteFile();
	
	public abstract File getLocalFile();

	public abstract Direction getDirection();
	
	public abstract void cancel();
	
	public abstract void exit();
	
	public abstract void start();
	
	public boolean equals(Object obj) {
		if (obj instanceof Transfer) {
			Transfer t = (Transfer)obj;
			
			return t.getRemoteFile().equals(this.getRemoteFile()) && t.getLocalFile().getAbsolutePath().equals(this.getLocalFile().getAbsolutePath());
		} else {
			return false;
		}
	}

}
