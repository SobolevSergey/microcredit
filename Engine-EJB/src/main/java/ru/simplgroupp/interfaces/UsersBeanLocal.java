package ru.simplgroupp.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.UserWorkplaceHistoryEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.persistence.WorkplaceEntity;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.Roles;
import ru.simplgroupp.transfer.UserType;
import ru.simplgroupp.transfer.Users;

@Local
public interface UsersBeanLocal extends Serializable {

    /**
     * ищем пользователя по имени входа, возвращаем entity
     *
     * @param value - имя входа
     */
    UsersEntity findUserByLogin(String value);

    /**
     * ищем пользователя по имени входа и паролю, возвращаем entity
     *
     * @param username - имя входа
     * @param password - пароль незашифрованный
     */
    Users findUserByLoginAndPassword(String username, String password);

    /**
     * ищем пользователя по временной ссылке
     *
     * @param templink - ссылка
     */
    UsersEntity findUserByLink(String templink);

    /**
     * ищем пользователя по id, возвращаем entity
     *
     * @param peopleId - id человека
     */
    UsersEntity findUserByPeopleId(Integer peopleId);

    /**
     * ищем пользователя по параметрам
     *
     * @param value    - имя входа
     * @param peopleId - id человека
     * @param userType - вид пользователя
     */
    List<UsersEntity> findUsers(String value, Integer peopleId, Integer userType, Integer role, String surname);

    /**
     * добавление пользователя
     *
     * @param peopleId - человек
     * @param surname  фамилия
     * @param name     имя
     * @param midname  отчество
     * @param usertype - тип пользователя
     * @param username - имя пользователя
     * @param password - пароль
     * @param roles    - набор ролей
     */
    UsersEntity addUser(PeopleMainEntity peopleId, String surname, String name, String midname, Integer usertype,
            String username, String password, List<RolesEntity> roles);

    /**
     * ищем пользователя по id, возвращаем entity
     *
     * @param id - id пользователя
     */
    UsersEntity getUserEntity(int id);

    /**
     * ищем пользователя по id, возвращаем трансферный объект
     *
     * @param id - id пользователя
     */
    Users getUser(int id, Set options);

    /**
     * ищем пользователя по имени входа, возвращаем трансферный объект
     *
     * @param userName - имя входа
     * @param options  - какие коллекции инициализировать
     */
    Users findUserByLogin(String userName, Set options);

    /**
     * ищем пользователя по шифрованному токену
     *
     * @param passPhrase     - ключ шифрования
     * @param encryptedToken - зашифрованный токен
     */
    Users findUserByToken(String passPhrase, String encryptedToken, Set options);

    /**
     * ищем пользователя по имени входа, возвращаем трансферный объект
     *
     * @param templink - временная ссылка
     * @param options  - какие коллекции инициализировать
     */
    Users findUserByLink(String templink, Set options);

    /**
     * смена пароля
     *
     * @param peopleMainId - id человека
     * @param password     - пароль
     */
    void changePassword(int peopleMainId, String password);

    /**
     * при переходе в лк, удаляем временную ссылку
     *
     * @param userId - id пользователя
     */
    void removeTempLink(Integer userId);

    /**
     * удаляем пользователя
     *
     * @param userId - id пользователя
     */
    void removeUser(Integer userId);

    /**
     * какие права
     *
     * @param businessObjectClass - класс бизнес-объекта
     * @param businessObjectId    - id бизнес-объекта
     * @param roleId              - id роли
     * @return
     */
    String getPermission(String businessObjectClass, Object businessObjectId, int roleId);

    /**
     * установить права
     *
     * @param businessObjectClass - класс бизнес-объекта
     * @param businessObjectId    - id бизнес-объекта
     * @param roleId              - id роли
     * @param bRead               - можно читать
     * @param bWrite              - можно писать
     */
    void setPermission(String businessObjectClass, Object businessObjectId, int roleId, boolean bRead, boolean bWrite);

