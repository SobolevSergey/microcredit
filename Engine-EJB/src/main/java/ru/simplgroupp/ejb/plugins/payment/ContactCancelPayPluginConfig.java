package ru.simplgroupp.ejb.plugins.payment;

import ru.simplgroupp.interfaces.plugins.payment.ContactPayBeanLocal;

public class ContactCancelPayPluginConfig extends ContactPayPluginConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -292730731665980801L;

	public ContactCancelPayPluginConfig() {
		super();
		this.propertiesImports = new String[] {ContactPayBeanLocal.SYSTEM_NAME};
		this.order = 1;
	}

	public ContactCancelPayPluginConfig(String plName) {
		super(plName);
		this.propertiesImports = new String[] {ContactPayBeanLocal.SYSTEM_NAME};
		this.order = 1;
	}

	@Override
	public String getPartnerName() {
		return ContactPayBeanLocal.SYSTEM_NAME;
	}
}
