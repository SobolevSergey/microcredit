package ru.simplgroupp.util;

import java.util.List;

import ru.simplgroupp.transfer.EventCode;

public class EventLogUtils {

	public static int find(List<EventCode> source, int eventCode) {
		for (int i = 0; i < source.size();i++) {
			EventCode evt = source.get(i);
			if (eventCode == evt.getId()) {
				return i;
			}
		}
		return -1;
	}
}
