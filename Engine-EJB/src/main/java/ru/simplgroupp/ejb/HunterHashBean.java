package ru.simplgroupp.ejb;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.experian.rfps.Transformation;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.util.ErrorKeys;

@Singleton
//@Startup
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class HunterHashBean {

	@Inject Logger LOG;

    private static final String RFPS_FILE_NAME = "rfps.xml";

    @Resource(name = "configurationDirectory")
    private String configurationDirectory;

    private Transformation transformation;

    @PostConstruct
    private void init() {
        try {
            transformation = Transformation.getInstance(RFPS_FILE_NAME, configurationDirectory);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE,"Experian transformation init error: " + ex,ex);
            throw new RuntimeException(new ActionException(ErrorKeys.EXPERIAN_CANT_INIT,
                    "Не удалось инициализировать experian transformation модуль",
                    Type.TECH, ResultType.FATAL, null));
        }
    }

    public boolean hash(InputStream inputStreamXml, OutputStream outputStreamXml) {
        return transformation.execute(inputStreamXml, outputStreamXml) == 0;
    }

    public String getConfigurationDirectory() {
        return configurationDirectory;
    }
}