package ru.simplgroupp.util;

import java.util.List;

import ru.simplgroupp.rules.RuleMessage;

public class RuleUtil {
	
	/**
	 * выдает сообщения 
	 * @param source 
	 * @return
	 */
	public static String messagesToString(List<RuleMessage> source) {
		if (source.size() > 0) {
			String sres = "";
			for (RuleMessage rms: source) {
		    	sres = sres  + rms.getMessageText() + ". ";
			}
			return sres;
		} else {
			return null;
		}
	}
}
