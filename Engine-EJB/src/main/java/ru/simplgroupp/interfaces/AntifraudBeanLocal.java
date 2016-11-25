package ru.simplgroupp.interfaces;

import ru.simplgroupp.persistence.antifraud.AntifraudFieldTypesEntity;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.AntifraudField;
import ru.simplgroupp.transfer.AntifraudFieldType;
import ru.simplgroupp.transfer.AntifraudPage;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface AntifraudBeanLocal {

    AntifraudField getAntifraudField(int id, Set options);

    List<AntifraudField> listAntifraudField(int first, int count, SortCriteria[] sortCriterias, Integer creditRequestId);

    int countAntifraudField(Integer creditRequestId);

    AntifraudPage getAntifraudPage(int id, Set options);

    List<AntifraudPage> listAntifraudPage(int first, int count, SortCriteria[] sortCriterias, Integer creditRequestId);

    int countAntifraudPage(Integer creditRequestId);

    List<AntifraudFieldType> getAntifraudFieldTypes();

    void addAntifraudFieldType(String type, String description);

    void saveAntifraudFieldTypes(List<AntifraudFieldType> antifraudFieldTypeList);

    AntifraudFieldTypesEntity getAntifraudFieldTypesEntityByType(String type);

    AntifraudFieldTypesEntity getAntifraudFieldTypesEntityById(Integer id);
}
