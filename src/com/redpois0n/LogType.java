package com.redpois0n;

public enum LogType {

	ERROR("Error"), REPLY("Reply"), COMMAND("Command"), STATUS("Status");

	private String text;
	
	private LogType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
