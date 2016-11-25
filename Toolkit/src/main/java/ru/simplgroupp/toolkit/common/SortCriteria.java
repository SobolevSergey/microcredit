package ru.simplgroupp.toolkit.common;

public class SortCriteria {

	private String expression;
	private boolean isAscending = true;
	
	public SortCriteria(String aexpr, boolean bAsc) {
		expression = aexpr;
		isAscending = bAsc;
	}
	
	public boolean getIsAscending() {
		return isAscending;
	}
	
	public String getExpression() {
		return expression;
	}

}
