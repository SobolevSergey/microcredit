package ru.simplgroupp.rules;

import java.util.HashMap;
import java.util.Map;

/**
 * возвращает результат из правила
 * в виде название - значение
 *
 */
public class NameValueRuleResult extends AbstractRuleResult {
	private HashMap<String, Object> variables = new HashMap<String, Object>(1);
	
	public Object getValue(String name) {
		return variables.get(name);
	}
	
	public void setValue(String name, Object value) {
		variables.put(name, value);
	}
	
	public Map<String, Object> getVariables() {
		return variables;
	}
	
	public void setValues(Map<String,Object> values) {
		variables.putAll(values);
	}
}
