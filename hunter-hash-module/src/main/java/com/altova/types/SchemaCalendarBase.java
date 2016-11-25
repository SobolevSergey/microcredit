package com.altova.types;

import com.altova.types.SchemaDuration;
import com.altova.types.SchemaTypeCalendar;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class SchemaCalendarBase implements SchemaTypeCalendar {
   public static final int TZ_MISSING = 0;
   public static final int TZ_UTC = 1;
   public static final int TZ_OFFSET = 2;
   protected int year;
   protected int month;
   protected int day;
   protected int hour;
   protected int minute;
   protected int second;
   protected double partsecond;
   protected int hasTZ;
   protected int offsetTZ;
   protected boolean isempty;

   public SchemaCalendarBase() {
      this.setEmpty();
   }

   public void setNull() {
      this.setEmpty();
   }

   public void setEmpty() {
      this.isempty = true;
      this.setInternalValues(0, 0, 0, 0, 0, 0, 0.0D, 0, 0);
   }

   public boolean equals(Object obj) {
      if(!(obj instanceof SchemaCalendarBase)) {
         return false;
      } else {
         SchemaCalendarBase dt = (SchemaCalendarBase)obj;
         return this.year != dt.year?false:(this.month != dt.month?false:(this.day != dt.day?false:(this.hour != dt.hour?false:(this.minute != dt.minute?false:(this.second != dt.second?false:(this.partsecond != dt.partsecond?false:(this.hasTZ != dt.hasTZ?false:this.offsetTZ == dt.offsetTZ)))))));
      }
   }

   public int hashCode() {
      return (int)Double.doubleToLongBits(this.getApproximatedTotal());
   }

   public String toDateString() {
      StringBuffer s = new StringBuffer();
      s.append((new DecimalFormat("0000")).format((long)this.year));
      s.append("-");
      s.append((new DecimalFormat("00")).format((long)this.month));
      s.append("-");
      s.append((new DecimalFormat("00")).format((long)this.day));
      return s.toString();
   }

   public String toTimeString() {
      StringBuffer s = new StringBuffer();
      s.append((new DecimalFormat("00")).format((long)this.hour));
      s.append(":");
      s.append((new DecimalFormat("00")).format((long)this.minute));
      s.append(":");
      s.append((new DecimalFormat("00")).format((long)this.second));
      if(this.partsecond > 0.0D && this.partsecond < 1.0D) {
         String absOffsetTZ = (new DecimalFormat("0.0###############")).format(this.partsecond);
         s.append(".");
         s.append(absOffsetTZ.substring(2, absOffsetTZ.length()));
      }

      if(this.hasTZ == 1) {
         s.append("Z");
      } else if(this.hasTZ == 2) {
         int absOffsetTZ1 = this.offsetTZ;
         if(this.offsetTZ < 0) {
            s.append("-");
            absOffsetTZ1 = -this.offsetTZ;
         } else {
            s.append("+");
         }

         s.append((new DecimalFormat("00")).format((long)(absOffsetTZ1 / 60)));
         s.append(":");
         s.append((new DecimalFormat("00")).format((long)(absOffsetTZ1 % 60)));
      }

      return s.toString();
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return true;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isEmpty();
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaCalendarBase)obj);
   }

   public int compareTo(SchemaCalendarBase obj) {
      return (int)(this.getApproximatedTotal() - obj.getApproximatedTotal());
   }

   protected void parseDate(String newvalue) throws StringParseException {
      if(newvalue.length() == 0) {
         this.isempty = true;
      } else {
         if(newvalue.length() < 10) {
            throw new StringParseException("date-part of string is too short", 0);
         }

         try {
            byte e = 0;
            if(newvalue.substring(0, 1).equals("-")) {
               e = 1;
            }

            this.year = Integer.parseInt(newvalue.substring(0, e + 4));
            if(!newvalue.substring(e + 4, e + 5).equals("-")) {
               throw new StringParseException("invalid date format", 2);
            }

            this.month = Integer.parseInt(newvalue.substring(e + 5, e + 7));
            if(!newvalue.substring(e + 7, e + 8).equals("-")) {
               throw new StringParseException("invalid date format", 2);
            }

            this.day = Integer.parseInt(newvalue.substring(e + 8, newvalue.length()));
         } catch (NumberFormatException var3) {
            throw new StringParseException("invalid date format", 2);
         }

         this.isempty = false;
      }

   }

   protected void parseTime(String newvalue) throws StringParseException {
      if(newvalue.length() < 8) {
         throw new StringParseException("time-part of string is too short", 0);
      } else {
         try {
            byte e = 0;
            this.hour = Integer.parseInt(newvalue.substring(e, e + 2));
            if(!newvalue.substring(e + 2, e + 3).equals(":")) {
               throw new StringParseException("invalid date format", 2);
            }

            this.minute = Integer.parseInt(newvalue.substring(e + 3, e + 5));
            if(!newvalue.substring(e + 5, e + 6).equals(":")) {
               throw new StringParseException("invalid date format", 2);
            }

            this.second = Integer.parseInt(newvalue.substring(e + 6, e + 8));
            int nTZStartPosition = e + 8;
            this.partsecond = 0.0D;
            if(newvalue.length() > e + 8) {
               int e1 = nTZStartPosition;
               int nEnd = newvalue.length();
               int nMSecEnd = newvalue.indexOf("Z", nTZStartPosition);
               if(nMSecEnd > -1 && nMSecEnd < nEnd) {
                  nEnd = nMSecEnd;
               }

               nMSecEnd = newvalue.indexOf("+", nTZStartPosition);
               if(nMSecEnd > -1 && nMSecEnd < nEnd) {
                  nEnd = nMSecEnd;
               }

               nMSecEnd = newvalue.indexOf("-", nTZStartPosition);
               if(nMSecEnd > -1 && nMSecEnd < nEnd) {
                  nEnd = nMSecEnd;
               }

               nTZStartPosition = nEnd;
               this.partsecond = Double.parseDouble("0" + newvalue.substring(e1, nEnd));
            }

            this.hasTZ = 0;
            this.offsetTZ = 0;
            if(newvalue.length() > nTZStartPosition && newvalue.substring(nTZStartPosition, nTZStartPosition + 1).equals("Z")) {
               this.hasTZ = 1;
            } else if(newvalue.length() == nTZStartPosition + 6) {
               this.hasTZ = 2;
               this.offsetTZ = Integer.parseInt(newvalue.substring(nTZStartPosition + 1, nTZStartPosition + 3)) * 60 + Integer.parseInt(newvalue.substring(nTZStartPosition + 4, nTZStartPosition + 6));
               if(newvalue.substring(nTZStartPosition, nTZStartPosition + 1).equals("-")) {
                  this.offsetTZ = -this.offsetTZ;
               }
            }
         } catch (NumberFormatException var6) {
            throw new StringParseException("invalid number format", 2);
         }

         this.isempty = false;
      }
   }

   public double getApproximatedTotal() {
      return (double)this.second + 60.0D * ((double)this.minute + 60.0D * ((double)this.hour + 24.0D * ((double)this.day + 31.0D * ((double)this.month + 12.0D * (double)this.year)))) + this.partsecond;
   }

   public Date getDate() {
      String s = this.toDateString() + " " + this.toTimeString();

      try {
         return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(s);
      } catch (ParseException var3) {
         throw new StringParseException("Could not convert to date.", 0);
      }
   }

   protected void setInternalValues(int newyear, int newmonth, int newday, int newhour, int newminute, int newsecond, double newpartsecond, int newhasTZ, int newoffsetTZ) {
      this.year = newyear;
      this.month = newmonth;
      this.day = newday;
      this.hour = newhour;
      this.minute = newminute;
      this.second = newsecond;
      this.partsecond = newpartsecond;
      this.hasTZ = newhasTZ;
      this.offsetTZ = newoffsetTZ;
      this.isempty = false;
   }

   public SchemaDuration durationValue() {
      throw new TypesIncompatibleException(this, new SchemaDuration("PT"));
   }
}
