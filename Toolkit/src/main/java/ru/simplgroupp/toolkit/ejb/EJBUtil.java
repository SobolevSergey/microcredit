package ru.simplgroupp.toolkit.ejb;

import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJBUtil {

    private static Logger log = Logger.getLogger(EJBUtil.class.getName());

    public static Object findEJB(String fullName) {
        try {
            InitialContext ctx = new InitialContext();
            Object aobj = ctx.lookup(fullName);
            return aobj;
        } catch (NamingException e) {
            log.severe("Не удалось найти ejb " + fullName + ", " + e);
            return null;
        }
    }
}
