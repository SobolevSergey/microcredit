package org.admnkz.common;

import javax.ejb.Local;

@Local
public interface IOptions {
//	public String getOptionsPath();
//	public void setOptionsPath(String optionsPath);
	public String getValue(String key);
}
