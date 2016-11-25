package ru.simplgroupp.webapp.view;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.ejb.EJBUtil;
import ru.simplgroupp.transfer.Products;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ProductsConverter implements Converter {

    @EJB
    protected ProductDAO productDAO;

    String ejbProd = "java:global/microcredit/engine-ejb/ProductDAOImpl!ru.simplgroupp.dao.interfaces.ProductDAO";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        checkService();

        int id = Convertor.toInteger(value);
        Products ref = productDAO.getProduct(id, null);
        return ref;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else {
            return ((Products) value).getId().toString();
        }
    }

    private void checkService() {
        if (productDAO == null) {
            productDAO = (ProductDAO) EJBUtil.findEJB(ejbProd);
        }
    }
}
