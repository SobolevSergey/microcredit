package ru.simplgroupp.ejb;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

public interface ExecutionTest {
	/**
	 * Предназначен для итерации по дереву процессов. Возвращает true, если проверка прошла удачно. После этого итерация прекращается.
	 * @param execution
	 * @param process
	 * @return
	 * @throws Throwable
	 */
	public boolean test(Execution execution, ProcessInstance process) throws Throwable;
}
