package ru.simplgroupp.ejb;

import ru.simplgroupp.dao.interfaces.VerificationReferenceDAO;
import ru.simplgroupp.interfaces.VerificationReferenceBeanLocal;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.transfer.*;

import javax.ejb.*;
import java.util.*;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class VerificationReferenceBean implements VerificationReferenceBeanLocal {
    
    @EJB
    private VerificationReferenceDAO verificationReferenceDAO;


    @Override
    public List<VerificationReference> findAllReference(Set options) {
        return verificationReferenceDAO.findAllReference(options);
    }

    @Override
    public VerificationReferenceEntity saveReference(VerificationReference reference) {
        VerificationReferenceEntity entity = reference.getEntity();

        List<VerificationConfigEntity> configEntityList = new ArrayList<>();
        for (VerificationConfig transferConfig : reference.getConfigurations()) {
            configEntityList.add(transferConfig.getEntity());
        }
        entity.setConfigurations(configEntityList);

        return verificationReferenceDAO.saveReference(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean removeReference(int id) {
        return verificationReferenceDAO.removeReference(id);
    }

    @Override
    public VerificationReferenceEntity getReferenceEntity(int id) {
        return verificationReferenceDAO.getReferenceEntity(id);
    }

    @Override
    public VerificationReference getReference(int id, Set options) {
        return verificationReferenceDAO.getReference(id, options);
    }
}
