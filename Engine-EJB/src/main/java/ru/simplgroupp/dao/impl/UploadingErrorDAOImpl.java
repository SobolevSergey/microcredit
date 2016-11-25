package ru.simplgroupp.dao.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.UploadingDAO;
import ru.simplgroupp.dao.interfaces.UploadingErrorDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingErrorEntity;
import ru.simplgroupp.toolkit.common.Convertor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UploadingErrorDAOImpl implements UploadingErrorDAO{

	@PersistenceContext(unitName = "MicroPU")
	protected EntityManager emMicro;
	
	@EJB
	CreditDAO creditDAO;
	
	@EJB
	CreditRequestDAO crDAO;
	
	@EJB
	UploadingDAO uploadingDAO;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingErrorEntity newUploadingError(Integer uploadingId,
			Integer creditId, Integer creditRequestId,String description) throws KassaException {
		 UploadingErrorEntity uerror=new UploadingErrorEntity();
		 uerror.setUploadingId(uploadingDAO.getUploadingEntity(uploadingId));
		 //id кредита
		 if (creditId!=null){
		     CreditEntity credit=creditDAO.getCreditEntity(creditId);
		     uerror.setCreditId(credit);
		 }
		//id заявки
		 if (creditRequestId!=null){
		     CreditRequestEntity creditRequest=crDAO.getCreditRequestEntity(creditRequestId);
		     uerror.setCreditRequestId(creditRequest);
		 }
		 uerror.setDescription(Convertor.toLimitString(description,255));
	     emMicro.persist(uerror);
		 return uerror;
	}

	@Override
	public UploadingErrorEntity getUploadingErrorEntity(Integer id) {
		return emMicro.find(UploadingErrorEntity.class, id);
	}
}
