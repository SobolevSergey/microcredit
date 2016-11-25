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

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.RulesDAO;
import ru.simplgroupp.persistence.AIConstantEntity;
import ru.simplgroupp.persistence.AIRuleEntity;
import ru.simplgroupp.transfer.AIRule;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RulesDAOImpl implements RulesDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;
    
	@Override
	public AIRuleEntity getAIRule(int id, Set options) {
		AIRuleEntity rule = getAIRule(id);
		if (rule == null) {
			return null;
		} else {
			rule.init(options);
			return rule;
		}
	}
	
	@Override
	public AIRuleEntity getAIRule(int id) {
		AIRuleEntity rule = emMicro.find(AIRuleEntity.class, new Integer(id));
		return rule;
	}	
	
	@Override
	public void deleteRule(int id) {
		AIRuleEntity rule = getAIRule(id);
		if (rule != null) {
			emMicro.remove(rule);
		}
	}    
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteConstant(Integer constantId) {
        AIConstantEntity cnst = getConstant(constantId);
        if (cnst != null) {
            emMicro.remove(cnst);
        }

    } 
    
    @Override
    public int countRules(String packageName, String description,Integer ruleType, String kbase) {
    	String hql = "select count(*) from ru.simplgroupp.persistence.AIRuleEntity where (1=1)";
    	if (packageName != null) {
    		hql = hql + " and (packageName like :packageName)";
    	}
    	if (description != null) {
    		hql = hql + " and (description like :description)";
    	}
    	if (ruleType != null) {
    		hql = hql + " and (ruleType = :ruleType)";
    	}
    	if (kbase != null) {
    		hql = hql + " and (kbase = :kbase)";
    	}    	
    	Query qry = emMicro.createQuery(hql);
    	if (packageName != null) {
    		qry.setParameter("packageName", "%"+packageName+"%");
    	}
    	if (description != null) {
    		qry.setParameter("description", "%"+description+"%");
    	}
    	if (ruleType != null) {
    		qry.setParameter("ruleType", ruleType);
    	}
    	if (kbase != null) {
    		qry.setParameter("kbase", kbase);
    	}    	
    	List lst = qry.getResultList();
    	if (lst == null || lst.size() == 0 || lst.get(0) == null) {
    		return 0;
    	} else {
    		return ((Number) lst.get(0)).intValue();
    	}
    }    
    
    @Override
    public List<AIRuleEntity> listRules(String packageName, String description,Integer ruleType, String kbase) {
    	String hql = "from ru.simplgroupp.persistence.AIRuleEntity where (1=1)";
    	if (packageName != null) {
    		hql = hql + " and (packageName like :packageName)";
    	}
    	if (description != null) {
    		hql = hql + " and (description like :description)";
    	}
    	if (ruleType != null) {
    		hql = hql + " and (ruleType = :ruleType)";
    	}
    	if (kbase != null) {
    		hql = hql + " and (kbase = :kbase)";
    	}     	
    	Query qry = emMicro.createQuery(hql);
    	if (packageName != null) {
    		qry.setParameter("packageName", "%"+packageName+"%");
    	}
    	if (description != null) {
        		qry.setParameter("description","%"+description+"%");	
    	}
    	if (ruleType != null) {
    		qry.setParameter("ruleType", ruleType);
    	}
    	if (kbase != null) {
    		qry.setParameter("kbase", kbase);
    	}      	
    	List<AIRuleEntity> lst = qry.getResultList();
    	return lst;
    	
    } 
    
    @Override
    public List<AIConstantEntity> listConstants(String packageName, String constNameTpl) {
    
        String hql=" from ru.simplgroupp.persistence.AIConstantEntity where (aiRule.packageName = :packageName)  ";
        if (StringUtils.isNotEmpty(constNameTpl)){
        	hql+=" and (upper(name) like :constNameTpl) ";
        }
        hql+=" order by name";
        Query qry=emMicro.createQuery(hql);
    	qry.setParameter("packageName", packageName);
        if (StringUtils.isNotEmpty(constNameTpl)) {
            qry.setParameter("constNameTpl", "%" + constNameTpl.toUpperCase() + "%");
        } 
        return qry.getResultList();
    }    
    
    @Override
    public AIRuleEntity findRuleByName(String packageName) {
    	Query qry = emMicro.createNamedQuery("findRuleByName");
    	qry.setParameter("packageName", packageName);
        List<AIRuleEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
            return lst.get(0);
        }
    } 
    
    @Override
    public AIRule findRuleByName(String packageName, Set options) {
    	Query qry = emMicro.createNamedQuery("findRuleByName");
    	qry.setParameter("packageName", packageName);
        List<AIRuleEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
        	AIRule rule = new AIRule(lst.get(0));
        	rule.getEntity().init(options);
            return rule;
        }
    }    
    
    @Override
    public void deleteRuleConstants(int ruleid) {
        Query qryDel = emMicro.createNamedQuery("deleteRuleConstant");
        qryDel.setParameter("ruleid", ruleid);
        qryDel.executeUpdate();    	
    }
    
    @Override
    public AIConstantEntity findConstant(String packageName, String constantName) {
        Query qry = emMicro.createNamedQuery("findConstant");
        qry.setParameter("packageName", packageName);
        qry.setParameter("constantName", constantName);
        List<AIConstantEntity> list = qry.getResultList();
        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }   
    
    @Override
    public AIConstantEntity getConstant(int id) {
    	return emMicro.find(AIConstantEntity.class, id);
    }
    
    @Override
    public AIConstantEntity saveConstant(AIConstantEntity acon) {
    	AIConstantEntity acon1 = emMicro.merge(acon);
    	return acon1;
    }
    
	@Override
	public AIRuleEntity saveRule(AIRuleEntity rule) {
		AIRuleEntity rule1 = emMicro.merge(rule);
		emMicro.persist(rule1);
		return rule1;
	} 
	
}
