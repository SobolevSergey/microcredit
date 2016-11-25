package ru.simplgroupp.util;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.ejb.ScheduleExpression;

import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.toolkit.common.DateRange;

public class DatesUtils {
    public static final DateFormat DATE_FORMAT_ddMMYYYY = new SimpleDateFormat("dd.MM.yyyy");
    public static final DateFormat DATE_FORMAT_ddMMYY = new SimpleDateFormat("dd.MM.yy");
    public static final DateFormat DATE_FORMAT_YYYYMMdd = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat DATE_FORMAT_HHmmss = new SimpleDateFormat("HHmmss");
    public static final DateFormat DATE_FORMAT_YYYY_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    public static final Pattern SIMPLE_DATE_PATTERN = Pattern.compile("\\d\\d\\.\\d\\d\\.\\d{2,4}");
    public static final DateFormat DATE_FORMAT_ddMMYYYY_HHmmss = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static final DateFormat DATE_FORMAT_YYYY_MM_dd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String FORMAT_ddMMYYYY="dd.MM.yyyy";
    public static final String FORMAT_ddMMYYYY_HHmmss="dd.MM.yyyy HH:mm:ss";
    public static final String FORMAT_YYYYMMddHHmmss="yyyyMMddHHmmss";
    public static final String FORMAT_YYYYMMdd="yyyyMMdd";
    public static final String FORMAT_YYYY_MM_dd="yyyy-MM-dd";
    
   
    /**
     * разница в днях между датами, возвращает положительное число или 0
     * @param date1 - большая дата
     * @param date2 - меньшая дата
     */
	public static int daysDiff(Date date1,Date date2){
		
       return (daysDiffAny(date1, date2)<0?0:daysDiffAny(date1, date2));
    }
	
    /**
     * разница в днях между датами
     * @param date1 - большая дата
     * @param date2 - меньшая дата
     */
	public static int daysDiffAny(Date date1,Date date2){
		
        LocalDate dd1 = new LocalDate(date1);
        LocalDate dd2 = new LocalDate(date2);
        return Days.daysBetween(dd2, dd1).getDays();
    }	
	/**
     * разница в годах между датами
     * @param date1 - большая дата
     * @param date2 - меньшая дата
     */
	public static int yearsDiff(Date date1,Date date2){
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int diff=0;
		if (c1.get(Calendar.DAY_OF_YEAR)<c2.get(Calendar.DAY_OF_YEAR)){
			diff=1;
		} 
		return c1.get(Calendar.YEAR)-c2.get(Calendar.YEAR)-diff;
	}
	/**
	 * разница в месяцах между датами
	 * @param date1  - большая дата
	 * @param date2 - меньшая дата
	 * @return
	 */
	public static int monthesDiff(Date date1,Date date2){
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int diff=0;
		if (c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)&&c1.get(Calendar.DAY_OF_MONTH)<c2.get(Calendar.DAY_OF_MONTH)) {
			diff=1;
		}
		return (c1.get(Calendar.MONTH)<c2.get(Calendar.MONTH)?c1.get(Calendar.MONTH)+12:c1.get(Calendar.MONTH))-c2.get(Calendar.MONTH)-diff+yearsDiff(date1,date2)*12;
	}
	
	/**
	 * возвращает год даты
	 * @param date - дата
	 */
	public static int getYear(Date date){
		Calendar c1=Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.YEAR);
	}
	
	/**
	 * возвращает месяц даты
	 * @param date - дата
	 */
	public static int getMonth(Date date){
		Calendar c1=Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.MONTH)+1;
	}
	
	/**
	 * возвращает день даты
	 * @param date - дата
	 */
	public static int getDay(Date date){
		Calendar c1=Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * возвращаем дату, если есть год, месяц и день
	 * @param year - год
	 * @param month - месяц
	 * @param day - день
	 * @return
	 */
	public static Date makeDate(int year,int month,int day){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH,day);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * возвращаем дату из даты со штампом времени
	 * @param date - дата со штампом времени
	 * @return
	 */
	public static Date makeDate(Date date){
		return makeDate(getYear(date),getMonth(date),getDay(date));
	}
	
	/**
	 * сколько дней в году
	 * @param date - дата
	 */
	public static int getDaysOfYear(Date date){
		if (getYear(date)%4==0){
    		return 366;
       	} else {
       		return 365;
       	}
	}
	
	public static String saveToCron(ScheduleExpression expr) {
		return expr.getSecond() + " " + expr.getMinute() + " " + expr.getHour() + " " + expr.getDayOfMonth() + " " + expr.getMonth()+ " " + expr.getDayOfWeek() + " " + expr.getYear();
	}

	public static ScheduleExpression parseFromCron(String cronExpr) {
		ScheduleExpression schedule = new ScheduleExpression();
		String[] vals = cronExpr.split(" ");
		schedule.second(vals[0]);
		schedule.minute(vals[1]);
		schedule.hour(vals[2]);
		schedule.dayOfMonth(vals[3]);
		schedule.month(vals[4]);
		schedule.dayOfWeek(vals[5]);
		schedule.year(vals[6]); // 0 0 2 * * * *
		return schedule;
	}
  
  /**
   * являются ли даты одинаковыми	
   * @param date1 - первая дата
   * @param date2 - вторая дата
   * @return
   */
  public static boolean isSameDay(Date date1, Date date2) {
    if (date1 == null && date2 == null){
      return true;
    } else if (date1 == null && date2 != null || date1 != null && date2 == null){
      return false;
    } else {
      return DateUtils.isSameDay(date1, date2);
    }
  }
  
  /**
   * делаем dateRange из одной даты, нужно для разных выборок
   * @param date - дата
   * @return
   */
  public static DateRange makeOneDayDateRange(Date date){
	  DateRange dateRange=new DateRange();
	  Calendar start = Calendar.getInstance();
	  start.setTime(date);
	  start.set(Calendar.HOUR_OF_DAY,0);
	  start.set(Calendar.MINUTE,0);
	  start.set(Calendar.SECOND,0);
	  start.set(Calendar.MILLISECOND, 0);
	  
	  Calendar end = Calendar.getInstance();
	  end.setTime(date);
	  end.set(Calendar.HOUR_OF_DAY,23);
	  end.set(Calendar.MINUTE,59);
	  end.set(Calendar.SECOND,59);
	  end.set(Calendar.MILLISECOND, 999);
	  
	  dateRange.setFrom(start.getTime());
	  dateRange.setTo(end.getTime());
	  return dateRange;
  }

	/**
	 * разница в годах между датой и сегодняшним днем
	 *
	 * @param date - дата
	 */
	public static int yearsDiffNow(Date date) {
		return yearsDiff(new Date(),date);
	}
}
