package ru.simplgroupp.ejb.service.impl;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.persistence.OrganizationEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.Organization;
import ru.simplgroupp.transfer.Partner;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OrganizationServiceImpl implements OrganizationService {

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    KassaBeanLocal kassaBean;

    @EJB
    UsersBeanLocal userBean;

    @EJB
    PeopleBeanLocal peopleBean;


    @Override
    public List<OrganizationEntity> getOrganization(Integer isactive,
                                                    Integer partner, Integer orgId, Integer orgTypeId) {

        String hql = "from ru.simplgroupp.persistence.OrganizationEntity where (1=1) ";
        if (partner != null) {
            hql = hql + " and (partnersId.id=:partner)";
        }
        if (isactive != null) {
            hql = hql + " and (isactive=:isactive)";
        }
        if (orgId != null) {
            hql = hql + " and (id=:orgId)";
        }
        if (orgTypeId != null) {
            hql = hql + " and (orgTypeId.codeinteger=:orgTypeId)";
        }
        Query qry = emMicro.createQuery(hql);
        if (partner != null) {
            qry.setParameter("partner", partner);
        }
        if (isactive != null) {
            qry.setParameter("isactive", isactive);
        }
        if (orgId != null) {
            qry.setParameter("orgId", orgId);
        }
        if (orgTypeId != null) {
            qry.setParameter("orgTypeId", orgTypeId);
        }
        List<OrganizationEntity> lst = qry.getResultList();

        return lst;
    }

    @Override
    public OrganizationEntity getOrganizationActive() {
        List<OrganizationEntity> org = getOrganization(ActiveStatus.ACTIVE, Partner.SYSTEM, null, null);
        return (OrganizationEntity) Utils.firstOrNull(org);
    }

    @Override
    public OrganizationEntity getOrganizationPartnerActive(Integer partner) {
        List<OrganizationEntity> org = getOrganization(ActiveStatus.ACTIVE, partner, null, null);
        return (OrganizationEntity) Utils.firstOrNull(org);
    }

    @Override
    public List<Organization> listOrganizations(String name, String inn, String kpp, String ogrn,
                                                String email, String phone, Integer isActive, Integer orgTypeId) {
        return listOrganizations(0, 0, name, inn, kpp, ogrn, email, phone, isActive, orgTypeId);
    }

    @Override
    public List<Organization> listOrganizations(int nFirst, int nCount, String name, String inn, String kpp, String ogrn,
                                         String email, String phone, Integer isActive, Integer orgTypeId) {
        String hql = "from ru.simplgroupp.persistence.OrganizationEntity where (1=1) ";

        if (StringUtils.isNotEmpty(name)) {
            hql += " and upper(name) like :name";
        }
        if (StringUtils.isNotEmpty(inn)) {
            hql += " and inn=:inn";
        }
        if (StringUtils.isNotEmpty(kpp)) {
            hql += " and kpp=:kpp";
        }
        if (StringUtils.isNotEmpty(ogrn)) {
            hql += " and ogrn=:ogrn";
        }
        if (StringUtils.isNotEmpty(phone)) {
            hql += " and phone=:phone";
        }
        if (StringUtils.isNotEmpty(email)) {
            hql += " and email=:email";
        }
        if (isActive != null) {
            hql += " and isactive=:isactive";
        }
        if (orgTypeId != null && orgTypeId > 0) {
            hql += " and orgTypeId.codeinteger=:orgTypeId ";
        }
        hql += " order by name";

        Query qry = emMicro.createQuery(hql);

        if (StringUtils.isNotEmpty(name)) {
            qry.setParameter("name", "%" + name.toUpperCase() + "%");
        }
        if (StringUtils.isNotEmpty(inn)) {
            qry.setParameter("inn", inn);
        }
        if (StringUtils.isNotEmpty(ogrn)) {
            qry.setParameter("ogrn", ogrn);
        }
        if (StringUtils.isNotEmpty(kpp)) {
            qry.setParameter("kpp", kpp);
        }
        if (StringUtils.isNotEmpty(phone)) {
            qry.setParameter("phone", phone);
        }
        if (StringUtils.isNotEmpty(email)) {
            qry.setParameter("email", email);
        }
        if (isActive != null) {
            qry.setParameter("isactive", isActive);
        }
        if (orgTypeId != null && orgTypeId > 0) {
            qry.setParameter("orgTypeId", orgTypeId);
        }


        if (nFirst >= 0) {
            qry.setFirstResult(nFirst);
        }
        if (nCount > 0) {
            qry.setMaxResults(nCount);
        }

        List<OrganizationEntity> entities = qry.getResultList();
        List<Organization> organizations = new ArrayList<>(entities.size());
        for (OrganizationEntity org : entities) {
            organizations.add(new Organization(org));
        }
        return organizations;
    }

    @Override
    public int countOrganizations(String name, String inn, String kpp, String ogrn,
                                  String email, String phone, Integer isActive, Integer orgTypeId) {
        return listOrganizations(name, inn, kpp, ogrn, email, phone, isActive, orgTypeId).size();
    }

    @Override
    public List<Organization> getCreditOrganizations() {
        return listOrganizations(null, null, null, null, null, null, null, Organization.CREDIT_ORGANIZATION);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveOrganization(Organization org, boolean withHistory) {
        OrganizationEntity oldOrg = getOrganizationEntity(org.getId());
        if (oldOrg != null) {
            //если меняем с историей
            if (withHistory) {
                //проверяем, поменялось ли что-нибудь
                if (org.equalsContent(oldOrg)) {
                    return;
                }

                //поставим недействительность в предыдущую запись
                oldOrg.setIsactive(ActiveStatus.ARCHIVE);
                oldOrg.setDataend(new Date());
                emMicro.merge(oldOrg);

                //запишем новые данные
                OrganizationEntity newOrg = new OrganizationEntity();
                newOrg.setName(org.getName());
                newOrg.setInn(org.getInn());
                newOrg.setKpp(org.getKpp());
                newOrg.setIsactive(org.getIsActive());
                newOrg.setEmail(org.getEmail());
                newOrg.setPhone(org.getPhone());
                newOrg.setFreePhone(org.getFreePhone());
                newOrg.setNotificationPhone(org.getNotificationPhone());
                newOrg.setDatabeg(new Date());
                newOrg.setIsactive(ActiveStatus.ACTIVE);
                emMicro.persist(newOrg);
            } else {
                oldOrg.setName(org.getName());
                oldOrg.setInn(org.getInn());
                oldOrg.setKpp(org.getKpp());
                oldOrg.setIsactive(org.getIsActive());
                oldOrg.setEmail(org.getEmail());
                oldOrg.setPhone(org.getPhone());
                oldOrg.setFreePhone(org.getFreePhone());
                oldOrg.setNotificationPhone(org.getNotificationPhone());
                emMicro.merge(oldOrg);
            }
        }
    }

    @Override
    public Organization getCreditOrganizationActive(Integer orgId) {
        List<OrganizationEntity> org = getOrganization(ActiveStatus.ACTIVE, null, orgId, null);
        if (org.size() > 0) {
            return new Organization(org.get(0));
        }
        return null;
    }

    @Override
    public Organization findOrganization(Integer id) {
        OrganizationEntity org = getOrganizationEntity(id);
        if (org != null) {
            Organization orgNew = new Organization(org);
            return orgNew;
        }
        return null;
    }

    @Override
    public List<Organization> findOrganizations() {
        String sql = "from ru.simplgroupp.persistence.OrganizationEntity where isactive=1 order by name";
        Query qry = emMicro.createQuery(sql);

        List<OrganizationEntity> lstr = qry.getResultList();
        List<Organization> lstRes = new ArrayList<Organization>(lstr.size());
        for (OrganizationEntity ent : lstr) {
            lstRes.add(new Organization(ent));
        }
        return lstRes;
    }

    @Override
    public OrganizationEntity getCreditOrganizationEntityActive(Integer orgId) {
        List<OrganizationEntity> org = getOrganization(ActiveStatus.ACTIVE, null, orgId, null);
        return (OrganizationEntity) Utils.firstOrNull(org);

    }

    @Override
    public OrganizationEntity getOrganizationEntity(Integer id) {
        return emMicro.find(OrganizationEntity.class, id);
    }
}
