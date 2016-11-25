package ru.simplgroupp.webapp.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 * Passport serial convertor
 */
@FacesConverter("ru.simplgroupp.webapp.view.PassportSerialConvertor")
public class PassportSerialConvertor extends DigitalConvertor{

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        String serial = (String)value;
        if (serial.length() == 4) {
            return serial.substring(0, 2) + "-" + serial.substring(2);
        }

        return serial;
    }
}
