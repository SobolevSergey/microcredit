package ru.simplgroupp.toolkit.common;

public class FileDataHolder {
	private String name;
	private String mimeType;
	private byte[] data;
	
	public String getName() {
		return name;
	}
	public String getMimeType() {
		return mimeType;
	}
	public byte[] getData() {
		return data;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
