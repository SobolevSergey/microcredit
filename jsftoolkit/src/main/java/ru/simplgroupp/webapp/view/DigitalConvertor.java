package ru.simplgroupp.webapp.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Remove all non digital symbols from string
 */
@FacesConverter("ru.simplgroupp.webapp.view.DigitalConvertor")
public class DigitalConvertor implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        } else {
            return value.replaceAll("[^\\d]", "");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
