package ru.simplgroupp.ejb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.UploadingDAO;
import ru.simplgroupp.dao.interfaces.UploadingDetailDAO;
import ru.simplgroupp.dao.interfaces.UploadingErrorDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.service.UploadingService;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.persistence.UploadingErrorEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.Uploading;
import ru.simplgroupp.util.DatesUtils;

/**
 * сервис для работы с выгрузками в КБ
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UploadingServiceImpl implements UploadingService{
	
	@EJB
	CreditDAO creditDAO;
	
	@EJB
	UploadingDAO uploadingDAO;
	
	@EJB
	UploadingDetailDAO uploadingDetailDAO;
	
	@EJB
	UploadingErrorDAO uploadingErrorDAO;
	
	@Override
	public UploadingEntity getLastUploading(Integer partnerId){
		return getLastUploading(partnerId,null);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity saveUpload(UploadingEntity upl) throws KassaException {
		return uploadingDAO.saveUpload(upl);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateAfterUpload(Date sendingDate) {
		uploadingDAO.updateAfterUpload(sendingDate);
	}
	
	@Override
	public Integer findMaxUploadId(Integer partnerId) {
		return findMaxUploadId(partnerId,null);
	}

	@Override
	public Integer findMaxUploadId(Integer partnerId,Integer typeId) {
		UploadingEntity upl=getLastUploading(partnerId,typeId);
		if (upl!=null){
			if (upl.getUploadId()!=null) {
				return upl.getUploadId()+1;
			} 
		}
	    return 1;
	}
	
	@Override
	public List<UploadingDetailEntity> findUploadingDetail(Integer creditRequestId,
			Integer uploadingId, Integer status) {
 		return uploadingDetailDAO.findUploadingDetail(creditRequestId, uploadingId, status);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUploadingDetail(CreditRequestEntity creditRequest,
			UploadingEntity uploading, Integer status) {
	    uploadingDetailDAO.saveUploadingDetail(creditRequest, uploading, status);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeUploadingDetail(CreditRequestEntity creditRequest,
			UploadingEntity uploading, Integer status) {
		uploadingDetailDAO.changeUploadingDetail(creditRequest, uploading, status);
    }

	@Override
	public List<Uploading> listUploading(int nFirst, int nCount, Set options,
			DateRange uploadingDate, DateRange responseDate, Integer partnerId,
			Integer uploadId, Integer status,Integer typeId) {
	    return uploadingDAO.listUploading(nFirst, nCount, options, 
			uploadingDate, responseDate, partnerId, uploadId, status, typeId);
	}

	@Override
	public int countUploading(DateRange uploadingDate, DateRange responseDate,
			Integer partnerId, Integer uploadId, Integer status,Integer typeId) {
		List<Uploading> lst=listUploading(0,0,null,uploadingDate,responseDate,partnerId,uploadId,status,typeId);
		return lst.size();
	}
	
	@Override
	public List<CreditRequestEntity> getListForUpload(DateRange sendingDate,Integer creditRequestOnly) {
     	return uploadingDAO.getListForUpload(sendingDate, creditRequestOnly);
	}
	
	@Override
	public List<CreditRequestEntity> getListForUploadAll(Integer creditOnly) {
		return uploadingDAO.getListForUploadAll(creditOnly);
	}
	
	@Override
	public List<CreditRequestEntity> getListForUploadDaily(Date sendingDate) {
       return uploadingDAO.getListForUploadDaily(sendingDate);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Uploading getUploading(Integer id, Set options) {
		UploadingEntity uploadingEntity=uploadingDAO.getUploadingEntity(id);
		if (uploadingEntity!=null){
			Uploading uploading=new Uploading(uploadingEntity);
			if (options!=null&&options.size()>0){
				uploading.init(options);
			}
			return uploading;
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingDetailEntity saveDetail(UploadingDetailEntity detail)
			throws KassaException {
	    return uploadingDetailDAO.saveDetail(detail);
	}

	@Override
	public UploadingEntity getLastUploading(Integer partnerId, Integer type) {
		return uploadingDAO.getLastUploading(partnerId, type);
	}
	
	@Override
	public DateRange getDatesForUpload(Date sendingDate,Integer partnerId,Integer uploadTypeId){
		//выбрали дату последней выгрузки
		UploadingEntity upld=null;
		if(uploadTypeId==null){
			upld=getLastUploading(partnerId);
		} else {
			upld=getLastUploading(partnerId,uploadTypeId);
		}
		Date dt=null;
		//если есть последняя выгрузка, то берем ее дату, иначе выгружаем все
		 if (upld!=null){
			dt=upld.getDateUploading();
		 } 
		 //ищем в БД первый кредит
		 CreditEntity firstCredit=creditDAO.findFirstSystemCredit();
		 if (dt==null) {
			//если есть, берем его дату начала 
			if (firstCredit!=null){ 
			    dt=firstCredit.getCreditdatabeg(); 
			} else {
				dt=DatesUtils.makeDate(2015, 1, 1);
			}
		  }
		 
		 // TODO убрать потом
		/* if (upld==null){
		     dt=DatesUtils.makeDate(2016, 2, 9);
		 }*/
		 //
		 
		 //если явно не задали дату выгрузки, берем текущую дату	
		 if (sendingDate==null){
			sendingDate=new Date();
		 }
		 //диапазон дат для выгрузки
		 return new DateRange(dt,sendingDate);
	}

	@Override
	public List<CreditRequestEntity> getListForUpload(DateRange sendingDate,
			List<Integer> creditIds) {
		return uploadingDAO.getListForUpload(sendingDate, creditIds);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity newUploading(UploadingEntity uploading,
			Integer partnersId, Integer uploadTypeId, String name, String info,
			Date dateUploading, Integer statusId, Date responseDate,
			String report, Integer recordsAll, Integer recordsCorrect,
			Integer recordsError,boolean isCreated) throws KassaException{
		
		return uploadingDAO.newUploading(uploading, partnersId, uploadTypeId, name, info, 
				dateUploading, statusId, responseDate, report, recordsAll, recordsCorrect, recordsError, isCreated);
	
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingErrorEntity newUploadingError(Integer uploadingId,
			Integer creditId, Integer creditRequestId,String description) throws KassaException {
		return uploadingErrorDAO.newUploadingError(uploadingId, creditId, creditRequestId, description);
	}

}
