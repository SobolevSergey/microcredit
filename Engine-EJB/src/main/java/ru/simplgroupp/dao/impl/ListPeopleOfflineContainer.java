package ru.simplgroupp.dao.impl;

import java.util.Date;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.AbstractListContainer;
import ru.simplgroupp.util.ListContainer;

/**
 * Список для быстрого поиска человека 
 * условие через или
 *
 */
@XmlRootElement
public class ListPeopleOfflineContainer extends AbstractListContainer<PeopleMain> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4156693064158921858L;
	/**
	 * дата рождения
	 */
	protected Date dateBirth;	
	/**
	 * фамилия
	 */
	protected String surname; 
	/**
	 * имя
	 */
	protected String name;
	/**
	 * отчество
	 */
	protected String midname;
	/**
	 * номер паспорта
	 */
	protected String paspNumber;
	/**
	 * серия паспорта
	 */
	protected String paspSeries;
	/**
	 * инн
	 */
	protected String inn;
	/**
	 * снилс
	 */
	protected String snils;
	/**
	 * почта
	 */
	protected String email;
	/**
	 * телефон
	 */
	protected String phone;
	/**
	 * партнер
	 */
	protected Integer creditPartnerId = null;
	/**
	 * фейковый параметр, чтобы не показывать список
	 */
	protected String fakeParameter="123";
    /**
     * пользователь, заводивший заявку
     */
	protected String userId;
	
	
	@Override
	public String buildHQL(int bForList) {
		String sql = null, sqlJoin = "";
		switch (bForList) {
			case FOR_LIST: sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.PeopleMainEntity as c [$JOIN$] where c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditRequestEntity ) "; break;
			case FOR_COUNT: sql = "select count(c) from ru.simplgroupp.persistence.PeopleMainEntity as c [$JOIN$] where c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditRequestEntity ) "; break;
			case FOR_IDS: sql = "select c.id from ru.simplgroupp.persistence.PeopleMainEntity as c [$JOIN$] where "; break;
		}
		
		if (! isAllEmpty()) {		
			sql = sql + " and (1=0)";
		} else {
			sql=sql+" and (c.snils=:fakeParameter)";
		}
		
		if (prmListId != null) {
			sql = sql + " or (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId)) ";
			return sql.replace("[$JOIN$]", sqlJoin);
		}
		
		if (creditPartnerId != null) {
			sql = sql + " or (c.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity where partnersId.id=:creditPartnerId))";
		}
		//снилс и инн
		if (StringUtils.isNotEmpty(snils))
			sql=sql+" or (c.snils=:snils)";
		if (StringUtils.isNotEmpty(inn))
			sql=sql+" or (c.inn=:inn)";
		
		//ФИО и дата рождения
		if (StringUtils.isNotEmpty(surname) || StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(midname) || (dateBirth != null) ) {

         sql = sql + " or ( (select count(*) from c.id.peoplepersonal as e where [$WHERE_PEOPLE$] ) > 0 )";

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
			sql = sql + " or ( (select count(*) from c.id.documents as e where [$WHERE_DOC$] ) > 0 )";
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
			sql = sql + " or ( (select count(*) from c.id.peoplecontact as e where [$WHERE_CONTACT$] ) > 0 )";
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
		if (isAllEmpty()){
			//если нет параметров, ставим фейковый параметр, дабы не показывать весь список
			qry.setParameter("fakeParameter", fakeParameter);
		} else {
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
	
	private boolean isAllEmpty() {
		return (StringUtils.isEmpty(surname)) 
				&& (StringUtils.isEmpty(name)) 
				&& (StringUtils.isEmpty(midname)) 
				&& (dateBirth == null) 
				&& (creditPartnerId == null) 
				&& (prmListId == null)
				&& (StringUtils.isEmpty(inn)) 
				&& (StringUtils.isEmpty(snils)) 
				&& (StringUtils.isEmpty(email)) 
				&& (StringUtils.isEmpty(phone))
				&& (StringUtils.isEmpty(paspSeries))
				&& (StringUtils.isEmpty(paspNumber));
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
		String sname = "Заёмщики оффлайн ";
		sname = sname + "от " + new Date().toString();
		return sname;
	}

	@Override
	protected PeopleMain wrapEntity(Object entity) {
		return new PeopleMain((PeopleMainEntity) entity);
	}

	@XmlElement
	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}

	@XmlElement
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getMidname() {
		return midname;
	}

	public void setMidname(String midname) {
		this.midname = midname;
	}

	@XmlElement
	public String getPaspNumber() {
		return paspNumber;
	}

	public void setPaspNumber(String paspNumber) {
		this.paspNumber = paspNumber;
	}

	@XmlElement
	public String getPaspSeries() {
		return paspSeries;
	}

	public void setPaspSeries(String paspSeries) {
		this.paspSeries = paspSeries;
	}

	@XmlElement
	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	@XmlElement
	public String getSnils() {
		return snils;
	}

	public void setSnils(String snils) {
		this.snils = snils;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
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

	public String getFakeParameter() {
		return fakeParameter;
	}

	public void setFakeParameter(String fakeParameter) {
		this.fakeParameter = fakeParameter;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public void copyParams(ListContainer<PeopleMain> source) {
		// TODO Auto-generated method stub
		
	}

	
}
