package ru.simplgroupp.fias.ejb;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.simplgroupp.fias.exception.FiasException;

/**
 * @author CVB
 */
@LocalClient
public class TestFiasUpdater {

  @EJB
  FiasUpdaterLocal fiasUpdater;

  @Before
  public void setUp() throws Exception {
    System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

    System.setProperty("openejb.validation.output.level", "VERBOSE");
    final Properties p = new Properties();
    p.load(this.getClass().getResourceAsStream("/test.properties"));

    p.setProperty(Context.INITIAL_CONTEXT_FACTORY,
        "org.apache.openejb.client.LocalInitialContextFactory");

    System.out.println("Создаем контейнер");
    final Context context = EJBContainer.createEJBContainer(p).getContext();
    System.out.println("Создали контейнер");
    context.bind("inject", this);

    fiasUpdater = (FiasUpdaterLocal) context.lookup("java:global/Fias/FiasUpdater!ru.simplgroupp.fias.ejb.FiasUpdaterLocal");
    fiasUpdater.setExternalUnrarCommand("C:/Program Files/7-Zip/7z.exe");
  }

  //@Test
  public void testEjbExists() {
    Assert.assertNotNull(fiasUpdater);
  }

  //@Test
  public void testGetters() {
    System.out.println("Download service URL: " + fiasUpdater.getDownloadServiceUrl());
    System.out.println("External Unrar command: " + fiasUpdater.getExternalUnrarCommand());
  }

  @Test
  public void testDbInfo() {
    System.out.println("Last updated: " + fiasUpdater.getLastUpdateDate());
    System.out.println("Version: " + fiasUpdater.getVersionId());
    System.out.println("Version description: " + fiasUpdater.getVersionText());
    String updateInfo = "n/a";
    try {
      updateInfo = fiasUpdater.getNumberOfUnprocessedUpdates() + " update(s) available";
    } catch (FiasException e) {
      updateInfo = e.getMessage();
    }
    System.out.println("Update info: " + updateInfo);
  }

  //@Test
  public void testReplace() throws FiasException {
    fiasUpdater.replaceFias("D:/Downloads/Fias/FullDB/fias_xml_20150521.rar", 174, "БД ФИАС от 21.05.2015");
  }
  
  //@Test
  public void testUpdate() throws FiasException {
    //fiasUpdater.updateFias(1, false);
    fiasUpdater.updateFias();
  }
  
}
