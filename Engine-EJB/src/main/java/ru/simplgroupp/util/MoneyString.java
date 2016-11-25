package ru.simplgroupp.util;

import java.util.ArrayList;
import java.util.Collections;

public class MoneyString {
	  

	    /**
	     * Вернуть денежныю сумму прописью, с точностью до копеек
	     */
	    public static String num2str(Double amount) {
	        return num2str(false,true,amount);
	    }
        /**
         * вернуть сумму с точностью до сотых
         * @param isMoney - это денежная сумма или число
         */
	    public static String num2str(boolean isMoney,Double amount) {
	        return num2str(false,isMoney,amount);
	    }
	   
	    
	    /**
	     * Выводим сумму прописью
	     * @param stripkop boolean флаг - показывать копейки или нет
	     * @param isMoney - это сумма или цифра
	     * @return String Сумма прописью
	     */
	    public static String num2str(boolean stripkop,boolean isMoney,Double amount) {
	        String[][] sex = {
	            {"", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
	            {"", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},};
	        String[] str100 = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
	        String[] str11 = {"", "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать", "двадцать"};
	        String[] str10 = {"", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
	        String[][] forms = {
	            {"копейка", "копейки", "копеек", "1"},
	            {"рубль", "рубля", "рублей", "0"},
	            {"тысяча", "тысячи", "тысяч", "1"},
	            {"миллион", "миллиона", "миллионов", "0"},
	            {"миллиард", "миллиарда", "миллиардов", "0"},
	            {"триллион", "триллиона", "триллионов", "0"}, // можно добавлсть дальше секстиллионы и т.д.
	        };
	        String[][] formsN = {
		            {"сотая", "сотых", "сотых", "1"},
		            {"целая", "целых", "целых", "0"},
		            {"тысяча", "тысячи", "тысяч", "1"},
		            {"миллион", "миллиона", "миллионов", "0"},
		            {"миллиард", "миллиарда", "миллиардов", "0"},
		            {"триллион", "триллиона", "триллионов", "0"}, 
		            {"десятая", "десятых", "десятых", "1"},
		    };
	// получаем отдельно рубли и копейки
	        long rub = amount.longValue();
	        String[] moi = amount.toString().split("\\.");
	        int kop = Integer.valueOf(moi[1]);
	        if (!moi[1].substring(0, 1).equals("0")) {// начинаетсс не с нулс
	            if (kop < 10&&isMoney) {
	                kop *= 10;
	            }
	        }
	        String kops = String.valueOf(kop);
	        
	        if (kops.length() == 1&&isMoney) {
	            kops = "0" + kops;
	        }
	        long rub_tmp = rub;
	// Разбиватель суммы на сегменты по 3 цифры с конца
	        ArrayList segments = new ArrayList();
	        while (rub_tmp > 999) {
	            long seg = rub_tmp / 1000;
	            segments.add(rub_tmp - (seg * 1000));
	            rub_tmp = seg;
	        }
	        segments.add(rub_tmp);
	        Collections.reverse(segments);
	// снализируем сегменты
	        String o = "";
	        if (rub == 0) {// если соль
	            o = "ноль " + morph(0, forms[1][0], forms[1][1], forms[1][2]);
	            if (stripkop) {
	                return o;
	            } else {
	                return o + " " + kop + " " + morph(kop, forms[0][0], forms[0][1], forms[0][2]);
	            }
	        }
	// Больше нуля
	        int lev = segments.size();
	        for (int i = 0; i < segments.size(); i++) {// перебираем сегменты
	            int sexi = (int) Integer.valueOf(forms[lev][3].toString());// определсем род
	            int ri = (int) Integer.valueOf(segments.get(i).toString());// текущий сегмент
	            if (ri == 0 && lev > 1) {// если сегмент ==0 И не последний уровень(там Units)
	                lev--;
	                continue;
	            }
	            String rs = String.valueOf(ri); // число в строку
	// нормализация
	            if (rs.length() == 1) {
	                rs = "00" + rs;// два нулика в префикс?
	            }
	            if (rs.length() == 2) {
	                rs = "0" + rs; // или лучше один?
	            }// получаем циферки длс анализа
	            int r1 = (int) Integer.valueOf(rs.substring(0, 1)); //первас цифра
	            int r2 = (int) Integer.valueOf(rs.substring(1, 2)); //вторас
	            int r3 = (int) Integer.valueOf(rs.substring(2, 3)); //третьс
	            int r22 = (int) Integer.valueOf(rs.substring(1, 3)); //вторас и третьс
	            
	         
	           // Супер-нано-анализатор циферок
	            if (ri > 99) {
	                o += str100[r1] + " "; // Сотни
	            }
	            if (r22 > 20) {// >20
	                o += str10[r2] + " ";
	                o += sex[sexi][r3] + " ";
	            } else { // <=20
	                if (r22 > 9) {
	                    o += str11[r22 - 9] + " "; // 10-20
	                } else {
	                    o += sex[sexi][r3] + " "; // 0-9
	                }
	            }
	// Единицы измерения (рубли...)
	            if (isMoney){
	                o += morph(ri, forms[lev][0], forms[lev][1], forms[lev][2]) + " ";
	            } else if ((lev!=1)||((lev==1)&&(kop!=0))){
	            	
	            	o += morph(ri, formsN[lev][0], formsN[lev][1], formsN[lev][2]) + " ";
	            }
	            lev--;
	        }
	// Копейки в цифровом виде
	      
	        if (stripkop||kop==0) {
	            o = o.replaceAll(" {2,}", " ");
	        } else {
	        	//копейки есть
	        	int k1=(int) Integer.valueOf(kops.substring(0, 1));
	        	int k2=0;
	        	if (kops.length()==2){
	               k2=(int) Integer.valueOf(kops.substring(1, 2));
	        	}
	             int sexi = (int) Integer.valueOf(forms[0][3].toString());
	        	 if (kop > 20) {// >20
		                o += str10[k1] + " ";
		                o += sex[sexi][k2] + " ";
		            } else { // <=20
		                if (kop > 9) {
		                    o += str11[kop - 9] + " "; // 10-20
		                } else {
		                    o += sex[sexi][k1] + " "; // 0-9
		                }
		            }
	        	
	        	if (isMoney){
	                o = o + "" + morph(kop, forms[0][0], forms[0][1], forms[0][2]);
	        	} else {
	        		//только десятые
	        		if (k2==0){
	        		   o = o + "" + morph(kop, formsN[6][0], formsN[6][1], formsN[6][2]);
	        		} else {
	        		    o = o + "" + morph(kop, formsN[0][0], formsN[0][1], formsN[0][2]) ;
	        		}
	        	}
	            o = o.replaceAll(" {2,}", " ");
	        }
	        return o;
	    }

	    /**
	     * Склонсем словоформу
	     * @param n Long количество объектов
	     * @param f1 String вариант словоформы длс одного объекта
	     * @param f2 String вариант словоформы длс двух объектов
	     * @param f5 String вариант словоформы длс псти объектов
	     * @return String правильный вариант словоформы длс указанного количества объектов
	     */
	    public static String morph(long n, String f1, String f2, String f5) {
	        n = Math.abs(n) % 100;
	        long n1 = n % 10;
	        if (n > 10 && n < 20) {
	            return f5;
	        }
	        if (n1 > 1 && n1 < 5) {
	            return f2;
	        }
	        if (n1 == 1) {
	            return f1;
	        }
	        return f5;
	    }
}
