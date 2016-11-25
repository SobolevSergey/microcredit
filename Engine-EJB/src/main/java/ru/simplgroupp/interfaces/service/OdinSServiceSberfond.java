package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.odins.model.sberfond.SputnikExchange;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Date;

/**
 *
 */
public interface OdinSServiceSberfond {
      public  SputnikExchange generateXMLResponse(Date date);
      public  SputnikExchange generateXMLAccruedInterest(Date date);
      public  SputnikExchange getDebts(Date date);
}
