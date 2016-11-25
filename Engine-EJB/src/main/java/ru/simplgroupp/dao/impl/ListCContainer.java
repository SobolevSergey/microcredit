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

import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.util.AbstractListContainer;
import ru.simplgroupp.util.ListContainer;

/**
 * Поиск по займам
 * @author irina
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ListCContainer extends AbstractListContainer<Credit> {
	
	/**
	 * списки кредитов
	 */
	private static final long serialVersionUID = 6189023558620011146L;

	private static final Integer NULL_INT_VALUE = -9999;
	
	@XmlElement
	protected Integer prmPeopleMainId;
	/**
	 * вид счета (как получали деньги)
	 */
	@XmlElement
	protected Integer prmAccountTypeId;
	/**
	 * номер кредита
	 */
	@XmlElement
	protected String prmCreditAccount;
	/**
	 * номер кредита по порядку
	 */
	@XmlElement
	protected String prmNomer;
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
	 * дата окончания фактическая
	 */
	@XmlElement
	protected DateRange prmDataendfact = new DateRange(null, null);	
	/**
	 * сумма кредита
	 */
	@XmlElement
	protected MoneyRange prmCreditsum = new MoneyRange(null, null);
	/**
	 * ставка
	 */
	@XmlElement
	protected MoneyRange prmStake = new MoneyRange(null, null);
	
	/**
	 * фамилия
	 */
	@XmlElement
	protected String prmSurname;
	/**
	 * статус кредита
	 */
	@XmlElement
	protected Integer prmStatusId;
	/**
	 * завершен ли кредит
	 */
	@XmlElement
	protected Integer prmIsOver;
	/**
	 * рабочее состояние
	 */
	@XmlElement
	protected Integer prmIsActive;
    /**
     * выдан своей организацией
     */
	@XmlElement
	protected Integer prmIsSameorg;

	//по умолчанию показываем кредиты только свои
	@XmlElement
	protected Integer prmPartnerId=Partner.SYSTEM;
	/**
	 * вид кредита
	 */
	@XmlElement
	protected Integer prmTypeId;
	/**
	 * ставка в день
	 */
	@XmlElement
	protected MoneyRange prmCreditstake = new MoneyRange(null, null);	

	@Override
	public String buildHQL(int bForList) {
		String sql = null, sqlJoin = "";
		switch (bForList) {
			case FOR_LIST: sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.CreditEntity as c [$JOIN$] where (1=1)"; break;
			case FOR_COUNT: sql = "select count(c) from ru.simplgroupp.persistence.CreditEntity as c [$JOIN$] where (1=1)"; break;
			case FOR_IDS: sql = "select c.id from ru.simplgroupp.persistence.CreditEntity as c [$JOIN$] where (1=1)"; break;
		}
		
		if (prmListId != null) {
			sql = sql + " and (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId)) ";
			return sql.replace("[$JOIN$]", sqlJoin);
		}

	    if (prmPeopleMainId != null) {
            sql =sql + " and (c.peopleMainId.id = :prmPeopleMainId)";
        }
	    if (prmAccountTypeId != null) {
	    	sql = sql + " and (c.accountTypeId.id = :prmAccountTypeId)";
	    }
	    if (prmPartnerId != null) {
	    	sql = sql + " and (c.partnersId.id = :prmPartnerId)";
	    }
	    if (prmDatabeg != null && prmDatabeg.getFrom() != null) {
	    	sql = sql + " and (c.creditdatabeg >= :prmDatabegFrom)";
	    }
	    if (prmDatabeg != null && prmDatabeg.getTo() != null) {
	    	sql = sql + " and (c.creditdatabeg < :prmDatabegTo)";
	    }
	    if (prmDataend != null && prmDataend.getFrom() != null) {
	    	sql = sql + " and (c.creditdataend >= :prmDataendFrom)";
	    }
	    if (prmDataend != null && prmDataend.getTo() != null) {
	    	sql = sql + " and (c.creditdataend < :prmDataendTo)";
	    }	    
	    if (prmTypeId != null) {
	    	sql = sql + " and (c.credittypeId.id = :prmTypeId)";
	    }
	    if (prmIsOver != null) {
	    	sql = sql + " and (c.isover = :prmIsOver)";
	    }
	    if (prmIsActive != null) {
	    	sql = sql + " and (c.isactive = :prmIsActive)";
	    }
	    if (StringUtils.isNotEmpty(prmCreditAccount)) {
	    	sql = sql + " and (c.creditAccount = :prmCreditAccount)";
	    }
	    if (prmIsSameorg != null) {
	    	sql = sql + " and (c.issameorg = :prmIsSameorg)";
	    }
	    if (prmDataendfact != null && prmDataendfact.getFrom() != null) {
	    	sql = sql + " and (c.creditdataendfact >= :prmDataendfactFrom)";
	    }
	    if (prmDataendfact != null && prmDataendfact.getTo() != null) {
	    	sql = sql + " and (c.creditdataendfact < :prmDataendfactTo)";
	    }	    
	    if (prmStatusId != null) {
	    	sql = sql + " and (c.creditStatusId.id =:prmStatusId)";
	    }
	    if (prmCreditsum != null && prmCreditsum.getFrom() != null) {
	    	sql = sql + " and (c.creditsum >= :prmCreditsumFrom)";
	    }
	    if (prmCreditsum != null && prmCreditsum.getTo() != null) {
	    	sql = sql + " and (c.creditsum <= :prmCreditsumTo)";
	    }
	    if (prmStake != null && prmStake.getFrom() != null) {
	    	sql = sql + " and (c.creditpercent >= :prmStakeFrom)";
	    }
	    if (prmStake != null && prmStake.getTo() != null) {
	    	sql = sql + " and (c.creditpercent <= :prmStakeTo)";
	    }	  
	    if (StringUtils.isNotEmpty(prmSurname)) {
	           sql = sql + " and ( (select count(*) from c.peopleMainId as pm inner join pm.peoplepersonal as p where (upper(p.surname) like :prmSurname))>0 )";		
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
	    if (prmAccountTypeId != null) {
	    	qry.setParameter("prmAccountTypeId", prmAccountTypeId);
	    }
	    if (prmPartnerId != null) {
	    	qry.setParameter("prmPartnerId", prmPartnerId);
	    }
	    if (prmDatabeg != null && prmDatabeg.getFrom() != null) {
	    	qry.setParameter("prmDatabegFrom", prmDatabeg.getFrom(),TemporalType.DATE);
	    }
	    if (prmDatabeg != null && prmDatabeg.getTo() != null) {
	    	qry.setParameter("prmDatabegTo", DateUtils.addDays(prmDatabeg.getTo(),1),TemporalType.DATE);
	    }
	    if (prmDataend != null && prmDataend.getFrom() != null) {
	    	qry.setParameter("prmDataendFrom", prmDataend.getFrom(),TemporalType.DATE);
	    }
	    if (prmDataend != null && prmDataend.getTo() != null) {
	    	qry.setParameter("prmDataendTo", DateUtils.addDays(prmDataend.getTo(),1),TemporalType.DATE);	    	
	    }	    
	    if (prmTypeId != null) {
	    	qry.setParameter("prmTypeId", prmTypeId);
	    }
	    if (prmIsOver != null) {
	    	qry.setParameter("prmIsOver", prmIsOver);
	    }
	    if (prmIsActive != null) {
	    	qry.setParameter("prmIsActive", prmIsActive);
	    }
	    if (StringUtils.isNotEmpty(prmCreditAccount)) {
	    	qry.setParameter("prmCreditAccount", prmCreditAccount);
	    }
	    if (prmIsSameorg != null) {
	    	qry.setParameter("prmIsSameorg", prmIsSameorg);
	    }
	    if (prmDataendfact != null && prmDataendfact.getFrom() != null) {
	    	qry.setParameter("prmDataendfactFrom", prmDataendfact.getFrom(),TemporalType.DATE);
	    }
	    if (prmDataendfact != null && prmDataendfact.getTo() != null) {
	    	qry.setParameter("prmDataendfactTo", DateUtils.addDays(prmDataendfact.getTo(),1),TemporalType.DATE);
	    }	    
	    if (prmStatusId != null) {
	    	qry.setParameter("prmStatusId", prmStatusId);
	    }
	    if (prmCreditsum != null && prmCreditsum.getFrom() != null) {
	    	qry.setParameter("prmCreditsumFrom", prmCreditsum.getFrom().doubleValue());
	    }
	    if (prmCreditsum != null && prmCreditsum.getTo() != null) {
	    	qry.setParameter("prmCreditsumTo", prmCreditsum.getTo().doubleValue());
	    }
	    if (prmStake != null && prmStake.getFrom() != null) {
	    	qry.setParameter("prmStakeFrom", prmStake.getFrom().doubleValue());
	    }
	    if (prmStake != null && prmStake.getTo() != null) {
	    	qry.setParameter("prmStakeTo", prmStake.getTo().doubleValue());
	    }	  
	    if (StringUtils.isNotEmpty(prmSurname)) {
    		qry.setParameter("prmSurname", "%" + prmSurname.trim().toUpperCase() + "%");
    	}
		
	}

	@Override
	public void nullIfEmpty() {
		if (NULL_INT_VALUE.equals(prmStatusId)) {
			prmStatusId = null;
		}
		if (NULL_INT_VALUE.equals(prmTypeId)) {
			prmTypeId = null;
		}
		if (StringUtils.isBlank(prmNomer)) {
			prmNomer = null;
		}
	
		if (StringUtils.isBlank(prmSurname)) {
			prmSurname = null;
		}
		if (NULL_INT_VALUE.equals(prmIsSameorg)) {
			prmIsSameorg = null;
		}	
		if (NULL_INT_VALUE.equals(prmIsOver)) {
			prmIsOver = null;
		}	
		
		if (NULL_INT_VALUE.equals(prmIsActive)) {
			prmIsActive = null;
		}	

		if (prmCreditsum != null && prmCreditsum.getFrom() != null && prmCreditsum.getFrom().intValue() == 0) {
			prmCreditsum.setFrom(null);
		}
		if (prmCreditsum != null && prmCreditsum.getTo() != null && prmCreditsum.getTo().intValue() == 0) {
			prmCreditsum.setTo(null);
		}		
		
		if (prmCreditstake != null && prmCreditstake.getFrom() != null && prmCreditstake.getFrom().intValue() == 0) {
			prmCreditstake.setFrom(null);
		}
		if (prmCreditstake != null && prmCreditstake.getTo() != null && prmCreditstake.getTo().intValue() == 0) {
			prmCreditstake.setTo(null);
		}	
		
	}

	@Override
	public void prepareParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Credit wrapEntity(Object entity) {
		return new Credit((CreditEntity) entity);
	}

	@Override
	public void clearParams() {
		prmStatusId = null;
		prmPartnerId = Partner.SYSTEM;
		prmTypeId = null;
		prmNomer = null;
		prmDatabeg = new DateRange(null, null);
		prmDataend = new DateRange(null, null);
		prmDataendfact = new DateRange(null, null);
		prmSurname=null;
		prmCreditsum = new MoneyRange(null, null);
		prmCreditstake = new MoneyRange(null, null);				
		prmIsOver = null;
		prmIsSameorg = null;
		prmIsActive=null;
		prmAccountTypeId = null;
		prmCreditAccount = null;
		prmListId = null;
		prmPeopleMainId = null;
		prmStake = new MoneyRange(null, null);
	}

	public String getPrmNomer() {
		return prmNomer;
	}

	public void setPrmNomer(String prmNomer) {
		this.prmNomer = prmNomer;
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

	public DateRange getPrmDataendfact() {
		return prmDataendfact;
	}

	public void setPrmDataendfact(DateRange prmDataendfact) {
		this.prmDataendfact = prmDataendfact;
	}

	public MoneyRange getPrmCreditsum() {
		return prmCreditsum;
	}

	public void setPrmCreditsum(MoneyRange prmCreditsum) {
		this.prmCreditsum = prmCreditsum;
	}

	public String getPrmSurname() {
		return prmSurname;
	}

	public void setPrmSurname(String prmSurname) {
		this.prmSurname = prmSurname;
	}

	public Integer getPrmStatusId() {
		return prmStatusId;
	}

	public void setPrmStatusId(Integer prmStatusId) {
		this.prmStatusId = prmStatusId;
	}

	public Integer getPrmIsOver() {
		return prmIsOver;
	}

	public void setPrmIsOver(Integer prmIsOver) {
		this.prmIsOver = prmIsOver;
	}

	public Integer getPrmIsActive() {
		return prmIsActive;
	}

	public void setPrmIsActive(Integer prmIsActive) {
		this.prmIsActive = prmIsActive;
	}

	public Integer getPrmIsSameorg() {
		return prmIsSameorg;
	}

	public void setPrmIsSameorg(Integer prmIsSameorg) {
		this.prmIsSameorg = prmIsSameorg;
	}

	public Integer getPrmPartnerId() {
		return prmPartnerId;
	}

	public void setPrmPartnerId(Integer prmPartnerId) {
		this.prmPartnerId = prmPartnerId;
	}

	public Integer getPrmTypeId() {
		return prmTypeId;
	}

	public void setPrmTypeId(Integer prmTypeId) {
		this.prmTypeId = prmTypeId;
	}

	public MoneyRange getPrmCreditstake() {
		return prmCreditstake;
	}

	public void setPrmCreditstake(MoneyRange prmCreditstake) {
		this.prmCreditstake = prmCreditstake;
	}

	@Override
	public String generateListName() {
		String sname = "Займы ";
		sname = sname + "от " + new Date().toString();
		return sname;
	}

	@Override
	public void copyParams(ListContainer<Credit> source) {
		ListCContainer src = (ListCContainer) source;
		this.prmAccountTypeId = src.prmAccountTypeId;
		this.prmCreditAccount = src.prmCreditAccount;
		this.prmCreditstake = src.prmCreditstake;
		this.prmCreditsum = src.prmCreditsum;
		this.prmDatabeg = src.prmDatabeg;
		this.prmDataend = src.prmDataend;
		this.prmDataendfact = src.prmDataendfact;
		this.prmIsActive = src.prmIsActive;
		this.prmIsOver = src.prmIsOver;
		this.prmIsSameorg = src.prmIsSameorg;
		this.prmListId = src.prmListId;
		this.prmNomer = src.prmNomer;
		this.prmPartnerId = src.prmPartnerId;
		this.prmPeopleMainId  = src.prmPeopleMainId;
		this.prmStake = src.prmStake;
		this.prmStatusId = src.prmStatusId;
		this.prmSurname = src.prmSurname;
		this.prmTypeId = src.prmTypeId;
		
	}
	
	

}
