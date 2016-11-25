package ru.simplgroupp.ejb;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import ru.simplgroupp.contact.protocol.v2.response.unpacked.RowUnp;
import ru.simplgroupp.exception.ReferenceException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.SearchUtil;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.*;

import java.util.*;
import java.util.logging.Logger;

@Stateless(name = "ReferenceBooks")
@Local(ReferenceBooksLocal.class)
public class ReferenceBooks implements ReferenceBooksLocal {
    private static Map<String, Object> blackListSortMapping = new HashMap<String, Object>(4);

    static {
        blackListSortMapping.put("surname", "rb.surname");
        blackListSortMapping.put("birthday", "rb.birthdate");
        blackListSortMapping.put("series", "rb.series");
        blackListSortMapping.put("mobilePhone", "rb.mobilePhone");
        blackListSortMapping.put("reasonId", "rb.reasonId.name");
        blackListSortMapping.put("sourceId", "rb.sourceId.name");
    }

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @Inject Logger logger;
 
    @Override
    public List<Reference> listReference(int refHeaderId) {
        Query qry = emMicro.createNamedQuery("listReferenceHA");
        qry.setParameter("headerid", refHeaderId);
        List<ReferenceEntity> lstRef = qry.getResultList();
        List<Reference> lstRes = new ArrayList<Reference>(lstRef.size());
        if (lstRef.size()>0){
          for (ReferenceEntity ent : lstRef) {
            lstRes.add(new Reference(ent));
          }
        }
        return lstRes;
    }
    
    @Override
    public List<Reference> listReferenceTop(int refHeaderId) {
        Query qry = emMicro.createNamedQuery("listReferenceHAT");
        qry.setParameter("headerid", refHeaderId);
        List<ReferenceEntity> lstRef = qry.getResultList();
        List<Reference> lstRes = new ArrayList<Reference>(lstRef.size());
        if (lstRef.size()>0){
          for (ReferenceEntity ent : lstRef) {
            lstRes.add(new Reference(ent));
          }
        }
        return lstRes;
    }

    @Override
    public List<Reference> listReferenceSub(int refHeaderId, int parentId) {
        Query qry = emMicro.createNamedQuery("listReferenceHAP");
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("parentid", parentId);
        List<ReferenceEntity> lstRef = qry.getResultList();
        List<Reference> lstRes = new ArrayList<Reference>(lstRef.size());
        if (lstRef.size()>0){
          for (ReferenceEntity ent : lstRef) {
            lstRes.add(new Reference(ent));
          }
        }
        return lstRes;
    }    
    

    protected List<Reference> listReference(int refHeaderId,int codeInteger) {
        Query qry = emMicro.createNamedQuery("listReferenceHC");
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("codeinteger", codeInteger);
        List<ReferenceEntity> lstRef = qry.getResultList();
        List<Reference> lstRes = new ArrayList<Reference>(lstRef.size());
        for (ReferenceEntity ent : lstRef) {
            lstRes.add(new Reference(ent));
        }
        return lstRes;
    }
    
    @Override
    public Reference findByCodeInteger(Integer refHeaderId, Integer codeInteger) {
        
    	 if (refHeaderId==null){
         	return null;
         }
         if (codeInteger==null){
         	return null;
         }
         
        Query qry = emMicro.createNamedQuery("listReferenceHC1");
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("codeInteger", codeInteger);
        List<ReferenceEntity> lstRef = qry.getResultList();
        if (lstRef.size() > 0) {
            return new Reference(lstRef.get(0));
        } else {
            return null;
        }
    }
    
    @Override
    public ReferenceEntity findByCodeIntegerEntity(Integer refHeaderId, Integer codeInteger) {
        if (refHeaderId==null){
        	return null;
        }
        if (codeInteger==null){
        	return null;
        }
        Query qry = emMicro.createNamedQuery("listReferenceHC");
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("codeInteger", codeInteger);
        List<ReferenceEntity> lstRef = qry.getResultList();
        if (lstRef.size() > 0) {
            return lstRef.get(0);
        } else {
            return null;
        }
    }    

    @Override
    public ReferenceEntity findByCodeEntity(Integer refHeaderId, String code) {
        if (refHeaderId==null){
        	return null;
        }
        if (StringUtils.isEmpty(code)){
        	return null;
        }
        Query qry = emMicro.createNamedQuery("listReferenceHCON");
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("code", code);
        List<ReferenceEntity> lstRef = qry.getResultList();
        if (lstRef.size() > 0) {
            return lstRef.get(0);
        } else {
            return null;
        }
    }    
    
     
    @Override
    public List<CreditStatus> getCreditRequestStatuses() {
        Query qry = emMicro.createNamedQuery("listCreditRequestStatuses");

        List<CreditStatusEntity> lstCs = qry.getResultList();
        List<CreditStatus> lstRes = new ArrayList<CreditStatus>(lstCs.size());
        for (CreditStatusEntity ent : lstCs) {
            lstRes.add(new CreditStatus(ent));
        }
        return lstRes;
    }

    @Override
    public CreditStatusEntity getCreditRequestStatus(int id) {
        CreditStatusEntity ent = emMicro.find(CreditStatusEntity.class, new Integer(id));
        return ent;
    }

    @Override
    public List<Country> getCountriesList() {
        String sql = "from ru.simplgroupp.persistence.CountryEntity order by name";
        Query qry = emMicro.createQuery(sql);

        List<CountryEntity> lstCnt = qry.getResultList();
        List<Country> lstRes = new ArrayList<Country>(lstCnt.size());
        for (CountryEntity ent : lstCnt) {
            lstRes.add(new Country(ent));
        }
        return lstRes;
    }
    
    @Override
    public CountryEntity getCountryEntity(String code2letter) {
    	return emMicro.find(CountryEntity.class, code2letter);
    }

