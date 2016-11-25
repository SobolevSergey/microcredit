package ru.simplgroupp.ejb;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.BizActionDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.UserAlreadyExistException;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.service.BusinessEventSender;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.WorkplaceHistoryService;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.UserWorkplaceHistoryEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.persistence.UsertypeEntity;
import ru.simplgroupp.persistence.WorkplaceEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.util.BizActionUtils;
import ru.simplgroupp.util.ProductKeys;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.Set;

@Stateless(name = "UsersBean")
@Local(UsersBeanLocal.class)
public class UsersBean implements UsersBeanLocal {

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    MailBeanLocal mailBean;

    @EJB
    KassaBeanLocal kassaBean;

    @EJB
    PeopleBeanLocal peopleBean;

    @EJB
    EventLogService logService;

    @EJB
    UsersDAO usersDAO;

    @EJB
    BusinessEventSender senderServ;

    @EJB
    ProductDAO productDAO;
    
    @EJB
    PeopleDAO peopleDAO;
    
    @EJB
    BizActionDAO bizDAO;
    
    @EJB
    ActionProcessorBeanLocal actionProcessor;
   
    @EJB
    WorkplaceHistoryService workplaceHistoryService;
    
    @Inject Logger logger;

    @Override
    public UsersEntity findUserByLogin(String value) {
       return usersDAO.findUserByLogin(value);
    }

    @Override
    public UsersEntity findUserByLink(String templink) {
    	return usersDAO.findUserByLink(templink);
    }

    @Override
    @TransactionAttribute
    public void changePassword(int peopleMainId, String password) {

        UsersEntity ent = findUserByPeopleId(peopleMainId);
        if (ent != null) {
            //если пользователь создается автоматически, генерим ему пароль сами
            if (StringUtils.isEmpty(password)) {
                password = mailBean.generatePasswordForSending();
            }
            logger.info("пароль для " + peopleMainId + " - " + password);
            if (StringUtils.isEmpty(password)) {
                logger.severe("Пустой пароль,  не получается поменять");
            } else {
                ent.setPassword(DigestUtils.md5Hex(password));
            }

            emMicro.merge(ent);

            //ищем шаблон письма
    	    ProductMessagesEntity message=productDAO.findMessageByName(ProductKeys.EMAIL_PASSWORD_CHANGED);
            if (message!=null){
            	//высылаем письмо
            	Object[] params=null;
                actionProcessor.sendEmailCommon(ent.getPeopleMainId().getId(), message.getSubject(), message.getBody(), params);
            }
         
        } else {
            logger.severe("Не удалось поменять пароль, пользователя для " + peopleMainId + " не существует");
        }
    }

    @Override
    public UsersEntity findUserByPeopleId(Integer peopleId) {
    	return usersDAO.findUserByPeopleId(peopleId);
   
    }

    @Override
    public Users findUserByPeopleId(Integer peopleId, Set options) {
        UsersEntity ent = findUserByPeopleId(peopleId);
        if (ent == null) {
            return null;
        } else {
            Users res = new Users(ent);
            res.init(options);
            return res;
        }
    }

    @Override
    public List<UsersEntity> findUsers(String value, Integer peopleId, Integer userType, Integer role, String surname) {
        String hql = "select u from ru.simplgroupp.persistence.UsersEntity as u $join$ where (1=1)";
        if (StringUtils.isNotEmpty(value)) {
            hql = hql + " and (upper(u.username) like :username)";
        }
        if (role != null) {
            hql = hql.replace("$join$", " inner join u.roles as r ");
            hql = hql + " and (r.id = :role)";
        } else {
            hql = hql.replace("$join$", " ");
        }

        if (peopleId != null) {
            hql = hql + " and (u.peopleMainId.id = :peopleId)";
        }
        if (userType != null) {
            hql = hql + " and (u.usertype.id = :userType)";
        }
        if (StringUtils.isNotEmpty(surname)) {
            hql = hql +
                    " and ( (select count(*) from u.peopleMainId.peoplepersonal as p where (upper(p.surname) like :surname) ) > 0 )";
        }
        Query qry = emMicro.createQuery(hql);
        if (StringUtils.isNotEmpty(value)) {
            qry.setParameter("username", "%" + value.toUpperCase() + "%");
        }
        if (role != null) {
            qry.setParameter("role", role);
        }
        if (peopleId != null) {
            qry.setParameter("id", peopleId);
        }
        if (userType != null) {
            qry.setParameter("userType", userType);
        }
        if (StringUtils.isNotEmpty(surname)) {
            qry.setParameter("surname", "%" + surname.toUpperCase() + "%");
        }
        List<UsersEntity> lst = qry.getResultList();
        return lst;
    }

