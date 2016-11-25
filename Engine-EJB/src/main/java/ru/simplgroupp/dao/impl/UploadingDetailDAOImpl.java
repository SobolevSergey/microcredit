package ru.simplgroupp.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.UploadingDetailDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.persistence.UploadingEntity;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UploadingDetailDAOImpl implements UploadingDetailDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
   	@Override
	public void changeUploadingDetail(CreditRequestEntity creditRequest,
			UploadingEntity uploading, Integer status) {
		String sql="update ru.simplgroupp.persistence.UploadingDetailEntity set status=:status where uploadingId.id=:uploadingId ";
		if (creditRequest!=null){
			sql+=" and creditRequestId.id=:creditRequestId ";
		}
		Query qry = emMicro.createQuery(sql);
		qry.setParameter("status", status);
		qry.setParameter("uploadingId", uploading.getId());
		if (creditRequest!=null){
			qry.setParameter("creditRequestId", creditRequest.getId());
		}
		qry.executeUpdate();	
		
	}
	
	@Override
	public void saveUploadingDetail(CreditRequestEntity creditRequest,
			UploadingEntity uploading, Integer status) {
		
		UploadingDetailEntity uploadingDetail=new UploadingDetailEntity();
		uploadingDetail.setCreditRequestId(creditRequest);
		uploadingDetail.setCreditId(creditRequest.getAcceptedcreditId());
		uploadingDetail.setUploadingId(uploading);
		uploadingDetail.setStatus(status);
		uploadingDetail=emMicro.merge(uploadingDetail);
		emMicro.persist(uploadingDetail);
	}
	
	@Override
	public UploadingDetailEntity saveDetail(UploadingDetailEntity detail)
			throws KassaException {
		detail=emMicro.merge(detail);
		emMicro.persist(detail);
		return detail;
	}
	
	@Override
	public List<UploadingDetailEntity> findUploadingDetail(Integer creditRequestId,
			Integer uploadingId, Integer status) {
        
		String hql="from ru.simplgroupp.persistence.UploadingDetailEntity where (1=1)";
		
		if (creditRequestId!=null){
			hql+=" and creditRequestId.id=:creditRequestId ";
		}
		if (uploadingId!=null){
			hql+=" and uploadingId.id=:uploadingId ";
		}
		if (status!=null){
			hql+=" and status=:status ";
		}
		Query qry=emMicro.createQuery(hql);
		if (creditRequestId!=null){
			qry.setParameter("creditRequestId", creditRequestId);
		}
		if (uploadingId!=null){
			qry.setParameter("uploadingId", uploadingId);
		}
		if (status!=null){
			qry.setParameter("status", status);
		}
		List<UploadingDetailEntity> lst=qry.getResultList();
		return lst;
	}

	@Override
	public UploadingDetailEntity getUploadingDetailEntity(Integer id) {
		return emMicro.find(UploadingDetailEntity.class, id);
	}

}
