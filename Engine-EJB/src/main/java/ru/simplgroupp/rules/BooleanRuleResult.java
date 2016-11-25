package ru.simplgroupp.rules;

/**
 * возвращает результат из правила
 * в виде true-false
 *
 */
public class BooleanRuleResult extends AbstractRuleResult {

	protected Boolean resultValue;
	
	public BooleanRuleResult() {
		super();
	}

	public Boolean getResultValue() {
		return resultValue;
	}

	public void setResultValue(Boolean resultValue) {
		this.resultValue = resultValue;
	}

}
