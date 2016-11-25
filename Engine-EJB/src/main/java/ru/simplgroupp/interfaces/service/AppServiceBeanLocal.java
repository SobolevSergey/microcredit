package ru.simplgroupp.interfaces.service;

import java.util.List;

import ru.simplgroupp.ejb.ApplicationAction;
import ru.simplgroupp.ejb.EnvironmentSnapshot;
import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.rules.AbstractContext;

public interface AppServiceBeanLocal {

	public ApplicationAction getApplicationAction(String actionId, BusinessObjectRef... bizRefs);

	public ApplicationAction startApplicationAction(ApplicationAction action);

	public void endApplicationAction(ApplicationAction action);

	public List<ApplicationAction> findActions(String prmActionId, Boolean exclisive, BusinessObjectRef[] bizRef);

	public EnvironmentSnapshot getSnapshot(AbstractContext context);

	public ApplicationAction startApplicationAction(String actionId, boolean bExclusive, String description, BusinessObjectRef... bizRefs);

	public EnvironmentSnapshot getSnapshot(AbstractContext context,
			String businessObjectClass, boolean bObjectActions,
			boolean bProcessActions);

	ApplicationAction startApplicationAction(String actionId,
			boolean bExclusive, String description);

	void endApplicationAction(String actionId);

}
