package ru.simplgroupp.transfer.annotation;

import javax.enterprise.util.AnnotationLiteral;

public class ABusinessClassEventQualifier extends AnnotationLiteral<ABusinessEvent>  implements ABusinessClassEvent {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9101109636442241236L;
	private String businessClassName;
	
	public ABusinessClassEventQualifier(String clsName) {
		businessClassName = clsName;
	}

	@Override
	public String className() {
		return businessClassName;
	}

}
