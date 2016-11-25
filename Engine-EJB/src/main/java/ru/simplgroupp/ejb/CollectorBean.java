package ru.simplgroupp.ejb;

import ru.simplgroupp.dao.interfaces.CollectorDAO;
import ru.simplgroupp.interfaces.CollectorBeanLocal;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.transfer.Collector;
import ru.simplgroupp.transfer.Users;
import ru.simplgroupp.util.AppUtil;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(CollectorBeanLocal.class)
public class CollectorBean implements CollectorBeanLocal {
	@Inject Logger logger;

    @EJB
    private CollectorDAO collectorDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Users> getCollectorList(Set options) {
        List<UsersEntity> list = collectorDAO.getCollectorList();
        List<Users> result = new ArrayList<Users>();
        for (UsersEntity entity : list) {
            Users people = new Users(entity);
            people.init(options);
            result.add(people);
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void passItemsToCollector(Integer toCollectorID, List<Collector> records) {
        logger.info("Passing selected items to collector with ID: " + toCollectorID);

        List<Integer> ids = AppUtil.extractIds(records);
        // меняем записи коллектора на другого коллектора
        collectorDAO.passItemsToCollector(toCollectorID, ids);

        // меняем несделанные задачи
        collectorDAO.passTasksToCollector(toCollectorID, ids);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void passFromOneCollectorToAnother(Integer fromCollectorID, Integer toCollectorID) {
        logger.info("Passing from one collector to another");
        logger.info("From: " + fromCollectorID);
        logger.info("To: " + toCollectorID);

        // передаем от одного коллектора другому
        collectorDAO.passFromOneCollectorToAnother(fromCollectorID, toCollectorID);

        // передаем не сделанные задачи так же
        collectorDAO.passTasksFromOneCollectorToAnother(fromCollectorID, toCollectorID);
    }
}
