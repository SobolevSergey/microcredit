package com.altova.types;

import com.altova.types.SchemaCalendarBase;
import com.altova.types.SchemaDate;
import com.altova.types.SchemaString;
import com.altova.types.SchemaTime;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeCalendar;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.util.Calendar;

public class SchemaDateTime extends SchemaCalendarBase {
   public SchemaDateTime() {
      this.setEmpty();
   }

   public SchemaDateTime(SchemaDateTime newvalue) {
      this.year = newvalue.year;
      this.month = newvalue.month;
      this.day = newvalue.day;
      this.hour = newvalue.hour;
      this.minute = newvalue.minute;
      this.second = newvalue.second;
      this.partsecond = newvalue.partsecond;
      this.hasTZ = newvalue.hasTZ;
      this.offsetTZ = newvalue.offsetTZ;
      this.isempty = newvalue.isempty;
   }

   public SchemaDateTime(int newyear, int newmonth, int newday, int newhour, int newminute, int newsecond, double newpartsecond, int newoffsetTZ) {
      this.setInternalValues(newyear, newmonth, newday, newhour, newminute, newsecond, newpartsecond, 2, newoffsetTZ);
      this.isempty = false;
   }

   public SchemaDateTime(int newyear, int newmonth, int newday, int newhour, int newminute, int newsecond, double newpartsecond) {
      this.setInternalValues(newyear, newmonth, newday, newhour, newminute, newsecond, newpartsecond, 0, 0);
      this.isempty = false;
   }

   public SchemaDateTime(Calendar newvalue) {
      this.setValue(newvalue);
   }

   public SchemaDateTime(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaDateTime(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaDateTime(SchemaTypeCalendar newvalue) {
      this.assign(newvalue);
   }

   public int getYear() {
      return this.year;
   }

   public int getMonth() {
      return this.month;
   }

   public int getDay() {
      return this.day;
   }

   public int getHour() {
      return this.hour;
   }

   public int getMinute() {
      return this.minute;
   }

   public int getSecond() {
      return this.second;
   }

   public double getPartSecond() {
      return this.partsecond;
   }

   public int getMillisecond() {
      return (int)Math.round(this.partsecond * 1000.0D);
   }

   public int hasTimezone() {
      return this.hasTZ;
   }

   public int getTimezoneOffset() {
      return this.hasTZ != 2?0:this.offsetTZ;
   }

   public Calendar getValue() {
      Calendar cal = Calendar.getInstance();
      cal.set(this.year, this.month - 1, this.day, this.hour, this.minute, this.second);
      cal.set(14, this.getMillisecond());
      this.hasTZ = 2;
      cal.set(15, this.offsetTZ * '\uea60');
      return cal;
   }

   public void setYear(int newyear) {
      this.year = newyear;
      this.isempty = false;
   }

   public void setMonth(int newmonth) {
      this.month = newmonth;
      this.isempty = false;
   }

   public void setDay(int newday) {
      this.day = newday;
      this.isempty = false;
   }

   public void setHour(int newhour) {
      this.hour = newhour;
      this.isempty = false;
   }

   public void setMinute(int newminute) {
      this.minute = newminute;
      this.isempty = false;
   }

   public void setSecond(int newsecond) {
      this.second = newsecond;
      this.isempty = false;
   }

   public void setPartSecond(double newpartsecond) {
      this.partsecond = newpartsecond;
      this.isempty = false;
   }

   public void setMillisecond(int newmillisecond) {
      this.partsecond = (double)newmillisecond / 1000.0D;
      this.isempty = false;
   }

   public void setTimezone(int newhasTZ, int newoffsetTZminutes) {
      this.hasTZ = newhasTZ;
      this.offsetTZ = newoffsetTZminutes;
      this.isempty = false;
   }

   public void setValue(Calendar newvalue) {
      if(newvalue == null) {
         this.setEmpty();
      } else {
         this.year = newvalue.get(1);
         this.month = newvalue.get(2) + 1;
         this.day = newvalue.get(5);
         this.hour = newvalue.get(11);
         this.minute = newvalue.get(12);
         this.second = newvalue.get(13);
         this.setMillisecond(newvalue.get(14));
         String sTZ = newvalue.getTimeZone().getID();
         this.hasTZ = 2;
         this.offsetTZ = newvalue.get(15) / '\uea60';
         this.isempty = false;
      }
   }

   public void parse(String newvalue) {
      if(newvalue != null && newvalue.length() != 0) {
         if(newvalue.length() < 19) {
            throw new StringParseException(newvalue + " cannot be converted to a dateTime value", 0);
         }

         try {
            int e = newvalue.indexOf("T");
            if(e == -1) {
               e = newvalue.length();
            }

            this.parseDate(newvalue.substring(0, e));
            if(e < newvalue.length()) {
               this.parseTime(newvalue.substring(e + 1, newvalue.length()));
            } else {
               this.hour = 0;
               this.minute = 0;
               this.second = 0;
               this.partsecond = 0.0D;
               this.hasTZ = 0;
               this.offsetTZ = 0;
            }
         } catch (NumberFormatException var3) {
            throw new StringParseException(newvalue + " cannot be converted to a dateTime value", 2);
         }

         this.isempty = false;
      } else {
         this.setEmpty();
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull() && !newvalue.isEmpty()) {
         if(newvalue instanceof SchemaDateTime) {
            this.setInternalValues(((SchemaDateTime)newvalue).year, ((SchemaDateTime)newvalue).month, ((SchemaDateTime)newvalue).day, ((SchemaDateTime)newvalue).hour, ((SchemaDateTime)newvalue).minute, ((SchemaDateTime)newvalue).second, ((SchemaDateTime)newvalue).partsecond, ((SchemaDateTime)newvalue).hasTZ, ((SchemaDateTime)newvalue).offsetTZ);
            this.isempty = false;
         } else if(newvalue instanceof SchemaDate) {
            this.setInternalValues(((SchemaDate)newvalue).year, ((SchemaDate)newvalue).month, ((SchemaDate)newvalue).day, 0, 0, 0, 0.0D, 0, 0);
            this.isempty = false;
         } else {
            if(!(newvalue instanceof SchemaString)) {
               throw new TypesIncompatibleException(newvalue, this);
            }

            this.parse(newvalue.toString());
         }
      } else {
         this.setEmpty();
      }

   }

   public void setEmpty() {
      this.setValue(Calendar.getInstance());
      this.isempty = true;
   }

   public Object clone() {
      return new SchemaDateTime(this);
   }

   public String toString() {
      if(this.isempty) {
         return "";
      } else {
         StringBuffer s = new StringBuffer();
         s.append(this.toDateString());
         s.append("T");
         s.append(this.toTimeString());
         return s.toString();
      }
   }

   public static SchemaDateTime now() {
      return new SchemaDateTime(Calendar.getInstance());
   }

   public int calendarType() {
      return 1;
   }

   public SchemaDateTime dateTimeValue() {
      return new SchemaDateTime(this);
   }

   public SchemaDate dateValue() {
      return new SchemaDate(this);
   }

   public SchemaTime timeValue() {
      return new SchemaTime(this);
   }
}
