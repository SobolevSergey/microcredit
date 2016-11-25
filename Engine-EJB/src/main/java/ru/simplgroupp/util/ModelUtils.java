package ru.simplgroupp.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.toolkit.common.Convertor;

public class ModelUtils {
	/**
	 * записываем параметры из коллекции в map
	 * @param source - коллекция
	 * @param dest - map, в который пишем
	 */
	public static void paramsToMap(Collection<AIModelParamEntity> source, Map<String, Object> dest) {
		for (AIModelParamEntity aiCon: source) {
			Object avalue = paramToObject(aiCon);
			
			dest.put(aiCon.getName(), avalue);
		}		
	}
	
	public static Object paramToObject(AIModelParamEntity aiCon) {
		Object avalue = null;
		if (Convertor.TYPE_SHORT_STRING.equals(aiCon.getDataType())) {
			String svalue = aiCon.getDataValue();
			if (StringUtils.isBlank(svalue) && (! StringUtils.isBlank(aiCon.getDataValueText()))) {
				svalue = aiCon.getDataValueText();
			}
			avalue = Convertor.toValue(aiCon.getDataType(), svalue);
		} else if (Convertor.TYPE_LONG_STRING.equals(aiCon.getDataType())) {
			String svalue = aiCon.getDataValueText();
			if (StringUtils.isBlank(svalue) && (! StringUtils.isBlank(aiCon.getDataValue()))) {
				svalue = aiCon.getDataValue();
			}			
			avalue = Convertor.toValue(aiCon.getDataType(), svalue);
		} else {
			avalue = Convertor.toValue(aiCon.getDataType(), aiCon.getDataValue());	
		}
		return avalue;
	}
	
	public static void copyParam(AIModelParamEntity from, AIModelParamEntity to) {
		to.setName(from.getName());
		to.setCustomKey(from.getCustomKey());
		to.setDescription(from.getDescription());
		to.setDataType(from.getDataType());
		to.setDataValue(from.getDataValue());
		to.setDataValueText(from.getDataValueText());
	}
	
	public static AIModelParamEntity findParam(Collection<AIModelParamEntity> source, String name) {
		for (AIModelParamEntity prm: source) {
			if (name.equalsIgnoreCase( prm.getName() ) ) {
				return prm;
			}
		}
		return null;
	}
	
	public static String genViewName(int modelId) {
		return "v_model_" + modelId;
	}
}
