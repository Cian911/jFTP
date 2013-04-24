package com.redpois0n.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;

import com.redpois0n.Direction;
import com.redpois0n.Frame;

public class Uploader extends Transfer implements Runnable, FTPDataTransferListener {
	
	private String remoteDir;
	private File localFile;
	private int uploaded = 0;
	private FTPClient client;
	
	public Uploader(String remoteDir, File localFile) {
		this.remoteDir = remoteDir;
		this.localFile = localFile;
	}
	
	public String getRemoteFile() {
		return remoteDir;
	}
	
	public File getLocalFile() {
		return localFile;
	}

	public Direction getDirection() {
		return Direction.UPLOAD;
	}

	@Override
	public void aborted() {
		Frame.getInstance().getTransferTable().setValueAt(this, "Aborted", 1);
		exit();
	}

	@Override
	public void completed() {
		Frame.getInstance().getTransferTable().setValueAt(this, "Completed", 1);
		exit();
	}

	@Override
	public void failed() {
		Frame.getInstance().getTransferTable().setValueAt(this, "Failed", 1);
		exit();
	}

	@Override
	public void started() {
		Frame.getInstance().getTransferTable().setValueAt(this, "Uploading...", 1);
		start();
	}

	@Override
	public void transferred(int arg0) {
		uploaded += arg0;
		Frame.getInstance().getTransferTable().setValueAt(this, uploaded + "", 3);
	}

	@Override
	public void run() {
		try {
			client = new FTPClient();
			client.connect(Frame.getInstance().getHost(), Frame.getInstance().getPort());
			
			client.login(Frame.getInstance().getUsername(), Frame.getInstance().getPass());
			
			client.list();
			
			client.changeDirectory(remoteDir);

			client.upload(localFile, this);

			client.disconnect(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		try {
			client.abortCurrentDataTransfer(true);
			client.disconnect(true);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void exit() {
		Transfer.remove(this);
	}

	@Override
	public void start() {
		Transfer.add(this);
	}

}