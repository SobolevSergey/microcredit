package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.interfaces.ServiceBeanLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.interfaces.service.AppServiceBeanLocal;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Payment;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Prolong;
import ru.simplgroupp.transfer.Refinance;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.WorkflowUtil;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.WorkflowObjectState;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(AppServiceBeanLocal.class)
public class AppServiceBean implements AppServiceBeanLocal {
	
	@EJB
	ServiceBeanLocal servBean;
	
	@EJB
	RulesBeanLocal rulesBean;
	
	@EJB
	WorkflowBeanLocal workflow;	
	
	@EJB
	BizActionBeanLocal bizBean;
	
	@EJB
	BizActionDAO bizDAO;
	
	@EJB
	ActionProcessorBeanLocal actProc;

	@Override
	public ApplicationAction getApplicationAction(String actionId, BusinessObjectRef... bizRefs) {		
		Arrays.sort(bizRefs);
		int key = ApplicationAction.hashCodeOf(actionId, bizRefs);
		return servBean.getAction(key);
	}
	
	@Override
	public ApplicationAction startApplicationAction(String actionId, boolean bExclusive, String description, BusinessObjectRef... bizRefs) {
		ApplicationAction action = new ApplicationAction(actionId, bExclusive, bizRefs);
		action.setDescription(description);
		ApplicationAction actRes = servBean.putAction(action);
		if (action == actRes) {
			return actRes;
		} else  {
			return null;
		}
	}	
	
	@Override
	public ApplicationAction startApplicationAction(String actionId, boolean bExclusive, String description) {
		ApplicationAction action = new ApplicationAction(actionId, bExclusive, new BusinessObjectRef[0]);
		action.setDescription(description);
		ApplicationAction actRes = servBean.putAction(action);
		if (action == actRes) {
			return actRes;
		} else  {
			// это действие уже выполняется, и не может быть запущено снова
			return null;
		}
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ApplicationAction startApplicationAction(ApplicationAction action) {
		ApplicationAction actRes = servBean.putAction(action);
		if (action == actRes) {
			return actRes;
		} else  {
			return null;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void endApplicationAction(ApplicationAction action) {
		servBean.removeAction(action);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void endApplicationAction(String actionId) {
		int key = AppUtil.calcAppActionHashCode(actionId);
		servBean.removeAction(key);
	}	
	
	@Override
	public List<ApplicationAction> findActions(String prmActionId, Boolean exclisive, BusinessObjectRef[] bizRef) {
		return servBean.findActions(prmActionId, exclisive, bizRef);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@AccessTimeout(50000)
	public EnvironmentSnapshot getSnapshot(AbstractContext context) {
		return getSnapshot(context, null, true, true);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@AccessTimeout(50000)
	public EnvironmentSnapshot getSnapshot(AbstractContext context, String businessObjectClass, boolean bObjectActions, boolean bProcessActions) {
		EnvironmentSnapshot res = new EnvironmentSnapshot();
		
		BusinessObjectRef[] ctxBizRefs = WorkflowUtil.extractBizRefs(context);
		
		if (businessObjectClass != null) {
			BusinessObjectRef ref1 = null;
			for (BusinessObjectRef bref: ctxBizRefs) {
				if (businessObjectClass.equals(bref.getBusinessObjectClass())) {
					ref1 = bref;
					break;
				}
			}
			if (ref1 == null) {
				return res;
			}
			ctxBizRefs = new BusinessObjectRef[1];
			ctxBizRefs[0] = ref1;
		}
		
		res.runningActions = findActions(null, null, ctxBizRefs);
		
		res.states = new ArrayList<WorkflowObjectState>();
		if (bProcessActions) {
			Set<String> varsForLoad = Utils.setOf(ProcessKeys.VAR_PAYMENT_FORM + ".type", ProcessKeys.VAR_PAYMENT_FORM + ".sumId", ProcessKeys.VAR_PAYMENT_FORM + ".sumFrom", ProcessKeys.VAR_PAYMENT_FORM + ".sumTo");
			for (BusinessObjectRef bref: ctxBizRefs) {
				if (CreditRequest.class.getName().equals(bref.getBusinessObjectClass())) {
					res.states.addAll(workflow.getProcWfActions(CreditRequest.class.getName(), context.getCreditRequest().getId(), varsForLoad, true));
				} else if (Credit.class.getName().equals(bref.getBusinessObjectClass())) {
					res.states.addAll(workflow.getProcWfActions(Credit.class.getName(), context.getCredit().getId(), varsForLoad, true));
				} else if (PeopleMain.class.getName().equals(bref.getBusinessObjectClass())) {
					res.states.addAll(workflow.getProcWfActions(PeopleMain.class.getName(), context.getClient().getId(), varsForLoad, true));
				} else if (Prolong.class.getName().equals(bref.getBusinessObjectClass())) {
					res.states.addAll(workflow.getProcWfActions(Prolong.class.getName(), context.getProlong().getId(), varsForLoad, true));
				} else if (Payment.class.getName().equals(bref.getBusinessObjectClass())) {
					res.states.addAll(workflow.getProcWfActions(Payment.class.getName(), context.getPayment().getId(), varsForLoad, true));
				} else if (Refinance.class.getName().equals(bref.getBusinessObjectClass())) {
					res.states.addAll(workflow.getProcWfActions(Refinance.class.getName(), context.getRefinance().getId(), varsForLoad, true));
				}
			}
			// дополняем действия
			WorkflowUtil.addDetailWFActions1(res.states, bizDAO);
		}
		
		if (bObjectActions) {
			res.objectActions = rulesBean.getObjectActions(null, true, null);
			WorkflowUtil.checkWFActionsByRefs(res.objectActions, ctxBizRefs);
		}
		
		// фильтруем через заданные бизнес-объекты
		WorkflowUtil.checkWFActionsByRefs1(res.states, ctxBizRefs);
		
		// фильтруем через запущенные действия
		WorkflowUtil.checkWFActionsByRunning1(res.states, res.runningActions);
		WorkflowUtil.checkWFActionsByRunning(res.objectActions, res.runningActions);		
		
		// фильтруем доступные действия по ролям пользователя
		WorkflowUtil.checkWFActionsByRoles1(res.states, context.getCurrentUser().getRoles());
		WorkflowUtil.checkWFActionsByRoles(res.objectActions, context.getCurrentUser().getRoles());
		
		// фильтруем действия через правила и добавляем стартовые переменные
		RuntimeServices runtimeServices = new RuntimeServices(null, rulesBean, null, null);
		runtimeServices.setActionProcessor(actProc);
		if (bProcessActions) {
			WorkflowUtil.checkWFActionsEnabled1(res.states, runtimeServices, context);
		}
		if (bObjectActions) {
			WorkflowUtil.checkWFActionsEnabled(res.objectActions, runtimeServices, context);
		}

		return res;
	}	
}
