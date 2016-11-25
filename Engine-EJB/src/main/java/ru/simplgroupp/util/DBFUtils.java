package ru.simplgroupp.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jamel.dbf.DbfReader;
import org.jamel.dbf.utils.DbfUtils;

public class DBFUtils {

	/**
	 * Парсинг файла DBF
	 * @param dbf представление файла в виде массива байт
	 * @param fieldTypes карта, в которой ключи - номера интересующих столбцов, а значения - типы этих столбцов
	 * @return список значений строк таблицы dbf, где каждый элемент списка - отображение номеров столбцов на их значение
	 */
	public static List<HashMap<Integer, Object>> parseDBF(byte[] dbf, String encoding, HashMap<Integer, ?> fieldTypes) throws UnsupportedEncodingException {
		List<HashMap<Integer, Object>> result = new ArrayList<HashMap<Integer, Object>>();
		ByteArrayInputStream bis = new ByteArrayInputStream(dbf);
		DbfReader reader = new DbfReader(bis);

		Object[] row;
        while ((row = reader.nextRecord()) != null) {
        	HashMap<Integer, Object> o = new HashMap<Integer, Object>();
            for(Entry<Integer, ?> e : fieldTypes.entrySet()) {
    			if(e.getValue() == String.class) {
    				o.put(e.getKey(), new String(DbfUtils.trimLeftSpaces((byte[]) row[e.getKey()]), encoding));
    			} else if(e.getValue() == Integer.class) {
    				o.put(e.getKey(), ((Number) row[e.getKey()]).intValue());
    			} else if(e.getValue() == Long.class) {
    				o.put(e.getKey(), ((Number) row[e.getKey()]).longValue());
    			} else if(e.getValue() == Double.class) {
    				o.put(e.getKey(), ((Number) row[e.getKey()]).doubleValue());
    			} else if(e.getValue() == Boolean.class) {
    				o.put(e.getKey(), (Boolean) row[e.getKey()]);
    			} else if(e.getValue() == Date.class) {
    				o.put(e.getKey(), (Date) row[e.getKey()]);
    			} 
    		}
            result.add(o);
        }
        
        reader.close();
		
		return result;
	}
}
