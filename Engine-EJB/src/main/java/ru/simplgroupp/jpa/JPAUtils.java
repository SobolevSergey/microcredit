package ru.simplgroupp.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ru.simplgroupp.toolkit.common.Convertor;

public class JPAUtils {
	
	/**
	 * возвращает список
	 * @param em - entity manager
	 * @param queryName - название запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static List getResultList(EntityManager em, String queryName, Map<Object,Object> params) {
		Query qry = createNamedQuery(em,queryName,params);
		return qry.getResultList();
	}
	
	/**
	 * возвращает список
	 * @param em - entity manager
	 * @param sql - строка запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static List getResultListFromSql(EntityManager em, String sql, Map<Object,Object> params) {
		Query qry = createQuery(em,sql,params);
		return qry.getResultList();
	}
	
	/**
	 * возвращает одну запись
	 * @param em - entity manager
	 * @param queryName - название запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static Object getSingleResult(EntityManager em, String queryName, Map<Object,Object> params) {
		List lst = getResultList(em, queryName, params);
		if (lst.size() == 0) {
			return null;
		} else {
			return lst.get(0);
		}
	}	
	
	/**
	 * возвращает одну запись
	 * @param em - entity manager
	 * @param sql - строка запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static Object getSingleResultFromSql(EntityManager em, String sql, Map<Object,Object> params) {
		List lst = getResultListFromSql(em, sql, params);
		if (lst.size() == 0) {
			return null;
		} else {
			return lst.get(0);
		}
	}	
	
	/**
	 * создаем query для запроса
     * @param em - entity manager
	 * @param queryName - название запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static Query createNamedQuery(EntityManager em, String queryName, Map<Object,Object> params){
		Query qry = em.createNamedQuery(queryName);
		for (Map.Entry<Object,Object> entry: params.entrySet()) {
			qry.setParameter(entry.getKey().toString(), entry.getValue());
		}
		return qry;
	}
	
	/**
	 * создаем native query для запроса
     * @param em - entity manager
	 * @param sql - строка запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static Query createQuery(EntityManager em, String sql, Map<Object,Object> params){
		Query qry = em.createQuery(sql);
		for (Map.Entry<Object,Object> entry: params.entrySet()) {
			qry.setParameter(entry.getKey().toString(), entry.getValue());
		}
		return qry;
	}
	
	/**
	 * возвращает целое число, например кол-во чего-то
	 * 
	 * @param em - entity manager
	 * @param queryName - название запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static Integer getSingleResultInteger(EntityManager em, String queryName, Map<Object,Object> params) {
		Query qry = createNamedQuery(em,queryName,params);
		Integer res = Convertor.toInteger(qry.getSingleResult());
	    return res == null ? 0 : res;
	}	
	
	/**
	 * возвращает целое число, например кол-во чего-то
	 * 
	 * @param em - entity manager
	 * @param queryName - название запроса
	 * @param params - параметры запроса
	 * @return
	 */
	public static Integer getSingleResultSqlInteger(EntityManager em, String sql, Map<Object,Object> params) {
		Query qry = createQuery(em,sql,params);
		Integer res = Convertor.toInteger(qry.getSingleResult());
	    return res == null ? 0 : res;
	}	
}