    /**
     * список пользователей
     *
     * @param nFirst   - 1 запись
     * @param nCount   - кол-во записей на стр.
     * @param sorting  - сортировка
     * @param options  - опции для init
     * @param userName - логин
     * @param peopleId - id человека
     * @param userType - вид пользователя
     * @param role     - роль
     * @return
     */
    List<Users> listUsers(int nFirst, int nCount, SortCriteria[] sorting, Set options, String userName,
            Integer peopleId, Integer userType, Integer role, String surname);

    /**
     * количество пользователей
     *
     * @param userName - логин
     * @param peopleId - id человека
     * @param userType - вид пользователя
     * @param role     - роль
     * @return
     */
    int countUsers(String userName, Integer peopleId, Integer userType, Integer role, String surname);

    /**
     * сохраняем данные пользователя
     *
     * @param user    - пользователь
     * @param surname - фамилия
     * @param name    - имя
     * @param midname - отчество
     */
    void saveUser(Users user, String surname, String name, String midname, Integer workplaceId)
            throws ObjectNotFoundException;

    /**
     * возвращает список видов пользователей
     */
    List<UserType> getUserTypes();

    /**
     * возвращает конкретный вид пользователя
     *
     * @param id - id вида
     */
    UserType getUserType(int id);

    /**
     * возвращает список ролей в системе
     */
    List<Roles> getRoles();

    /**
     * возвращает конкретную роль
     */
    RolesEntity getRoleEntity(int id);

    /**
     * возвращает конкретную роль
     */
    Roles getRole(int id);

    /**
     * ищем пользователя по человеку
     *
     * @param peopleId -id человека
     * @param options  - опции для инициализации
     * @return
     */
    Users findUserByPeopleId(Integer peopleId, Set options);

    /**
     * ищем системного пользователя
     *
     * @return
     */
    Users getSystemUser();

    /**
     * сохраняем пользователя
     *
     * @param source - пользователь
     * @return
     */
    Users saveUsers(Users source);

    /**
     * Создаём роль, и сохраняем её
     *
     * @param roleName
     * @param realName
     * @return
     */
    Roles createRole(String roleName, String realName) throws ActionException;

    /**
     * Сохраняем роль
     *
     * @param role
     * @return
     * @throws ActionException
     */
    Roles saveRoles(Roles role) throws ActionException;

    /**
     * Удаляем роль
     *
     * @param roleId
     * @throws ActionException
     */
    void deleteRole(int roleId) throws ActionException;

    /**
     * устанавливает разрешения для ролей
     *
     * @param roleId
     * @param featureIds
     * @param actionIds
     */
    void setRPermissions(int roleId, List<Integer> featureIds, List<Integer> actionIds);

    /**
     * добавляем пользователя клиента
     *
     * @param peopleMain - человек
     * @param email      - почта
     * @return
     * @throws UserAlreadyExistException
     */
    UsersEntity addUserClient(PeopleMainEntity peopleMain, String email);

    /**
     * Изменить имя входа пользователя
     *
     * @param oldUsername старое имя входа
     * @param username    новое имя входа
     * @return пользователь
     */
    UsersEntity updateUsername(String oldUsername, String username) throws ObjectNotFoundException;

    /**
     * Найти пользователя по телефону или логину
     *
     * @param phone телефон
     * @return пользователь
     */
   // UsersEntity findUserByCellPhoneOrUsername(String phone);

    /**
     * Найти пользователя по телефону или логину
     *
     * @param phone   телефон
     * @param options - опции инициализации
     * @return пользователь
     */
   // Users findUserByCellPhoneOrUsername(String phone, Set<Users.Options> options);

    /**
     * генерируем код для авторизации по ссылке
     *
     * @param username логин
     */
   //String generateLoginCode(String username) throws KassaException;
    
    /**
     * возвращает рабочее место по id
     * @param id - id рабочего места
     * @return
     */
    WorkplaceEntity getWorkplaceEntity(int id);
    /**
     * возвращает запись с историей рабочего места
     * @param id - id записи
     * @return
     */
    UserWorkplaceHistoryEntity getUserWorkplaceHistoryEntity(int id);
}

