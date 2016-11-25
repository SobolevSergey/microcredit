package ru.simplgroupp.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.simplgroupp.toolkit.common.NameValuePair;
import ru.simplgroupp.util.DecisionKeys;

public class DecisionState implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2327078028481576211L;
	
	public static final int STATE_IN_PROCESS = 1;
	public static final int STATE_COMPLETED_OK = 2;
	public static final int STATE_COMPLETED_ERRORS = 3;
	
	/**
	 * результат
	 */
	private int resultCode;
	/**
	 * состояние
	 */
	private int state;
	/**
	 * внешние подсистемы
	 */
	private Map<String,ExternalSystemState> extSystems = new HashMap<String,ExternalSystemState>(3);
	/**
	 * следующий шаг
	 */
	private String nextLabel;
	/**
	 * ошибки
	 */
	private List<NameValuePair> errors = new ArrayList<NameValuePair>(3);
	/**
	 * переменные
	 */
	private Map<String,Object> vars = new HashMap<String, Object>(10);
	private boolean saveResultOnce = false;

	public DecisionState() {
		super();
		
		//оценки
		vars.put("score_1", (int) 0);
		vars.put("score_2", (int) 0);
		vars.put("score_3", (int) 0);
		vars.put("score_4", (int) 0);
		vars.put("score_5", (int) 0);
		
		vars.put("margin", (float) 0);
		vars.put("ncl", (float) 0);
		vars.put("var_costs", (float) 0);
		
		vars.put("challenger", new Boolean(false));
		vars.put("verification", new Boolean(false));

		vars.put("risk_group", new String());
	}
	
	public boolean isInProcess() {
		return (state == STATE_IN_PROCESS);
	}
	
	public boolean isCompleted() {
		return (state > STATE_IN_PROCESS);
	}
	
	public boolean isCompletedOK() {
		return (state == STATE_COMPLETED_OK);
	}
	
	public boolean isCompletedErrors() {
		return (state == STATE_COMPLETED_ERRORS);
	}
	
	public boolean hasErrors() {
		return (errors.size() > 0);
	}
	
	public NameValuePair findError(String errorCode) {
		for (NameValuePair err: errors) {
			if (errorCode.equalsIgnoreCase(err.getValue().toString()) ) {
				return err;
			}
		}
		return null;
	}
	
	public void start() {
		state = STATE_IN_PROCESS;
	}
	
	public void addError(String errorCode, String errorText) {
		NameValuePair pair = new NameValuePair(errorCode, errorText);
		errors.add(pair);
	}
	
	public List<NameValuePair> getErrors() {
		return errors;
	}
	
	public void internalFinish(boolean bOK, Object resultCode) {
		
		int nResultCode = DecisionKeys.RESULT_MANUAL;
		if (resultCode != null && resultCode instanceof Number) {
			 nResultCode = ((Number) resultCode).intValue();
		}
		
		if (bOK) {
			state = STATE_COMPLETED_OK;
			errors.clear();
			this.resultCode = nResultCode;
		} else {			
			state = STATE_COMPLETED_ERRORS;
			if (errors.size() == 0) {
				addError("000", "Неизвестная ошибка");
			}
		}
	}		
	
	public void finish(boolean bOK, Object resultCode) {
		if (saveResultOnce && isCompleted() ) {
			return;
		}
		
		internalFinish(bOK, resultCode);
	}	
	
	public void reset() {
		state = 0;
	}

	public Map<String,ExternalSystemState> getExternalSystems() {
		return extSystems;
	}
	
	private List<String> getExtCalls(String prefix) {
		ArrayList<String> lst = new ArrayList<String>(extSystems.size());
		for (Map.Entry<String,ExternalSystemState> entry: extSystems.entrySet()) {
			if (entry.getValue().isNeedCall() && entry.getKey().startsWith(prefix) ) {
				lst.add(entry.getKey().substring(prefix.length() ));
			}
		}
		return lst;
	}
	
	public List<String> getTaskCalls() {
		return getExtCalls(ProcessKeys.PREFIX_TASK);
	}
	
	public void setExternalSystem(String name, boolean bNeedCall, int aState) {
		ExternalSystemState estate = extSystems.get(name);
		if (estate == null) {
			estate = new ExternalSystemState();
			estate.setName(name);
			extSystems.put(name, estate);
		}
		estate.setNeedCall(bNeedCall);
		estate.state = aState;
	}
	
	/**
	 * Условное обозначение места, в которое процесс расчёта должен вернуться после асинхронного вызова подсистемы 
	 * @return
	 */
	public String getNextLabel() {
		return nextLabel;
	}

	public void setNextLabel(String nextLabel) {
		this.nextLabel = nextLabel;
	}

	public int getResultCode() {
		return resultCode;
	}

	public class ExternalSystemState implements Serializable  {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2795795328488131810L;
		private String name;
		private boolean needCall = false;
		public boolean isWork = false;
		private int state = 0;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isNeedCall() {
			return needCall;
		}

		public void setNeedCall(boolean needCall) {
			this.needCall = needCall;
		}
		
		public boolean isInprocess() {
			return (state == STATE_IN_PROCESS);
		}
		
		public boolean isCompleted() {
			return (state > STATE_IN_PROCESS);
		}
		
		public boolean isCompletedOK() {
			return (state == STATE_COMPLETED_OK);
		}
		
		public boolean isCompletedErrors() {
			return (state == STATE_COMPLETED_ERRORS);
		}
		
		public void start() {
			state = STATE_IN_PROCESS;
		}
		
		public void finish(boolean bOK) {
			if (bOK) {
				state = STATE_COMPLETED_OK;
			} else {
				state = STATE_COMPLETED_ERRORS;
			}
		}

		public boolean isWork() {
			return isWork;
		}

	}

	public Map<String, Object> getVars() {
		return vars;
	}
	
	public void setVars(Map<String, Object> vars) {
		this.vars=vars;
	}

	public boolean isSaveResultOnce() {
		return saveResultOnce;
	}

	public void setSaveResultOnce(boolean saveResultOnce) {
		this.saveResultOnce = saveResultOnce;
	}

}
