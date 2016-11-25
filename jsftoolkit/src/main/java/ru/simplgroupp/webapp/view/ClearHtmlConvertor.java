package ru.simplgroupp.webapp.view;

import org.apache.commons.lang.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * 03.09.2015
 * 12:19
 */

public class ClearHtmlConvertor implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return value.replaceAll("<[^>]*>","");
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null) {
            return null;
        }
        return value.toString().replaceAll("<[^>]*>", "");
    }
}