    @Override
    public List<Users> listUsers(int nFirst, int nCount, SortCriteria[] sorting, Set options, String userName,
            Integer peopleId, Integer userType, Integer role, String surname) {
        List<UsersEntity> lstEnt = findUsers(userName, peopleId, userType, role, surname);

        List<Users> lstRes = new ArrayList<>(lstEnt.size());
        for (UsersEntity ent : lstEnt) {
            Users usr = new Users(ent);
            usr.init(options);
            lstRes.add(usr);
        }
        return lstRes;
    }

    @Override
    public int countUsers(String userName, Integer peopleId, Integer userType, Integer role, String surname) {
        List<UsersEntity> lstEnt = findUsers(userName, peopleId, userType, role, surname);
        return lstEnt.size();
    }

    @Override
    @TransactionAttribute
    public UsersEntity addUser(PeopleMainEntity peopleId, String surname, String name, String midname, Integer usertype,
            String username, String password, List<RolesEntity> roles) {
        logger.info("Добавление пользователя " + surname);
        UsersEntity user = new UsersEntity();

        //если это новый человек, которого еще нет в БД
        if (peopleId == null) {
            peopleId = new PeopleMainEntity();
            emMicro.persist(peopleId);
            PeoplePersonalEntity pplpers = new PeoplePersonalEntity();
            pplpers.setSurname(surname);
            pplpers.setName(name);
            pplpers.setMidname(midname);
            pplpers.setPartnersId(refBooks.getPartner(Partner.CLIENT).getEntity());
            pplpers.setIsactive(ActiveStatus.ACTIVE);
            pplpers.setDatabeg(new Date());
            pplpers.setPeopleMainId(peopleId);
            emMicro.persist(pplpers);
        }

        user.setPeopleMainId(peopleId);
        user.setUsername(username);

        //если пароль не пустой
        if (StringUtils.isNotEmpty(password)) {
        	user.setPassword(DigestUtils.md5Hex(password));
        	//высылаем пароль на email
            mailBean.send(username, "Пароль от рабочего кабинета ","Пароль от рабочего кабинета на сайте "+password+".");
            
        } else {
        	//если добавляем пользователя автоматически, генерим ему пароль
          	password=mailBean.generatePasswordForSending();
        	if (usertype==Users.USER_CLIENT) {
        	    user.setPassword(DigestUtils.md5Hex(password));
        	} else {
        		user.setPassword(password);
        	}
        	// TODO убрать смс, когда смс будет нормально запускаться
        	//ищем телефон и высылаем на него пароль
            PeopleContactEntity phone=peopleBean.findPeopleByContactMan(PeopleContact.CONTACT_CELL_PHONE, peopleId.getId());
            if (phone!=null) {
            	String messageText="Логин от личного кабинета "+username+", пароль "+password+". Обязательно смените пароль при первом входе.";
                mailBean.sendSMSV2(phone.getValue(), messageText);
                mailBean.saveMessage(peopleId.getId(), null, Messages.SMS, Reference.AUTO_EXEC, new Date(), messageText,
                        null);
            }
        }
        logger.info("пароль для " + peopleId.getId() + " - " + password);

        user.setUsertype(getUserType(usertype).getEntity());

        //генерим временную ссылку для захода в ЛК, если это клиент
        if (user.getUsertype().getId() == Users.USER_CLIENT) {

            try {
                user.setTempLink(Convertor.toDigest(mailBean.generateCodeForSending()));
            } catch (Exception e) {
                logger.severe("Не удалось создать временную ссылку для захода в ЛК " + e.getMessage());
            }
        }

        //запишем роли для человека
        user.setRoles(roles);

        user = emMicro.merge(user);
        emMicro.persist(user);

        Users auser = new Users(user);

        //если юзер приглашен пользователем в системе, добавляем бонусы
        PeopleFriend friend = peopleBean.findCalledFriend(auser.getUserName());
        if (friend != null) {
            RefBonus refb = refBooks.getBonusByCodeInteger(PeopleBonus.BONUS_CODE_INVITE);
            if (refb != null) {
                Double amount = refb.getAmount();
                try {
                    peopleBean.addBonus(friend.getPeopleMain().getId(), PeopleBonus.BONUS_CODE_INVITE, BaseCredit.OPERATION_IN, new Date(), amount,
                            null, auser.getPeopleMain().getId());
                } catch (Exception e) {
                    logger.severe("Не удалось добавить бонусы"+ e);
                }
            }
        } else {
            senderServ.fireEvent(auser.getPeopleMain(), EventCode.ADD_USER, Utils.mapOfSO());
        }

        // ивент срабатывает позже, не зачисляет бонус
//        if (friend != null) {
//            senderServ.fireEvent(auser.getPeopleMain(), EventCode.ADD_USER,
//                    Utils.mapOfSO("invitedByPeopleId", friend.getPeopleMain().getId()));
//        } else {
//            senderServ.fireEvent(auser.getPeopleMain(), EventCode.ADD_USER, Utils.mapOfSO());
//        }

        return user;
    }

