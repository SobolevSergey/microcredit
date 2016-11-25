package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.ReportException;
import ru.simplgroupp.persistence.ReportEntity;
import ru.simplgroupp.persistence.ReportTypeEntity;
import ru.simplgroupp.transfer.Report;
import ru.simplgroupp.transfer.ReportGenerated;
import ru.simplgroupp.transfer.ReportType;

import javax.ejb.Local;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Local
public interface ReportBeanLocal {

    /**
     * возвращает представление отчета
     *
     * @param reportId вид отчета
     * @param params   параметры, передаваемые в отчет
     */
    public ReportGenerated generateReport(Integer productId,String code, Map<String, Object> params) throws ReportException;

    /**
     * возвращает запись отчета по id
     *
     * @param id id отчета
     */
    public ReportEntity getReportEntity(int id);

    /**
     * возвращает запись отчета по id, трансферный объект
     *
     * @param id id отчета
     */
    public Report getReport(int id, Set options);

    /**
     * список отчетов
     *
     * @param nFirst     первый номер
     * @param nCount     сколько на страницу
     * @param options    параметры для инициализации
     * @param reportType вид отчета
     * @param mimeType   тип mime
     * @param executor   как выполняем
     * @param reportName название отчета
     */
    public List<Report> listReports(int nFirst, int nCount, Set options, Integer reportType, String mimeType, 
    		String executor, String reportName,String code);

    /**
     * посчитать отчеты
     *
     * @param reportType вид отчета
     * @param mimeType   тип mime
     * @param executor   как выполняем
     * @param reportName название отчета
     */
    public int countReports(Integer reportType, String mimeType, String executor, String reportName,String code);

    /**
     * сохраняем измененные данные отчета
     *
     * @param report данные отчета
     */
    public void saveReport(Report report);

    /**
     * возвращает список видов отчетов
     */
    public List<ReportType> getReportTypes();

    /**
     * возвращает вид отчета по id
     *
     * @param id id вида отчета
     */
    public ReportType getReportType(int id);
    /**
     * возвращает вид отчета по id
     *
     * @param id id вида отчета
     */
    public ReportTypeEntity getReportTypeEntity(int id);
    /**
     * ищем отчеты
     * 
     * @param reportTypeId - вид отчета
     * @param productId - продукт
     * @param code - код отчета
     * @return
     */
    public List<ReportEntity> findReports(Integer reportTypeId,Integer productId,String code);
    /**
     * ищем отчет по продукту и коду
     * 
     * @param productId - продукт
     * @param code - код
     * @return
     */
    public ReportEntity findReportByProductAndCode(Integer productId,String code);
    /**
     * ищем отчет по коду
     * 
     * @param code - код
     * @return
     */
    public ReportEntity findReportByCode(String code);
}
