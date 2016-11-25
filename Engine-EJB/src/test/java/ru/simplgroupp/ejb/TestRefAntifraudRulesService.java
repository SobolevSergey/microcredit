package ru.simplgroupp.ejb;

import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ru.simplgroupp.dao.interfaces.AntifraudOccasionDAO;
import ru.simplgroupp.dao.interfaces.AntifraudSuspicionDAO;
import ru.simplgroupp.dao.interfaces.RefAntifraudRulesDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.RefAntifraudRulesService;
import ru.simplgroupp.persistence.ExtendedBaseEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.transfer.Partner;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestRefAntifraudRulesService {

    @EJB
    private RefAntifraudRulesService rulesService;

    @EJB
    private AntifraudSuspicionDAO antifraudSuspicionDAO;

    @EJB
    private AntifraudOccasionDAO antifraudOccasionDAO;

    @EJB
    private RefAntifraudRulesDAO antifraudRulesDAO;

    @EJB
    private RefAntifraudRulesService refAntifraudRulesService;

    @EJB
    private ReferenceBooksLocal referenceBooks;


    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
    }

//    @Test
    public void testMatches() {
        rulesService.getMatchesForAntifraudRules(52921, 52922);
    }

    @Test
    public void testGetSuspision() {
        System.out.println(antifraudSuspicionDAO.getEntity(52982));
        System.out.println(antifraudSuspicionDAO.getSuspicion(52982, null));
        System.out.println(antifraudSuspicionDAO.find(52922, 52921, 1, 21, 52860, false, null));
    }

    @Test
    public void testAntiFraudOccasion() {
        AntifraudOccasionEntity entity = antifraudOccasionDAO.save(antifraudOccasionDAO.buildEntity(518, 52922, 52921, "MA", 1, null, null, null));
        System.out.println(antifraudOccasionDAO.getEntity(entity.getId()));
        System.out.println(antifraudOccasionDAO.getMain(entity.getId(), null));
        System.out.println(antifraudOccasionDAO.find(52922, 52921, "MA", 1, 1, null));
        antifraudOccasionDAO.delete(entity.getId());
    }

    @Test
    public void testBuildSuspicion() {
        RefAntifraudRulesEntity refAntifraudRule = antifraudRulesDAO.getRefAntifraudRulesEntity(1);
        AntifraudSuspicionEntity suspicionEntity = antifraudSuspicionDAO.buildSuspicion(56145, 55288, Partner.SYSTEM,
                refAntifraudRule, 54994, true, "test", 5.1, new Date());

        antifraudSuspicionDAO.saveSuspicion(suspicionEntity);
    }


    // Тест "Тот же человек, другой паспорт за последние 30 дней, код MA_SPER_DPAS_30"
    @Test
    public void testMaSperDpas30() {
        Integer peopleMainId = 1;

        RefAntifraudRulesEntity rule = antifraudRulesDAO.getRefAntifraudRulesEntityByCode("MA_SPER_DPAS_30");
        ExtendedBaseEntity entity = antifraudRulesDAO.createAndExecuteSqlForRequest(rule, peopleMainId);


        Map<String, Object> map = refAntifraudRulesService.getParamsFromRequest(entity);

        List<ExtendedBaseEntity> result = antifraudRulesDAO.createAndExecuteSqlForResponse(rule, peopleMainId, map);
        System.out.println(result);
    }

    @Test
    public void testFindBlackAddress() {
        assertFalse("must be not empty", referenceBooks.findInRefBlacklistByAddress("Алтайский край", "Бурлинский", null,
                null, null, null, null, null, null).isEmpty());
    }
}
