package ru.simplgroupp.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import ru.simplgroupp.dao.interfaces.RefAntifraudRulesDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesParamsEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.RefAntifraudRules;
import ru.simplgroupp.transfer.RefAntifraudRulesParams;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RefAntifraudRulesDAOImpl implements RefAntifraudRulesDAO {

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Override
    public RefAntifraudRulesEntity getRefAntifraudRulesEntity(int id) {
        return emMicro.find(RefAntifraudRulesEntity.class, id);
    }

    @Override
    public RefAntifraudRulesParamsEntity getRefAntifraudRulesParamsEntity(int id) {
        return emMicro.find(RefAntifraudRulesParamsEntity.class, id);
    }

    @Override
    public List<RefAntifraudRulesEntity> findActiveRules() {
        return JPAUtils.getResultList(emMicro, "findActiveAntifraudRules",
                Utils.mapOf("isActive", ActiveStatus.ACTIVE));
    }

    @Override
    public List<RefAntifraudRulesParamsEntity> findParamsForRuleAll(int ruleId) {
        return JPAUtils.getResultList(emMicro, "findParamsForRuleAll",
                Utils.mapOf("ruleId", ruleId));
    }

    @Override
    public List<RefAntifraudRulesParams> findParams(int ruleId, Set options) {
        List<RefAntifraudRulesParamsEntity> entities = findParamsForRuleAll(ruleId);

        List<RefAntifraudRulesParams> list = new ArrayList<>();
        for (RefAntifraudRulesParamsEntity entity : entities) {
            list.add(new RefAntifraudRulesParams(entity));
        }
        Utils.initCollection(list, options);

        return list;
    }

    @Override
    public List<RefAntifraudRules> findAllRules(Set options) {
        List<RefAntifraudRulesEntity> entities = JPAUtils.getResultList(emMicro, "findAllAntifraudRules", Utils.mapOf());

        List<RefAntifraudRules> list = new ArrayList<>();
        for (RefAntifraudRulesEntity entity : entities) {
            list.add(new RefAntifraudRules(entity));
        }
        Utils.initCollection(list, options);

        return list;
    }

    @Override
    public List<RefAntifraudRulesParamsEntity> findParamsForRule(int ruleId,
                                                                 int forRequest) {
        return JPAUtils.getResultList(emMicro, "findParamsForRule",
                Utils.mapOf("ruleId", ruleId, "forRequest", forRequest));
    }

    @Override
    public <T extends ExtendedBaseEntity> T createAndExecuteSqlForRequest(
            Integer ruleId, Integer peopleMainId) {
        RefAntifraudRulesEntity rule = getRefAntifraudRulesEntity(ruleId);
        if (rule != null) {
            return createAndExecuteSqlForRequest(rule, peopleMainId);
        }
        return null;
    }

    /**
     * возвращает значение параметра для запроса
     *
     * @param param - параметр
     * @return
     */
    private Object findParamValue(RefAntifraudRulesParamsEntity param) {
        if (param.getValueDate() != null && param.getDataType().equals("D")) {
            return param.getValueDate();
        } else if (param.getValueFloat() != null
                && param.getDataType().equals("F")) {
            return param.getValueFloat();
        } else if (param.getValueInteger() != null) {
            if (param.getDataType().equals("D")) {
                return DateUtils.addDays(new Date(),
                        -1 * param.getValueInteger());
            } else if (param.getDataType().equals("N")) {
                return param.getValueInteger();
            }
        } else if (param.getValueString() != null
                && param.getDataType().equals("S")) {
            return param.getValueString();
        }
        return null;
    }

    @Override
    public <T extends ExtendedBaseEntity> T createAndExecuteSqlForRequest(
            RefAntifraudRulesEntity rule, Integer peopleMainId) {
        String sql = "from " + rule.getEntityName() + " " + (rule.getEntityAlias() != null ? rule.getEntityAlias() : "") + " where " +
                (rule.getEntityAlias() != null ? rule.getEntityAlias() + "." : " ") +
                "peopleMainId.id=:peopleMainId ";
        List<RefAntifraudRulesParamsEntity> params = findParamsForRule(
                rule.getId(), 1);
        if (params.size() > 0) {
            for (RefAntifraudRulesParamsEntity param : params) {
                sql += param.getCondition();
            }
        }

        sql += " order by id desc ";
        Query qry = emMicro.createQuery(sql);
        qry.setParameter("peopleMainId", peopleMainId);
        if (params.size() > 0) {
            for (RefAntifraudRulesParamsEntity param : params) {
                if (findParamValue(param) != null) {
                    qry.setParameter(param.getName(), findParamValue(param));
                }
            }
        }
        List<T> lst = qry.getResultList();
        return (T) Utils.firstOrNull(lst);
    }

    @Override
    public <T extends ExtendedBaseEntity> List<T> createAndExecuteSqlForResponse(
            RefAntifraudRulesEntity rule, Integer peopleMainId,
            Map<String, Object> queryParams) {
        String sql = (StringUtils.isNotBlank(rule.getResultType()) ? "select " + rule.getResultTypeAlias() : "") +
                " from " + StringUtils.defaultIfEmpty(rule.getResultType(), rule.getEntityName()) + " " +
                StringUtils.defaultString(rule.getResultTypeAlias()) + " " +
                (rule.getResultJoin() != null ? " join " + rule.getResultJoin() : "") + " " + StringUtils.defaultString(rule.getEntityAlias()) + " " +
                " where (1=1) ";
        // если делаем только по этому человеку
        if (rule.getCheckSamePerson() == 1) {
            sql += " and " + (rule.getResultType() != null ? rule.getResultTypeAlias() + "." : " ") + "peopleMainId.id=:peopleMainId ";
        } else if (rule.getIncludeSamePerson() == 0) {
            // если запрос не должен включать этого человека
            sql += " and " + (rule.getResultType() != null ? rule.getResultTypeAlias() + "." : " ") + "peopleMainId.id<>:peopleMainId ";
        }
        List<RefAntifraudRulesParamsEntity> params = findParamsForRule(rule.getId(), 0);
        if (params.size() > 0) {
            Set<Integer> conditionIndexList = new HashSet<>();
            for (RefAntifraudRulesParamsEntity param : params) {
                // добавляем условие, если либо параметр есть в БД, либо в мапе
                // вернулся ненулевой
                if (findParamValue(param) != null || (findParamValue(param) == null && queryParams.get(param.getName()) != null)) {
                    if (param.getGroupIndex() != null && !conditionIndexList.contains(param.getGroupIndex())) {
                        String groupCondition = "";
                        boolean first = true;
                        for (RefAntifraudRulesParamsEntity group : params) {
                            if (group.getGroupIndex() != null && group.getGroupIndex().equals(param.getGroupIndex()) &&
                                    (findParamValue(group) != null || (findParamValue(group) == null && queryParams.get(group.getName()) != null))) {
                                if (first) {
                                    groupCondition += (" " + group.getGroupCondition() + " (");
                                    first = false;
                                    groupCondition += group.getCondition().replaceAll("or", "").replace("and", "");
                                } else {
                                    groupCondition += group.getCondition();
                                }
                            }
                        }
                        groupCondition = StringUtils.isNotEmpty(groupCondition) ? groupCondition += ") " : "";
                        // группировка к основному запросу
                        sql += groupCondition;
                        // добавляем сгруппированные в список отработаных
                        conditionIndexList.add(param.getGroupIndex());
                    }
                    if (param.getGroupIndex() == null) {
                        sql += param.getCondition();
                    }
                }
            }
        }
        Query qry = emMicro.createQuery(sql);
        if (rule.getCheckSamePerson() == 1 || rule.getIncludeSamePerson() == 0) {
            qry.setParameter("peopleMainId", peopleMainId);
        }
        if (params.size() > 0) {
            for (RefAntifraudRulesParamsEntity param : params) {
                // добавляем параметр, если параметр есть в БД
                if (findParamValue(param) != null) {
                    qry.setParameter(param.getName(), findParamValue(param));
                } else {
                    // добавляем параметр, если в мапе вернулся ненулевой
                    if (queryParams.get(param.getName()) != null) {
                        qry.setParameter(param.getName(),
                                queryParams.get(param.getName()));
                    }
                }
            }
        }
        return qry.getResultList();
    }

    @Override
    public <T extends ExtendedBaseEntity> List<T> createAndExecuteSqlForResponse(
            Integer ruleId, Integer peopleMainId,
            Map<String, Object> queryParams) {
        RefAntifraudRulesEntity rule = getRefAntifraudRulesEntity(ruleId);
        if (rule != null) {
            return createAndExecuteSqlForResponse(rule, peopleMainId,
                    queryParams);
        }
        return null;
    }

    @Override
    public RefAntifraudRulesEntity getRefAntifraudRulesEntityByCode(String ruleCode) {
        Query query = emMicro.createNamedQuery("findRefAntifraudRuleByCode");
        query.setParameter("code", ruleCode);
        return (RefAntifraudRulesEntity) Utils.firstOrNull(query.getResultList());
    }

    @Override
    public RefAntifraudRules getRefAntifraudRule(int id, Set options) {
        RefAntifraudRulesEntity entity = getRefAntifraudRulesEntity(id);

        if (entity != null) {
            RefAntifraudRules rule = new RefAntifraudRules(entity);
            rule.init(options);
            return rule;
        }

        return null;
    }

    @Override
    public RefAntifraudRulesEntity saveEntity(RefAntifraudRulesEntity rule) {
        return emMicro.merge(rule);
    }

    @Override
    public RefAntifraudRules saveRefAntifraudRule(RefAntifraudRules rule) {
        RefAntifraudRulesEntity entity = rule.getEntity();
        return new RefAntifraudRules(saveEntity(entity));
    }

    @Override
    public void updateParams(int ruleId, List<RefAntifraudRulesParams> params) {
        List<RefAntifraudRulesParams> originalParams = findParams(ruleId, null);

        List<RefAntifraudRulesParams> updateParams = new ArrayList<>(params);
        updateParams.retainAll(originalParams);

        List<RefAntifraudRulesParams> removeParams = new ArrayList<>(originalParams);
        removeParams.removeAll(params);

        List<RefAntifraudRulesParams> addParams = new ArrayList<>(params);
        addParams.removeAll(originalParams);

        for (RefAntifraudRulesParams param : updateParams) {
            emMicro.merge(param.getEntity());
        }

        for (RefAntifraudRulesParams param : removeParams) {
            emMicro.remove(param.getEntity());
        }

        for (RefAntifraudRulesParams param : addParams) {
            emMicro.persist(param.getEntity());
        }
    }

    @Override
    public boolean removeRule(int ruleId) {
        RefAntifraudRulesEntity entity = getRefAntifraudRulesEntity(ruleId);

        if (entity != null) {
            emMicro.remove(entity);
            return true;
        }

        return false;
    }
}
