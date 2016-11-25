package com.altova.types;

import com.altova.types.SchemaCalendarBase;
import com.altova.types.SchemaDate;
import com.altova.types.SchemaDateTime;
import com.altova.types.SchemaString;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeCalendar;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class SchemaTime extends SchemaCalendarBase {
   public SchemaTime() {
   }

   public SchemaTime(SchemaTime newvalue) {
      this.setInternalValues(0, 0, 0, newvalue.hour, newvalue.minute, newvalue.second, newvalue.partsecond, newvalue.hasTZ, newvalue.offsetTZ);
      this.isempty = newvalue.isempty;
   }

   public SchemaTime(SchemaDateTime newvalue) {
      this.setInternalValues(0, 0, 0, newvalue.hour, newvalue.minute, newvalue.second, newvalue.partsecond, newvalue.hasTZ, newvalue.offsetTZ);
      this.isempty = newvalue.isempty;
   }

   public SchemaTime(int newhour, int newminute, int newsecond, double newpartsecond, int newoffsetTZ) {
      this.setInternalValues(0, 0, 0, newhour, newminute, newsecond, newpartsecond, 2, newoffsetTZ);
      this.isempty = false;
   }

   public SchemaTime(int newhour, int newminute, int newsecond, double newpartsecond) {
      this.setInternalValues(0, 0, 0, newhour, newminute, newsecond, newpartsecond, 0, 0);
      this.isempty = false;
   }

   public SchemaTime(Calendar newvalue) {
      this.setValue(newvalue);
   }

   public SchemaTime(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaTime(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaTime(SchemaTypeCalendar newvalue) {
      this.assign(newvalue);
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
      cal.set(11, this.hour);
      cal.set(12, this.minute);
      cal.set(13, this.second);
      cal.set(14, this.getMillisecond());
      this.hasTZ = 2;
      cal.setTimeZone(new SimpleTimeZone(this.offsetTZ * '\uea60', ""));
      return cal;
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
         this.setNull();
      } else {
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
         try {
            this.parseTime(newvalue);
         } catch (NumberFormatException var3) {
            throw new StringParseException("invalid time format", 2);
         }
      } else {
         this.setEmpty();
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull() && !newvalue.isEmpty()) {
         if(newvalue instanceof SchemaTime) {
            this.setInternalValues(0, 0, 0, ((SchemaTime)newvalue).hour, ((SchemaTime)newvalue).minute, ((SchemaTime)newvalue).second, ((SchemaTime)newvalue).partsecond, ((SchemaTime)newvalue).hasTZ, ((SchemaTime)newvalue).offsetTZ);
         } else if(newvalue instanceof SchemaDateTime) {
            this.setInternalValues(0, 0, 0, ((SchemaDateTime)newvalue).hour, ((SchemaDateTime)newvalue).minute, ((SchemaDateTime)newvalue).second, ((SchemaDateTime)newvalue).partsecond, ((SchemaDateTime)newvalue).hasTZ, ((SchemaDateTime)newvalue).offsetTZ);
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

   public Object clone() {
      return new SchemaTime(this);
   }

   public String toString() {
      return this.isempty?"":this.toTimeString();
   }

   public static SchemaTime now() {
      return new SchemaTime(Calendar.getInstance());
   }

   public int calendarType() {
      return 3;
   }

   public SchemaDateTime dateTimeValue() {
      throw new TypesIncompatibleException(this, new SchemaDateTime("2003-07-28T12:00:00"));
   }

   public SchemaDate dateValue() {
      throw new TypesIncompatibleException(this, new SchemaDate("2003-07-28"));
   }

   public SchemaTime timeValue() {
      return new SchemaTime(this);
   }
}
