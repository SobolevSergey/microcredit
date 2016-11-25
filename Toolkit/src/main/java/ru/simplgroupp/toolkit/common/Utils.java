package ru.simplgroupp.toolkit.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class Utils {
    
    /**
     * Инициализируем поле many-to-one, если это указано в options
     * @param relation
     * @param options
     * @param option
     */
    public static void initRelation(Object relation, Set options, Object option) 
    {
        if (options != null && options.contains(option) && relation != null) {
            if (relation instanceof Initializable)
               ((Initializable) relation).init(options);
        }
    }    

    /**
     * Инициализируем lazy- коллекцию, если это указано в options
     * @param acoll
     * @param options
     * @param option
     */
    public static void initCollection(Collection acoll, Set options, Object option) 
    {
        if (options != null && options.contains(option) && acoll != null) {
            for (Object aobj: acoll) {
                if (aobj instanceof Initializable)
                    ((Initializable) aobj).init(options);
            }
        }
    }
    
    public static int searchText(String[] source, String avalue, boolean bExtract, boolean bCaseSensitive) {
    	if (bExtract) {
    		avalue = avalue.trim();
    	}
    	
    	for (int i = 0; i < source.length; i++) {
    		String ss = source[i];
    		if (bExtract) {
    			ss = ss.trim();
    		}
    		if (equalsNull(ss, avalue, bCaseSensitive)) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static Set excludeOptions(Set options, Object ... excs) {
    	if (options == null) {
    		return null;
    	}
    	if (options.size() == 0) {
    		return options;
    	}
    	
    	Set myOpts = new HashSet(options.size());
    	myOpts.addAll(options);
    	for (Object exc: excs) {
    		myOpts.remove(exc);
    	}
    	return myOpts;
    }
    
    /**
     * Инициализируем lazy- коллекцию
     * @param acoll
     * @param options
     */
    public static void initCollection(Collection acoll, Set options) 
    {
        for (Object aobj: acoll) {
            if (aobj instanceof Initializable)
                ((Initializable) aobj).init(options);
        }
    }
    
    public static List listOf(Object ... args) {
        List res = new ArrayList(args.length);
        for (Object aobj: args)
            res.add(aobj);
        return res;
    }    

    public static Set setOf(Object ... args) {
        HashSet res = new HashSet(args.length);
        for (Object aobj: args)
            res.add(aobj);
        return res;
    }
    
    public static <KEY, VALUE> Map<KEY, VALUE> mapOf(Object ... args) {
    	HashMap res = new HashMap();
    	
    	for (int i = 0; i < args.length; i = i + 2) {
    		res.put(args[i], args[i + 1]);
    	}
    	
    	return res;
    }
    
    public static Map<String, Object> mapOfSO(Object ... args) {
    	HashMap res = new HashMap();
    	
    	for (int i = 0; i < args.length; i = i + 2) {
    		res.put(args[i].toString(), args[i + 1]);
    	}
    	
    	return res;
    }    
    
    /**
     * Превращает null/1/0 в null/true/false
     * @param source
     * @return
     */
    public static Boolean triStateToBoolean(Integer source) {
    	if (source == null) {
    		return null;
    	} else {
    		return (source.intValue() == 1);
    	}
    }
    
    /**
     * Превращает null/1/0 в false/true/false
     * @param source
     * @return
     */
    public static Boolean defaultBooleanFromIntegerNull(Integer source) {
    	if (source == null) {
    		return false;
    	} else {
    		return (source.intValue() == 1);
    	}
    }
    
	public static Number safeNumber( Object source) {
		if (source == null) {
			return null;
		}
		if (source instanceof Number) {
			return (Number) source;
		} else {
            return Convertor.toLong(source);
     	}
	}    
    
	/**
	 * превращает 1/0 в true/false
	 */
	public static boolean booleanFromNumber(Object param){
		Integer num = Convertor.toInteger(param);
		if (num == null) {
			return false;
		} else {
			return (num.intValue() == 1);
		}
	}    
    
    /**
     * Превращает null/true/false в null/1/0
     * @param source
     * @return
     */
    public static Integer booleanToTriState(Boolean source) {
    	if (source == null) {
    		return null;
    	} else {
    		return (source.booleanValue()?1:0);
    	}
    }
    
    /**
     * являются ли объекты одинаковыми
     * @param aobj1 - первый объект
     * @param aobj2 - второй объект
     * @return true, если одинаковые. Если оба null, то тоже true.
     */
    public static boolean equalsNull(Object aobj1, Object aobj2) {
    	if (aobj1 instanceof String && StringUtils.isEmpty((String) aobj1)) {
    		aobj1 = null;
    	}
    	if (aobj2 instanceof String && StringUtils.isEmpty((String) aobj2)) {
    		aobj2 = null;
    	}    	
    	if (aobj1 == null && aobj2 == null) {
    		return true;
    	}
    	if (aobj1 != null && aobj2 != null) {
    		if (aobj1 instanceof String && aobj2 instanceof String){
    			return aobj1.toString().equalsIgnoreCase(aobj2.toString());
    		} else {
    		    return aobj1.equals(aobj2);
    		}
    	} else {
    		return false;
    	}
    }
    
    /**
     * являются ли объекты одинаковыми
     * @param aobj1 - первый объект
     * @param aobj2 - второй объект
     * @return true, если одинаковые. Если оба null, то тоже true.
     */
    public static boolean equalsNull(Object aobj1, Object aobj2, boolean bCaseSensitive) {
    	if (aobj1 instanceof String && StringUtils.isEmpty((String) aobj1)) {
    		aobj1 = null;
    	}
    	if (aobj2 instanceof String && StringUtils.isEmpty((String) aobj2)) {
    		aobj2 = null;
    	}    	
    	if (aobj1 == null && aobj2 == null) {
    		return true;
    	}
    	if (aobj1 != null && aobj2 != null) {
    		if (aobj1 instanceof String && aobj2 instanceof String && (! bCaseSensitive) ) {
    			return aobj1.toString().equalsIgnoreCase(aobj2.toString());
    		} else {
    		    return aobj1.equals(aobj2);
    		}
    	} else {
    		return false;
    	}
    }    
    
    public static int compareNull(Object o1, Object o2) {
    	if (o1 == null && o2 == null) {
    		return 0;
    	}
    	if (o1 == null && o2 != null) {
    		return -1;
    	}
    	if (o1 != null && o2 == null) {
    		return 1;
    	}
    	if (o1 instanceof Comparable) {
    		return ((Comparable) o1).compareTo(o2);
    	} else {
    		return 0;
    	}
    }
    
    /**
     * возвращаем double или 0 по умолчанию, если в значении null
     * @param doubleValue - double значение
     * @return
     */
    public static Double defaultDoubleFromNull(Double doubleValue){
    	if (doubleValue==null){
    		return new Double(0);
    	} else {
    		return doubleValue;
    	}
    }
    
    /**
     * возвращаем integer или 0 по умолчанию, если в значении null
     * @param intValue - integer значение
     * @return
     */
    public static Integer defaultIntegerFromNull(Integer intValue){
    	if (intValue==null){
    		return 0;
    	} else {
    		return intValue;
    	}
    }
    
    /**
     * возвращаем первый элемент списка или null
     * @param source - список
     * @return
     */
    public static Object firstOrNull(List source ) {
    	if (source == null || source.size() == 0) {
    		return null;
    	} else {
    		return source.get(0);
    	}
    }
    
    /**
     * заменяем символ (строку) на символ (строку) в исходной строке
     * @param source - исходная строка
     * @param pattern - что меняем
     * @param replacement - на что меняем
     * @param all - если true, то меняем все вхождения
     * @return
     */
    public static String safeReplace(String source,String pattern,String replacement,boolean all){
    	String st="";
    	if (StringUtils.isNotEmpty(source)){
    		if (all){
    			st=source.replaceAll(pattern, replacement);
    		} else {
    		    st=source.replace(pattern, replacement);
    		}
    	}
    	return st;
    }
}
