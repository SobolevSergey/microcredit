package ru.simplgroupp.transfer;

import java.io.UnsupportedEncodingException;

public class ReportGenerated {
	private byte[] data;
	private String mimeType;
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public String getAsString() {
		if (data == null) {
			return null;
		} else {
			try {
				return new String(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return new String(data);
			}
		}
	}
	
	public void setAsString(String avalue) {
		if (avalue == null) {
			data = null;
		} else {
			try {
				data = avalue.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				data = avalue.getBytes();
			}
		}
	}
}
