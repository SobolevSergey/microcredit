package ru.simplgroupp.interfaces;

import ru.simplgroupp.transfer.Collector;
import ru.simplgroupp.transfer.Users;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface CollectorBeanLocal {
    /**
     * Список коллекторов в системе
     *
     * @param options параметры для инициализации
     * @return список коллекторов
     */
    List<Users> getCollectorList(Set options);

    /**
     * Метод устанавливает у записей в records исполнителя toCollectorID
     *
     * @param toCollectorID ID коллектора кому передаем записи
     * @param records       записи
     */
    void passItemsToCollector(Integer toCollectorID, List<Collector> records);

    /**
     * Метод обновляет все записи fromCollectorID в toCollectorID
     *
     * @param fromCollectorID коллектор чьи записи передаем
     * @param toCollectorID   коллектор кому передаем все записи
     */
    void passFromOneCollectorToAnother(Integer fromCollectorID, Integer toCollectorID);
}
