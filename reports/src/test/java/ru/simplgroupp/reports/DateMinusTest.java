package ru.simplgroupp.reports;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by victor on 16.09.15.
 */
public class DateMinusTest {
    private static final Logger logger = Logger.getLogger(DateMinusTest.class.getName());
    private Date date;
    @Before
    public void setUp() throws ParseException {
        date = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("01-01-2015 23:00");
    }
    @Test
    public void testMinusDay(){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -2);
        logger.info(c.getTime()+" ");
    }
}
