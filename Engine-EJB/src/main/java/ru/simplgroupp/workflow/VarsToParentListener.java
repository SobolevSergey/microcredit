package ru.simplgroupp.workflow;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.Expression;

public class VarsToParentListener implements ExecutionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7981994572282366739L;
	Expression varsFrom;
	Expression varsTo;

	@Override
	public void notify(DelegateExecution execution) throws Exception {			
		String varNamesFrom = varsFrom.getValue(execution).toString();
		String varNamesTo = varsTo.getValue(execution).toString();
		
		String[] avfrom = varNamesFrom.split(",");
		String[] avto = varNamesTo.split(",");
		
		Map<String, Object> varsLocal = execution.getVariablesLocal();
		
		Map<String, Object> varsTo = new HashMap<String,Object>(avto.length);
		for (int i = 0; i < avfrom.length; i++) {
			Object avalue = varsLocal.get(avfrom[i]);
			if (avalue != null) {
				varsTo.put(avto[i], avalue);
			}
		}
		execution.getEngineServices().getRuntimeService().setVariablesLocal(execution.getParentId(), varsTo);
		
	}

}
