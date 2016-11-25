package ru.simplgroupp.reports.wrapper;

import net.sf.jasperreports.engine.JRException;

import javax.ejb.Remote;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * заполняет отчет из элементов коллекции
 */
public interface ReportCompiler{
    String compile(String filename, Collection<?> items, Map<String, Object> params) throws JRException;

    public String compile(String filename, Collection<?> items) throws JRException;
    public String exportToOutputStream(String filename,Collection<?> items,OutputStream output) throws JRException;

    String exportToOutputStream(String filename, Collection<?> items, OutputStream output, Map<String, Object> parameters) throws JRException;
}
