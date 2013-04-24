package com.redpois0n.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.redpois0n.FileChanged;
import com.redpois0n.Frame;
import com.redpois0n.Main;

public class Connection implements Runnable {

	private final String host;
	private final String username;
	private final String password;
	private final int port;
	private FTPClient ftp;
	private final List<FTPAction> queue = new ArrayList<FTPAction>();
	private final List<FTPAction> preConnect = new ArrayList<FTPAction>();
	
	public Connection(String host, String username, String password, int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public static Connection connect(String host, String username, String password, int port) {
		Connection con = new Connection(host, username, password, port);
		new Thread(con).start();
		return con;
	}

	public FTPClient getClient() {
		return ftp;
	}

	public synchronized void addToQueue(FTPAction action) {
		queue.add(action);
	}
	
	public synchronized void addPreconnectAction(FTPAction action) {
		preConnect.add(action);
	}

	@Override
	public void run() {
		try {
			ftp = new FTPClient();
			
			for (FTPAction action : preConnect) {
				action.perform(ftp);
			}
			
			ftp.addCommunicationListener(new CommunicationListener(this));
			
			try {
				ftp.connect(host, port);
			} catch (Exception ex) {
				throw new FailedToConnectException(ex);
			}
			
			try {
				ftp.login(username, password);
			} catch (Exception ex) {
				throw new AuthenticationException(ex);
			}

			Frame.getInstance().getRemoteTable().setConnection(this);

			list(null, Frame.getInstance());
			
			Frame.getInstance().setTitle(Main.getTitle());

			while (ftp.isConnected()) {
				if (queue.size() == 0) {
					ftp.noop();
					Thread.sleep(250L);					
				} else {
					for (int i = 0; i < queue.size(); i++) {
						FTPAction action = queue.get(i);
						try {
							action.perform(ftp);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						queue.remove(i);
						i--;
					}
				}
			}
		} catch (AuthenticationException ex) {
			Frame.getInstance().getLog().error("Failed to login: " + ex.getMessage());
			ex.printStackTrace();
		} catch (FailedToConnectException ex) {
			Frame.getInstance().getLog().error("Failed to connect: " + ex.getMessage());
			ex.printStackTrace();
		} catch (FTPIllegalReplyException ex) {
			Frame.getInstance().getLog().error("Illegal reply: " + ex.getMessage());
			ex.printStackTrace();
		} catch (FTPException ex) {
			Frame.getInstance().getLog().error("Error: " + ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			Frame.getInstance().getLog().error(ex.getMessage());
			ex.printStackTrace();
		}
		
		Frame.getInstance().setTitle(Main.getTitle());
	}

	public void list(final String where, final FileChanged event) {
		addToQueue(new FTPAction() {
			@Override
			public void perform(FTPClient client) throws Exception {
				if (where != null && where.equals("..")) {
					client.changeDirectoryUp();
					event.remoteFileChanged(client.currentDirectory());
				}
				Frame.getInstance().getRemoteTable().browse(client.list(where), where);
			}
		});
	}

	public void delete(final String dir, final boolean folder) {
		addToQueue(new FTPAction() {
			@Override
			public void perform(FTPClient client) throws Exception {
				if (folder) {
					client.deleteDirectory(dir);
				} else {
					client.deleteFile(dir);
				}
			}
		});
	}

	public void mkdir(final String dir) {
		addToQueue(new FTPAction() {
			@Override
			public void perform(FTPClient client) throws Exception {
				client.createDirectory(dir);
			}
		});
	}

	public void download(final String name, final File loc, final Downloader downloader) {
		addToQueue(new FTPAction() {
			@Override
			public void perform(FTPClient client) throws Exception {
				client.download(name, loc, downloader);
			}
		});
	}

	public void rename(final String remote, final String dir) {

	}

	public void changeDir(final String string, final FileChanged event) {
		addToQueue(new FTPAction() {
			@Override
			public void perform(FTPClient client) throws Exception {
				if (!string.equals("..")) {
					client.changeDirectory(string);		
					event.remoteFileChanged(client.currentDirectory());
				}				
			}
		});
	}

}
