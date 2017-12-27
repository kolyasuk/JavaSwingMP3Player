package gui;

import java.io.Serializable;

import utils.FileUtils;

public class Mp3 implements Serializable {
	private String name;
	private String path;

	public Mp3(String name, String path) {
		this.name = name;
		this.path = path;
	}

	@Override
	public String toString() {
		return FileUtils.getFileNameWithoutExtension(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
