package ru.simplgroupp.ejb.plugins.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.AbstractPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.ManualPayBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.persistence.PaymentEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ManualPayBeanLocal.class)
public class ManualPayBean  extends AbstractPluginBean implements ManualPayBeanLocal {
	
	@EJB 
	ServiceBeanLocal servBean;	
	
	@EJB
	WorkflowBeanLocal workflow; 	 
	
	@EJB
	ReferenceBooksLocal refBooks;
	
	@EJB
	KassaBeanLocal kassaBean;

    @EJB
    PaymentService paymentService;
    
    @EJB
    PaymentDAO payDAO;

	@Override
	public String getSystemName() {
		return ManualPayBeanLocal.SYSTEM_NAME;
	}

	@Override
	public boolean isFake() {
		return false;
	}
	
	@Override
	public Set<String> getModelTargetsSupported() {
		return Utils.setOf();
	}	

	@Override
	public EnumSet<Mode> getModesSupported() {
		return EnumSet.of(Mode.PACKET);
	}

	@Override
	public EnumSet<ExecutionMode> getExecutionModesSupported() {
		return EnumSet.of(ExecutionMode.MANUAL);
	}

	@Override
	public EnumSet<SyncMode> getSyncModesSupported() {
		return EnumSet.of(SyncMode.SYNC);
	}

	@Override
	public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToPacket(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		Integer paymentId = (Integer) businessObjectId;

		Partner par = refBooks.getPartner(Partner.SYSTEM);
		PaymentEntity pay = payDAO.getPayment(paymentId);
		pay.setPartnersId(par.getEntity());
        paymentService.savePayment(pay);
	}

	@Override
	public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
		List<BusinessObjectResult> lstRes = new ArrayList<BusinessObjectResult>( context.getTaskResults().size() );
		for (BusinessObjectResult bres: context.getTaskResults()) {
			if (bres.getError() != null) {
				// объект обработан с ошибками, возвращаем его как есть, пост-обработку пропускаем
				lstRes.add(bres);
				continue;
			}
			
			if (bres.getAnswered()) {
				// всё сделано в ручной задаче, пост-обработка не нужна
				lstRes.add(bres);
				continue;
			}
			
			// пост-обработка нужна
			Integer paymentId = (Integer) bres.getBusinessObjectId();
			PaymentEntity pay =  payDAO.getPayment(paymentId);
			pay.setIsPaid(true);
			pay.setProcessDate(new Date());
            paymentService.savePayment(pay);
			// объект успешно обработан
			lstRes.add(new BusinessObjectResult(bres.getBusinessClassName(), bres.getBusinessObjectId(), true, null));
		}
		return lstRes;
	}

	@Override
	public boolean sendSingleRequest(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean querySingleResponse(String businessObjectClass,
			Object businessObjectId, PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getSupportedAccountTypes() {
		return new int[] {Account.BANK_TYPE, Account.CARD_TYPE, Account.CONTACT_TYPE, Account.YANDEX_TYPE};
	}
	
	@Override
	public String getBusinessObjectClass() {
		return Payment.class.getName();
	}

	@Override
	public String getSystemDescription() {
		return ManualPayBeanLocal.SYSTEM_DESCRIPTION;
	}

}
