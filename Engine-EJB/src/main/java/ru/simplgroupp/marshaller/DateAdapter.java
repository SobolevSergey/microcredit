package ru.simplgroupp.marshaller;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Адаптер даты в строку
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public Date unmarshal(String string) throws Exception {
        return dateFormat.parse(string);
    }

    @Override
    public String marshal(Date date) throws Exception {
        return dateFormat.format(date);
    }
}
