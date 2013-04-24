package com.redpois0n;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.Serializable;

import com.redpois0n.ftp.Connection;
import com.redpois0n.ftp.FTPAction;

public class Profile implements Serializable {

	private static final long serialVersionUID = -4786457357020039264L;

	private String name;
	
	private String host;
	private int port;
	private String username;
	private String password;
	private String comment;
	private String security;
	
	public Profile(String name) {
		this.name = name;
	}

	public static final Connection connect(Profile p) {
		Connection con = new Connection(p.host, p.username, p.password, p.port);
		
		con.addPreconnectAction(new FTPAction() {
			public void perform(FTPClient client) {
				
			}
		});
		
		new Thread(con).start();
		return con;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