    @Override
    @TransactionAttribute
    public Users getSystemUser() {
        List<UsersEntity> lst = findUsers(null, null, Users.USER_SYSTEM, null, null);
        if (lst.size() == 0) {
            return null;
        } else {
            UsersEntity ent = lst.get(0);
            Users aobj = new Users(ent);
            aobj.init(null);
            return aobj;
        }
    }

    @Override
    @TransactionAttribute
    public UsersEntity getUserEntity(int id) {
        return usersDAO.getUsersEntity(id);
    }

	@Override
    @TransactionAttribute
    public Users getUser(int id, Set options) {
        UsersEntity ent = usersDAO.getUsersEntity(id);
        if (ent == null) {
            return null;
        }

        Users aobj = new Users(ent);
        aobj.init(options);
        return aobj;
    }

    @Override
    public Users findUserByLogin(String userName, Set options) {
        UsersEntity ent = findUserByLogin(userName);
        if (ent == null) {
            return null;
        }

        Users aobj = new Users(ent);
        aobj.init(options);
        return aobj;
    }

    @Override
    public Users findUserByToken(String passPhrase, String encryptedToken, Set options) {
       
        UsersEntity entity=usersDAO.findUserByToken(passPhrase, encryptedToken);
        
        if (entity==null){
        	return null;
        }
        Users aobj = new Users(entity);
        aobj.init(options);
        return aobj;
    }

    @Override
    public Users findUserByLink(String templink, Set options) {
        UsersEntity ent = findUserByLink(templink);
        if (ent == null) {
            return null;
        }

        Users aobj = new Users(ent);
        aobj.init(options);
        return aobj;
    }

	@Override
	@TransactionAttribute
	public void removeTempLink(Integer userId) {
		UsersEntity usr=getUserEntity(userId);
		usr.setTempLink(null);
		emMicro.merge(usr);
	}

    @Override
    public Users findUserByLoginAndPassword(String username, String password) {
        UsersEntity usr = usersDAO.findUserByLoginAndPassword(username, DigestUtils.md5Hex(password.trim()));
        if (usr == null) {
            return null;
        } else {
            Users auser = new Users(usr);
            return auser;
        }
    }

    @Override
    public String getPermission(String businessObjectClass, Object businessObjectId, int roleId) {
        Query qry = emMicro.createNamedQuery("getPermission");
        qry.setParameter("businessobjectclass", businessObjectClass);
        qry.setParameter("businessobjectid", businessObjectId);
        qry.setParameter("roles_id", roleId);
        List<Object[]> lstRes = qry.getResultList();
        if (lstRes.size() == 0) {
            return "";
        } else {
            String sres = "";
            Object[] row = lstRes.get(0);
            if (row[0] != null && ((Boolean) row[0]).booleanValue()) {
                sres = sres + "R";
            }
            if (row[1] != null && ((Boolean) row[1]).booleanValue()) {
                sres = sres + "W";
            }
            return sres;
        }
    }

