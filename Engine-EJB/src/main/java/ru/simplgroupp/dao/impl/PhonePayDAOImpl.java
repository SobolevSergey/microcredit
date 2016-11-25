package ru.simplgroupp.dao.impl;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ru.simplgroupp.dao.interfaces.PhonePayDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.PhonePayEntity;
import ru.simplgroupp.persistence.PhonePaySummaryEntity;
import ru.simplgroupp.transfer.RefHeader;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PhonePayDAOImpl implements PhonePayDAO{
	
	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@Override
	public void savePhonePays(PhonePaySummaryEntity summary,
			Integer periodNumber, Date firstDate, Integer count,
			Integer type, Double sum) throws KassaException {
		 PhonePayEntity phonePay=new PhonePayEntity();
		 phonePay.setSummaryId(summary);
		 phonePay.setPeriodNumber(periodNumber);
		 phonePay.setPeriodFirstDate(firstDate);
		 phonePay.setPaymentCount(count);
		 phonePay.setPaymentType(refBooks.findByCodeIntegerEntity(RefHeader.PHONE_PAY_TYPE, type));
		 phonePay.setPaymentSum(sum);
		 phonePay=emMicro.merge(phonePay);
		 emMicro.persist(phonePay);
		
	}

	@Override
	public PhonePayEntity getPhonePayEntity(Integer id) {
		return emMicro.find(PhonePayEntity.class, id);
	}
}
