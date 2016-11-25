package com.altova.types;

import com.altova.types.SchemaDate;
import com.altova.types.SchemaDateTime;
import com.altova.types.SchemaDuration;
import com.altova.types.SchemaTime;
import com.altova.types.SchemaType;

public interface SchemaTypeCalendar extends SchemaType {
   int CALENDAR_VALUE_UNDEFINED = -1;
   int CALENDAR_VALUE_DURATION = 0;
   int CALENDAR_VALUE_DATETIME = 1;
   int CALENDAR_VALUE_DATE = 2;
   int CALENDAR_VALUE_TIME = 3;

   int calendarType();

   SchemaDuration durationValue();

   SchemaDateTime dateTimeValue();

   SchemaDate dateValue();

   SchemaTime timeValue();
}
