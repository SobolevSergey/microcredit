package ru.simplgroupp.webapp.view;

import ru.simplgroupp.toolkit.common.Convertor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * конвертирует номер вида 79992221133 в +7 (999) 222-11-33
 */
public class PhoneNumberConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return s;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object s) {
        String phoneNumber = Convertor.toPhoneFormat((String) s);
        if (phoneNumber == null) {
            return (String) s;
        }

        return phoneNumber;
    }
}
