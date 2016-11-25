package ru.simplgroupp.ejb;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

public interface ExecutionVisitor {
	public void visit(Execution execution, ProcessInstance process) throws Throwable;
}
