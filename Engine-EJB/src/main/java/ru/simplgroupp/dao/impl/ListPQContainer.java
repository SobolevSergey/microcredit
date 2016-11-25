package ru.simplgroupp.dao.impl;

import java.util.Date;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.AbstractListContainer;
import ru.simplgroupp.util.ListContainer;
import ru.simplgroupp.util.SearchUtil;

/**
 * Список для быстрого поиска человека 
 * условие через или
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ListPQContainer extends AbstractListContainer<PeopleMain> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4156693064158921858L;
	/**
	 * дата рождения
	 */
	@XmlElement
	protected Date dateBirth;	
	/**
	 * фамилия
	 */
	@XmlElement
	protected String surname; 
	/**
	 * имя
	 */
	@XmlElement
	protected String name;
	/**
	 * отчество
	 */
	@XmlElement
	protected String midname;
	/**
	 * номер паспорта
	 */
	@XmlElement
	protected String paspNumber;
	/**
	 * серия паспорта
	 */
	@XmlElement
	protected String paspSeries;
	/**
	 * инн
	 */
	@XmlElement
	protected String inn;
	/**
	 * снилс
	 */
	@XmlElement
	protected String snils;
	/**
	 * почта
	 */
	@XmlElement
	protected String email;
	/**
	 * телефон
	 */
	@XmlElement
	protected String phone;
	/**
	 * партнер
	 */
	@XmlElement
	protected Integer creditPartnerId = null;
	
    /**
     * пользователь, заводивший заявку
     */
	@XmlElement
	protected String userId;
	
	@Override
	public String buildHQL(int bForList) {
		String sql = null, sqlJoin = "";
		switch (bForList) {
			case FOR_LIST: sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.PeopleMainEntity as c [$JOIN$] where c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity where partnersId.id = :partnersId ) "; break;
			case FOR_COUNT: sql = "select count(c) from ru.simplgroupp.persistence.PeopleMainEntity as c [$JOIN$] where c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity  where partnersId.id = :partnersId ) "; break;
			case FOR_IDS: sql = "select c.id from ru.simplgroupp.persistence.PeopleMainEntity as c [$JOIN$] where c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity  where partnersId.id = :partnersId ) "; break;
		}
		
		if (SearchUtil.findSortCriteria(sorting, "Initials") != null || SearchUtil.findSortCriteria(sorting, "DateBirth") != null) {
			// надо сортировать по ФИО
			sqlJoin = "inner join c.peoplepersonal as e1 ";
			sql = sql + " and (e1.isactive = 1)";
		}
		
		if (prmListId != null) {
			sql = sql + " and (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId)) ";
			return sql.replace("[$JOIN$]", sqlJoin);
		}
		
		if (creditPartnerId != null) {
			sql = sql + " and (c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity where partnersId.id=:creditPartnerId))";
		}
		//снилс и инн
		if (StringUtils.isNotEmpty(snils))
			sql=sql+" and (c.snils=:snils)";
		if (StringUtils.isNotEmpty(inn))
			sql=sql+" and (c.inn=:inn)";
		
		//ФИО и дата рождения
		if (StringUtils.isNotEmpty(surname) || StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(midname) || (dateBirth != null) ) {

			sql = sql + " and ( (select count(*) from c.peoplepersonal as e where [$WHERE_PEOPLE$] ) > 0 )";

			String swhere = "(1=1)";
           if (StringUtils.isNotEmpty(surname)) {
                swhere = swhere + " and (upper(e.surname) like :surname)";
            }
	    	if (StringUtils.isNotEmpty(name)) {
	    		swhere = swhere + " and (upper(e.name) like :name)";
	    	}
	    	if (StringUtils.isNotEmpty(midname)) {
	    		swhere = swhere + " and (upper(e.midname) like :midname)";
	    	}	 
	    	if (dateBirth != null)
	    		swhere=swhere+" and (e.birthdate = :dateBirth)";

	    	sql = sql.replace("[$WHERE_PEOPLE$]", swhere);
	    }
		//ищем по документам
		if (StringUtils.isNotEmpty(paspSeries)||StringUtils.isNotEmpty(paspNumber)){
			sql = sql + " and ( (select count(*) from c.documents as e where [$WHERE_DOC$] ) > 0 )";
			String swhere = "(1=1)";
	        if (StringUtils.isNotEmpty(paspSeries)) {
	            swhere = swhere + " and e.series=:series";
	        }
	        if (StringUtils.isNotEmpty(paspNumber)) {
	            swhere = swhere + " and e.number=:number";
	        }
	        sql = sql.replace("[$WHERE_DOC$]", swhere);
		}
		
		//ищем по контактам
		if (StringUtils.isNotEmpty(email)||StringUtils.isNotEmpty(phone)){
			sql = sql + " and ( (select count(*) from c.peoplecontact as e where [$WHERE_CONTACT$] ) > 0 )";
			String swhere = "(1=1)";
		    if (StringUtils.isNotEmpty(email)) {
		        swhere = swhere + " and e.value=:email";
		    }
			if (StringUtils.isNotEmpty(phone)) {
			    swhere = swhere + " and e.value=:phone";
			}
			sql = sql.replace("[$WHERE_CONTACT$]", swhere);
		}		
		return sql.replace("[$JOIN$]", sqlJoin);
	}

	@Override
	public void setHQLParams(Query qry, int bForList) {
		qry.setParameter("partnersId", Partner.SYSTEM);
		
		if (prmListId != null) {
			qry.setParameter("prmListId", prmListId);
			return;
		}
		
			
		    if (creditPartnerId != null) {
			    qry.setParameter("creditPartnerId", creditPartnerId);
		    }
		    if (StringUtils.isNotEmpty(inn)) {
			    qry.setParameter("inn", inn);
		    }
		    if (StringUtils.isNotEmpty(snils)) {
			    qry.setParameter("snils", snils);
		    }
		    if (StringUtils.isNotEmpty(surname)) {
    		    qry.setParameter("surname", "%" + surname.trim().toUpperCase() + "%");
    	    }
		    if (StringUtils.isNotEmpty(name)) {
    		    qry.setParameter("name", "%" + name.trim().toUpperCase() + "%");
    	    }
		    if (StringUtils.isNotEmpty(midname)) {
    		    qry.setParameter("midname", "%" + midname.trim().toUpperCase() + "%");
    	    }
	        if (dateBirth != null) {
	            qry.setParameter("dateBirth", dateBirth,TemporalType.DATE);
	        }
	        if (StringUtils.isNotEmpty(paspSeries)) {
	           qry.setParameter("series", paspSeries);
	        }
	        if (StringUtils.isNotEmpty(paspNumber)) {
	            qry.setParameter("number", paspNumber);
	        }     
	        if (StringUtils.isNotEmpty(email)) {
	            qry.setParameter("email", email);
	        }
	        if (StringUtils.isNotEmpty(phone)) {
	            qry.setParameter("phone", phone);
	        }
		
	}

	@Override
	public void nullIfEmpty() {
		if (StringUtils.isBlank(surname)) {
			surname = null;
		}
		if (StringUtils.isBlank(name)) {
			name = null;
		}	
		if (StringUtils.isBlank(midname)) {
			midname = null;
		}
		if (StringUtils.isBlank(paspNumber)) {
			paspNumber = null;
		}
		
		if (StringUtils.isBlank(paspSeries)) {
			paspSeries = null;
		}	
		if (StringUtils.isBlank(inn)) {
			inn = null;
		}	
		if (StringUtils.isBlank(snils)) {
			snils = null;
		}	
		if (StringUtils.isBlank(email)) {
			email = null;
		}	
		if (StringUtils.isBlank(phone)) {
			phone = null;
		}			
		
	}
	
	
	@Override
	public void prepareParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearParams() {
		dateBirth = null;
		surname = null; 
		name = null;
		midname = null;
		paspNumber = null; 
		paspSeries = null; 
		inn = null; 
		snils = null; 
		email = null; 
		phone = null; 
		creditPartnerId = null;
	}

	@Override
	public String generateListName() {
		String sname = "Заёмщики ";
		sname = sname + "от " + new Date().toString();
		return sname;
	}

	@Override
	protected PeopleMain wrapEntity(Object entity) {
		return new PeopleMain((PeopleMainEntity) entity);
	}

	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMidname() {
		return midname;
	}

	public void setMidname(String midname) {
		this.midname = midname;
	}

	public String getPaspNumber() {
		return paspNumber;
	}

	public void setPaspNumber(String paspNumber) {
		this.paspNumber = paspNumber;
	}

	public String getPaspSeries() {
		return paspSeries;
	}

	public void setPaspSeries(String paspSeries) {
		this.paspSeries = paspSeries;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getSnils() {
		return snils;
	}

	public void setSnils(String snils) {
		this.snils = snils;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getCreditPartnerId() {
		return creditPartnerId;
	}

	public void setCreditPartnerId(Integer creditPartnerId) {
		this.creditPartnerId = creditPartnerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public void copyParams(ListContainer<PeopleMain> source) {
		ListPQContainer src = (ListPQContainer) source;
		this.creditPartnerId = src.creditPartnerId;
		this.dateBirth = src.dateBirth;
		this.email = src.email;
		this.inn = src.inn;
		this.midname = src.midname;
		this.name = src.name;
		this.paspNumber = src.paspNumber;
		this.paspSeries = src.paspSeries;
		this.phone = src.phone;
		this.prmListId = src.prmListId;
		this.snils = src.snils;
		this.surname = src.surname;
		this.userId = src.userId;
	}

	
}
