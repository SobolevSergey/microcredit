package ru.simplgroupp.dao.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.transfer.Account;

@Local
public interface AccountDAO {

    /**
     * возвращает счет для организации
     * @param organizationId - id организации
     * @return
     */
	public Account getOrganizationAccount(Integer organizationId);

	 /**
     * возвращает счет по id
     * @param id - id счета
     */	
	public AccountEntity getAccountEntity(int id);

    /**
     * возвращает счет по id
     * @param id - id счета
     * @param options - какие коллекции надо инициализировать
     */	
	public Account getAccount(int id, Set options);

	/**
	 * сохраняем счет
	 * @param source - счет
	 * @return
	 */
	AccountEntity saveAccountEntity(AccountEntity source);

	List<AccountEntity> findAccountsByPeople(PeopleMainEntity peopleMainId, ReferenceEntity type, String number);

	List<AccountEntity> findAccountsByPeopleAndCardMask(PeopleMainEntity peopleMainId, ReferenceEntity yandexCardType, String cardMask);
}
