package ru.simplgroupp.reports.dao.impl;

import ru.simplgroupp.persistence.reports.model.*;
import ru.simplgroupp.reports.dao.FilterHelperDao;
import ru.simplgroupp.reports.model.*;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class FilterHelperDaoImpl implements FilterHelperDao{
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    @Override
    public List<ProfessionSearchModel> getAllProfessions() {
        TypedQuery<ProfessionSearchModel> qry=emMicro.createNamedQuery("getAllProfessions",ProfessionSearchModel.class);
        return (List<ProfessionSearchModel>)qry.getResultList();
    }

    @Override
    public List<GenderSearchModel> getAllGenders() {
        TypedQuery<GenderSearchModel> qry=emMicro.createNamedQuery("getAllGenders",GenderSearchModel.class);
        return (List<GenderSearchModel>)qry.getResultList();
    }

    @Override
    public List<MarriageSearchModel> getAllMarriages() {
        TypedQuery<MarriageSearchModel> qry=emMicro.createNamedQuery("getAllMarriages",MarriageSearchModel.class);
        return (List<MarriageSearchModel>)qry.getResultList();
    }
    @Override
    public List<RegionSearchModel> getAllRegions() {
        TypedQuery<RegionSearchModel> qry=emMicro.createNamedQuery("getAllRegions",RegionSearchModel.class);
        return (List<RegionSearchModel>)qry.getResultList();
    }
    @Override
    public List<PlaceSearchModel> getAllCitiesForRegion(String region) {
        TypedQuery<PlaceSearchModel> qry=emMicro.createNamedQuery("getAllCitiesForRegion",PlaceSearchModel.class);
        qry.setParameter("region",region);
        return (List<PlaceSearchModel>)qry.getResultList();
    }
    @Override
    public List<PlaceSearchModel> getAllPlacesForRegion(String region) {
        TypedQuery<PlaceSearchModel> qry=emMicro.createNamedQuery("getAllPlacesForRegion",PlaceSearchModel.class);
        qry.setParameter("region",region);
        return (List<PlaceSearchModel>)qry.getResultList();
    }
    @Override
    public List<ManagerSearchModel> getAllManagers() {
        TypedQuery<ManagerSearchModel> qry=emMicro.createNamedQuery("getAllManagers",ManagerSearchModel.class);
        return (List<ManagerSearchModel>)qry.getResultList();
    }

    @Override
    public List<ProductSearchModel> getAllProducts() {
        TypedQuery<ProductSearchModel> qry=emMicro.createNamedQuery("getAllProducts",ProductSearchModel.class);
        return (List<ProductSearchModel>)qry.getResultList();
    }
    @Override
    public List<PaymentWaySearchModel> getAllPaymentWays() {
        TypedQuery<PaymentWaySearchModel> qry=emMicro.createNamedQuery("getAllPaymentWays",PaymentWaySearchModel.class);
        return (List<PaymentWaySearchModel>)qry.getResultList();
    }
    @Override
    public List<ChannelSearchModel> getAllChannels() {
        TypedQuery<ChannelSearchModel> qry=emMicro.createNamedQuery("getAllChannels",ChannelSearchModel.class);
        return (List<ChannelSearchModel>)qry.getResultList();
    }
}
