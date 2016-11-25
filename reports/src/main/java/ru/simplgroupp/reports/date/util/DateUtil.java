package ru.simplgroupp.reports.date.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class DateUtil {
    public static Date getDateMinusInterval(Date date,int amt,int calendarInterval){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarInterval, amt);
        return c.getTime();
    }
    //получаем дату начала
    public static Date getDateEnd(Date now){
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, -1);//прошлый день
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        return c.getTime();
    }
    public static Date getDateStart(Date now,int amt,int calendarInterval){
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(calendarInterval, amt);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }
}
