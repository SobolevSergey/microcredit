package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.exception.ReportException;
import ru.simplgroupp.interfaces.ReportBeanLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.persistence.ReportEntity;
import ru.simplgroupp.persistence.ReportTypeEntity;
import ru.simplgroupp.toolkit.common.Template;
import ru.simplgroupp.transfer.Report;
import ru.simplgroupp.transfer.ReportGenerated;
import ru.simplgroupp.transfer.ReportType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ReportBeanLocal.class)
public class ReportBean implements ReportBeanLocal {
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;	
    @Inject Logger logger;
    public static final String TEMPLATE_SIMPLE="template.simple";
    public static final String TEMPLATE_EL="template.el";
    
    @EJB
    ProductDAO productDAO;
    
    @EJB
    ServiceBeanLocal servBean;
    
	@Override
	public ReportGenerated generateReport(Integer productId,String code, Map<String,Object> params) throws ReportException {
	
		ReportEntity report=findReportByProductAndCode(productId,code);
		if (report==null){
			report=findReportByCode(code);
		}
		if (report == null) {
			logger.severe("Отчёт " + code + " не существует");
			throw new ReportException("Отчёт " + code + " не существует");
		}
		//обычный текстовый отчет
		if (TEMPLATE_SIMPLE.equalsIgnoreCase(report.getReportExecutor())) {
			ReportGenerated rgen = new ReportGenerated();
			rgen.setMimeType(report.getMimeType());
			
			Template tpl = new Template();
			tpl.setContent(report.getBody());
			
			for (Map.Entry<String, Object> param: params.entrySet()) {
				String svalue = "";
				//logger.info("Parameter "+param.getKey());
				//logger.info("Value "+param.getValue());
				if (param.getValue() != null) {
					svalue = param.getValue().toString();
				}
				tpl.replaceData(param.getKey().toString(), svalue);
			}
			rgen.setAsString(tpl.getContent());
			
			return rgen;
		} else if (TEMPLATE_EL.equalsIgnoreCase(report.getReportExecutor())) {
			ReportGenerated rgen = new ReportGenerated();
			rgen.setMimeType(report.getMimeType());			
			Object atext = servBean.evaluateEL(report.getBody(), params);
			rgen.setAsString((String) atext);
			return rgen;
		}
        logger.severe("Неизвестный тип генерации отчёта: " + code);
		throw new ReportException("Неизвестный тип генерации отчёта: " + code );
	}
	
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public ReportEntity getReportEntity(int id) {
		return emMicro.find(ReportEntity.class, new Integer(id));
	}
	
    @Override
	public Report getReport(int id, Set options) {
		ReportEntity ent = getReportEntity(id);
		if (ent == null) {
			return null;
		} else {
			Report rep = new Report(ent);
			rep.init(options);
			return rep;
		}
	}