    @Override
    @TransactionAttribute
    public void setPermission(String businessObjectClass, Object businessObjectId, int roleId, boolean bRead,
            boolean bWrite) {
        String sql = "select * from bpermissions where (businessobjectclass = :businessobjectclass) and (businessobjectid = :businessobjectid) and (roles_id = :roles_id)";
        Query qry = emMicro.createNativeQuery(sql);
        qry.setParameter("businessobjectclass", businessObjectClass);
        qry.setParameter("businessobjectid", businessObjectId);
        qry.setParameter("roles_id", roleId);
        List<Object[]> lstRes = qry.getResultList();
        if (lstRes.size() == 0) {
            sql = "insert into bpermissions (businessobjectclass, businessobjectid, roles_id, permread, permwrite) values (:businessobjectclass, :businessobjectid, :roles_id, :permread, :permwrite)";
            qry = emMicro.createNativeQuery(sql);
            qry.setParameter("businessobjectclass", businessObjectClass);
            qry.setParameter("businessobjectid", businessObjectId.toString());
            qry.setParameter("roles_id", roleId);
            qry.setParameter("permread", bRead);
            qry.setParameter("permwrite", bWrite);
            qry.executeUpdate();
        } else {
            sql = "update bpermissions set permread = :permread, permwrite = :permwrite where (businessobjectclass = :businessobjectclass) and (businessobjectid = :businessobjectid) and (roles_id = :roles_id)";
            qry = emMicro.createNativeQuery(sql);
            qry.setParameter("businessobjectclass", businessObjectClass);
            qry.setParameter("businessobjectid", businessObjectId.toString());
            qry.setParameter("roles_id", roleId);
            qry.setParameter("permread", bRead);
            qry.setParameter("permwrite", bWrite);
            qry.executeUpdate();
        }
    }

    @Override
    @TransactionAttribute
    public void removeUser(Integer userId) {
        Query qry = emMicro.createNamedQuery("removeUser");
        qry.setParameter("id", userId);
        Object i = qry.getSingleResult();

    }

    @Override
    @TransactionAttribute
    public Users saveUsers(Users source) {
        UsersEntity usr = usersDAO.saveUsersEntity(source.getEntity());
        // TODO запустить событие
        return new Users(usr);
    }

    @Override
    @TransactionAttribute
    public void saveUser(Users user, String surname, String name, String midname, Integer workplaceId)
            throws ObjectNotFoundException {
        Partner parClient = refBooks.getPartner(Partner.CLIENT);
        UsersEntity usr = usersDAO.getUsersEntity(user.getId());
        //этот пользователь уже есть
        if (usr != null) {
            usr.setUsername(user.getUserName().toLowerCase());
            usr.setPassword(DigestUtils.md5Hex(user.getPassword()));
            UsertypeEntity userType = getUserType(user.getUserType().getId()).getEntity();
            usr.setUsertype(userType);
            if (user.getRoles().size() > 0) {
                usr.getRoles().clear();
                //перепишем роли
                List<RolesEntity> role = new ArrayList<RolesEntity>(user.getRoles().size());
                for (Roles rl : user.getRoles()) {
                    role.add(getRoleEntity(rl.getId()));
                }
                usr.setRoles(role);
            }

            WorkplaceEntity oldWorkplace = usr.getWorkplace();
            if (workplaceId != null && workplaceId > 0) {
                WorkplaceEntity workplace = getWorkplaceEntity(workplaceId);
                if (workplace == null) {
                    throw new ObjectNotFoundException("Workplace with id " + workplaceId + " not found");
                }
                if (workplace != null && !workplace.equals(oldWorkplace)) {
                    usr.setWorkplace(workplace);
                }
            } else {
                usr.setWorkplace(null);
            }
            if ((oldWorkplace != null && !oldWorkplace.equals(usr.getWorkplace())) ||
                    (oldWorkplace == null && usr.getWorkplace() != null)) {
                UserWorkplaceHistoryEntity workplaceHistory = new UserWorkplaceHistoryEntity();
                workplaceHistory.setUser(usr);
                workplaceHistory.setWorkplace(usr.getWorkplace());
                workplaceHistory.setModifiedDate(new Date());
                //TODO: добавить в параметры userId - кто менял запись и писать ее
                // UsersEntity modifiedBy = getUserEntity(userId);
                // workplaceHistory.setModifiedBy(modifiedBy);
                workplaceHistoryService.save(workplaceHistory);
            }

            emMicro.merge(usr);
            //ищем человека в БД
            PeopleMainEntity pmain = peopleDAO.getPeopleMainEntity(user.getPeopleMain().getId());
            if (pmain != null) {

                PeoplePersonalEntity ppl = peopleBean.findPeoplePersonalActive(pmain);

                if (ppl != null) {
                	//если пользователь уже был, поменялось фио
                    if (StringUtils.isNotEmpty(ppl.getSurname()) && StringUtils.isNotEmpty(ppl.getName())) {
                    
                    	if (!ppl.getSurname().equalsIgnoreCase(surname) || !ppl.getName().equalsIgnoreCase(name)) {
           
                            //поставим в предыдущую запись недействительность
                            ppl.setIsactive(ActiveStatus.ARCHIVE);
                            ppl.setDataend(new Date());
                            emMicro.merge(ppl);

                            //запишем новые данные
                            PeoplePersonalEntity pplpernew = new PeoplePersonalEntity();

                            pplpernew.setPartnersId(parClient.getEntity());
                            pplpernew.setPeopleMainId(pmain);
                            pplpernew.setDatabeg(new Date());
                            pplpernew.setDataend(null);
                            pplpernew.setIsactive(ActiveStatus.ACTIVE);
                            pplpernew.setIsUploaded(false);

                            pplpernew.setSurname(Convertor.toRightString(surname));
                            pplpernew.setName(Convertor.toRightString(name));
                            pplpernew.setMidname(Convertor.toRightString(midname));
                            pplpernew.setBirthdate(ppl.getBirthdate());
                            pplpernew.setBirthplace(ppl.getBirthplace());
                            pplpernew.setGender(ppl.getGender());
                            pplpernew.setCitizen(ppl.getCitizen());

                            emMicro.merge(pplpernew);
                        }
                    }
                    //пустой пользователь
                    else {
                            ppl.setSurname(Convertor.toRightString(surname));
                            ppl.setName(Convertor.toRightString(name));
                            ppl.setMidname(Convertor.toRightString(midname));
                            emMicro.merge(ppl);
                    }
                    

                }
            }
        }//end user not null

   }

