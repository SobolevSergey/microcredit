package ru.simplgroupp.dao.impl;

import java.util.ArrayList;
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

import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.Reference;
import ru.simplgroupp.transfer.Users;
import ru.simplgroupp.util.AppUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UsersDAOImpl implements UsersDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

	@Override
	public UsersEntity findUserByLoginAndPassword(String username, String passwordDigest) {
		return (UsersEntity) JPAUtils.getSingleResult(emMicro, "findUserByLoginAndPassword", Utils.mapOf(
				"username", username.trim(), "password", passwordDigest			
				));

	}
	
	@Override
	public UsersEntity saveUsersEntity(UsersEntity source) {
		UsersEntity usr = emMicro.merge(source);
		return usr;
	}
	
	@Override
	public RolesEntity saveRolesEntity(RolesEntity source) {
		RolesEntity role = emMicro.merge(source);
		return role;
	}
	
	@Override
	public List<RolesEntity> findRoleByName(String roleName) {
		return JPAUtils.getResultList(emMicro, "findRoleByName", Utils.mapOf(
				"roleName", roleName));
	}
	
	@Override
	public List<RolesEntity> findRoleByNameExId(String roleName, Integer roleId) {
		
		return JPAUtils.getResultList(emMicro, "findRoleByNameExId", Utils.mapOf(
				"roleName", roleName,
				"roleId", roleId));
		
	}
	
	@Override
	public void deleteRole(int roleId) {
		Query qry = emMicro.createNamedQuery("deleteRole");
		qry.setParameter("id", roleId);
		qry.executeUpdate();
	}
	
	
	@Override
	public Integer getMaxRoleId() {
		Query qry = emMicro.createNamedQuery("getMaxRoleId");
		List lst = qry.getResultList();
		if (lst.size() == 0 || lst.get(0) == null) {
			return 0;
		} else {
			return ((Number) lst.get(0)).intValue();
		}
	}
	
	@Override
	public List<Object[]> listRolePerm(Integer roleId) {
		Query qry = emMicro.createNamedQuery("listRPByRole");
		qry.setParameter("roleId", roleId);
		return qry.getResultList();
	}
	
	@Override
	public void insertRPByRoleFeature(int roleId, Integer featureId) {
		Query qry = emMicro.createNamedQuery("insRPFByRole");
		qry.setParameter("roleId", roleId);
		qry.setParameter("featureId", featureId);
		qry.executeUpdate();
	}
	
	@Override
	public void insertRPByRoleAction(int roleId, Integer bizactId) {
		Query qry = emMicro.createNamedQuery("insRPAByRole");
		qry.setParameter("roleId", roleId);
		qry.setParameter("bizactId", bizactId);
		qry.executeUpdate();
	}			
	
	@Override
	public void deleteRPByRoleAction(int roleId) {
		Query qry = emMicro.createNamedQuery("delRPAByRole");
		qry.setParameter("roleId", roleId);
		qry.executeUpdate();
	}	
	
	@Override
	public void deleteRPByRoleFeature(int roleId) {
		Query qry = emMicro.createNamedQuery("delRPFByRole");
		qry.setParameter("roleId", roleId);
		qry.executeUpdate();
	}
	
	@Override
	public List<ReferenceEntity> listRPermFeatureE(List<Integer> roleIds) {
		Query qry = emMicro.createNamedQuery("listRPFByRole", ReferenceEntity.class);
		qry.setParameter("roleIds", roleIds);
		return qry.getResultList();
	}
	
	@Override
	public List<Reference> listRPermFeature(List<Integer> roleIds) {
		List<ReferenceEntity> lst = listRPermFeatureE(roleIds);
		return AppUtil.wrapCollectionSilent(ReferenceEntity.class, Reference.class, lst);
	}
	
	@Override
	public List<BizActionEntity> listRPermActionE(List<Integer> roleIds) {
		Query qry = emMicro.createNamedQuery("listRPAByRole", BizActionEntity.class);
		qry.setParameter("roleIds", roleIds);
		return qry.getResultList();
	}

	@Override
	public UsersEntity getFreeCollector() {
		Query qry = emMicro.createNamedQuery("findFreeCollector");
		List<Integer> res = qry.getResultList();
		if (res == null || res.size() == 0) {
			return null;
		}
		Integer userID = Convertor.toInteger(res.get(0));
		return getUsersEntity(userID);
	}

	@Override
	public List<BizAction> listRPermAction(List<Integer> roleIds) {
		List<BizActionEntity> lst = listRPermActionE(roleIds);
		return AppUtil.wrapCollectionSilent(BizActionEntity.class, BizAction.class, lst);
	}

	@Override
	public UsersEntity getUsersEntity(int userId) {
		return emMicro.find(UsersEntity.class, userId);
	}

	@Override
	public RolesEntity getRolesEntity(int roleId) {
		return emMicro.find(RolesEntity.class, roleId);
	}

	@Override
	public UsersEntity findUserByPeopleId(Integer peopleId) {
		return (UsersEntity) JPAUtils.getSingleResult(emMicro, "findUserByPeopleId",  Utils.mapOf("peopleId", peopleId));
	}

	@Override
	public UsersEntity findUserByLogin(String value) {
		return (UsersEntity) JPAUtils.getSingleResult(emMicro, "findUserByLogin",  Utils.mapOf("username", value.trim().toLowerCase()));
	}

	@Override
	public UsersEntity findUserByLink(String templink) {
		return (UsersEntity) JPAUtils.getSingleResult(emMicro, "findUserByLink",  Utils.mapOf("templink", templink));
	}

	@Override
	public UsersEntity findUserByToken(String passPhrase, String encryptedToken) {
		 Query qry = emMicro.createNamedQuery("findUserByToken", UsersEntity.class);
	     qry.setParameter(1, passPhrase);
	     qry.setParameter(2, encryptedToken);
	     List<UsersEntity> lst = qry.getResultList();

	     return (UsersEntity) Utils.firstOrNull(lst);
	}

	@Override
	public UsersEntity findByOktellUuid(String uuid) {
		Query qry = emMicro.createNamedQuery("findUserByOktellUUID", UsersEntity.class);
		qry.setParameter("oktellUuid", uuid);
		List<UsersEntity> lst = qry.getResultList();

		return (UsersEntity) Utils.firstOrNull(lst);
	}

	@Override
	public List<Users> findUsersByRoleID(Integer roleID, Set options) {
		Query qry = emMicro.createNamedQuery("findUsersByRoleID", UsersEntity.class);
		qry.setParameter("roleID", roleID);
		List<UsersEntity> lst = qry.getResultList();
		List<Users> result = new ArrayList<>();
		for (UsersEntity user : lst) {
			Users u = new Users(user);
			u.init(options);
			result.add(u);
		}
		return result;
	}
}