	@Override
	public List<Report> listReports(int nFirst, int nCount, Set options,
			Integer reportType, String mimeType, String executor,
			String reportName,String code) {
		String hql="from ru.simplgroupp.persistence.ReportEntity where (1=1)";
		if (reportType!=null){
			hql+=" and reportTypeId.Id=:reportType ";
		}
		if (StringUtils.isNotEmpty(mimeType)){
			hql+=" and upper(mimeType) like :mimeType ";
		}
		if (StringUtils.isNotEmpty(executor)){
			hql+=" and upper(reportExecutor) like :executor ";
		}
		if (StringUtils.isNotEmpty(reportName)){
			hql+=" and upper(name) like :reportName ";
		}
		if (StringUtils.isNotEmpty(code)){
			hql+=" and code=:code ";
		}
		Query qry=emMicro.createQuery(hql);
		if (reportType!=null){
			qry.setParameter("reportType", reportType);
		}
		if (StringUtils.isNotEmpty(mimeType)){
			qry.setParameter("mimeType", "%"+mimeType.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(executor)){
			qry.setParameter("executor", "%"+executor.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(reportName)){
			qry.setParameter("reportName", "%"+reportName.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(code)){
			qry.setParameter("code", code);
		}
		if (nFirst >= 0) {
            qry.setFirstResult(nFirst);
		}
        if (nCount > 0) {
            qry.setMaxResults(nCount);
        }
        
		List<ReportEntity> lst=qry.getResultList();
		
		if (lst.size()>0){
			 List<Report> lst1 = new ArrayList<Report>(lst.size());
			 for (ReportEntity report : lst) {
			     Report creport = new Report(report);
			     lst1.add(creport);
			 }
			 return lst1;
		} else {
			 return new ArrayList<Report>(0);
		}

	}

	@Override
	public int countReports(Integer reportType, String mimeType,
			String executor, String reportName,String code) {
		List<Report> lst=listReports(0,0,null,reportType,mimeType,executor,reportName,code);
		return lst.size();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveReport(Report report) {
		ReportEntity reportEntity=getReportEntity(report.getId());
		if (reportEntity!=null){
			reportEntity.setBody(report.getBody());
			reportEntity.setMimeType(report.getMimeType());
			reportEntity.setName(report.getName());
			reportEntity.setReportExecutor(report.getReportExecutor());
			reportEntity.setCode(report.getCode());
			if (report.getProduct()!=null){
				reportEntity.setProductId(productDAO.getProduct(report.getProduct().getId()));
			}
			if (report.getReportType()!=null){
				reportEntity.setReportTypeId(getReportTypeEntity(report.getReportType().getId()));
			}
			reportEntity=emMicro.merge(reportEntity);
			emMicro.persist(reportEntity);
		}
		
	}

	@Override
	public List<ReportType> getReportTypes() {
		   
	        Query qry = emMicro.createNamedQuery("listReportType");
	     
	        List<ReportTypeEntity> lst = qry.getResultList();
	        List<ReportType> lstRes = new ArrayList<ReportType>(lst.size());
	        if (lst.size()>0){
	          for (ReportTypeEntity ent : lst) {
	            lstRes.add(new ReportType(ent));
	          }
	        }
	        return lstRes;
	}

	@Override
	public ReportType getReportType(int id) {
		ReportTypeEntity reportTypeEntity=getReportTypeEntity(id);
		if (reportTypeEntity!=null){
			ReportType reportType=new ReportType(reportTypeEntity);
			return reportType;
		}
		return null;
	}

	@Override
	public ReportTypeEntity getReportTypeEntity(int id) {
		ReportTypeEntity reportTypeEntity=emMicro.find(ReportTypeEntity.class, id);
		return reportTypeEntity;
	}
	
	@Override
	public List<ReportEntity> findReports(Integer reportTypeId,
			Integer productId, String code) {
		String hql = "from ru.simplgroupp.persistence.ReportEntity where (1=1) ";
	    if (reportTypeId!=null){
	    	hql+=" and reportTypeId.id=:reportTypeId ";
	    }
	    if (productId!=null){
	    	hql+=" and productId.id=:productId ";
	    }
	    if (StringUtils.isNotEmpty(code)){
	    	hql+=" and code=:code ";
	    }
	    Query qry = emMicro.createQuery(hql);
	    if (reportTypeId!=null){
	    	qry.setParameter("reportTypeId", reportTypeId);
	    }
	    if (productId!=null){
	    	qry.setParameter("productId", productId);
	    }
	    if (StringUtils.isNotEmpty(code)){
	    	qry.setParameter("code", code);
	    }
	    List<ReportEntity> lst = qry.getResultList();
		return lst;
	}

	@Override
	public ReportEntity findReportByProductAndCode(Integer productId,
			String code) {
		List<ReportEntity> lst=findReports(null,productId,code);
		if (lst.size()>0){
			return lst.get(0);
		}
		return null;
	}

	@Override
	public ReportEntity findReportByCode(String code) {
		List<ReportEntity> lst=findReports(null,null,code);
		if (lst.size()>0){
			return lst.get(0);
		}
		return null;
	}
}
