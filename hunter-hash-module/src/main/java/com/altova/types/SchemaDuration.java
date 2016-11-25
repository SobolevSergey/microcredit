package com.altova.types;

import com.altova.types.SchemaCalendarBase;
import com.altova.types.SchemaDate;
import com.altova.types.SchemaDateTime;
import com.altova.types.SchemaString;
import com.altova.types.SchemaTime;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeCalendar;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.text.DecimalFormat;

public class SchemaDuration extends SchemaCalendarBase {
   protected boolean bNegative;

   public SchemaDuration() {
      this.setEmpty();
   }

   public SchemaDuration(SchemaDuration newvalue) {
      this.year = newvalue.year;
      this.month = newvalue.month;
      this.day = newvalue.day;
      this.hour = newvalue.hour;
      this.minute = newvalue.minute;
      this.second = newvalue.second;
      this.partsecond = newvalue.partsecond;
      this.hasTZ = newvalue.hasTZ;
      this.offsetTZ = newvalue.offsetTZ;
      this.bNegative = newvalue.isNegative();
      this.isempty = newvalue.isempty;
   }

   public SchemaDuration(int newyear, int newmonth, int newday, int newhour, int newminute, int newsecond, double newpartsecond, boolean newisnegative) {
      this.setYear(newyear);
      this.setMonth(newmonth);
      this.setDay(newday);
      this.setHour(newhour);
      this.setMinute(newminute);
      this.setSecond(newsecond);
      this.setPartSecond(newpartsecond);
      this.bNegative = newisnegative;
      this.isempty = false;
   }

