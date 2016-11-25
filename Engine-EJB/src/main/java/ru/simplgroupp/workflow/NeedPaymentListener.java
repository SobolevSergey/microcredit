package ru.simplgroupp.workflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.Expression;

import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.util.PaymentUtils;

/**
 * Создаём запрос на платёж
 * @author irina
 *
 */
public class NeedPaymentListener implements ExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Expression sumFrom;
	Expression sumTo;
	Expression type;
	Expression sumId;

	@Override
	public void notify(DelegateExecution execution) throws Exception {

		Number nSumFrom = null, nSumTo = null;
		Number nType = Payment.TO_SYSTEM;
		Number nSumId = null;
		if (sumFrom != null) {
			nSumFrom = (Number) sumFrom.getValue(execution);
		}
		if (sumTo != null) {
			nSumTo = (Number) sumTo.getValue(execution);
		}
		if (type != null) {
			nType = (Number) type.getValue(execution);
		}
		if (sumId != null) {
			nSumId = (Number) sumId.getValue(execution);
		}
		
		execution.setVariablesLocal(PaymentUtils.paymentAsParams(nType, nSumId, nSumFrom, nSumTo));
		
	}

}
