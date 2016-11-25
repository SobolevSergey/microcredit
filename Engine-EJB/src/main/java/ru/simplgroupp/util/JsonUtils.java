package ru.simplgroupp.util;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonUtils {

	/**
	 * возвращает массив строк из объекта json
	 * @param obj - объект json
	 * @param key - ключ
	 * @return
	 */
	 public static ArrayList<String> getStringArray(JSONObject obj, String key) {
		    ArrayList<String> result = new ArrayList<>();
		    Object value = obj.get(key);
		    if (value != null) {
		      if (value instanceof String) {
		        for (String item : StringUtils.split((String) value, "|")){
		          result.add(item);
		        }
		      } else if (value instanceof JSONArray) {
		        for (Object item : (JSONArray) value){
		          result.add(item.toString());
		        }
		      }
		    }
		    return result;
		  }
	
	  /**
	   * возвращает строку, соответствующую ключу в json объекте
	   * @param obj - json объект
	   * @param key - ключ
	   * @return
	   */
	  public static String getStringValue(JSONObject obj, String key) {
		    ArrayList<String> values = getStringArray(obj, key);
		    return values.isEmpty() ? null : values.get(0);
	  }
}
