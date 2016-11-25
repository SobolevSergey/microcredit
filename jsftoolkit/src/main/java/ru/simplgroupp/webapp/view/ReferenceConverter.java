package ru.simplgroupp.webapp.view;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.Reference;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ReferenceConverter implements Converter {

    @EJB
    protected ReferenceBooksLocal refBooks;

    String ejbRef = "java:global/microcredit/engine-ejb/ReferenceBooks!ru.simplgroupp.interfaces.ReferenceBooksLocal";

    @Override
    public Object getAsObject(FacesContext facesCtx, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        checkRefBooks();

        int id = Convertor.toInteger(value);
        Reference ref = refBooks.getReference(id);
        return ref;
    }

    @Override
    public String getAsString(FacesContext facesCtx, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else {
            return ((Reference) value).getId().toString();
        }
    }

    private void checkRefBooks() {
        if (refBooks == null) {
            refBooks = (ReferenceBooksLocal) EJBUtil.findEJB(ejbRef);
        }
    }
}
