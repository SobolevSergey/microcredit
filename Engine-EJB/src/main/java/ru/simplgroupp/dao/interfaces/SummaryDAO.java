package ru.simplgroupp.dao.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RefSummaryEntity;
import ru.simplgroupp.persistence.SummaryEntity;

@Local
public interface SummaryDAO {

    /**
     * сохраняем запись в таблицу с итогами
     *
     * @param creditRequest - заявка
     * @param peopleMain - человек
     * @param partner - партнер
     * @param value - значение итогового параметра
     * @param ref   - связь со справочником вида итога
     * @param refid - id справочника, если значение, записываемое в таблицу, связано со справочником
     * @param summaryDate - дата информации
     */
    void saveSummary(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain, PartnersEntity partner, String value, 
    		RefSummaryEntity ref, Integer refid,Date summaryDate);
    
    /**
     * возвращает суммарную информацию по id
     * @param summaryId - id информации
     * @return
     */
    SummaryEntity getSummaryEntity(Integer summaryId);
    
    /**
     * ищем записи в summary
     *
     * @param pmain   - человек
     * @param cre     - заявка
     * @param partner - партнер
     * @param ref     - параметр из справочника RefSummary, что именно ищем
     */
    public List<SummaryEntity> findSummary(PeopleMainEntity pmain, CreditRequestEntity cre, PartnersEntity partner, RefSummaryEntity ref);

}