    @Override
    public UserType getUserType(int id) {
        UsertypeEntity ent = emMicro.find(UsertypeEntity.class, new Integer(id));
        if (ent == null) {
            return null;
        } else {
            return new UserType(ent);
        }

    }

    @Override
    public List<Roles> getRoles() {

        Query qry = emMicro.createNamedQuery("getRoles");

        List<RolesEntity> lstr = qry.getResultList();
        List<Roles> lstRes = new ArrayList<Roles>(lstr.size());
        for (RolesEntity ent : lstr) {
            lstRes.add(new Roles(ent));
        }
        return lstRes;
    }

    @Override
    public Roles getRole(int id) {
        RolesEntity ent = getRoleEntity(id);
        if (ent == null) {
            return null;
        } else {
            return new Roles(ent);
        }
    }

    @Override
    public RolesEntity getRoleEntity(int id) {
        return emMicro.find(RolesEntity.class, id);
    }

    @Override
    @TransactionAttribute
    public Roles createRole(String roleName, String realName) throws ActionException {
        RolesEntity ent = new RolesEntity();
        if (StringUtils.isBlank(roleName)) {
            Date dt = new Date();
            roleName = "role" + String.valueOf(dt.getTime());
        } else {
            List<RolesEntity> lst = usersDAO.findRoleByName(roleName);
            if (lst.size() > 0) {
                throw new ActionException("Роль с таким именем уже существует", Type.BUSINESS, ResultType.FATAL);
            }
        }
        ent.setName(roleName);
        if (StringUtils.isBlank(realName)) {
            realName = "новая роль";
        }
        ent.setRealName(realName);
        Integer aid = usersDAO.getMaxRoleId();
        ent.setId(aid + 1);
        ent = usersDAO.saveRolesEntity(ent);

        Roles role = new Roles(ent);
        senderServ.fireEvent(role, EventCode.ROLE_CREATED, Utils.mapOfSO());
        return role;
    }

