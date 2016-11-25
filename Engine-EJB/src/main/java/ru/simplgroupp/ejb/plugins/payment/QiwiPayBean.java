package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.ejb.PaymentProcessorBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.plugins.payment.QiwiPayBeanLocal;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;

import java.util.EnumSet;
import java.util.List;

/**
 * Плугин для передачи займа через qiwi
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class QiwiPayBean extends PaymentProcessorBean implements QiwiPayBeanLocal {

    @Override
    public String getSystemName() {
        return QiwiPayBeanLocal.SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return QiwiPayBeanLocal.SYSTEM_DESCRIPTION;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE, Mode.PACKET);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.ASYNC);
    }

    @Override
    public String getBusinessObjectClass() {
        return Payment.class.getName();
    }

    private String buildPaymentRequest(QiwiPayPluginConfig config) {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        xml.append("<request>")
                .append("<request-type>pay</request-type>")
                .append("<terminal-id>").append(config.getTerminalId()).append("</terminal-id>")
            .append("</request>")
        ;
        return xml.toString();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public int[] getSupportedAccountTypes() {
        return new int[0];
    }
    
	@Override
	public Integer getPartnerId() {
		return Partner.QIWI;
	}     
}
