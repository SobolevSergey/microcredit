package ru.simplgroupp.transfer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Позволяет найти пункт справочника по его числовому или строковому коду
 * @author irina
 *
 */
@Target(value=ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ReferenceConstant {
	public int codeInteger() default -1;
	public String code() default "";
}