    @Override
    public Country getCountry(String code2letter) {
        String sql = "from ru.simplgroupp.persistence.CountryEntity where code = :code";
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("code", code2letter);
        List<CountryEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
            return new Country(lst.get(0));
        }
    }

    @Override
    public List<Reference> getDocverTypes() {
        return listReference(RefHeader.DOC_VER_TYPE);
    }

    @Override
    public List<Reference> getPensDocTypes() {
        return listReference(RefHeader.PENSDOC_TYPES);
    }

    @Override
    public Reference getPensDocType(Integer codeInteger) {
        return findByCodeInteger(RefHeader.PENSDOC_TYPES,codeInteger);
    }

    @Override
    public List<Reference> getContactTypes() {
        return listReference(RefHeader.CONTACT_TYPE);
    }
    
    @Override
    public List<Reference> getApplicationWays() {
        return listReference(RefHeader.APPLICATION_WAY);
    }

    @Override
    public List<Reference> getCallResultTypesList() {
        return listReference(RefHeader.CALL_RESULT_TYPE);
    }

    @Override
    public Reference getContactType(int codeInteger) {
        return findByCodeInteger(RefHeader.CONTACT_TYPE, codeInteger);
    }

    @Override
    public List<Reference> getMarriageTypes() {
        return listReference(RefHeader.MARITAL_STATUS_SYSTEM);
    }

    @Override
    public List<Reference> getEducationTypes() {
        return listReference(RefHeader.EDUCATION_TYPE);
    }

    @Override
    public List<Reference> getEmployTypes() {
        return listReference(RefHeader.EMPLOY_TYPE);
    }

    @Override
    public Reference getEmployType(int codeInteger) {
        return findByCodeInteger(RefHeader.EMPLOY_TYPE, codeInteger);
    }

    @Override
    public List<Reference> getProfessionTypes() {
        return listReference(RefHeader.PROFESSION_TYPE);
    }

    @Override
    public List<Reference> getIncomeFreqTypes() {
        return listReference(RefHeader.INCOME_FREQ_TYPE);
    }

    @Override
    public Reference getIncomeFreqType(int codeInteger) {
        return this.findByCodeInteger(RefHeader.INCOME_FREQ_TYPE, codeInteger);
    }

    @Override
    public List<Reference> getRealtyTypes() {
        return listReference(RefHeader.REALTY_TYPE);
    }

  
    @Override
    public List<Reference> getOrganizationTypes() {
        return listReference(RefHeader.ORGANIZATION_BUSINESS);
    }

    @Override
    public List<Reference> getOrganizationByTypes() {
        return listReference(RefHeader.ORGANIZATION_TYPE);
    }
    
    @Override
    public List<Reference> getCurrencyTypes() {
        return listReference(RefHeader.CURRENCY_TYPE);
    }

    @Override
    public List<Reference> getCreditStatusTypes() {
        return listReference(RefHeader.CREDIT_STATUS_TYPE);
    }

    @Override
    public List<Reference> getAccountTypes() {
        return listReference(RefHeader.ACCOUNT_TYPE);
    }

    @Override
    public Reference getAccountType(int codeInteger) {
        return this.findByCodeInteger(RefHeader.ACCOUNT_TYPE, codeInteger);
    }
    
    @Override
    public List<Reference> getAddressTypes() {
        return listReference(RefHeader.ADDRESS_TYPE);
    }

    @Override
    public List<Reference> getRefusalReasonTypes() {
        return listReference(RefHeader.REFUSE_TYPE);
    }

    @Override
    public List<Reference> getCreditTypes() {
        return listReference(RefHeader.CREDIT_TYPE);
    }

    @Override
    public List<Reference> getPaymentTypes() {
        return listReference(RefHeader.PAYMENT_TYPE);
    }

    @Override
    public List<Partner> getPartners() {
        Query qry = emMicro.createNamedQuery("listPartners");

        List<PartnersEntity> lstRqs = qry.getResultList();
        List<Partner> lstRes = new ArrayList<Partner>(lstRqs.size());
        for (PartnersEntity ent : lstRqs) {
            lstRes.add(new Partner(ent));
        }
        return lstRes;
    }
    
    @Override
    public PartnersEntity getPartnerEntity(int id) {
    	return emMicro.find(PartnersEntity.class, new Integer(id));
    }

    @Override
    public Partner getPartner(int id) {
        PartnersEntity ent = getPartnerEntity(id);
        if (ent == null) {
            return null;
        } else {
            return new Partner(ent);
        }
    }

    @Override
    public List<Reference> getDocumentTypes() {
        return listReference(RefHeader.SYSTEM_DOC_TYPE);
    }    

    @Override
    public List<Reference> getGenders() {
        return listReference(RefHeader.GENDER_TYPE);
    }

    @Override
    public Reference findByCode(Integer refHeaderId, String code) {
    	 if (refHeaderId==null){
         	return null;
         }
         if (StringUtils.isEmpty(code)){
         	return null;
         }
         
        String hql = "from ru.simplgroupp.persistence.ReferenceEntity where (refHeaderId.id = :headerid) and (code like :code)";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("code", code);
        List<ReferenceEntity> lstRef = qry.getResultList();
        if (lstRef.size() > 0) {
            return new Reference(lstRef.get(0));
        } else {
            return null;
        }
    }

    protected Country findCountryByCode(String code) {
    	if (StringUtils.isEmpty(code)){
         	return null;
        }
    	
        String hql = "from ru.simplgroupp.persistence.CountryEntity where code = :code";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("code", code);
        List<CountryEntity> lst = qry.getResultList();
        if (lst.size() > 0) {
            return new Country(lst.get(0));
        } else {
            return null;
        }
    }
    
    @Override
    public Reference getDocumentType(int codeInteger) {
        return findByCodeInteger(RefHeader.SYSTEM_DOC_TYPE, codeInteger);
    }    

    @Override
    public Reference getGender(int codeInteger) {
        return findByCodeInteger(RefHeader.GENDER_TYPE, codeInteger);
    }

    @Override
    public Reference getAddressType(int codeInteger) {
        return findByCodeInteger(RefHeader.ADDRESS_TYPE, codeInteger);
    }

    @Override
    public Reference getDocverType(int codeInteger) {
        return findByCodeInteger(RefHeader.DOC_VER_TYPE, codeInteger);
    }

   
	@Override
	public Integer getCodeIntegerById(int id) {
		ReferenceEntity reference=getReferenceEntity(id);
		if (reference!=null){
			return reference.getCodeinteger();
		}
		return null;

	}

	@Override
	public String getCodeById(int id) {
		ReferenceEntity reference=getReferenceEntity(id);
		if (reference!=null){
			return reference.getCode();
		}
		return null;
	}

	@Override
	public Reference getCurrency(String code) {
		return findByCode(RefHeader.CURRENCY_TYPE, code);
	}

	@Override
	public Reference getCreditType(String code) {
		return findByCode(RefHeader.CREDIT_TYPE, code);
	}

	@Override
	public ScoreModelEntity getScoreModel(int id){
		return emMicro.find(ScoreModelEntity.class, id);
	}
	
    @Override
    public ScoreModel getModelByCode(PartnersEntity partnersId, String code) {
      if (partnersId == null || StringUtils.isBlank(code)) {
        return null;
      }
      String hql = "from ru.simplgroupp.persistence.ScoreModelEntity where partnersId = :partnersId and code = :code";
      List<ScoreModelEntity> lst = JPAUtils.getResultListFromSql(emMicro, hql, Utils.mapOf("partnersId", partnersId,
    		"code", code));
      if (lst.size() > 0) {
          return new ScoreModel(lst.get(0));
      } else {
          return null;
      }
    }
    
	@Override
	public Reference getRefusalReason(int codeInteger) {
		return findByCodeInteger(RefHeader.REFUSE_TYPE, codeInteger);
	}
	
	@Override
	public ReferenceEntity getRefusalReasonEntity(int codeInteger) {
		return findByCodeIntegerEntity(RefHeader.REFUSE_TYPE, codeInteger);
	}	
	
	
	 public List<FMSRegion> getRegionsList(){
		String sql = "from ru.simplgroupp.persistence.RegionsEntity order by name";
	    Query qry = emMicro.createQuery(sql); 
        List<RegionsEntity> lstCnt = qry.getResultList();
        List<FMSRegion> lstRes = new ArrayList<FMSRegion>(lstCnt.size());
        for (RegionsEntity ent : lstCnt) {
            lstRes.add(new FMSRegion(ent));
        }
        return lstRes;

    }
	 
	 @Override
	 public List<FMSRegion> getRegionsNewList(){
		String sql = "from ru.simplgroupp.persistence.RegionsEntity order by name";
	    Query qry = emMicro.createQuery(sql); 
        List<RegionsEntity> lstCnt = qry.getResultList();
        List<FMSRegion> lstRes = new ArrayList<FMSRegion>(lstCnt.size());
        for (RegionsEntity ent : lstCnt) {
            lstRes.add(new FMSRegion(ent));
        }
        return lstRes;
    }
	 
    @Override
    public FMSRegion getRegion(String codeReg) {
        String sql = "from ru.simplgroupp.persistence.RegionsEntity where codereg = :codereg";
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("codereg", codeReg);
        List<RegionsEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
            return new FMSRegion(lst.get(0));
        }

    }

 
    @Override
    public Reference getMarriageType(int codeInteger) {
        return findByCodeInteger(RefHeader.MARITAL_STATUS_SYSTEM, codeInteger);
    }

    @Override
    public Reference getRealtyType(int codeInteger) {
        return findByCodeInteger(RefHeader.REALTY_TYPE, codeInteger);
    }

    @Override
	public List<Bank> getBanksList() {
        Query qry = emMicro.createNamedQuery("banksList");

        List<BankEntity> lstCnt = qry.getResultList();
        List<Bank> lstRes = new ArrayList<Bank>(lstCnt.size());
        for (BankEntity ent : lstCnt) {
            lstRes.add(new Bank(ent));
        }
        return lstRes;
		
	}
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateBanks(List<Bank> banks) {
    	List<Bank> savedBanks = getBanksList();
    	HashMap<String, BankEntity> bankMap = new HashMap<String, BankEntity>();
    	for(Bank b : banks) {
    		bankMap.put(b.getBik(), b.getEntity());
    	}
    	Iterator<Bank> it = savedBanks.iterator();
    	while(it.hasNext()){
    		Bank sb = it.next();
    		BankEntity b = bankMap.get(sb.getBik());
    		// есть ли в списке активных банков
    		if(b != null){
    			if(b.getAddress().equalsIgnoreCase(sb.getAddress()) && 
    					b.getCorAccount().equals(sb.getCorAccount()) &&
    					b.getName().equalsIgnoreCase(sb.getName()) && 
    					b.getIsActive() == sb.getIsActive()){
    				//поля не изменились - удаляем
    				it.remove();
    				bankMap.remove(sb.getBik());
    				continue;
    			}
    			//поля изменились - вносим в список обновлений
    			sb.setAddress(b.getAddress());
    			sb.setCorAccount(b.getCorAccount());
    			sb.setName(b.getName());
    			sb.setIsActive(b.getIsActive());
    		} else {
    			sb.setIsActive(0);
    		}
    		bankMap.put(sb.getBik(), sb.getEntity());
        }
    	
    	for(BankEntity b : bankMap.values()) {
    		emMicro.merge(b);
    	}
    }

    @Override
    public List<Bank> getBanksList(String term, Integer page, Integer pageSize) {
        Query qry;

        if (term == null) {
            qry = emMicro.createNamedQuery("banksList");
        } else {
            qry = emMicro.createNamedQuery("banksListTerm");
            qry.setParameter("term", term);
        }

        if (page != null && pageSize != null) {
            qry.setFirstResult((page-1) * pageSize);
            qry.setMaxResults(pageSize);
        }

        List<BankEntity> lstCnt = qry.getResultList();
        List<Bank> lstRes = new ArrayList<Bank>(lstCnt.size());
        for (BankEntity ent : lstCnt) {
            lstRes.add(new Bank(ent));
        }
        return lstRes;
    }    

	@Override
	public Bank getBankByName(String name) {
	      Query qry = emMicro.createNamedQuery("getBankByName");
	      qry.setParameter("name", "%"+name.toUpperCase()+"%");
	      List<BankEntity> lst = qry.getResultList();
	      if (lst.size() == 0) {
	            return null;
	      } else {
	            return new Bank(lst.get(0));
	      }
		
	}
	
	@Override
	public Reference getCurrency(int codeInteger) {
		return findByCodeInteger(RefHeader.CURRENCY_TYPE, codeInteger);
	}

	@Override
	public Reference getCreditType(int codeInteger) {
		return findByCodeInteger(RefHeader.CREDIT_TYPE, codeInteger);
	}

	@Override
	public Reference getCreditStatusType(int codeInteger) {
		return findByCodeInteger(RefHeader.CREDIT_STATUS_TYPE, codeInteger);
	}
	
	@Override
	public List<Reference> listReferenceAll(int refHeaderId) {
		 String hql = "from ru.simplgroupp.persistence.ReferenceEntity where (refHeaderId.id = :headerid) order by name";
	     Query qry = emMicro.createQuery(hql);
	     qry.setParameter("headerid", refHeaderId);
	     List<ReferenceEntity> lstRef = qry.getResultList();
	     List<Reference> lstRes = new ArrayList<Reference>(lstRef.size());
	     for (ReferenceEntity ent : lstRef) {
	          lstRes.add(new Reference(ent));
	     }
	     return lstRes;
	}

	@Override
	public Reference getEducationType(int codeInteger) {
		return findByCodeInteger(RefHeader.EDUCATION_TYPE, codeInteger);
	}

	@Override
	public Reference getProfessionType(int codeInteger) {
		return findByCodeInteger(RefHeader.PROFESSION_TYPE, codeInteger);
	}

	@Override
	public RegionsEntity getRegions(String codeReg) {
		return (RegionsEntity) JPAUtils.getSingleResultFromSql(emMicro, "from ru.simplgroupp.persistence.RegionsEntity where codereg = :codereg", 
				Utils.mapOf("codereg", codeReg));
    }

	@Override
	public Reference getMismatchType(String code) {
		return findByCode(RefHeader.MISMATCH_TYPE, code);
	}
	
	@Override
	public ReferenceEntity getReferenceEntity(int id) {
		return emMicro.find(ReferenceEntity.class, new Integer(id));
	}

	@Override
	public Reference getReference(int id) {
		ReferenceEntity ent = getReferenceEntity(id);
		if (ent!=null){
		    return new Reference(ent);
		}
		return null;
	}

	@Override
	public Reference getSpouseType(int codeInteger) {
		 return findByCodeInteger(RefHeader.SPOUSE_TYPE, codeInteger);
	}

	@Override
	public List<Reference> getSpouseTypes() {
		  return listReference(RefHeader.SPOUSE_TYPE);
	}

	@Override
	public Reference getOrganizationType(int codeInteger) {
		return findByCodeInteger(RefHeader.ORGANIZATION_BUSINESS, codeInteger);
	}

	@Override
	public Reference getOrganizationByType(int codeInteger) {
		return findByCodeInteger(RefHeader.ORGANIZATION_TYPE, codeInteger);
	}
	
	@Override
	public List<Reference> getExtSalaryTypes() {
		return listReference(RefHeader.EXT_SALARY_TYPE);
	}

	@Override
	public Reference getExtSalaryType(int codeInteger) {
		return findByCodeInteger(RefHeader.EXT_SALARY_TYPE, codeInteger);
	}

	@Override
	public Reference getPaymentType(int codeInteger) {
		return findByCodeInteger(RefHeader.PAYMENT_TYPE, codeInteger);
	}
	
	@Override
	public List<Reference> getReasonEndTypes() {
		return listReference(RefHeader.DOC_END_TYPE);
	}

	@Override
	public Reference getReasonEndType(int codeInteger) {
		return findByCodeInteger(RefHeader.DOC_END_TYPE, codeInteger);
	}

	@Override
	public List<Reference> getAuthorityTypes() {
		return listReference(RefHeader.AUTHORITY_TYPE);
	}

	@Override
	public Reference getAuthorityType(int codeInteger) {
		return findByCodeInteger(RefHeader.AUTHORITY_TYPE, codeInteger);
	}

	@Override
	public List<Reference> getCreditOkbTypes() {
		return listReference(RefHeader.CREDIT_TYPE_OKB);
	}

	@Override
	public Reference getCreditOkbType(int codeInteger) {
		return findByCodeInteger(RefHeader.CREDIT_TYPE_OKB, codeInteger);
	}

	@Override
	public List<Reference> getCreditRelationTypes() {
		return listReference(RefHeader.CREDIT_RELATION_STATE);
	}

	@Override
	public Reference getCreditRelationType(int codeInteger) {
		return findByCodeInteger(RefHeader.CREDIT_RELATION_STATE, codeInteger);
	}

	@Override
	public List<Reference> getCreditFreqPaymentTypes() {
		return listReference(RefHeader.CREDIT_PAYMENT_FREQ);
	}

	@Override
	public Reference getCreditFreqPaymentType(int codeInteger) {
		return findByCodeInteger(RefHeader.CREDIT_PAYMENT_FREQ, codeInteger);
	}

	@Override
	public List<RefSummary> getRefSummaryList() {
		   String sql = "from ru.simplgroupp.persistence.RefSummaryEntity";
	        Query qry = emMicro.createQuery(sql);

	        List<RefSummaryEntity> lstCs = qry.getResultList();
	        List<RefSummary> lstRes = new ArrayList<RefSummary>(lstCs.size());
	        for (RefSummaryEntity ent : lstCs) {
	            lstRes.add(new RefSummary(ent));
	        }
	        return lstRes;
	}

	@Override
	public RefSummary getRefSummaryItem(int id) {
		 RefSummaryEntity ref = emMicro.find(RefSummaryEntity.class, id);
	     if (ref!=null){
	    	 return new RefSummary(ref);
	     }
	     return null;
	}

	@Override
	public VerificationMark getVerificationMarks(double mark, int kind) {
		String sql = "from ru.simplgroupp.persistence.VerificationMarkEntity where kind=:kind and (:mark between markMin and markMax)";
		 Query qry = emMicro.createQuery(sql);
	     qry.setParameter("kind", kind); 
	     qry.setParameter("mark", mark); 
	     List<VerificationMarkEntity> lst=qry.getResultList();
	     if (lst.size()>0)
	    	 return new VerificationMark(lst.get(0));
	     else
		    return null;
	}

	@Override
	public ReferenceEntity findByCodeIntegerMain(int refHeaderId, int codeInteger) {
		   String hql = "from ru.simplgroupp.persistence.ReferenceEntity where (refHeaderIdMain.id = :headerid) and (codeintegermain = :codeInteger)";
	        Query qry = emMicro.createQuery(hql);
	        qry.setParameter("headerid", refHeaderId);
	        qry.setParameter("codeInteger", codeInteger);
	        List<ReferenceEntity> lstRef = qry.getResultList();
	        return (ReferenceEntity) Utils.firstOrNull(lstRef);
	}

	@Override
	public RefNegative findByArticle(String article) {
		String hql = "from ru.simplgroupp.persistence.RefNegativeEntity where (article = :article)";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("article", article);
        List<RefNegativeEntity> lstRef = qry.getResultList();
        if (lstRef.size() > 0) {
            return new RefNegative(lstRef.get(0));
        } else {
            return null;
        }
	}

	@Override
	public ReferenceEntity findByCodeMain(int refHeaderId, String code) {
		String hql = "from ru.simplgroupp.persistence.ReferenceEntity where (refHeaderIdMain.id = :headerid) and (codemain = :code)";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("headerid", refHeaderId);
        qry.setParameter("code", code);
        List<ReferenceEntity> lstRef = qry.getResultList();
        return (ReferenceEntity) Utils.firstOrNull(lstRef);
     
	}

	@Override
	public ReferenceEntity findFromKb(int refHeaderId, int codeInteger) throws ReferenceException {
		ReferenceEntity rf=findByCodeIntegerEntity(refHeaderId, codeInteger);
		if (rf!=null)
		{
			ReferenceEntity rfe=null;
			//если в справочнике есть числовой код
			if (rf.getCodeinteger()!=null){
				if (rf.getRefHeaderIdMain()!=null){	
			       rfe=findByCodeIntegerEntity(rf.getRefHeaderIdMain().getId(), rf.getCodeintegermain());
				}
			}  else {
				//если в справочнике есть символьный код
				if (rf.getRefHeaderIdMain()!=null){
				   rfe=findByCodeEntity(rf.getRefHeaderIdMain().getId(), rf.getCodemain());
				}
			}
			return rfe;
		}
		return null;
	}

	@Override
	public ReferenceEntity findFromKbCode(int refHeaderId, String code) throws ReferenceException {
		ReferenceEntity rf=findByCodeEntity(refHeaderId,code);
		if (rf!=null)
		{
			ReferenceEntity rfe=null;
			//если в справочнике есть символьный код
			if (rf.getCode()!=null) {
				if (rf.getRefHeaderIdMain()!=null){
				  rfe=findByCodeEntity(rf.getRefHeaderIdMain().getId(),rf.getCodemain());
				}
			} else {	
				//если в справочнике есть числовой код
				if (rf.getRefHeaderIdMain()!=null){
				  rfe=findByCodeIntegerEntity(rf.getRefHeaderIdMain().getId(),rf.getCodeintegermain());
				}
			}
			return rfe;
		}
		return null;
	}

	@Override
	public PartnersEntity getPartnerFromLink(ReferenceEntity ref) {
		String hql = "from ru.simplgroupp.persistence.PartnerLinksEntity where (referenceId = :ref) and (isActive = :isactive)";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("ref", ref);
        qry.setParameter("isactive", ActiveStatus.ACTIVE);
        List<PartnerLinksEntity> lst=qry.getResultList();
        if (lst.size()>0){
        	return lst.get(0).getPartnersId();
        } 
		return null;
	}

	@Override
	public Partner findPartnerByName(String name) {
		String sql = "from ru.simplgroupp.persistence.PartnersEntity where (name = :name)";
		Query qry = emMicro.createQuery(sql);
		qry.setParameter("name", name);
		List<PartnersEntity> lst = qry.getResultList();
		if (lst.size() == 0) {
			return null;
		} else {
			return new Partner(lst.get(0));
		}
	}

	@Override
	public List<Reference> getPaySumTypes() {
		return listReference(RefHeader.PAY_SUM_TYPE);
	}

	@Override
	public Reference getPaySumType(int codeInteger) {
		return findByCodeInteger(RefHeader.PAY_SUM_TYPE, codeInteger);
	}

	
	
	@Override
	public List<Reference> getCreditOverdueTypes() {
		return listReference(RefHeader.CREDIT_OVERDUE_STATE);
	}

	@Override
	public Reference getCreditOverdueType(String code) {
		return findByCode(RefHeader.CREDIT_OVERDUE_STATE, code);
	}

	@Override
	public List<Reference> getPaymentStatusTypes() {
		return listReference(RefHeader.PAY_STATUS_TYPE);
	}

	@Override
	public Reference getPaymentStatusType(String code) {
		return findByCode(RefHeader.PAY_STATUS_TYPE, code);
	}

	@Override
	public List<Partner> getPartnersForPayment() {
		    Query qry = emMicro.createNamedQuery("partnersForPayment");
		
	        List<PartnersEntity> lstRqs = qry.getResultList();
	        List<Partner> lstRes = new ArrayList<Partner>(lstRqs.size());
	        for (PartnersEntity ent : lstRqs) {
	            lstRes.add(new Partner(ent));
	        }
	        return lstRes;
	}

	@Override
	public List<Partner> getPartnersForCreditHistory() {
	        Query qry = emMicro.createNamedQuery("partnersForCreditHistory");

	        List<PartnersEntity> lstRqs = qry.getResultList();
	        List<Partner> lstRes = new ArrayList<Partner>(lstRqs.size());
	        for (PartnersEntity ent : lstRqs) {
	            lstRes.add(new Partner(ent));
	        }
	        return lstRes;
	}

	@Override
	public List<Partner> getPartnersForScoring() {
	        Query qry = emMicro.createNamedQuery("partnersForScoring");

	        List<PartnersEntity> lstRqs = qry.getResultList();
	        List<Partner> lstRes = new ArrayList<Partner>(lstRqs.size());
	        for (PartnersEntity ent : lstRqs) {
	            lstRes.add(new Partner(ent));
	        }
	        return lstRes;
	}

	@Override
	public List<RefuseReason> getRefuseReasons() {
		  String sql = "from ru.simplgroupp.persistence.RefuseReasonEntity order by name";
	      Query qry = emMicro.createQuery(sql);
	      
	        List<RefuseReasonEntity> lstRef = qry.getResultList();
	        List<RefuseReason> lstRes = new ArrayList<RefuseReason>(lstRef.size());
	        for (RefuseReasonEntity ent : lstRef) {
	            lstRes.add(new RefuseReason(ent));
	        }
	        return lstRes;
	}

	@Override
	public List<RefuseReason> getRefuseReasonsForDecision() {
		  String sql = "from ru.simplgroupp.persistence.RefuseReasonEntity where forDecision=:forDecision order by name";
	      List<RefuseReasonEntity> lstRef = JPAUtils.getResultListFromSql(emMicro, sql, Utils.mapOf("forDecision",ActiveStatus.ACTIVE));
	      List<RefuseReason> lstRes = new ArrayList<RefuseReason>(lstRef.size());
	      for (RefuseReasonEntity ent : lstRef) {
	          lstRes.add(new RefuseReason(ent));
	      }
	      return lstRes;
	}
	
    @Override
    public List<RefBonus> getRefBonus() {
        String sql = "from ru.simplgroupp.persistence.RefBonusEntity order by name";
        TypedQuery<RefBonusEntity> qry = emMicro.createQuery(sql, RefBonusEntity.class);

        List<RefBonusEntity> lstRef = qry.getResultList();
        List<RefBonus> lstRes = new ArrayList<RefBonus>(lstRef.size());
        for (RefBonusEntity ent : lstRef) {
            lstRes.add(new RefBonus(ent));
        }
        return lstRes;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveRefBonus(List<RefBonus> bonusList) {
        if (bonusList != null) {
            for (RefBonus bonus : bonusList) {
                RefBonusEntity entity = getBonusType(bonus.getId());
                entity.setName(bonus.getName());
                entity.setAmount(bonus.getAmount());
                entity.setRate(bonus.getRate());
                entity.setIsactive(bonus.getIsactive() ? ActiveStatus.ACTIVE : ActiveStatus.ARCHIVE);
                entity.setCodeinteger(bonus.getCodeinteger());
                entity = emMicro.merge(entity);
                emMicro.persist(entity);
            }
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeRefBonus(Integer id) {
        String sql = "delete from ref_bonus where id=:id";
        Query qry = emMicro.createNativeQuery(sql);
        qry.setParameter("id", id);
        qry.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public RefBonus addBonus(RefBonus bonus) {
        RefBonusEntity entity = new RefBonusEntity();
        entity.setName(bonus.getName());
        entity.setAmount(bonus.getAmount());
        entity.setIsactive(bonus.getIsactive() ? ActiveStatus.ACTIVE : ActiveStatus.ARCHIVE);
        entity.setCodeinteger(bonus.getCodeinteger());
        entity = emMicro.merge(entity);
        emMicro.persist(entity);

        return new RefBonus(entity);
    }

    @Override
	public RefuseReason getRefuseReason(int id) {
		RefuseReasonEntity rr=getRefuseReasonEntity(id);
		if (rr!=null){
			return new RefuseReason(rr);
		}
		return null;
	}

	@Override
	public RefuseReasonEntity getRefuseReasonEntity(int id) {
		return emMicro.find(RefuseReasonEntity.class, id);
	}
	
	@Override
	public List<RefuseReason> getRefuseReasonByType(int reasonId) {
		  String sql = "from ru.simplgroupp.persistence.RefuseReasonEntity where reasonId.id=:reasonId order by name";
	      Query qry = emMicro.createQuery(sql);
	      qry.setParameter("reasonId", reasonId);
	      List<RefuseReasonEntity> lstRef = qry.getResultList();
	      List<RefuseReason> lstRes = new ArrayList<RefuseReason>(lstRef.size());
	      for (RefuseReasonEntity ent : lstRef) {
	            lstRes.add(new RefuseReason(ent));
	      }
	      return lstRes;
	}

	

	@Override
	public List<Reference> getRefuseReasonStateTypes() {
		return listReference(RefHeader.REFUSE_REASON_TYPE);
	}

	@Override
	public Reference getRefuseReasonStateType(int codeInteger) {
		return findByCodeInteger(RefHeader.REFUSE_REASON_TYPE, codeInteger);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addRefuseReason(int type,String name,String nameFull, 
			String constantName,Integer forDecision) {
		RefuseReasonEntity res=new RefuseReasonEntity();
		res.setName(name);
		res.setNameFull(nameFull);
		res.setReasonId(getRefuseReasonStateType(type).getEntity());
		res.setConstantName(constantName);
		res.setForDecision(forDecision);
		emMicro.persist(res);
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void removeRefuseReason(int id) {
		 String sql = "delete from refusereason where id=:id";
	     Query qry = emMicro.createNativeQuery(sql);
	     qry.setParameter("id", id);
	     qry.executeUpdate();
		
	}

	@Override
	public List<RefHeader> listRefHeaders(Integer partnerId,String name,Boolean isInclude) {
		  String hql = "from ru.simplgroupp.persistence.RefHeaderEntity where (1=1)";
	        if (partnerId!=null){
	        	if (isInclude){
	        	    hql=hql+" and partnersId.id=:partnerid ";
	        	} else {
	        		hql=hql+" and partnersId.id<>:partnerid ";
	        	}
	        }
	        if (StringUtils.isNotEmpty(name)){
	        	hql=hql+" and upper(name) like :name"; 
	        }
		    Query qry = emMicro.createQuery(hql);
		    if (partnerId!=null){
	           qry.setParameter("partnerid", partnerId);
		    }
		    if (StringUtils.isNotEmpty(name)){
		    	qry.setParameter("name", "%"+name.trim().toUpperCase()+"%");
		    }
	        List<RefHeaderEntity> lstRef = qry.getResultList();
	        List<RefHeader> lstRes = new ArrayList<RefHeader>(lstRef.size());
	        for (RefHeaderEntity ent : lstRef) {
	            lstRes.add(new RefHeader(ent));
	        }
	        return lstRes;
	}

	@Override
	public int countRefHeaders(Integer partnerId, String name,Boolean isInclude) {
		List<RefHeader> lstref=listRefHeaders(partnerId,name,isInclude);
	    return lstref.size();
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)		
	public RefHeader getRefHeader(int id, Set options) {
		RefHeaderEntity ent = getRefHeaderEntity(id);
		if (ent == null) {
			return null;
		} else {
			RefHeader ref = new RefHeader(ent);
			ref.init(options);
			return ref;
		}
	}

	@Override
	public RefHeaderEntity getRefHeaderEntity(int id){
		return emMicro.find(RefHeaderEntity.class, id);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveRefuseReasons(List<RefuseReason> lst) {
		if (lst.size()>0 && lst!=null)
		for (RefuseReason rf:lst){
			RefuseReasonEntity rre=getRefuseReasonEntity(rf.getId());
			rre.setName(rf.getName());
			rre.setNameFull(rf.getNameFull());
			rre.setConstantName(rf.getConstantName());
			rre.setForDecision(rf.getForDecision());
			if (rf.getReasonId()!=null){
			    rre.setReasonId(getReferenceEntity(rf.getReasonId().getId()));
			}
			rre=emMicro.merge(rre);
			emMicro.persist(rre);
		}
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addReferenceItem(int refId, String name, String code,Integer codeInteger) {
		RefHeaderEntity rh=getRefHeaderEntity(refId);
		if (rh==null){
			return;
		}
		ReferenceEntity re=new ReferenceEntity();
		re.setRefHeaderId(rh);
		re.setName(name);
		re.setCode(code);
		re.setCodeinteger(codeInteger);
		re.setIsactive(ActiveStatus.ACTIVE);
		emMicro.persist(re);
		
	}

		
	@Override
	public List<Reference> listReferences(Integer refHeader, String name,String code, Integer codeInteger, Integer refHeaderMain,String codeMain, Integer codeIntegerMain,Integer isActive) {
		   String hql = "from ru.simplgroupp.persistence.ReferenceEntity where (1=1) ";
	       if (refHeader!=null) {
	    	   hql=hql+" and (refHeaderId.id = :refHeader) ";
	       }
	       if (StringUtils.isNotEmpty(name)) {
	    	   hql=hql+" and (upper(name) like :name) ";
	       }
	       if (StringUtils.isNotEmpty(code)){
	    	   hql=hql+" and (code=:code) ";
	       }
	       if (codeInteger!=null) {
	    	   hql=hql+" and (codeinteger = :codeInteger) ";
	       }
	       if (isActive!=null){
	    	   hql=hql+" and (isactive = :isActive) ";
	       }
	       if (refHeaderMain!=null) {
	    	   hql=hql+" and (refHeaderMainId.id = :refHeaderMain) ";
	       }
	      if (StringUtils.isNotEmpty(codeMain)) {
	    	   hql=hql+" and (codemain=:codeMain) ";
	      }
	       if (codeIntegerMain!=null) {
	    	   hql=hql+" and (codeintegermain = :codeIntegerMain) ";
	       }
	       hql=hql+" order by name";
		   Query qry = emMicro.createQuery(hql);
		   if (refHeader!=null) {
	         qry.setParameter("refHeader", refHeader);
		   }
		   if (StringUtils.isNotEmpty(name)) {
			   qry.setParameter("name", "%"+name.toUpperCase()+"%"); 
		   }
		   if (StringUtils.isNotEmpty(code)) {
			   qry.setParameter("code", code);
		   }
		   if (codeInteger!=null) {
			   qry.setParameter("codeInteger", codeInteger);
		   }
		   if (isActive!=null) {
			   qry.setParameter("isActive", isActive);
		   }
		   if (refHeaderMain!=null){
		         qry.setParameter("refHeaderMain", refHeaderMain);
		   }
		   if (StringUtils.isNotEmpty(codeMain)) {
			   qry.setParameter("codeMain", codeMain);
		   }
		   if (codeIntegerMain!=null) {
			   qry.setParameter("codeIntegerMain", codeIntegerMain);
		   }
	       List<ReferenceEntity> lstRef = qry.getResultList();
	       List<Reference> lstRes = new ArrayList<Reference>(lstRef.size());
	       for (ReferenceEntity ent : lstRef) {
	            lstRes.add(new Reference(ent));
	       }
	       return lstRes;
	}

	@Override
	public int countReferences(Integer refHeader, String name, String code,
			Integer codeInteger, Integer refHeaderMain, String codeMain,
			Integer codeIntegerMain,Integer isActive) {
		  
		List<Reference> lstref=listReferences(refHeader,name,code,codeInteger,refHeaderMain,codeMain,codeIntegerMain,isActive);
        return lstref.size();
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)		
	public Reference getReference(int id, Set options) {
		ReferenceEntity ent = getReferenceEntity(id);
		if (ent == null) {
			return null;
		} else {
			Reference ref = new Reference(ent);
			ref.init(options);
			return ref;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void removeReferenceItem(int refId) {
		ReferenceEntity ent=getReferenceEntity(refId);
		if (ent == null) {
    		return;
    	}
    	emMicro.remove(ent);
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveReference(List<Reference> lst) {
		if (lst.size()>0 && lst!=null)
			for (Reference rf:lst)
			{
				ReferenceEntity rre=getReferenceEntity(rf.getId());
				if (rre!=null){
				    rre.setName(rf.getName());
				    rre.setCode(rf.getCode());
				    rre.setIsactive(rf.getIsActive());
				    rre.setCodeinteger(rf.getCodeInteger());
				    if (rf.getRefHeader()!=null){ 
				      rre.setRefHeaderId(rf.getRefHeader().getEntity());
				    }
				    rre.setCodemain(rf.getCodeMain());
				    rre.setCodeintegermain(rf.getCodeIntegerMain());
				    if (rf.getRefHeaderMain()!=null){
					    rre.setRefHeaderIdMain(rf.getRefHeaderMain().getEntity());
				    }
				    emMicro.merge(rre);
				}
			}
		
	}

	@Override
	public List<ContactPoints> getContactPoints(String cityName) {
	    return getContactPoints(cityName,null);
	}

	@Override
	public List<ContactPoints> getContactPoints(String cityName,Integer regionId) {
		String hql = "from ru.simplgroupp.persistence.ContactPointsEntity where (1=1) ";
        if (StringUtils.isNotEmpty(cityName))
        	hql=hql+" and (upper(cityHead) like :cityName) ";
        if (regionId!=null)
        	hql=hql+" and region=:regionId ";
        hql=hql+" order by address1";
		Query qry = emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(cityName))
			qry.setParameter("cityName", "%"+cityName.toUpperCase()+"%");
		if (regionId!=null)
			qry.setParameter("regionId", regionId);
        List<ContactPointsEntity> lstRef = qry.getResultList();
        List<ContactPoints> lstRes = new ArrayList<ContactPoints>(lstRef.size());
        if (lstRef.size()>0){
          for (ContactPointsEntity ent : lstRef) {
            lstRes.add(new ContactPoints(ent));
            
          }
        }
        return lstRes;
	}

	@Override
	public ContactPoints getContactPoint(int id) {
		ContactPointsEntity cp=emMicro.find(ContactPointsEntity.class, id);
		if (cp!=null)
			return new ContactPoints(cp);
		else
			return null;
	}

	@Override
	public BankEntity getBank(String bik) {
		BankEntity bnk=emMicro.find(BankEntity.class, bik);
	    return bnk;
	}

    @Override
    public List<BankEntity> findBankByBik(String bikPattern, int limit) {
        return emMicro.createQuery("FROM BankEntity WHERE bik LIKE :bikPattern ESCAPE :escape")
                .setParameter("bikPattern", bikPattern.replace("%", "\\%").replace("_", "\\_") + "%")
                .setParameter("escape", "\\")
                .setMaxResults(limit).getResultList();
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCreditRequestStatus(List<CreditStatus> lst) {
		if (lst.size()>0 && lst!=null)
			for (CreditStatus st:lst){
				CreditStatusEntity rre=getCreditRequestStatus(st.getId());
				if (rre!=null){
				    rre.setName(st.getName());
				    rre.setId(st.getId());
				    emMicro.merge(rre);
				}
			}
		
	}
	
	 
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addCreditRequestStatus(String name) {
		CreditStatusEntity res=new CreditStatusEntity();
		res.setName(name);
		emMicro.merge(res);
				
	}
	

	@Override
	public List<Reference> getBlackListReasonTypes() {
	    return listReference(RefHeader.BLACKLIST_REASON_TYPE);
	}

	@Override
	public Reference getBlackListReasonType(int codeInteger) {
		return findByCodeInteger(RefHeader.BLACKLIST_REASON_TYPE, codeInteger);
	}

    @Override
    public List<Reference> getBlackListSourceTypes() {
        return listReference(RefHeader.SOURCE_BLACKLIST_TYPES);
    }

    @Override
    public List<Reference> getBlackListSourceType(int codeInteger) {
        return listReference(RefHeader.SOURCE_BLACKLIST_TYPES, codeInteger);
    }

    @Override
    public List<Reference> getDecisionWayTypes() {
        return listReference(RefHeader.EXECUTION_WAY);
    }

    @Override
    public Reference getDecisionWayType(int codeInteger) {
        return findByCodeInteger(RefHeader.EXECUTION_WAY, codeInteger);
    }

    @Override
    public Reference getBlackListOneSourceType(int codeInteger) {
        return findByCodeInteger(RefHeader.SOURCE_BLACKLIST_TYPES, codeInteger);
    }

    @Override
    public Reference getMessageType(int codeInteger) {
        return findByCodeInteger(RefHeader.MESSAGE_TYPE, codeInteger);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addContactPoint(RowUnp input) {
        if(input==null) return;
        ContactPointsEntity res = emMicro.find(ContactPointsEntity.class,input.getId());

        if(res == null){
            res=new ContactPointsEntity();
            res.setId(input.getId());
        }

        if(StringUtils.isNotEmpty(input.getNameRus())){
            res.setNameRus(input.getNameRus());
        }
        if(StringUtils.isNotEmpty(input.getName())){
            res.setNameRus(input.getName());
        }
        if(StringUtils.isNotEmpty(input.getAdr1())){
            res.setAddress1(input.getAdr1());
        }
        if(StringUtils.isNotEmpty(input.getAdr2())){
            res.setAddress2(input.getAdr2());
        }
        if(StringUtils.isNotEmpty(input.getAdr3())){
            res.setAddress3(input.getAdr3());
        }
        if(StringUtils.isNotEmpty(input.getAdr4())){
            res.setAddress4(input.getAdr4());
        }
        if(StringUtils.isNotEmpty(input.getAdrLat())){
            res.setAddrLat(input.getAdrLat());
        }
        if(input.getAttrGRPS() != null){
            res.setAttrGrps(input.getAttrGRPS());
        }
        if(input.getBic() != null){
            res.setBic(input.getBic());
        }
        if(input.getCanChange() != null){
            res.setCanChange(input.getCanChange());
        }
        if(input.getCanRevoke() != null){
            res.setCanRevoke(input.getCanRevoke());
        }
        if(input.getCityHead() != null){
            res.setCityHead(input.getCityHead());
        }
        if(input.getCityId() != null){
            res.setCityId(input.getCityId());
        }
        if(StringUtils.isNotEmpty(input.getCityLat())){
            res.setCityLat(input.getCityLat());
        }
        if(input.getContact() != null){
            res.setContact(input.getContact());
        }
        if(input.getCountry() != null){
            res.setCountry(Convertor.toInteger(input.getCountry()));   
        }
        if(input.getDeleted() != null){
            res.setDeleted(input.getDeleted());
        }
        if(input.getErased() != null){
            res.setErased(input.getErased());
        }

        if(input.getGetMoney() != null){
            res.setGetMoney(input.getGetMoney());
        }
        if(input.getLogoId() != null){
            res.setLogoId(input.getLogoId());
        }
        if(input.getMetro() != null){
            res.setMetro(input.getMetro());
        }
        if(input.getPhone() != null){
            res.setPhone(input.getPhone());
        }
        if(input.getPpCode() != null){
            res.setPpCode(input.getPpCode());
        }
        if(input.getRecCurr() != null){
            res.setRecCurr(input.getRecCurr());
        }
        if(input.getRegion() != null){
            res.setRegion(input.getRegion());
        }

        if(input.getSceneId() != null){
            res.setScenId(input.getSceneId());
        }
        if(input.getSendCurr() != null){
            res.setSendCurr(input.getSendCurr());
        }
        if(input.getUniqueTrn() != null){
            res.setUniqueTrn(input.getUniqueTrn());
        }
        if(input.getVersion() != null){
            res.setVersion(input.getVersion());
        }
        emMicro.merge(res);
    }


    /**
     * Вызывается при получении справочника из контакта
     * @param input
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addContactServ(RowUnp input) {
        if(input==null) return;
        ContactServiceEntity res = emMicro.find(ContactServiceEntity.class,input.getId());

        if(res == null){
            res=new ContactServiceEntity();
            res.setId(input.getId());
        }
        if(input.getVersion() != null){
            res.setVersion(input.getVersion());
        }
        if(input.getErased() != null){
            res.setErased(input.getErased());
        }
        if(input.getParentId() != null){
            res.setParentId(input.getParentId());
        }
        if(StringUtils.isNotEmpty(input.getCaption())){
            res.setCaption(input.getCaption());
        }
        if(StringUtils.isNotEmpty(input.getComment())){
            res.setComment(input.getComment());
        }
        if(StringUtils.isNotEmpty(input.getCaptionLa())){
            res.setCaptionLa(input.getCaptionLa());
        }
        if(StringUtils.isNotEmpty(input.getCommentLa())){
            res.setCommentLa(input.getCommentLa());
        }

        if(input.getCanIn() != null){
            res.setCanIn(input.getCanIn());
        }
        if(input.getCanPay() != null){
            res.setCanPay(input.getCanPay());
        }
        if(input.getCsIn() != null){
            res.setCsIn(input.getCsIn());
        }
        if(input.getCsInFee() != null){
            res.setCsInFee(input.getCsInFee());
        }
        if(input.getCsPay() != null){
            res.setCsPay(input.getCsPay());
        }
        emMicro.merge(res);
    }


    /**
     * Вызывается при получении справочника из контакта
     * @param input
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addContactBankServ(RowUnp input) {
        if(input==null) return;
        ContactPointsServiceEntity res = emMicro.find(ContactPointsServiceEntity.class,input.getId());
        if(res == null){
            res=new ContactPointsServiceEntity();
            res.setId(input.getId());
        }
        if(input.getVersion() != null){
            res.setVersion(input.getVersion());
        }
        if(input.getErased() != null){
            res.setErased(input.getErased());
        }
        if(input.getParentId() != null){
            res.setParentId(input.getParentId());
        }
        if(input.getBankId() != null){
            ContactPointsEntity cpe = emMicro.find(ContactPointsEntity.class,input.getBankId());
            res.setPointId(cpe);
        }
        if(input.getServId() != null){
            ContactServiceEntity cse = emMicro.find(ContactServiceEntity.class,input.getServId());
            res.setServiceId(cse);
        }

        emMicro.merge(res);
    }

	@Override
	public ContactServiceEntity getContactService(int id) {
		return emMicro.find(ContactServiceEntity.class,id);
	}

	@Override
	public List<ContactPoints> getContactPoints(int serviceId,String cityName,Integer regionId) {
		String hql="select c from ContactPointsEntity c, ContactPointsServiceEntity p  where c.id=p.pointId.id and p.serviceId.id=:serviceId";
		if (StringUtils.isNotEmpty(cityName))
	      	hql=hql+" and (upper(c.cityHead) like :cityName) ";
	    if (regionId!=null)
	       	hql=hql+" and c.region=:regionId ";
	    hql=hql+" order by c.address1";
		Query qry=emMicro.createQuery(hql);
		qry.setParameter("serviceId", serviceId);
		if (StringUtils.isNotEmpty(cityName))
			qry.setParameter("cityName", "%"+cityName.toUpperCase()+"%");
		if (regionId!=null)
			qry.setParameter("regionId", regionId);
		List<ContactPointsEntity> lstRef=qry.getResultList();
		List<ContactPoints> lstRes = new ArrayList<ContactPoints>(lstRef.size());
        if (lstRef.size()>0){
          for (ContactPointsEntity ent : lstRef) {
            lstRes.add(new ContactPoints(ent));
            
          }
          return lstRes;
        } else {
          return new ArrayList<ContactPoints>(0);	
        }
       
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RefBlacklistEntity addRefBlacklist() {
		RefBlacklistEntity re=new RefBlacklistEntity();
		
		re.setDatabeg(new Date());
		re.setDataend(DateUtils.addYears(new Date(), 1));
	    re.setIsactive(ActiveStatus.ACTIVE);
	    emMicro.persist(re);
		return re;
	
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklist(String surname,String name,String midname,Date birthdate,
			String series,String number,String mobilephone,String email,String employerFullName,
    		String employerShortName,String regionName,String areaName,String cityName,String placeName,
    		String streetName,String house,String corpus,String building,String flat){
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		//сначала проверяем фио и др
		
		if (StringUtils.isNotEmpty(surname)){
			hql=hql+" and (upper(surname) like :surname) ";
		}
		if (StringUtils.isNotEmpty(name)){
			hql=hql+" and (upper(name) like :name) ";
		}
		if (StringUtils.isNotEmpty(midname)){
			hql=hql+" and (upper(midname) like :midname) ";
		}
		if (birthdate!=null){
			hql+=" and date_part('day',birthdate)=:day and date_part('month',birthdate)=:month and date_part('year',birthdate)=:year ";
		
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(surname)){
			qry.setParameter("surname", "%"+surname.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(name)){
			qry.setParameter("name", "%"+name.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(midname)){
			qry.setParameter("midname", "%"+midname.toUpperCase()+"%");
		}
		if (birthdate!=null){
		
			qry.setParameter("day", DatesUtils.getDay(birthdate));
	        qry.setParameter("month", DatesUtils.getMonth(birthdate));
	        qry.setParameter("year", DatesUtils.getYear(birthdate));
		}
		//если нашлась запись, возвращаем результат
		if (qry.getResultList().size()>0){
			logger.info("Нашлась запись по ФИО+ДР "+surname+" "+name+" "+midname);
			return qry.getResultList();
		} else {
			//если нет, проверяем на паспорт
			hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
			if (StringUtils.isNotEmpty(series)){
			    hql=hql+" and (series=:series) ";
			}
			if (StringUtils.isNotEmpty(number)){
			    hql=hql+" and (number=:number) ";
			}
			qry=emMicro.createQuery(hql);
			if (StringUtils.isNotEmpty(series)){
				qry.setParameter("series", series);
			}
			
			if (StringUtils.isNotEmpty(number)){
				qry.setParameter("number", number);
			}
			//если нашлась запись возвращаем результат
			if (qry.getResultList().size()>0){
				logger.info("Нашлась запись по паспорту "+series+" "+number);
				return qry.getResultList();
			} else {
				//проверяем на мобильный телефон
				hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
				if (StringUtils.isNotEmpty(mobilephone)){
					hql=hql+" and (mobilePhone=:mobilephone) ";
				}
				qry=emMicro.createQuery(hql);
				if (StringUtils.isNotEmpty(mobilephone)){
					qry.setParameter("mobilephone", mobilephone);
				}
				if (qry.getResultList().size()>0){
					logger.info("Нашлась запись по телефону "+mobilephone);
					return qry.getResultList();
				} else {
					//проверяем на адрес
					hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
					if (StringUtils.isNotEmpty(regionName)){
						hql=hql+" and (upper(regionName)=:regionName) ";
					}
					if (StringUtils.isNotEmpty(areaName)){
						hql=hql+" and (upper(areaName)=:areaName) ";
					}
					if (StringUtils.isNotEmpty(cityName)){
						hql=hql+" and (upper(cityName)=:cityName) ";
					}
					if (StringUtils.isNotEmpty(placeName)){
						hql=hql+" and (upper(placeName)=:placeName) ";
					}
					if (StringUtils.isNotEmpty(streetName)){
						hql=hql+" and (upper(streetName)=:streetName) ";
					}
					if (StringUtils.isNotEmpty(house)){
						hql=hql+" and (upper(house)=:house) ";
					}
					if (StringUtils.isNotEmpty(corpus)){
						hql=hql+" and (upper(corpus)=:corpus) ";
					}
					if (StringUtils.isNotEmpty(building)){
						hql=hql+" and (upper(building)=:building) ";
					}
					if (StringUtils.isNotEmpty(flat)){
						hql=hql+" and (upper(flat)=:flat) ";
					}
					qry=emMicro.createQuery(hql);
					if (StringUtils.isNotEmpty(regionName)){
						regionName=regionName.indexOf(" ")<0?regionName:regionName.substring(0, regionName.indexOf(" "));
						qry.setParameter("regionName", regionName.toUpperCase());
					}
					if (StringUtils.isNotEmpty(areaName)){
						qry.setParameter("areaName", areaName.toUpperCase());
					}
					if (StringUtils.isNotEmpty(cityName)){
						qry.setParameter("cityName", cityName.toUpperCase());
					}
					if (StringUtils.isNotEmpty(placeName)){
						qry.setParameter("placeName", placeName.toUpperCase());
					}
					if (StringUtils.isNotEmpty(streetName)){
						qry.setParameter("streetName", streetName.toUpperCase());
					}
					if (StringUtils.isNotEmpty(house)){
						qry.setParameter("house", house.toUpperCase());
					}
					if (StringUtils.isNotEmpty(corpus)){
						qry.setParameter("corpus", corpus.toUpperCase());
					}
					if (StringUtils.isNotEmpty(building)){
						qry.setParameter("building", building.toUpperCase());
					}
					if (StringUtils.isNotEmpty(flat)){
						qry.setParameter("flat", flat.toUpperCase());
					}
					if (qry.getResultList().size()>0||StringUtils.isEmpty(email)){
						return qry.getResultList();
					} else {
						//проверяем на email
						hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
						if (StringUtils.isNotEmpty(email)){
							hql=hql+" and (upper(email)=:email) ";
						}
						qry=emMicro.createQuery(hql);
						if (StringUtils.isNotEmpty(email)){
							qry.setParameter("email", email.toUpperCase());
						}
					//возвращаем, что есть
					return qry.getResultList();
					}//end check address
				}//end check email
			}//end check phone
		}//end check passport
	}
	 
	@Override
	public List<RefBlacklistEntity> listRefBlacklist(int nFirst, int nCount, SortCriteria[] sorting,String surname,
			String name, String midname, String maidenname, Date birthdate,
			Integer reason, String series, String number,Integer isactive,
			String mobilephone,String email,Integer sourceId,String employerFullName,
    		String employerShortName,String regionName,String areaName,String cityName,String placeName,
    		String streetName,String house,String corpus,String building,String flat) {
		String hql="select rb [$SELECT_SORTING$] from ru.simplgroupp.persistence.RefBlacklistEntity as rb where (1=1) ";
		
		if (StringUtils.isNotEmpty(surname)){
			hql=hql+" and (upper(surname) like :surname) ";
		}
		if (StringUtils.isNotEmpty(name)){
			hql=hql+" and (upper(name) like :name) ";
		}
		if (StringUtils.isNotEmpty(midname)){
			hql=hql+" and (upper(midname) like :midname) ";
		}
		if (StringUtils.isNotEmpty(maidenname)){
			hql=hql+" and upper(maidenname) like :maidenname ";
		}
		if (StringUtils.isNotEmpty(series)){
		    hql=hql+" and (series=:series) ";
		}
		if (StringUtils.isNotEmpty(number)){
		    hql=hql+" and (number=:number) ";
		}
		if (StringUtils.isNotEmpty(mobilephone)){
			hql=hql+" and (mobilePhone=:mobilephone) ";
		}
		if (birthdate!=null){
			hql=hql+" and (birthdate=:birthdate) ";
		}
		if (reason!=null&&reason>0){
			hql=hql+" and (reasonId.id=:reason) ";
		}
		if (isactive!=null){
			hql=hql+" and (isactive=:isactive) ";
		}
		if (sourceId!=null&&sourceId>0){
			hql=hql+" and (sourceId.id=:source) ";
		}
		if (StringUtils.isNotEmpty(email)){
		    hql=hql+" and (upper(email)=:email) ";
		}
		if (StringUtils.isNotEmpty(employerFullName)){
		    hql=hql+" and (upper(employerFullName)=:employerFullName) ";
		}
		if (StringUtils.isNotEmpty(employerShortName)){
		    hql=hql+" and (upper(employerShortName)=:employerShortName) ";
		}
		if (StringUtils.isNotEmpty(regionName)){
		    hql=hql+" and (upper(regionName)=:regionName) ";
		}
		if (StringUtils.isNotEmpty(areaName)){
		    hql=hql+" and (upper(areaName)=:areaName) ";
		}
		if (StringUtils.isNotEmpty(cityName)){
		    hql=hql+" and (upper(cityName)=:cityName) ";
		}
		if (StringUtils.isNotEmpty(placeName)){
		    hql=hql+" and (upper(placeName)=:placeName) ";
		}
		if (StringUtils.isNotEmpty(streetName)){
		    hql=hql+" and (upper(streetName)=:streetName) ";
		}

        hql = hql + SearchUtil.sortToString(blackListSortMapping, sorting);
        String sortSelect = SearchUtil.sortSelectToString(blackListSortMapping, sorting);
        hql = hql.replace("[$SELECT_SORTING$]", sortSelect);

        Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(surname)){
			qry.setParameter("surname", "%"+surname.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(name)){
			qry.setParameter("name", "%"+name.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(midname)){
			qry.setParameter("midname", "%"+midname.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(maidenname)){
			qry.setParameter("maidenname", "%"+maidenname.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(series)){
			qry.setParameter("series", series);
		}
		if (StringUtils.isNotEmpty(mobilephone)){
			qry.setParameter("mobilephone", mobilephone);
		}
		if (StringUtils.isNotEmpty(number)){
			qry.setParameter("number", number);
		}
		if (birthdate!=null){
			qry.setParameter("birthdate", birthdate);
		}
		if (reason!=null&&reason>0){
			qry.setParameter("reason", reason);
		}
		if (isactive!=null){
			qry.setParameter("isactive", isactive);
		}
		if (sourceId!=null&&sourceId>0){
			qry.setParameter("source", sourceId);
		}
		if (StringUtils.isNotEmpty(email)){
			qry.setParameter("email", email.toUpperCase());
		}
		if (StringUtils.isNotEmpty(employerFullName)){
			qry.setParameter("employerFullName", employerFullName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(employerShortName)){
			qry.setParameter("employerShortName", employerShortName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(regionName)){
			qry.setParameter("regionName", regionName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(areaName)){
			qry.setParameter("areaName", areaName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(cityName)){
			qry.setParameter("cityName", cityName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(placeName)){
			qry.setParameter("placeName", placeName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(streetName)){
			qry.setParameter("streetName", streetName.toUpperCase());
		}
		if (nFirst >= 0){
			qry.setFirstResult(nFirst);
		}
		if (nCount > 0){
			qry.setMaxResults(nCount);
		}

        List<RefBlacklistEntity> result;
        if (sorting == null || sorting.length == 0) {
            result = qry.getResultList();
        } else {
            List<Object[]> lstSource = qry.getResultList();
            result = new ArrayList<RefBlacklistEntity>(lstSource.size());
            SearchUtil.extractColumn(lstSource, 0, result);
        }
        return result;

    }

	@Override
	public int countRefBlacklist(String surname, String name, String midname,
			String maidenname, Date birthdate, Integer reason, String series,
			String number,Integer isactive,String mobilephone,String email,Integer sourceId,
			String employerFullName,String employerShortName,String regionName,String areaName,
			String cityName,String placeName,
    		String streetName,String house,String corpus,String building,String flat) {
		List<RefBlacklistEntity> re=listRefBlacklist(0,0,null,surname,name,midname,maidenname,birthdate,reason,
				series,number,isactive,mobilephone,email,sourceId,employerFullName,
	    		employerShortName,regionName,areaName,cityName,placeName,
	    		streetName,house,corpus,building,flat);
		return re.size();
		
	}

	@Override
	public RefBlacklistEntity getRefBlacklist(int id, Set options) {
		RefBlacklistEntity re=getRefBlacklistEntity(id);
		if (re!=null){
			re.init(options);
			return re;
		}
		return null;
	}
	
	@Override
	public RefBlacklistEntity getRefBlacklistEntity(int id){
		return emMicro.find(RefBlacklistEntity.class, id);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeRefBlacklist(int id) {
		RefBlacklistEntity ent=getRefBlacklistEntity(id);
		if (ent != null) {
	        String sql = "delete from ref_blacklist where id=:id";
	        Query qry = emMicro.createNativeQuery(sql);
	        qry.setParameter("id", id);
	        qry.executeUpdate();
	     
    	}
    		
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveBulkRefBlacklist(List<RefBlacklistEntity> entities) {
        for (RefBlacklistEntity entity : entities) {
            saveRefBlacklist(entity);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveRefBlacklist(RefBlacklist blacklist) {
        RefBlacklistEntity entity = blacklist.getEntity();


        if (blacklist.getReason() != null){
            entity.setReasonId(findByCodeIntegerEntity(RefHeader.BLACKLIST_REASON_TYPE, blacklist.getReason().getCodeInteger()));
        } else {
            entity.setReasonId(null);
        }

        if (blacklist.getSource() != null){
            entity.setSourceId(findByCodeIntegerEntity(RefHeader.SOURCE_BLACKLIST_TYPES, blacklist.getSource().getCodeInteger()));
        } else {
            entity.setSourceId(null);
        }

        saveRefBlacklist(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveRefBlacklist(RefBlacklistEntity entity) {
        entity = emMicro.merge(entity);
        emMicro.persist(entity);
    }

    @Override
    public Boolean findRefBlacklistByBlock(RefBlacklistEntity entity) {
        String hql = "select count(id) from ru.simplgroupp.persistence.RefBlacklistEntity where (1=1) AND ( ";

        boolean or = false;
        if (StringUtils.isNotEmpty(entity.getSurname()) && StringUtils.isNotEmpty(entity.getName()) && entity.getBirthdate() != null) { // Блок ФИО + ДР
            hql += " (lower(surname)=:surname and lower(name)=:name and birthdate=:birthdate) ";
            or = true;
        }
        if (StringUtils.isNotEmpty(entity.getSeries()) && StringUtils.isNotEmpty(entity.getNumber())) { // Блок серия и номер паспорта
            if (or) {
                hql += " OR ";
            }
            or = true;
            hql += " (series=:series and number=:number) ";
        }
        if (StringUtils.isNotEmpty(entity.getMobilePhone())) { // Блок телефон
            if (or) {
                hql += " OR ";
            }
            or = true;
            hql += " (mobilePhone=:mobilephone) ";
        }
        if (StringUtils.isNotEmpty(entity.getEmail())) { // Блок email
            if (or) {
                hql += " OR ";
            }
            or = true;
            hql += " (email=:email) ";
        }
        if (StringUtils.isNotEmpty(entity.getEmployerFullName()) || StringUtils.isNotEmpty(entity.getEmployerShortName())) { // Блок работодатель
            if (or) {
                hql += " OR ";
            }
            or = true;
            hql += "(";
            boolean and = false;
            if (StringUtils.isNotEmpty(entity.getEmployerFullName())) {
                hql += " employerFullName=:employerFullName ";
                and = true;
            }
            if (StringUtils.isNotEmpty(entity.getEmployerShortName())) {
                if (and) hql += " AND ";
                hql += " employerShortName=:employerShortName ";
            }
            hql += ")";
        }
        if (StringUtils.isNotEmpty(entity.getRegionName()) && StringUtils.isNotEmpty(entity.getHouse())) { // Блок адрес
            if (or) {
                hql += " OR ";
            }
            or = true;
            hql += "(";
            hql += " regionName=:regionName and house=:house ";

            boolean subAnd = false;
            if (StringUtils.isNotEmpty(entity.getAreaName())) {
                subAnd = true;
                hql += " AND ";
                hql += " areaName=:areaName ";
            }
            if (StringUtils.isNotEmpty(entity.getCityName())) {
                if (subAnd) {
                    hql += " AND ";
                }
                subAnd = true;
                hql += " cityName=:cityName ";
            }
            if (StringUtils.isNotEmpty(entity.getPlaceName())) {
                if (subAnd) {
                    hql += " AND ";
                }
                subAnd = true;
                hql += " placeName=:placeName ";
            }
            if (StringUtils.isNotEmpty(entity.getStreetName())) {
                if (subAnd) {
                    hql += " AND ";
                }
                subAnd = true;
                hql += " streetName=:streetName ";
            }
            if (StringUtils.isNotEmpty(entity.getCorpus())) {
                if (subAnd) {
                    hql += " AND ";
                }
                subAnd = true;
                hql += " corpus=:corpus ";
            }
            if (StringUtils.isNotEmpty(entity.getBuilding())) {
                if (subAnd) {
                    hql += " AND ";
                }
                subAnd = true;
                hql += " building=:building ";
            }
            if (StringUtils.isNotEmpty(entity.getFlat())) {
                if (subAnd) {
                    hql += " AND ";
                }
                subAnd = true;
                hql += " flat=:flat ";
            }

            hql += ")";
        }
        hql += ")";

        Query qry = emMicro.createQuery(hql);
        if (StringUtils.isNotEmpty(entity.getSurname()) && StringUtils.isNotEmpty(entity.getName()) && entity.getBirthdate() != null) { // Блок ФИО + ДР
            qry.setParameter("surname", entity.getSurname().toLowerCase());
            qry.setParameter("name", entity.getName().toLowerCase());
            qry.setParameter("birthdate", entity.getBirthdate(), TemporalType.DATE);
        }
        if (StringUtils.isNotEmpty(entity.getSeries()) && StringUtils.isNotEmpty(entity.getNumber())) { // Блок серия и номер паспорта
            qry.setParameter("series", entity.getSeries());
            qry.setParameter("number", entity.getNumber());
        }
        if (StringUtils.isNotEmpty(entity.getMobilePhone())) { // Блок телефон
            qry.setParameter("mobilephone", entity.getMobilePhone());
        }
        if (StringUtils.isNotEmpty(entity.getEmail())) { // Блок email
            qry.setParameter("email", entity.getEmail());
        }
        if (StringUtils.isNotEmpty(entity.getEmployerFullName()) || StringUtils.isNotEmpty(entity.getEmployerShortName())) { // Блок работодатель
            if (StringUtils.isNotEmpty(entity.getEmployerFullName())) {
                qry.setParameter("employerFullName", entity.getEmployerFullName());
            }
            if (StringUtils.isNotEmpty(entity.getEmployerShortName())) {
                qry.setParameter("employerShortName", entity.getEmployerShortName());
            }
        }


        if (StringUtils.isNotEmpty(entity.getRegionName()) && StringUtils.isNotEmpty(entity.getHouse())) { // Блок адрес
            qry.setParameter("regionName", entity.getRegionName());
            qry.setParameter("house", entity.getHouse());
            if (StringUtils.isNotEmpty(entity.getAreaName())) {
                qry.setParameter("areaName", entity.getAreaName());
            }
            if (StringUtils.isNotEmpty(entity.getCityName())) {
                qry.setParameter("cityName", entity.getCityName());
            }
            if (StringUtils.isNotEmpty(entity.getPlaceName())) {
                qry.setParameter("placeName", entity.getPlaceName());
            }
            if (StringUtils.isNotEmpty(entity.getStreetName())) {
                qry.setParameter("streetName", entity.getStreetName());
            }
            if (StringUtils.isNotEmpty(entity.getCorpus())) {
                qry.setParameter("corpus", entity.getCorpus());
            }
            if (StringUtils.isNotEmpty(entity.getBuilding())) {
                qry.setParameter("building", entity.getBuilding());
            }
            if (StringUtils.isNotEmpty(entity.getFlat())) {
                qry.setParameter("flat", entity.getFlat());
            }
        }
        return ((Long) qry.getSingleResult()) > 0;
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveRefBlacklist(Integer id,String surname, String name, String midname,
			String maidenname,Date birthdate, Date databeg, Date dataend, Integer reason,
			String series, String number,Integer isactive,String mobilephone,
			String email,Integer source,String comment,String employerFullName,
    		String employerShortName,String regionName,String areaName,String cityName,String placeName,
    		String streetName,String house,String corpus,String building,String flat) {
		
		RefBlacklistEntity re=null;
		if (id!=null){
			re=getRefBlacklistEntity(id);
		    if (re==null){
			    return;
		    }
		} else {
			re=new RefBlacklistEntity();
		}
		re.setSurname(surname);
		re.setName(name);
		re.setMaidenname(maidenname);
		re.setMidname(midname);
		re.setBirthdate(birthdate);
		re.setDatabeg(databeg);
		re.setDataend(dataend);
		re.setSeries(series);
		re.setNumber(number);
		re.setIsactive(isactive);
		if (reason!=null){
			re.setReasonId(findByCodeIntegerEntity(RefHeader.BLACKLIST_REASON_TYPE,reason));
		}
		if (source!=null){
			re.setSourceId(findByCodeIntegerEntity(RefHeader.SOURCE_BLACKLIST_TYPES,source));
		}
		re.setSeries(series);
		re.setNumber(number);
		re.setMobilePhone(mobilephone);
		re.setEmail(email);
		re.setComment(comment);
		re.setEmployerFullName(employerFullName);
		re.setEmployerShortName(employerShortName);
		re.setRegionName(regionName);
		re.setAreaName(areaName);
		re.setCityName(cityName);
		re.setPlaceName(placeName);
		re.setStreetName(streetName);
		re.setHouse(house);
		re.setCorpus(corpus);
		re.setBuilding(building);
		re.setFlat(flat);
        saveRefBlacklist(re);
	}

	
	
	@Override
	public List<Reference> getOperations() {
		return listReference(RefHeader.PEOPLE_SUM_OPERATION);
	}

	@Override
	public Reference getOperation(int codeInteger) {
		return findByCodeInteger(RefHeader.PEOPLE_SUM_OPERATION, codeInteger);
	}

	@Override
	public List<Reference> getOperationTypes() {
		return listReference(RefHeader.PEOPLE_SUM_OPERATION_TYPE);
	}

	@Override
	public Reference getOperationType(int codeInteger) {
		return findByCodeInteger(RefHeader.PEOPLE_SUM_OPERATION_TYPE, codeInteger);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePartner(Partner partner) {
		PartnersEntity partners=getPartnerEntity(partner.getId());
		if (partners==null){
			return;
		}
		partners.setApplicationId(partner.getApplicationId());
		partners.setCanMakeCreditHistory(partner.getCanMakeCreditHistory());
		partners.setCanMakePayment(partner.getCanMakePayment());
		partners.setCanMakeScoring(partner.getCanMakeScoring());
		partners.setCodetest(partner.getCodeTest());
		partners.setCodework(partner.getCodeWork());
		partners.setGroupTest(partner.getGroupTest());
		partners.setGroupWork(partner.getGroupWork());
		partners.setLogintest(partner.getLoginTest());
		partners.setLoginwork(partner.getLoginWork());
		partners.setName(partner.getName());
		partners.setPasswordtest(partner.getPasswordTest());
		partners.setPasswordwork(partner.getPasswordWork());
		partners.setPasswordUploadTest(partner.getPasswordUploadTest());
		partners.setPasswordUploadWork(partner.getPasswordUploadWork());
		partners.setDateVersion(partner.getDateVersion());
		partners.setRealname(partner.getRealName());
		partners.setRequestVersion(partner.getRequestVersion());
		partners.setUploadinbox(partner.getUploadInbox());
		partners.setUploadoutbox(partner.getUploadOutbox());
		partners.setUploadVersion(partner.getUploadVersion());
		partners.setUrltest(partner.getUrlTest());
		partners.setUrluploadtest(partner.getUrlUploadTest());
		partners.setUrluploadwork(partner.getUrlUploadWork());
		partners.setUrlwork(partner.getUrlWork());
		partners=emMicro.merge(partners);
		emMicro.persist(partners);
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PartnersEntity addPartner(String name, String realName) {
		PartnersEntity partner=new PartnersEntity();
		partner.setName(name);
		partner.setRealname(realName);
		partner=emMicro.merge(partner);
		emMicro.persist(partner);
		return partner;
	}

	@Override
	public Integer getValidationMarkFromVerify(Integer verifyPersonal,
			Integer verifyDocument) {
		if (verifyPersonal==null&&verifyDocument==null){
			return VerificationMark.NO_DATA;
		}
		if (verifyPersonal==1&&verifyDocument!=1){
			return VerificationMark.VERIFY_PERSONALDATA;
		}
		if (verifyPersonal!=1&&verifyDocument==1){
			return VerificationMark.VERIFY_DOCUMENT;
		}
		if (verifyPersonal==1&&verifyDocument==1){
			return VerificationMark.VERIFY_PERSONALDATA_DOCUMENT;
		}
		return null;
	}

	@Override
	public VerificationMarkEntity getValidationMark(Integer id) {
		if (id==null){ 
		  return null;
		}
		VerificationMarkEntity valMark=emMicro.find(VerificationMarkEntity.class, id);
		return valMark;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removePartner(Integer id) {
		PartnersEntity ent=getPartnerEntity(id);
		if (ent != null) {
	        String sql = "delete from partners where id=:id";
	        Query qry = emMicro.createNativeQuery(sql);
	        qry.setParameter("id", id);
	        qry.executeUpdate();
	     
    	}
	}

	@Override
	public RegionsEntity findRegionByName(String name) {
	    return (RegionsEntity) JPAUtils.getSingleResultFromSql(emMicro, "from ru.simplgroupp.persistence.RegionsEntity where upper(name) like :name", 
	    		Utils.mapOf("name", "%"+name.toUpperCase().trim()+"%"));
	}

	
	@Override
	public List<Reference> getCreditDetailsOperations() {
		return listReference(RefHeader.CREDIT_DETAIL_OPERATIONS);
	}

	@Override
	public Reference getCreditDetailsOperation(int codeInteger) {
		return findByCodeInteger(RefHeader.CREDIT_DETAIL_OPERATIONS, codeInteger);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addRegionNew(String code, String name, String codeReg,String codeIso) {
		RegionsEntity regions=findRegion(code);
		//если такого региона нет, запишем
		if (regions==null){
			RegionsEntity regionsNew=new RegionsEntity(code);
			regionsNew.setName(name);
			regionsNew.setCodereg(codeReg);
			regionsNew.setCodeIso(codeIso);
			emMicro.persist(regionsNew);
		} else {
			logger.severe("Регион "+code+" уже существует в базе");
		}
		
	}

		
	@Override
	public RegionsEntity findRegion(String code) {
		return emMicro.find(RegionsEntity.class, code);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveRegionsNew(List<FMSRegion> lst) {
		if (lst.size()>0 && lst!=null)
			for (FMSRegion rf:lst)	{
				RegionsEntity region=findRegion(rf.getCode());
				region.setName(rf.getName());
				region.setCodereg(rf.getCodereg());
				region.setCodeIso(rf.getCodeIso());
				emMicro.merge(region);
			}
		
	}

	@Override
	public List<Reference> getBehaviorGATypes() {
		return listReference(RefHeader.PEOPLE_BEHAVIOR_GA_TYPE);
	}

	@Override
	public Reference getBehaviorGAType(String code) {
		return findByCode(RefHeader.PEOPLE_BEHAVIOR_GA_TYPE, code);
	}
	
	@Override
	public List<RefBonus> getBonusTypeList() {
        Query qry = emMicro.createNamedQuery("bonusList");

        List<RefBonusEntity> lstCnt = qry.getResultList();
        List<RefBonus> lstRes = new ArrayList<RefBonus>(lstCnt.size());
        for (RefBonusEntity ent : lstCnt) {
            lstRes.add(new RefBonus(ent));
        }
        return lstRes;
		
	}

	@Override
	public void updateStatusBonusType(RefBonus ent) {
		if (ent!=null){
			String sql = "update ref_bonus set amount = :amount, isactive = :isactive where id=:id";
			Query qry = emMicro.createNativeQuery(sql);
			qry.setParameter("id", ent.getId());
			qry.setParameter("amount", ent.getAmount());
			qry.setParameter("isactive", Utils.booleanToTriState(ent.getIsactive()));
			qry.executeUpdate();
		}
	}
	 
	@Override
	public RefBonusEntity getBonusType(int id) {
	   return emMicro.find(RefBonusEntity.class, new Integer(id));
	}
	
	@Override
	public RefBonus getBonusByCodeInteger(Integer code) {
		  String sql = "from ru.simplgroupp.persistence.RefBonusEntity where codeinteger = :code and isactive=:isactive";
	      List<RefBonusEntity> lst = JPAUtils.getResultListFromSql(emMicro, sql, Utils.mapOf("code", code,
	    		  "isactive",ActiveStatus.ACTIVE));
	      if (lst.size() == 0) {
	            return null;
	      } else {
	            return new RefBonus(lst.get(0));
	      }
		
	}

	@Override
	public Integer referenceIdOrNull(Reference reference) {
		return reference!=null?reference.getId():null;
	}

	@Override
	public Integer referenceCodeIntegerOrNull(Reference reference) {
		return reference!=null?reference.getCodeInteger():null;
	}

	@Override
	public String referenceCodeOrNull(Reference reference) {
		return reference!=null?reference.getCode():null;
	}

	@Override
	public List<Reference> getCreditPurposeTypes() {
		return listReference(RefHeader.CREDIT_PURPOSE);
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklistByPersonalData(
			String surname, String name, String midname, Date birthdate) {
		if (StringUtils.isEmpty(surname)&&StringUtils.isEmpty(name)&&StringUtils.isEmpty(midname)
				&&birthdate==null){
			logger.warning("Нет ФИО и ДР для поиска в ЧС");
			return new ArrayList<>(0);
		}
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		//проверяем фио и др
		
		if (StringUtils.isNotEmpty(surname)){
			hql=hql+" and (upper(surname) like :surname) ";
		}
		if (StringUtils.isNotEmpty(name)){
			hql=hql+" and (upper(name) like :name) ";
		}
		if (StringUtils.isNotEmpty(midname)){
			hql=hql+" and (upper(midname) like :midname) ";
		}
		if (birthdate!=null){
			hql+=" and date_part('day',birthdate)=:day and date_part('month',birthdate)=:month and date_part('year',birthdate)=:year ";
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(surname)){
			qry.setParameter("surname", "%"+surname.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(name)){
			qry.setParameter("name", "%"+name.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(midname)){
			qry.setParameter("midname", "%"+midname.toUpperCase()+"%");
		}
		if (birthdate!=null){
		
			qry.setParameter("day", DatesUtils.getDay(birthdate));
	        qry.setParameter("month", DatesUtils.getMonth(birthdate));
	        qry.setParameter("year", DatesUtils.getYear(birthdate));
		}
		return qry.getResultList();
		
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklistByPassport(String series,
			String number) {
		if (StringUtils.isEmpty(series)&&StringUtils.isEmpty(number)){
			logger.warning("Нет паспорта для поиска в ЧС");
			return new ArrayList<>(0);
		}
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		if (StringUtils.isNotEmpty(series)){
		    hql=hql+" and (series=:series) ";
		}
		if (StringUtils.isNotEmpty(number)){
		    hql=hql+" and (number=:number) ";
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(series)){
			qry.setParameter("series", series);
		}
		
		if (StringUtils.isNotEmpty(number)){
			qry.setParameter("number", number);
		}
		return qry.getResultList();
		
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklistByPhone(String phone) {
		if (StringUtils.isEmpty(phone)){
			logger.warning("Нет телефона для поиска в ЧС");
			return new ArrayList<>(0);
		}
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		if (StringUtils.isNotEmpty(phone)){
			hql=hql+" and (mobilePhone=:mobilephone) ";
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(phone)){
			qry.setParameter("mobilephone", phone);
		}
		return qry.getResultList();
		
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklistByEmail(String email) {
		if (StringUtils.isEmpty(email)){
			logger.warning("Нет email для поиска в ЧС");
			return new ArrayList<>(0);
		}
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		if (StringUtils.isNotEmpty(email)){
			hql=hql+" and (email=:email) ";
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(email)){
			qry.setParameter("email", email);
		}
		return qry.getResultList();
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklistByEmployer(
			String employerFullName, String employerShortName) {
		if (StringUtils.isEmpty(employerFullName)&&StringUtils.isEmpty(employerShortName)){
			logger.warning("Нет работодателя для поиска в ЧС");
			return new ArrayList<>(0);
		}
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		if (StringUtils.isNotEmpty(employerFullName)){
		    hql=hql+" and (employerFullName=:employerFullName) ";
		}
		if (StringUtils.isNotEmpty(employerShortName)){
		    hql=hql+" and (employerShortName=:employerShortName) ";
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(employerFullName)){
			qry.setParameter("employerFullName", employerFullName);
		}
		
		if (StringUtils.isNotEmpty(employerShortName)){
			qry.setParameter("employerShortName", employerShortName);
		}
		return qry.getResultList();
	}

	@Override
	public List<RefBlacklistEntity> findInRefBlacklistByAddress(
			String regionName, String areaName, String cityName,
			String placeName, String streetName, String house, String corpus,
			String building, String flat) {
		if (StringUtils.isEmpty(regionName)&&StringUtils.isEmpty(areaName)&&StringUtils.isEmpty(cityName)
				&&StringUtils.isEmpty(placeName)&&StringUtils.isEmpty(streetName)&&StringUtils.isEmpty(house)
				&&StringUtils.isEmpty(corpus)&&StringUtils.isEmpty(building)&&StringUtils.isEmpty(flat)){
			logger.warning("Нет адреса для поиска в ЧС");
			return new ArrayList<>(0);
		}
		
		String hql="from ru.simplgroupp.persistence.RefBlacklistEntity where (isactive=1) ";
		if (StringUtils.isNotEmpty(regionName)){
			hql=hql+" and (upper(regionName)=:regionName) ";
		}
		if (StringUtils.isNotEmpty(areaName)){
			hql=hql+" and (upper(areaName)=:areaName) ";
		}
		if (StringUtils.isNotEmpty(cityName)){
			hql=hql+" and (upper(cityName)=:cityName) ";
		}
		if (StringUtils.isNotEmpty(placeName)){
			hql=hql+" and (upper(placeName)=:placeName) ";
		}
		if (StringUtils.isNotEmpty(streetName)){
			hql=hql+" and (upper(streetName)=:streetName) ";
		}
		if (StringUtils.isNotEmpty(house)){
			hql=hql+" and (upper(house)=:house) ";
		}
		if (StringUtils.isNotEmpty(corpus)){
			hql=hql+" and (upper(corpus)=:corpus) ";
		}
		if (StringUtils.isNotEmpty(building)){
			hql=hql+" and (upper(building)=:building) ";
		}
		if (StringUtils.isNotEmpty(flat)){
			hql=hql+" and (upper(flat)=:flat) ";
		}
		Query qry=emMicro.createQuery(hql);
		if (StringUtils.isNotEmpty(regionName)){
			regionName=regionName.indexOf(" ")<0?regionName:regionName.substring(0, regionName.indexOf(" "));
			qry.setParameter("regionName", regionName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(areaName)){
			qry.setParameter("areaName", areaName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(cityName)){
			qry.setParameter("cityName", cityName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(placeName)){
			qry.setParameter("placeName", placeName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(streetName)){
			qry.setParameter("streetName", streetName.toUpperCase());
		}
		if (StringUtils.isNotEmpty(house)){
			qry.setParameter("house", house.toUpperCase());
		}
		if (StringUtils.isNotEmpty(corpus)){
			qry.setParameter("corpus", corpus.toUpperCase());
		}
		if (StringUtils.isNotEmpty(building)){
			qry.setParameter("building", building.toUpperCase());
		}
		if (StringUtils.isNotEmpty(flat)){
			qry.setParameter("flat", flat.toUpperCase());
		}
		return qry.getResultList();
	}
	
    @Override
    public List<Reference> getTimeRanges() {
        return listReference(RefHeader.TIME_RANGE);
    }

    @Override
    public Reference getTimeRange(int codeInteger) {
        return findByCodeInteger(RefHeader.TIME_RANGE, codeInteger);
    }

    @Override
    public List<Reference> getHunterObjects() {
        return listReference(RefHeader.HUNTER_OBJECTS);
    }

    @Override
    public Reference getHunterObject(String code) {
        return findByCode(RefHeader.HUNTER_OBJECTS, code);
    }

    @Override
    public List<Reference> getAntifraudStatuses() {
        return listReference(RefHeader.ANTIFRAUD_STATUSES);
    }

    @Override
    public Reference getAntifraudStatus(int codeInteger) {
        return findByCodeInteger(RefHeader.ANTIFRAUD_STATUSES, codeInteger);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeReferenceStatus(Integer refHeaderID, Integer codeInteger, Integer status) {
        ReferenceEntity ref = findByCodeIntegerEntity(refHeaderID, codeInteger);
        if (ref != null) {
            ref.setIsactive(status);
            ref = emMicro.merge(ref);
            emMicro.persist(ref);
        }
    }

    @Override
    public List<Reference> getVerifierQuestionTypes() {
        return listReference(RefHeader.VERIFIER_QUESTION_TYPES);
    }

    @Override
    public Reference getVerifierQuestionType(int codeInteger) {
        return findByCodeInteger(RefHeader.VERIFIER_QUESTION_TYPES, codeInteger);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteBlackListBySourceCode(Integer sourceCode) {
        ReferenceEntity ref = findByCodeIntegerEntity(RefHeader.SOURCE_BLACKLIST_TYPES, sourceCode);
        Query qry = emMicro.createNamedQuery("deleteBlackListBySourceCode");
        qry.setParameter("sourceID", ref.getId());
        qry.executeUpdate();
    }
}
