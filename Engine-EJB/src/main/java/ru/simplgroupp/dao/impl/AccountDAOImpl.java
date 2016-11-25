package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.AccountDAO;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.transfer.Account;
import ru.simplgroupp.transfer.ActiveStatus;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AccountDAOImpl implements AccountDAO {

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;	
    
	@Override
	public Account getOrganizationAccount(Integer organizationId) {
		Query qry=emMicro.createNamedQuery("getOrganizationAccount");
		qry.setParameter("organizationId", organizationId);
		qry.setParameter("isactive", ActiveStatus.ACTIVE);
		List<AccountEntity> lst=qry.getResultList();
		if (lst.size()>0){
			Account account=new Account(lst.get(0));
			return account;
		}
		return null;
	}  
	
	@Override
	public AccountEntity saveAccountEntity(AccountEntity source) {
		AccountEntity acc = emMicro.merge(source);
		return acc;
	}

	@Override
	public List<AccountEntity> findAccountsByPeople(PeopleMainEntity people, ReferenceEntity type, String number) {
		Query query = emMicro.createNamedQuery("findAccountsByPeople");
		query.setParameter("people", people);
		query.setParameter("type", type);
		query.setParameter("number", number);
		return query.getResultList();
	}

	@Override
	public List<AccountEntity> findAccountsByPeopleAndCardMask(PeopleMainEntity people,
			ReferenceEntity type, String cardMask) {
		Query query = emMicro.createNamedQuery("findAccountsByPeopleAndCardMask");
		query.setParameter("people", people);
		query.setParameter("type", type);
		query.setParameter("cardNumberMasked", cardMask);
		return query.getResultList();
	}

	@Override
	public AccountEntity getAccountEntity(int id) {
		return emMicro.find(AccountEntity.class, id);
	}
			    
	@Override
	public Account getAccount(int id, Set options) {
		AccountEntity ent = getAccountEntity(id);
		if (ent == null) {
			return null;
		} else {
			Account act = new Account(ent);
			act.init(options);
			return act;
		}
	}	
}
