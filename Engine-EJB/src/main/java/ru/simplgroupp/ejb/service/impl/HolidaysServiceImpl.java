package ru.simplgroupp.ejb.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.interfaces.service.HolidaysService;
import ru.simplgroupp.persistence.HolidaysEntity;
import ru.simplgroupp.util.DatesUtils;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class HolidaysServiceImpl implements HolidaysService {

 
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @Override
    public HolidaysEntity getHolidays(Date date) {
        String sql = "from ru.simplgroupp.persistence.HolidaysEntity where (:date between databeg and dataend)";
        Query query = emMicro.createQuery(sql);

        query.setParameter("date", date);

        List<HolidaysEntity> holidays = query.getResultList();
        //если сегодня праздник есть
        if (holidays.size() > 0) {
            return holidays.get(0);
        }
        //возможно, он будет завтра
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            query = emMicro.createQuery(sql);
            if (calendar.get(Calendar.HOUR_OF_DAY) >= HolidaysEntity.HOUR_PM - 1) {
                
            	query.setParameter("date", DateUtils.addDays(date, 1));
                holidays = query.getResultList();
                if (holidays.size() > 0) {
                    return holidays.get(0);
                }
            }
        }
        return null;
    }

    @Override
    public boolean isHoliday(Date date) {
        return getHolidays(date) != null;
    }

    @Override
    public int getDaysOfHolidays(Date date) {
        HolidaysEntity holiday = getHolidays(date);
        if (holiday != null) {
            return DatesUtils.daysDiff(holiday.getDataend(), date) + 1;
        }
        return 0;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public HolidaysEntity addHolidays(Date databeg, Date dataend, Integer kind,
                                      String name) {
        HolidaysEntity holiday = new HolidaysEntity();
        holiday.setDatabeg(databeg);
        holiday.setDataend(dataend);
        holiday.setKind(kind);
        holiday.setName(name);
        emMicro.persist(holiday);
        return holiday;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteHolidays(int holidaysId) {
        HolidaysEntity holiday = emMicro.find(HolidaysEntity.class, holidaysId);
        if (holiday != null) {
            String sql = "delete from holidays where id=:id";
            Query query = emMicro.createNativeQuery(sql);
            query.setParameter("id", holidaysId);
            query.executeUpdate();
        }
    }

    @Override
    public List<HolidaysEntity> listHolidays(Integer month, Integer year,
                                             String name, Integer kind) {
        String sql = "from ru.simplgroupp.persistence.HolidaysEntity where (1=1)";
        if (month != null)
            sql = sql + " and date_part('month',databeg)=:month ";
        if (year != null)
            sql = sql + " and date_part('year',databeg)=:year ";
        if (StringUtils.isNotEmpty(name))
            sql = sql + " and upper(name) like :name ";
        if (kind != null)
            sql = sql + " and kind=:kind ";
        sql = sql + " order by databeg desc";
        Query query = emMicro.createQuery(sql);
        if (month != null)
            query.setParameter("month", month);
        if (year != null)
            query.setParameter("year", year);
        if (kind != null)
            query.setParameter("kind", kind);
        if (StringUtils.isNotEmpty(name))
            query.setParameter("name", "%" + name.toUpperCase() + "%");
        return query.getResultList();
    }

    @Override
    public int countHolidays(Integer month, Integer year, String name, Integer kind) {
        List<HolidaysEntity> holidays = listHolidays(month, year, name, kind);
        return holidays.size();
    }

    @Override
    public HolidaysEntity getHolidaysEntity(Integer id, Set options) {
        HolidaysEntity holiday = emMicro.find(HolidaysEntity.class, id);
        if (holiday != null) {
            holiday.init(options);
        }
        return holiday;
    }

}