    @Override
    @TransactionAttribute
    public void setRPermissions(int roleId, List<Integer> featureIds, List<Integer> actionIds) {
        usersDAO.deleteRPByRoleFeature(roleId);
        usersDAO.deleteRPByRoleAction(roleId);
        for (Integer featureId : featureIds) {
            usersDAO.insertRPByRoleFeature(roleId, featureId);
        }
        for (Integer actionId : actionIds) {
            usersDAO.insertRPByRoleAction(roleId, actionId);
        }
        Roles arole = getRole(roleId);
        
        List<BizActionEntity> lstBiz = bizDAO.listBOActions(null, null, null, null);
        for (BizActionEntity biz: lstBiz) {
        	if (actionIds.contains(biz.getId())) {
        		// действие должно быть для этой роли
        		boolean bChanged = BizActionUtils.setRole(biz, arole.getName());
        		if (bChanged) {
        			bizDAO.saveBizActionEntity(biz);
        		}
        	} else {
        		// действия не должно быть для этой роли
        		boolean bChanged = BizActionUtils.removeRole(biz, arole.getName());
        		if (bChanged) {
        			bizDAO.saveBizActionEntity(biz);
        		}        		
        	}
        }
        
        senderServ.fireEvent(arole, EventCode.ROLE_PERM_CHANGED, Utils.mapOfSO());
    }

    @Override
    @TransactionAttribute
    public void deleteRole(int roleId) throws ActionException {
    	Roles arole = getRole(roleId);
    	
        usersDAO.deleteRole(roleId);
        
        senderServ.fireEvent(arole, EventCode.ROLE_DELETED, Utils.mapOfSO());
    }

    @Override
    @TransactionAttribute
    public Roles saveRoles(Roles role) throws ActionException {
        List<RolesEntity> lst = usersDAO.findRoleByNameExId(role.getName(), role.getId());
        if (lst.size() > 0) {
            throw new ActionException("Роль с таким именем уже существует", Type.BUSINESS, ResultType.FATAL);
        }
        RolesEntity ent = usersDAO.saveRolesEntity(role.getEntity());
        Roles aobj = new Roles(ent);
        senderServ.fireEvent(aobj, EventCode.ROLE_UPDATED, Utils.mapOfSO());
        return aobj;
    }

    @Override
    public List<UserType> getUserTypes() {
        Query qry = emMicro.createNamedQuery("getUserTypes");

        List<UsertypeEntity> lstUsr = qry.getResultList();
        List<UserType> lstRes = new ArrayList<UserType>(lstUsr.size());
        for (UsertypeEntity ent : lstUsr) {
            lstRes.add(new UserType(ent));
        }
        return lstRes;
    }

    @Override
    @TransactionAttribute
    public UsersEntity addUserClient(PeopleMainEntity peopleMain, String email) {
        UsersEntity user = findUserByLogin(email);
        //пишем пользователя   
        if (user == null) {
            //добавляем роли человека
            List<RolesEntity> lst = new ArrayList<RolesEntity>(0);
            lst.add(getRole(Roles.ROLE_CLIENT).getEntity());
            //добавляем пользователя
            user = addUser(peopleMain, "", "", "", Users.USER_CLIENT, email, "", lst);
            return user;
        } else {
            logger.severe("Пользователь с именем входа " + email + " уже существует");
            throw new UserAlreadyExistException();
        }
    }

    @Override
    @TransactionAttribute
    public UsersEntity updateUsername(String oldUsername, String username) throws ObjectNotFoundException {
        UsersEntity user = findUserByLogin(oldUsername);
        if (user == null) {
            throw new ObjectNotFoundException("User with login " + oldUsername + " not found");
        }

        user.setUsername(username);
        user=usersDAO.saveUsersEntity(user);
        //TODO запустить событие "смена username", послать сообщение человеку о смене логина
        //ищем шаблон письма
	    ProductMessagesEntity message=productDAO.findMessageByName(ProductKeys.EMAIL_LOGIN_CHANGED);
        if (message!=null){
        	//высылаем письмо
        	Object[] params=new Object[]{user.getUsername()};
              	
            actionProcessor.sendEmailCommon(user.getPeopleMainId().getId(), message.getSubject(), message.getBody(), params);
        }
        return user;
    }

   
	@Override
	public WorkplaceEntity getWorkplaceEntity(int id) {
		return emMicro.find(WorkplaceEntity.class, id);
	}

	@Override
	public UserWorkplaceHistoryEntity getUserWorkplaceHistoryEntity(int id) {
		return emMicro.find(UserWorkplaceHistoryEntity.class, id);
	}
}
