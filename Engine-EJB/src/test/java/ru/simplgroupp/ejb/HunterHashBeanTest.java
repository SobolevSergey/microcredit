package ru.simplgroupp.ejb;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.openejb.api.LocalClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@LocalClient
public class HunterHashBeanTest {

    private HunterHashBean hunterHashBean;

    @Before
    public void setUp() throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

        hunterHashBean = (HunterHashBean) context.lookup("java:global/Engine-EJB/HunterHashBean!ru.simplgroupp.ejb.HunterHashBean");
        System.out.println("Hunter configuration directory:" + hunterHashBean.getConfigurationDirectory());
    }

    @Test
    public void run() throws IOException {
        InputStream inputStream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("hunter-lm.xml"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Assert.assertTrue(hunterHashBean.hash(inputStream, outputStream));

        outputStream.close();
        inputStream.close();
    }
}
