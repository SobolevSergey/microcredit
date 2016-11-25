package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.Expression;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.toolkit.TransientMap;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.WorkflowUtil;

import javax.ejb.EJB;
import java.util.Map;

public class SendSMSNotifyAction implements JavaDelegate {
    @EJB
    protected ActionProcessorBeanLocal actionProcessor;

    Expression smsCode;


    protected void checkActionProcessor() {
        if (actionProcessor == null) {
            actionProcessor = (ActionProcessorBeanLocal) Context.getProcessEngineConfiguration().getBeans().get(ProcessKeys.VAR_ACTION_PROCESSOR);
        }
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        checkActionProcessor();

        //ищем в переменных процесса класс бизнес-объекта и его id
        Map<String, Object> varsLocal = execution.getVariablesLocal();
        String businessObjectClass = (String) varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_CLASS);
        Object businessObjectid = varsLocal.get(ProcessKeys.VAR_BUSINESS_OBJECT_ID);

        Integer peopleMainId = null;
        Integer productId = null;

        TransientMap<String, Object> trm = (TransientMap<String, Object>) execution.getVariable(ProcessKeys.VAR_RUNTIME_VARS);
        if (trm == null) {
            trm = new TransientMap<>(1);
        }

        PeopleMain client = null;
        if (Credit.class.getName().equals(businessObjectClass)) {
            Credit credit = actionProcessor.getCredit(Convertor.toInteger(businessObjectid), Utils.setOf());
            if (credit != null) {
                client = credit.getPeopleMain();
                peopleMainId = credit.getPeopleMain().getId();
                if (credit.getProduct() != null) {
                    productId = credit.getProduct().getId();
                }
                trm.put(ProcessKeys.VAR_CREDIT, credit);
                trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());
            }
        } else if (CreditRequest.class.getName().equals(businessObjectClass)) {
            CreditRequest ccRequest = actionProcessor.getCreditRequest(Convertor.toInteger(businessObjectid), Utils.setOf());
            if (ccRequest != null) {
                client = ccRequest.getPeopleMain();
                peopleMainId = ccRequest.getPeopleMain().getId();
                if (ccRequest.getProduct() != null) {
                    productId = ccRequest.getProduct().getId();
                }
                trm.put(ProcessKeys.VAR_CREDIT_REQUEST, ccRequest);
            }
        } else if (Payment.class.getName().equals(businessObjectClass)) {
            Payment pay = actionProcessor.getPayment(Convertor.toInteger(businessObjectid), Utils.setOf());
            if (pay != null) {
                client = pay.getCredit().getPeopleMain();
                peopleMainId = pay.getCredit().getPeopleMain().getId();
                if (pay.getCredit().getProduct() != null) {
                    productId = pay.getCredit().getProduct().getId();
                }
                trm.put(ProcessKeys.VAR_PAYMENT, pay);
                Credit credit = pay.getCredit();
                trm.put(ProcessKeys.VAR_CREDIT, credit);
                trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());
            }
        } else if (Prolong.class.getName().equals(businessObjectClass)) {
            Prolong prolong = actionProcessor.getProlong(Convertor.toInteger(businessObjectid), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
            if (prolong != null) {
                trm.put(ProcessKeys.VAR_PROLONG, prolong);
                Credit credit = prolong.getCredit();
                trm.put(ProcessKeys.VAR_CREDIT, credit);
                trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());
                client = credit.getPeopleMain();
                peopleMainId = client.getId();
                if (credit.getProduct() != null) {
                    productId = credit.getProduct().getId();
                }
            }
        } else if (Refinance.class.getName().equals(businessObjectClass)) {
            Refinance refinance = actionProcessor.getRefinance(Convertor.toInteger(businessObjectid), Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
            if (refinance != null) {
                trm.put(ProcessKeys.VAR_REFINANCE, refinance);
                Credit credit = refinance.getCredit();
                trm.put(ProcessKeys.VAR_CREDIT, credit);
                trm.put(ProcessKeys.VAR_CREDIT_REQUEST, credit.getCreditRequest());
                client = credit.getPeopleMain();
                peopleMainId = client.getId();
                if (credit.getProduct() != null) {
                    productId = credit.getProduct().getId();
                }
            }
        }

        execution.setVariable(ProcessKeys.VAR_RUNTIME_VARS, trm);

        String smsMessageCode = smsCode.getValue(execution).toString();
        String procDefKey = WorkflowUtil.extractProcessDefKey(execution.getProcessDefinitionId());

        execution.removeVariable(ProcessKeys.VAR_RUNTIME_VARS);

        actionProcessor.sendSmsNotification(productId, peopleMainId, procDefKey, smsMessageCode);
    }

}
