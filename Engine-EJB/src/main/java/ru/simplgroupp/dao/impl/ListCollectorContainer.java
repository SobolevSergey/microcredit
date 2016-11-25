package ru.simplgroupp.dao.impl;

import java.util.Date;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.persistence.CollectorEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.transfer.Collector;
import ru.simplgroupp.util.AbstractListContainer;
import ru.simplgroupp.util.ListContainer;
/**
 * списки для работы коллектора
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ListCollectorContainer extends AbstractListContainer<Collector>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2587117924722661349L;

	@XmlElement
	protected Integer prmPeopleMainId;
	/**
	 * номер кредита
	 */
	@XmlElement
	protected String prmCreditAccount;
	/**
	 * фамилия
	 */
	@XmlElement
	protected String prmSurname;
	/**
	 * имя
	 */
	@XmlElement
	protected String prmName;
	/**
	 * телефон
	 */
	@XmlElement
	protected String prmPhone;
	/**
	 * дата начала
	 */
	@XmlElement
	protected DateRange prmDatabeg = new DateRange(null, null);	
	/**
	 * дата окончания по графику
	 */
	@XmlElement
	protected DateRange prmDataend = new DateRange(null, null);	
	/**
	 * дней просрочки по кредиту
	 */
	@XmlElement
	protected IntegerRange prmMaxDelay=new IntegerRange(null,null);
	/**
	 * активность записи
	 */
	@XmlElement
	protected Integer prmIsActive;
	/**
	 * id пользователя (коллектора)
	 */
	@XmlElement
	protected Integer prmUserId;
	/**
	 * сумма кредита
	 */
	@XmlElement
	protected MoneyRange prmCreditsum = new MoneyRange(null, null);
	/**
	 * сумма кредита к возврату
	 */
	@XmlElement
	protected MoneyRange prmCreditsumback = new MoneyRange(null, null);
	/**
	 * дата начала кредита
	 */
	@XmlElement
	protected DateRange prmCreditDataBeg = new DateRange(null, null);
	/**
	 * дата окончания кредита
	 */
	@XmlElement
	protected DateRange prmCreditDataEnd = new DateRange(null, null);
	/**
	 * дата следующего контакта
	 */
	@XmlElement
	protected DateRange prmDateNextContact = new DateRange(null, null);

	@Override
	public String buildHQL(int bForList) {
		String sql = null, sqlJoin = "";
		switch (bForList) {
			case FOR_LIST: sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.CollectorEntity as c [$JOIN$] where (1=1)"; break;
			case FOR_COUNT: sql = "select count(c) from ru.simplgroupp.persistence.CollectorEntity as c [$JOIN$] where (1=1)"; break;
			case FOR_IDS: sql = "select c.id from ru.simplgroupp.persistence.CollectorEntity as c [$JOIN$] where (1=1)"; break;
		}
		
		if (prmListId != null) {
			sql+= " and (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId)) ";
			return sql.replace("[$JOIN$]", sqlJoin);
		}

	    if (prmPeopleMainId != null) {
            sql+= " and (c.peopleMainId.id = :prmPeopleMainId)";
        }
	    if (prmDatabeg != null && prmDatabeg.getFrom() != null) {
	    	sql+= " and (c.databeg >= :prmDatabegFrom)";
	    }
	    if (prmDatabeg != null && prmDatabeg.getTo() != null) {
	    	sql+= " and (c.databeg < :prmDatabegTo)";
	    }
	    if (prmDataend != null && prmDataend.getFrom() != null) {
	    	sql+= " and (c.dataend >= :prmDataendFrom)";
	    }
	    if (prmDataend != null && prmDataend.getTo() != null) {
	    	sql+= " and (c.dataend < :prmDataendTo)";
	    }	    
	    if (StringUtils.isNotEmpty(prmCreditAccount)) {
	    	sql+= " and (c.creditId.creditAccount = :prmCreditAccount)";
	    }
	    if (prmMaxDelay != null && prmMaxDelay.getFrom() != null) {
	    	sql+= " and (c.creditId.maxDelay >= :prmMaxDelayFrom)";
	    }
	    if (prmMaxDelay != null && prmMaxDelay.getTo() != null) {
	    	sql+= " and (c.creditId.maxDelay <= :prmMaxDelayTo)";
	    }
	    if (StringUtils.isNotEmpty(prmSurname)) {
	        sql+= " and ( (select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.surname) like :prmSurname))>0 )";		
	    }
	    if (StringUtils.isNotEmpty(prmName)) {
	        sql+= " and ( (select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.name) like :prmName))>0 )";
	    }
	    if (StringUtils.isNotEmpty(prmPhone)) {
	        sql+= " and ( (select count(*) from c.peopleMainId.peoplecontact as t where t.value=:prmPhone)>0 )";		
	    }
	    if (prmIsActive != null) {
            sql+= " and (c.isActive = :prmIsActive)";
        }
	    if (prmUserId != null) {
            sql+= " and (c.userId.id = :prmUserId)";
        }
	    if (prmCreditsum != null && prmCreditsum.getFrom() != null) {
	    	sql = sql + " and (c.creditId.creditsum >= :prmCreditsumFrom)";
	    }
	    if (prmCreditsum != null && prmCreditsum.getTo() != null) {
	    	sql = sql + " and (c.creditId.creditsum <= :prmCreditsumTo)";
	    }
	    if (prmCreditsumback != null && prmCreditsumback.getFrom() != null) {
	    	sql = sql + " and (c.creditId.creditsumback >= :prmCreditsumbackFrom)";
	    }
	    if (prmCreditsumback != null && prmCreditsumback.getTo() != null) {
	    	sql = sql + " and (c.creditId.creditsumback <= :prmCreditsumbackTo)";
	    }
		if (prmCreditDataBeg != null && prmCreditDataBeg.getFrom() != null) {
			sql+= " and ((select count(*) from c.creditId as cr where cr.creditdatabeg >= :prmCreditDataBegFrom) > 0)";
		}
		if (prmCreditDataBeg != null && prmCreditDataBeg.getTo() != null) {
			sql+= " and ((select count(*) from c.creditId as cr where cr.creditdatabeg < :prmCreditDataBegTo) > 0)";
		}
		if (prmCreditDataEnd != null && prmCreditDataEnd.getFrom() != null) {
			sql+= " and ((select count(*) from c.creditId as cr where cr.creditdataend >= :prmCreditDataEndFrom) > 0)";
		}
		if (prmCreditDataEnd != null && prmCreditDataEnd.getTo() != null) {
			sql+= " and ((select count(*) from c.creditId as cr where cr.creditdataend < :prmCreditDataEndTo) > 0)";
		}

		String promiseDateSql = "(select count(*) from CallResultEntity as cre where (1=1) and cre.peopleMainId.id = c.peopleMainId.id";
		if (prmUserId != null) {
			promiseDateSql += " and (cre.userId.id = :prmUserId)";
		}
		if (prmDateNextContact != null && prmDateNextContact.getFrom() != null) {
			promiseDateSql += " and (cre.dateNextContact >= :prmDateNextContactFrom)";
		}
		if (prmDateNextContact != null && prmDateNextContact.getTo() != null) {
			promiseDateSql += " and (cre.dateNextContact < :prmDateNextContactTo)";
		}
		promiseDateSql += ") > 0";

		if ((prmDateNextContact != null && prmDateNextContact.getFrom() != null) || (prmDateNextContact != null && prmDateNextContact.getTo() != null)) {
			sql += " and (" + promiseDateSql + ")";
		}
		return sql.replace("[$JOIN$]", sqlJoin);
	}

	@Override
	public void setHQLParams(Query qry, int bForList) {
		if (prmListId != null) {
			qry.setParameter("prmListId", prmListId);
			return;
		}
	    if (prmPeopleMainId != null) {
	    	qry.setParameter("prmPeopleMainId", prmPeopleMainId);
        }
	    if (prmDatabeg != null && prmDatabeg.getFrom() != null) {
	    	qry.setParameter("prmDatabegFrom", prmDatabeg.getFrom(),TemporalType.DATE);
	    }
	    if (prmDatabeg != null && prmDatabeg.getTo() != null) {
	    	qry.setParameter("prmDatabegTo", DateUtils.addDays(prmDatabeg.getTo(), 1),TemporalType.DATE);
	    }
	    if (prmDataend != null && prmDataend.getFrom() != null) {
	    	qry.setParameter("prmDataendFrom", prmDataend.getFrom(),TemporalType.DATE);
	    }
	    if (prmDataend != null && prmDataend.getTo() != null) {
	    	qry.setParameter("prmDataendTo", DateUtils.addDays(prmDataend.getTo(), 1),TemporalType.DATE);
	    }
	    if (prmMaxDelay != null && prmMaxDelay.getFrom() != null) {
	    	qry.setParameter("prmMaxDelayFrom", prmMaxDelay.getFrom());
	    }
	    if (prmMaxDelay != null && prmMaxDelay.getTo() != null) {
	    	qry.setParameter("prmMaxDelayTo", prmMaxDelay.getTo());
	    }
	    if (prmIsActive != null) {
	    	qry.setParameter("prmIsActive", prmIsActive);
	    }
	    if (prmUserId != null) {
	    	qry.setParameter("prmUserId", prmUserId);
        }
	    if (StringUtils.isNotEmpty(prmSurname)) {
    		qry.setParameter("prmSurname", "%" + prmSurname.trim().toUpperCase() + "%");
    	}
	    if (StringUtils.isNotEmpty(prmName)) {
    		qry.setParameter("prmName", "%" + prmName.trim().toUpperCase() + "%");
    	}
	    if (StringUtils.isNotEmpty(prmPhone)) {
    		qry.setParameter("prmPhone", prmPhone);
    	}
	    if (StringUtils.isNotEmpty(prmCreditAccount)) {
    		qry.setParameter("prmCreditAccount", prmCreditAccount);
    	}
	    if (prmCreditsum != null && prmCreditsum.getFrom() != null) {
	    	qry.setParameter("prmCreditsumFrom", prmCreditsum.getFrom().doubleValue());
	    }
	    if (prmCreditsum != null && prmCreditsum.getTo() != null) {
	    	qry.setParameter("prmCreditsumTo", prmCreditsum.getTo().doubleValue());
	    }
	    if (prmCreditsumback != null && prmCreditsumback.getFrom() != null) {
	    	qry.setParameter("prmCreditsumbackFrom", prmCreditsumback.getFrom().doubleValue());
	    }
	    if (prmCreditsumback != null && prmCreditsumback.getTo() != null) {
	    	qry.setParameter("prmCreditsumbackTo", prmCreditsumback.getTo().doubleValue());
	    }
		if (prmCreditDataBeg != null && prmCreditDataBeg.getFrom() != null) {
			qry.setParameter("prmCreditDataBegFrom", prmCreditDataBeg.getFrom(), TemporalType.DATE);
		}
		if (prmCreditDataBeg != null && prmCreditDataBeg.getTo() != null) {
			qry.setParameter("prmCreditDataBegTo", DateUtils.addDays(prmCreditDataBeg.getTo(), 1), TemporalType.DATE);
		}
		if (prmCreditDataEnd != null && prmCreditDataEnd.getFrom() != null) {
			qry.setParameter("prmCreditDataEndFrom", prmCreditDataEnd.getFrom(), TemporalType.DATE);
		}
		if (prmCreditDataEnd != null && prmCreditDataEnd.getTo() != null) {
			qry.setParameter("prmCreditDataEndTo", DateUtils.addDays(prmCreditDataEnd.getTo(), 1), TemporalType.DATE);
		}
		if (prmDateNextContact != null && prmDateNextContact.getFrom() != null) {
			qry.setParameter("prmDateNextContactFrom", prmDateNextContact.getFrom(), TemporalType.DATE);
		}
		if (prmDateNextContact != null && prmDateNextContact.getTo() != null) {
			qry.setParameter("prmDateNextContactTo",  DateUtils.addDays(prmDateNextContact.getTo(), 1), TemporalType.DATE);
		}
	}

	@Override
	public void nullIfEmpty() {
		if (prmCreditsum != null && prmCreditsum.getFrom() != null && prmCreditsum.getFrom().intValue() == 0) {
			prmCreditsum.setFrom(null);
		}
		if (prmCreditsum != null && prmCreditsum.getTo() != null && prmCreditsum.getTo().intValue() == 0) {
			prmCreditsum.setTo(null);
		}		
		if (prmCreditsumback != null && prmCreditsumback.getFrom() != null && prmCreditsumback.getFrom().intValue() == 0) {
			prmCreditsumback.setFrom(null);
		}
		if (prmCreditsumback != null && prmCreditsumback.getTo() != null && prmCreditsumback.getTo().intValue() == 0) {
			prmCreditsumback.setTo(null);
		}		
		
	}

	@Override
	public void prepareParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearParams() {
		prmDatabeg = new DateRange(null, null);
		prmDataend = new DateRange(null, null);
		prmCreditsum = new MoneyRange(null, null);
		prmCreditsumback = new MoneyRange(null, null);
		prmMaxDelay = new IntegerRange(null, null);
	    prmSurname=null;
		prmPhone = null;
		prmUserId = null;
		prmIsActive=null;
		prmCreditAccount = null;
		prmListId = null;
		prmPeopleMainId = null;
		
	}

	@Override
	public String generateListName() {
	    String str= " Список для коллектора от " + new Date().toString();
		return str;
	}

	@Override
	protected Collector wrapEntity(Object entity) {
		return new Collector((CollectorEntity) entity);
	}

	public Integer getPrmPeopleMainId() {
		return prmPeopleMainId;
	}

	public void setPrmPeopleMainId(Integer prmPeopleMainId) {
		this.prmPeopleMainId = prmPeopleMainId;
	}

	public String getPrmCreditAccount() {
		return prmCreditAccount;
	}

	public void setPrmCreditAccount(String prmCreditAccount) {
		this.prmCreditAccount = prmCreditAccount;
	}

	public String getPrmSurname() {
		return prmSurname;
	}

	public void setPrmSurname(String prmSurname) {
		this.prmSurname = prmSurname;
	}

	public String getPrmPhone() {
		return prmPhone;
	}

	public void setPrmPhone(String prmPhone) {
		this.prmPhone = prmPhone;
	}

	public DateRange getPrmDatabeg() {
		return prmDatabeg;
	}

	public void setPrmDatabeg(DateRange prmDatabeg) {
		this.prmDatabeg = prmDatabeg;
	}

	public DateRange getPrmDataend() {
		return prmDataend;
	}

	public void setPrmDataend(DateRange prmDataend) {
		this.prmDataend = prmDataend;
	}

	public Integer getPrmIsActive() {
		return prmIsActive;
	}

	public void setPrmIsActive(Integer prmIsActive) {
		this.prmIsActive = prmIsActive;
	}

	public Integer getPrmUserId() {
		return prmUserId;
	}

	public void setPrmUserId(Integer prmUserId) {
		this.prmUserId = prmUserId;
	}

	public IntegerRange getPrmMaxDelay() {
		return prmMaxDelay;
	}

	public void setPrmMaxDelay(IntegerRange prmMaxDelay) {
		this.prmMaxDelay = prmMaxDelay;
	}

	public MoneyRange getPrmCreditsum() {
		return prmCreditsum;
	}

	public void setPrmCreditsum(MoneyRange prmCreditsum) {
		this.prmCreditsum = prmCreditsum;
	}

	public MoneyRange getPrmCreditsumback() {
		return prmCreditsumback;
	}

	public void setPrmCreditsumback(MoneyRange prmCreditsumback) {
		this.prmCreditsumback = prmCreditsumback;
	}

	public String getPrmName() {
		return prmName;
	}

	public void setPrmName(String prmName) {
		this.prmName = prmName;
	}

	public DateRange getPrmCreditDataEnd() {
		return prmCreditDataEnd;
	}

	public void setPrmCreditDataEnd(DateRange prmCreditDataEnd) {
		this.prmCreditDataEnd = prmCreditDataEnd;
	}

	public DateRange getPrmCreditDataBeg() {
		return prmCreditDataBeg;
	}

	public void setPrmCreditDataBeg(DateRange prmCreditDataBeg) {
		this.prmCreditDataBeg = prmCreditDataBeg;
	}

	public DateRange getPrmDateNextContact() {
		return prmDateNextContact;
	}

	public void setPrmDateNextContact(DateRange prmDateNextContact) {
		this.prmDateNextContact = prmDateNextContact;
	}

	@Override
	public void copyParams(ListContainer<Collector> source) {
		ListCollectorContainer src = (ListCollectorContainer) source;
		this.prmCreditAccount = src.prmCreditAccount;
		this.prmCreditDataBeg = src.prmCreditDataBeg;  
		this.prmCreditDataEnd = src.prmCreditDataEnd;
		this.prmCreditsum = src.prmCreditsum;
		this.prmCreditsumback = src.prmCreditsumback;
		this.prmDatabeg = src.prmDatabeg;
		this.prmDataend = src.prmDataend;
		this.prmDateNextContact = src.prmDateNextContact;
		this.prmIsActive = src.prmIsActive;
		this.prmListId = src.prmListId;
		this.prmMaxDelay = src.prmMaxDelay;
		this.prmName = src.prmName;
		this.prmPeopleMainId = src.prmPeopleMainId;
		this.prmPhone = src.prmPhone;
		this.prmSurname = src.prmSurname;
		this.prmUserId = src.prmUserId;
	}
}
