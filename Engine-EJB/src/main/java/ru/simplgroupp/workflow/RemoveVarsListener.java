package ru.simplgroupp.workflow;

import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.Expression;
import org.apache.commons.lang.StringUtils;

/**
 * Удаляет указанные локальные переменные. В поле variableNames перечислить имена переменных для удаления через запятую.
 * @author irina
 *
 */
public class RemoveVarsListener implements ExecutionListener  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3132678103535244135L;
	
	Expression variableNames;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String svars = variableNames.getValue(execution).toString();
		if (StringUtils.isEmpty(svars)) {
			return;
		}
		String[] svarnames = svars.split(",");
		ArrayList<String> lst = new ArrayList<String>(svarnames.length);
		for (String ss: svarnames) {
			if ( ! StringUtils.isEmpty(ss)) {
				lst.add(ss);
			}
		}
		execution.removeVariablesLocal(lst);	
	}

}
