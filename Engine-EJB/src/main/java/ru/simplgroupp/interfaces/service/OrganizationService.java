package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.persistence.OrganizationEntity;
import ru.simplgroupp.transfer.Organization;

import java.util.List;

public interface OrganizationService {

    /**
     * возвращает список организаций
     *
     * @param isactive  - активность
     * @param partner   - партнер
     * @param orgId     - id организации
     * @param orgTypeId - id типа организации
     * @return
     */
    public List<OrganizationEntity> getOrganization(Integer isactive, Integer partner,
                                                    Integer orgId, Integer orgTypeId);

    /**
     * ищем организацию по id
     */
    public Organization findOrganization(Integer id);

    /**
     * список организаций из БД
     */
    public List<Organization> findOrganizations();

    /**
     * возвращает нашу действующую организацию
     */
    public OrganizationEntity getOrganizationActive();

    /**
     * возвращает действующую организацию партнера
     */
    public OrganizationEntity getOrganizationPartnerActive(Integer partner);

    /**
     * возвращает список организаций, выдающих кредиты
     */
    public List<Organization> getCreditOrganizations();

    /**
     * возвращает список организаций
     *
     * @param name      - название
     * @param inn       - инн
     * @param kpp       - кпп
     * @param ogrn      - огрн
     * @param email     - email
     * @param phone     - телефон
     * @param isActive  - активно
     * @param orgTypeId - тип организации
     */
    public List<Organization> listOrganizations(String name, String inn, String kpp, String ogrn,
                                                String email, String phone, Integer isActive, Integer orgTypeId);

    /**
     * возвращает список организаций
     *
     * @param nFirst    первый номер
     * @param nCount    на страницу
     * @param name      название
     * @param inn       инн
     * @param kpp       кпп
     * @param ogrn      огрн
     * @param email     email
     * @param phone     телефон
     * @param isActive  активно
     * @param orgTypeId тип организации
     */
    List<Organization> listOrganizations(int nFirst, int nCount, String name, String inn, String kpp, String ogrn,
                                         String email, String phone, Integer isActive, Integer orgTypeId);

    /**
     * возвращает кол-во организаций, выдающих кредиты
     *
     * @param name      - название
     * @param inn       - инн
     * @param kpp       - кпп
     * @param ogrn      - огрн
     * @param email     - email
     * @param phone     - телефон
     * @param isActive  - активно
     * @param orgTypeId - тип организации
     */
    public int countOrganizations(String name, String inn, String kpp, String ogrn,
                                  String email, String phone, Integer isActive, Integer orgTypeId);

    /**
     * сохраняем организацию
     *
     * @param org         - организация
     * @param withHistory - пишем историю или нет
     */
    public void saveOrganization(Organization org, boolean withHistory);

    /**
     * возвращает организацию по Id
     */
    public Organization getCreditOrganizationActive(Integer orgId);

    /**
     * возвращает организацию по Id
     */
    public OrganizationEntity getCreditOrganizationEntityActive(Integer orgId);

    /**
     * возвращает организацию по id
     *
     * @param id - id организации
     * @return
     */
    OrganizationEntity getOrganizationEntity(Integer id);
}
