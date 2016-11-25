package ru.simplgroupp.reports.dao;

import ru.simplgroupp.persistence.reports.model.*;

import java.util.List;

/**
 *
 */
public interface FilterHelperDao {
    public List<ProfessionSearchModel> getAllProfessions();
    public List<GenderSearchModel> getAllGenders();
    public List<MarriageSearchModel> getAllMarriages();

    List<RegionSearchModel> getAllRegions();

    List<PlaceSearchModel> getAllCitiesForRegion(String region);

    List<PlaceSearchModel> getAllPlacesForRegion(String region);

    List<ManagerSearchModel> getAllManagers();
    List<ProductSearchModel> getAllProducts();

    List<PaymentWaySearchModel> getAllPaymentWays();

    List<ChannelSearchModel> getAllChannels();
}
