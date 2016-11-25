package ru.simplgroupp.interfaces;

import ru.simplgroupp.interfaces.service.ListService;

public class RuntimeServices {

    private RulesBeanLocal rulesBean;

    private ServiceBeanLocal servBean;

    private WorkflowBeanLocal workflowBean;

    private QuestionBeanLocal questionBean;
    
    private WorkflowEngineBeanLocal wfEngine;
    
    private BizActionProcessorBeanLocal bizProc;
    
    private ActionProcessorBeanLocal actionProcessor;
    
    private ListService listService;
    
    public RuntimeServices() {
    	super();
    }

    public RuntimeServices(ServiceBeanLocal aservBean, RulesBeanLocal arulesBean, WorkflowBeanLocal aworkflowBean,
                           QuestionBeanLocal questBean) {
        super();
        servBean = aservBean;
        rulesBean = arulesBean;
        workflowBean = aworkflowBean;
        questionBean = questBean;
    }

    public RulesBeanLocal getRulesBean() {
        return rulesBean;
    }

    public ServiceBeanLocal getServBean() {
        return servBean;
    }

    public WorkflowBeanLocal getWorkflowBean() {
        return workflowBean;
    }

    public QuestionBeanLocal getQuestionBean() {
        return questionBean;
    }

	public WorkflowEngineBeanLocal getWfEngine() {
		return wfEngine;
	}

	public void setWfEngine(WorkflowEngineBeanLocal wfEngine) {
		this.wfEngine = wfEngine;
	}

	public BizActionProcessorBeanLocal getBizProc() {
		return bizProc;
	}

	public void setBizProc(BizActionProcessorBeanLocal bizProc) {
		this.bizProc = bizProc;
	}

	public ActionProcessorBeanLocal getActionProcessor() {
		return actionProcessor;
	}

	public void setActionProcessor(ActionProcessorBeanLocal actionProcessor) {
		this.actionProcessor = actionProcessor;
	}

	public ListService getListService() {
		return listService;
	}

	public void setListService(ListService listService) {
		this.listService = listService;
	}
}
