package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.transfer.Reference;
import ru.simplgroupp.transfer.Users;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface UsersDAO {


    /**
     * ищем пользователя по логину и паролю
     *
     * @param username       - логин
     * @param passwordDigest - пароль
     * @return
     */
    public UsersEntity findUserByLoginAndPassword(String username, String passwordDigest);

    /**
     * сохраняем пользователя
     *
     * @param source - пользователь
     * @return
     */
    public UsersEntity saveUsersEntity(UsersEntity source);

    /**
     * сохраняем роль
     *
     * @param source - роль
     * @return
     */
    public RolesEntity saveRolesEntity(RolesEntity source);

    /**
     * ищем роль по названию
     *
     * @param roleName - название роли
     * @return
     */
    public List<RolesEntity> findRoleByName(String roleName);

    /**
     * ищем роль по названию и id
     *
     * @param roleName - название роли
     * @param roleId   - id
     * @return
     */
    public List<RolesEntity> findRoleByNameExId(String roleName, Integer roleId);

    /**
     * удаляем роль
     *
     * @param roleId - id роли
     */
    public void deleteRole(int roleId);

    /**
     * возвращает максимальный id роли
     *
     * @return
     */
    public Integer getMaxRoleId();

    /**
     * список разрешений роли
     *
     * @param roleId - id роли
     * @return
     */
    public List<Object[]> listRolePerm(Integer roleId);

    /**
     * возвращает пользователя по id
     *
     * @param userId - id пользователя
     * @return
     */
    public UsersEntity getUsersEntity(int userId);

    /**
     * возвращает роль по id
     *
     * @param roleId - id роли
     * @return
     */
    public RolesEntity getRolesEntity(int roleId);

    public void deleteRPByRoleFeature(int roleId);

    void insertRPByRoleFeature(int roleId, Integer featureId);

    void insertRPByRoleAction(int roleId, Integer bizactId);

    void deleteRPByRoleAction(int roleId);

    List<ReferenceEntity> listRPermFeatureE(List<Integer> roleIds);

    List<Reference> listRPermFeature(List<Integer> roleIds);

    List<BizAction> listRPermAction(List<Integer> roleIds);

    List<BizActionEntity> listRPermActionE(List<Integer> roleIds);

    /**
     * Метод возващает коллектора у которого меньше всего клиентов
     *
     * @return коллектор у которого меньше всего клиентов
     */
    UsersEntity getFreeCollector();

    /**
     * ищем пользователя по id человека
     *
     * @param peopleId
     * @return
     */
    public UsersEntity findUserByPeopleId(Integer peopleId);

    /**
     * ищем пользователя по логину
     *
     * @param value
     * @return
     */
    public UsersEntity findUserByLogin(String value);

    /**
     * ищем пользователя по временной ссылке
     *
     * @param templink -временная ссылка
     * @return
     */
    public UsersEntity findUserByLink(String templink);

    /**
     * ищем пользователя по токену
     *
     * @param passPhrase     - пароль
     * @param encryptedToken - токен
     * @return
     */
    public UsersEntity findUserByToken(String passPhrase, String encryptedToken);

    /**
     * Ищет пользователя по UUID oktell
     *
     * @param uuid строка с идентификатором
     * @return пользователь иначе null
     */
    UsersEntity findByOktellUuid(String uuid);

    /**
     * Метод возвращает список пользователей по роли
     *
     * @param roleID  ID роли
     * @param options инициализация
     * @return список пользователей
     */
    List<Users> findUsersByRoleID(Integer roleID, Set options);
}
