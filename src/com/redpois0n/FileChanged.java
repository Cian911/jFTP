package com.redpois0n;

import java.io.File;

public abstract interface FileChanged {
	
	public abstract void localFileChanged(File file);
	
	public abstract void remoteFileChanged(String file);

}
