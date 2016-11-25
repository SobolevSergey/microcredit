package ru.simplgroupp.toolkit.common;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class GenUtils {

	/**
	 * генерирует случайную последовательность символов
	 * @param length - длина
     */
	public static String gen(int length) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = length; i > 0; i -= 12) {
	      int n = min(12, abs(i));
	      sb.append(StringUtils.leftPad(Long.toString(round(random() * pow(36, n)), 36), n));
	    }
	    return sb.toString();
	  }

	 /**
	  * генерирует случайный цифровой код
	  * @param num - длина
	  */
	 public static String genCode(int num) {
	        StringBuilder code = new StringBuilder();
	        int[] arr = new int[num];
	        int max = 10;
	        for (int i = 0; i < arr.length; i++) {
	        	arr[i] = (int) (Math.random() * max );
	            code.append(arr[i]);
	        }
	        return code.toString();
		  //  return "123456";
	    }

	  /**
	     * Генерирует уникальный номер для заявки в формате
	     * @param date - дата
	     * @param nn - номер по порядку
	     * @param pp - ставка
	     */
		public static String genUniqueNumber(Date date, Integer nn, Double pp) {
	        NumberFormat formatter = NumberFormat.getInstance();
	        formatter.setMinimumFractionDigits(2);
	        formatter.setMinimumIntegerDigits(2);
	        String sn = nn.toString();
	        while (sn.length() < 5) {
	            sn = "0" + sn;
	        }
	        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	        String st = "1" + formatter.format(pp).replace(",", "").replace(".", "") + dateFormat.format(date) + sn;
	        return st;
	    }
}