   public SchemaDuration(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaDuration(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaDuration(SchemaTypeCalendar newvalue) {
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

   public boolean isNegative() {
      return this.bNegative;
   }

   public void setYear(int newyear) {
      if(newyear < 0) {
         this.year = -newyear;
         this.bNegative = true;
      } else {
         this.year = newyear;
      }

      this.isempty = false;
   }

   public void setMonth(int newmonth) {
      if(newmonth < 0) {
         this.month = -newmonth;
         this.bNegative = true;
      } else {
         this.month = newmonth;
      }

      this.isempty = false;
   }

   public void setDay(int newday) {
      if(newday < 0) {
         this.day = -newday;
         this.bNegative = true;
      } else {
         this.day = newday;
      }

      this.isempty = false;
   }

   public void setHour(int newhour) {
      if(newhour < 0) {
         this.hour = -newhour;
         this.bNegative = true;
      } else {
         this.hour = newhour;
      }

      this.isempty = false;
   }

   public void setMinute(int newminute) {
      if(newminute < 0) {
         this.minute = -newminute;
         this.bNegative = true;
      } else {
         this.minute = newminute;
      }

      this.isempty = false;
   }

   public void setSecond(int newsecond) {
      if(newsecond < 0) {
         this.second = -newsecond;
         this.bNegative = true;
      } else {
         this.second = newsecond;
      }

      this.isempty = false;
   }

   public void setPartSecond(double newpartsecond) {
      if(newpartsecond < 0.0D) {
         this.partsecond = -newpartsecond;
         this.bNegative = true;
      } else {
         this.partsecond = newpartsecond;
      }

      this.isempty = false;
   }

   public void setMillisecond(int newmillisecond) {
      int normalizedMSec = newmillisecond;
      int overflowSeconds;
      if(newmillisecond < 0) {
         overflowSeconds = newmillisecond / 1000 + 1;
         normalizedMSec = overflowSeconds * 1000 + newmillisecond;
         if(!this.bNegative) {
            this.second -= overflowSeconds;
         } else {
            this.second += overflowSeconds - 1;
         }
      }

      if(normalizedMSec >= 1000) {
         overflowSeconds = normalizedMSec / 1000;
         normalizedMSec %= 1000;
         if(!this.bNegative) {
            this.second += overflowSeconds;
         } else {
            this.second -= overflowSeconds;
         }
      }

      this.partsecond = (double)normalizedMSec / 1000.0D;
      this.isempty = false;
   }

   public void setNegative(boolean newisnegative) {
      this.bNegative = newisnegative;
      this.isempty = false;
   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull() && !newvalue.isEmpty()) {
         if(newvalue instanceof SchemaDuration) {
            this.setInternalValues(((SchemaDuration)newvalue).year, ((SchemaDuration)newvalue).month, ((SchemaDuration)newvalue).day, ((SchemaDuration)newvalue).hour, ((SchemaDuration)newvalue).minute, ((SchemaDuration)newvalue).second, ((SchemaDuration)newvalue).partsecond, 0, 0);
            this.bNegative = ((SchemaDuration)newvalue).bNegative;
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
      this.setInternalValues(0, 0, 0, 0, 0, 0, 0.0D, 0, 0);
      this.bNegative = false;
      this.isempty = true;
   }

   public Object clone() {
      return new SchemaDuration(this);
   }

   public String toString() {
      if(this.isempty) {
         return "";
      } else {
         StringBuffer s = new StringBuffer();
         if(this.bNegative) {
            s.append("-");
         }

         s.append("P");
         if(this.year != 0) {
            s.append((new DecimalFormat("0")).format((long)this.year));
            s.append("Y");
         }

         if(this.month != 0) {
            s.append((new DecimalFormat("0")).format((long)this.month));
            s.append("M");
         }

         if(this.day != 0) {
            s.append((new DecimalFormat("0")).format((long)this.day));
            s.append("D");
         }

         if(this.hour != 0 || this.minute != 0 || this.second != 0 || this.partsecond > 0.0D) {
            s.append("T");
            if(this.hour != 0) {
               s.append((new DecimalFormat("0")).format((long)this.hour));
               s.append("H");
            }

            if(this.minute != 0) {
               s.append((new DecimalFormat("0")).format((long)this.minute));
               s.append("M");
            }

            if(this.second != 0) {
               s.append((new DecimalFormat("0")).format((long)this.second));
            }

            if(this.partsecond > 0.0D && this.partsecond < 1.0D) {
               String sPartSecond = (new DecimalFormat("0.0###############")).format(this.partsecond);
               s.append(".");
               s.append(sPartSecond.substring(2, sPartSecond.length()));
            }

            if(this.second != 0 || this.partsecond > 0.0D && this.partsecond < 1.0D) {
               s.append("S");
            }
         }

         return s.toString();
      }
   }

   public boolean booleanValue() {
      return true;
   }

   public void parse(String newvalue) {
      int nStart = newvalue.indexOf("P");
      if(nStart < 0) {
         throw new StringParseException("P expected", 0);
      } else {
         if(nStart > 0 && newvalue.substring(nStart - 1, nStart).compareTo("-") == 0) {
            this.bNegative = true;
         } else {
            this.bNegative = false;
         }

         int nEnd = newvalue.indexOf("Y", nStart + 1);
         int nTPos = newvalue.indexOf("T", nStart + 1);
         int nLastEnd = nTPos > nEnd?nTPos:nEnd;

         try {
            if(nEnd > nStart) {
               this.year = Integer.parseInt(newvalue.substring(nStart + 1, nEnd));
               nStart = nEnd;
            } else {
               this.year = 0;
            }

            nEnd = newvalue.indexOf("M", nStart + 1);
            nLastEnd = nEnd > nLastEnd?nEnd:nLastEnd;
            if(nEnd > nStart && (nTPos == -1 || nEnd < nTPos)) {
               this.month = Integer.parseInt(newvalue.substring(nStart + 1, nEnd));
               nStart = nEnd;
            } else {
               this.month = 0;
            }

            nEnd = newvalue.indexOf("D", nStart + 1);
            nLastEnd = nEnd > nLastEnd?nEnd:nLastEnd;
            if(nEnd > nStart) {
               this.day = Integer.parseInt(newvalue.substring(nStart + 1, nEnd));
            } else {
               this.day = 0;
            }

            if(nTPos > -1) {
               nStart = nTPos;
               nEnd = newvalue.indexOf("H", nTPos + 1);
               nLastEnd = nEnd > nLastEnd?nEnd:nLastEnd;
               if(nEnd > nTPos) {
                  this.hour = Integer.parseInt(newvalue.substring(nTPos + 1, nEnd));
                  nStart = nEnd;
               } else {
                  this.hour = 0;
               }

               nEnd = newvalue.indexOf("M", nStart + 1);
               nLastEnd = nEnd > nLastEnd?nEnd:nLastEnd;
               if(nEnd > nStart) {
                  this.minute = Integer.parseInt(newvalue.substring(nStart + 1, nEnd));
                  nStart = nEnd;
               } else {
                  this.minute = 0;
               }

               this.second = 0;
               this.partsecond = 0.0D;
               int e = newvalue.indexOf(".", nStart + 1);
               nEnd = newvalue.indexOf("S", nStart + 1);
               nLastEnd = nEnd > nLastEnd?nEnd:nLastEnd;
               if(e == -1 && nEnd > nStart) {
                  this.second = Integer.parseInt(newvalue.substring(nStart + 1, nEnd));
               } else if(e > nStart && nEnd > e) {
                  this.second = Integer.parseInt(newvalue.substring(nStart + 1, e));
                  this.partsecond = Double.parseDouble("0." + newvalue.substring(e + 1, nEnd));
               }
            } else {
               nLastEnd = nLastEnd > -1?nLastEnd:0;
            }
         } catch (NumberFormatException var7) {
            throw new StringParseException("invalid duration format", 2);
         }

         if(nLastEnd + 1 < newvalue.length()) {
            throw new StringParseException("Invalid characters after the duration string", 2);
         } else if(this.year >= 0 && this.month >= 0 && this.day >= 0 && this.hour >= 0 && this.minute >= 0 && this.second >= 0 && this.partsecond >= 0.0D) {
            this.isempty = false;
         } else {
            throw new StringParseException("no negative values allowed in parts. Use \'-\' before \'P\'.", 3);
         }
      }
   }

   public int calendarType() {
      return 0;
   }

   public SchemaDuration durationValue() {
      return new SchemaDuration(this);
   }

   public SchemaDateTime dateTimeValue() {
      throw new TypesIncompatibleException(this, new SchemaDateTime("2003-07-28T12:00:00"));
   }

   public SchemaDate dateValue() {
      throw new TypesIncompatibleException(this, new SchemaDate("2003-07-28"));
   }

   public SchemaTime timeValue() {
      throw new TypesIncompatibleException(this, new SchemaTime("12:00:00"));
   }
}
