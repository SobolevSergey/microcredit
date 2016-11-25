package ru.simplgroupp.reports.wrapper.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import ru.simplgroupp.reports.configuration.ReportsConfiguration;
import ru.simplgroupp.reports.wrapper.ReportCompiler;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * заполняет отчет из элементов коллекции
 */
@Stateless
public class ReportCompilerImpl implements ReportCompiler{
    @Inject
    private ReportsConfiguration reportsConfiguration;

    /**
     * компилирует отчет в виде html используя коллекцию элементов для отображения с использованием библиотеки jasperreports
     * @param filename имя файла шаблона jrxml
     * @param items коллекция элементов для отображения в отчете
     * @param params дополнительные параметры для отображения в отчете
     * @return
     * @throws JRException
     */
    @Override
    public String compile(String filename, Collection<?> items, Map<String,Object> params) throws JRException {
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(items);
        JasperPrint jasperPrint = JasperFillManager.fillReport(new StringBuilder(reportsConfiguration.getPathToCompiledReports()).append(filename).toString(), params, beanColDataSource);
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        StringBuffer stringBuffer = new StringBuffer();
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(stringBuffer));
        htmlExporter.exportReport();
        return stringBuffer.toString();

    }

    /**
     * компилирует отчет в виде html используя коллекцию элементов для отображения с использованием библиотеки jasperreports
     * @param filename имя файла шаблона jrxml
     * @param items коллекция элементов для отображения в отчете
     * @return
     * @throws JRException
     */
    @Override
    public String compile(String filename, Collection<?> items) throws JRException {
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(items);
        JasperPrint jasperPrint = JasperFillManager.fillReport(new StringBuilder(reportsConfiguration.getPathToCompiledReports()).append(filename).toString(), new HashMap<String, Object>(), beanColDataSource);
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        StringBuffer stringBuffer = new StringBuffer();
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(stringBuffer));
        htmlExporter.exportReport();
        return stringBuffer.toString();

    }
    /**
     * экспортирует отчет в excel формат в outputStream используя коллекцию элементов для отображения с использованием библиотеки jasperreports
     * @param filename имя файла шаблона jrxml
     * @param items коллекция элементов для отображения в отчете
     * @return
     * @throws JRException
     */
    @Override
    public String exportToOutputStream(String filename,Collection<?> items,OutputStream output) throws JRException {
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(items);
        JasperPrint jasperPrint = JasperFillManager.fillReport(new StringBuilder(reportsConfiguration.getPathToCompiledReports()).append(filename).toString(), new HashMap<String, Object>(), beanColDataSource);
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        StringBuffer stringBuffer = new StringBuffer();
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
        xlsExporter.exportReport();
        return stringBuffer.toString();

    }
    /**
     * экспортирует отчет в excel формат в outputStream используя коллекцию элементов для отображения с использованием библиотеки jasperreports
     * @param filename имя файла шаблона jrxml
     * @param items коллекция элементов для отображения в отчете
     * @return
     * @throws JRException
     */
    @Override
    public String exportToOutputStream(String filename,Collection<?> items,OutputStream output,Map<String,Object> parameters) throws JRException {
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(items);
        JasperPrint jasperPrint = JasperFillManager.fillReport(new StringBuilder(reportsConfiguration.getPathToCompiledReports()).append(filename).toString(), parameters, beanColDataSource);
        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        StringBuffer stringBuffer = new StringBuffer();
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
        xlsExporter.exportReport();
        return stringBuffer.toString();

    }
}
