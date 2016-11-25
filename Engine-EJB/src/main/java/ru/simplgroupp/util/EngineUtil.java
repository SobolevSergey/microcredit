package ru.simplgroupp.util;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.workflow.ProcessKeys;

public class EngineUtil {
	
	public static final String POSTFIX_TIMER_PACKET = "_EXECUTE_PACKET";
	public static final String POSTFIX_TIMER_ASYNC = "_QUERY_ASYNC_ANSWER";
	
	private static String createTimerName(String customizationKey, String pluginName, String dest) {
		return ProcessKeys.PREFIX_PLUGIN + StringUtils.defaultString(customizationKey, "") + dest;
	}
	
	public static String createTimerNamePacket(String customizationKey, String pluginName) {
		return createTimerName(customizationKey, pluginName, POSTFIX_TIMER_PACKET);
	}
	
	public static String createTimerNameAsync(String customizationKey, String pluginName) {
		return createTimerName(customizationKey, pluginName, POSTFIX_TIMER_ASYNC);
	}
	
	public static boolean isTimerPacket(String timerName, String customizationKey, String pluginName) {
		String packetName = createTimerNamePacket(customizationKey, pluginName);
		return (timerName.equals(packetName));
	}
	
	public static boolean isTimerAsync(String timerName, String customizationKey, String pluginName) {
		String asyncName = createTimerNameAsync(customizationKey, pluginName);
		return (timerName.equals(asyncName));
	}

	public static boolean isTimerForPlugin(String timerName, String customizationKey, String pluginName) {
		return timerName.startsWith(ProcessKeys.PREFIX_PLUGIN + StringUtils.defaultString(customizationKey, "") + pluginName);
	}

}
