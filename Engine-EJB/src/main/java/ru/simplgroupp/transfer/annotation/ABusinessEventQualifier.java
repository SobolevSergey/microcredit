package ru.simplgroupp.transfer.annotation;

import javax.enterprise.util.AnnotationLiteral;

public class ABusinessEventQualifier extends AnnotationLiteral<ABusinessEvent>  implements ABusinessEvent {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2725839598230374201L;
	
	private String businessClassName;
	private int eventCode;
	
	public ABusinessEventQualifier(String clsName, int evtCode) {
		businessClassName = clsName;
		eventCode = evtCode;
	}

	@Override
	public String className() {
		return businessClassName;
	}

	@Override
	public int eventCode() {
		return eventCode;
	}

}
