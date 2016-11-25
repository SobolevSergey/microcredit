package ru.simplgroupp.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * Удаляем запрос на платёж
 * @author irina
 *
 */
public class ClearNeedPaymentListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2801782537808460885L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Map<String,Object> mpVars = execution.getVariablesLocal();
		List<String> mpRemove = new ArrayList<String>(5);
		for (Map.Entry<String, Object> ent: mpVars.entrySet()) {
			if (ent.getKey().startsWith(ProcessKeys.VAR_PAYMENT_FORM)) {
				mpRemove.add(ent.getKey());
			}
		}
		if (mpRemove.size() > 0) {
			execution.removeVariablesLocal(mpRemove);
		}
		
	}

}
