package ru.simplgroupp.rules;

import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.PeopleMain;

public class CreditRequestContext extends AbstractContext {

	public CreditRequestContext() {
		super();
	}
	
	public CreditRequestContext(PeopleMain client, CreditRequest ccReq) {
		super();
		this.client = client;
		this.ccRequest = ccReq;
	}

}
