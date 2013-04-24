package com.redpois0n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.redpois0n.io.Base64InputStream;
import com.redpois0n.io.Base64OutputStream;

public class Profiles {
	
	private static HashMap<String, Profile> profiles = new HashMap<String, Profile>();	
	
	public static HashMap<String, Profile> getProfiles() {
		return profiles;
	}
	
	public static void addProfile(String name, Profile profile) {
		profiles.remove(name);
		profiles.put(name, profile);
	}
	
	public static void remove(String name) {
		profiles.remove(name);
	}
	
	public static void load() {
		try {
			File dir = Main.getProfiles();
			for (File file : dir.listFiles()) {
				if (file.getName().endsWith(".ftp")) {
					ObjectInputStream ois = new ObjectInputStream(new Base64InputStream(new FileInputStream(file)));
					
					Profile profile = (Profile) ois.readObject();
					
					profiles.put(profile.getName(), profile);
					
					ois.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void save() {
		try {
			for (String key : profiles.keySet()) {
				Profile profile = profiles.get(key);
				
				ObjectOutputStream out = new ObjectOutputStream(new Base64OutputStream(new FileOutputStream(new File(Main.getProfiles(), key + ".ftp"))));
				
				out.writeObject(profile);
				
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
