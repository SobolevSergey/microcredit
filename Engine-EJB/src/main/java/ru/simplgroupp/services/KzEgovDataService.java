package ru.simplgroupp.services;

import ru.simplgroupp.exception.KzEgovDataException;
import ru.simplgroupp.transfer.kzegovdata.Ats;
import ru.simplgroupp.transfer.kzegovdata.Geonims;

import javax.ejb.Local;
import java.util.List;

@Local
public interface KzEgovDataService {

    public List<Ats> getAtsList(Long parentId, Integer page, Integer pageSize, String term, boolean isActual);

    public List<Geonims> getGeonimsList(Long atsId, Long parentId, Integer page, Integer pageSize, String term, boolean isActual);

    public void updateAtsType();

    public void updateGeonimsType();

    public Ats getAts(Long atsId) throws KzEgovDataException;

    public Geonims getGeonims(Long atsId, Long geonimsId) throws KzEgovDataException;

}
