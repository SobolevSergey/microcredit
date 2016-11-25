package ru.simplgroupp.reports;

import org.junit.Before;
import org.junit.Test;
import ru.simplgroupp.persistence.reports.model.*;
import ru.simplgroupp.reports.dao.FilterHelperDao;
import ru.simplgroupp.reports.dao.FilterHelperDao;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 */
public class FiltersTest {
    private static final Logger logger = Logger.getLogger(ReportsSelectingTest.class.getName());
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager em;
    private Context context;
    @Inject
    private FilterHelperDao filterHelperDao;
    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }
    @Test
    public void testListRegions() throws IllegalAccessException {
        List<RegionSearchModel> professionSearchModelList = filterHelperDao.getAllRegions();
        for (RegionSearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListCitiesForRegion() throws IllegalAccessException {
        List<PlaceSearchModel> professionSearchModelList = filterHelperDao.getAllCitiesForRegion("1bd3414d-ffa4-484b-aee2-024ef27a7f2f");
        for (PlaceSearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListPlacesForRegion() throws IllegalAccessException {
        List<PlaceSearchModel> professionSearchModelList = filterHelperDao.getAllPlacesForRegion("1bd3414d-ffa4-484b-aee2-024ef27a7f2f");
        for (PlaceSearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListManagers() throws IllegalAccessException {
        List<ManagerSearchModel> professionSearchModelList = filterHelperDao.getAllManagers();
        for (ManagerSearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListProducts() throws IllegalAccessException {
        List<ProductSearchModel> professionSearchModelList = filterHelperDao.getAllProducts();
        for (ProductSearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListPaymentWays() throws IllegalAccessException {
        List<PaymentWaySearchModel> professionSearchModelList = filterHelperDao.getAllPaymentWays();
        for (PaymentWaySearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListChannels() throws IllegalAccessException {
        List<ChannelSearchModel> professionSearchModelList = filterHelperDao.getAllChannels();
        for (ChannelSearchModel regionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(regionSearchModel));
        }
    }
    @Test
    public void testListProfessions() throws IllegalAccessException {
        List<ProfessionSearchModel> professionSearchModelList = filterHelperDao.getAllProfessions();
        List<GenderSearchModel> genders = filterHelperDao.getAllGenders();
        List<MarriageSearchModel> marriages = filterHelperDao.getAllMarriages();
        for (ProfessionSearchModel professionSearchModel : professionSearchModelList) {
            logger.info(printClassFields(professionSearchModel));
        }
        for (GenderSearchModel genderSearchModel : genders) {
            logger.info(printClassFields(genderSearchModel));
        }
        for (MarriageSearchModel marriageSearchModel : marriages) {
            logger.info(printClassFields(marriageSearchModel));
        }
    }
    private String printClassFields(Object object) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(object);
            result.append(String.format("Field name: %s, Field value: %s%n", name, value));
        }
        return result.toString();
    }
}
