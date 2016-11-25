package ru.simplgroupp.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.DoubleRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.SortCriteria;

/**
 * Вспомогательные утилиты для работы со списками
 * @author irina
 *
 */
public class SearchUtil {
	
	public static SortCriteria findSortCriteria(SortCriteria[] source, String expression) {
		for (SortCriteria cr: source) {
			if (expression.equals(cr.getExpression())) {
				return cr;
			}
		}
		return null;
	}
	
	public static String sortToString(Map<String, Object> orderMapping, SortCriteria[] sorting) {
		String sres = "";
		
		if (sorting == null || sorting.length == 0)
			return sres;
		
		for (SortCriteria cri: sorting) 
		{
			if (cri == null)
				continue;

			Object amap = orderMapping.get(cri.getExpression());
			if (amap == null)
				continue;
			
			if (sres.length() > 0)
				sres = sres + ",";
			else
				sres = sres + " order by ";
			
			if (amap instanceof String) {
				sres = sres + amap.toString();
				if (cri.getIsAscending())
					sres = sres + " asc";
				else
					sres = sres + " desc";				
			} else {
				// состоит из нескольких полей
				List<String> lst = (List<String>) amap;
				int n = 0;
				for (String sexpr: lst) {
					if (n > 0)
						sres = sres + ", ";
					
					sres = sres + sexpr;
					if (cri.getIsAscending())
						sres = sres + " asc";
					else
						sres = sres + " desc";				
					n++;
				}
			}
		}
		return sres;
	}
	
    public static String sortSelectToString(Map<String, Object> orderMapping, SortCriteria[] sorting) {
        String sres = "";
        
        if (sorting == null || sorting.length == 0)
            return sres;
        
        for (SortCriteria cri: sorting) 
        {
            if (cri == null)
                continue;

            Object amap = orderMapping.get(cri.getExpression());
            if (amap == null)
                continue;
            
            if (sres.length() > 0)
                sres = sres + ",";
            
            if (amap instanceof String) {
                sres = sres + amap.toString();
            } else {
                // состоит из нескольких полей
                List<String> lst = (List<String>) amap;
                int n = 0;
                for (String sexpr: lst) {
                    if (n > 0)
                        sres = sres + ", ";
                    
                    sres = sres + sexpr;
                    n++;
                }
            }
        }
        return "," +sres;
    }
    
	public static void extractColumn(List<Object[]> source, int ncol, List dest) {
	    for (Object[] row: source) {
	        dest.add(row[ncol]);
	    }
	}
	
	/**
	 * делает диапазон дробных величин из map
	 * @param source - map
	 * @param prefix - название
	 * @return
	 */
	public static DoubleRange doubleRangeFromMap(Map<String, Object> source, String prefix) {
		DoubleRange dr = null;
		if (source.containsKey(prefix + ".from") || source.containsKey(prefix + ".to")) {
			dr = new DoubleRange(Convertor.toDouble( source.get(prefix + ".from")), Convertor.toDouble( source.get(prefix + ".to") ));
			return dr;
		} else {
			return dr;
		}		
	}
	
	/**
	 * делает диапазон денежных величин из map
	 * @param source - map
	 * @param prefix - название
	 * @return
	 */
	public static MoneyRange moneyRangeFromMap(Map<String, Object> source, String prefix) {
		MoneyRange dr = null;
		if (source.containsKey(prefix + ".from") || source.containsKey(prefix + ".to")) {
			dr = new MoneyRange(Convertor.toMoney( source.get(prefix + ".from")), Convertor.toMoney( source.get(prefix + ".to") ));
			return dr;
		} else {
			return dr;
		}		
	}	
	
	/**
	 * делает диапазон целых величин из map
	 * @param source - map
	 * @param prefix - название
	 * @return
	 */
	public static IntegerRange intRangeFromMap(Map<String, Object> source, String prefix) {
		IntegerRange dr = null;
		if (source.containsKey(prefix + ".from") || source.containsKey(prefix + ".to")) {
			dr = new IntegerRange(Convertor.toInteger( source.get(prefix + ".from")), Convertor.toInteger( source.get(prefix + ".to") ));
			return dr;
		} else {
			return dr;
		}		
	}	
	
	public static Date asDate(Object source) {
		if (source == null) {
			return null;
		}
		
		if (source instanceof Date) {
			return (Date) source;
		}
		
		if (source instanceof Calendar) {
			return ((Calendar) source).getTime();
		}
		
		if (source instanceof Timestamp) {
			return new Date(((Timestamp) source).getTime());
		}
		
		return Convertor.toDate(source.toString(), "dd-MM-yyyy HH:mm:ss");
	}
	
	/**
	 * делает диапазон дат из map
	 * @param source - map
	 * @param prefix - название
	 * @return
	 */
	public static DateRange dateRangeFromMap(Map<String, Object> source, String prefix) {
		DateRange dr = null;
		if (source.containsKey(prefix + ".from") || source.containsKey(prefix + ".to")) {
			dr = new DateRange(asDate( source.get(prefix + ".from")), asDate( source.get(prefix + ".to")));
			return dr;
		} else {
			return dr;
		}
	}

}
