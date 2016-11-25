package ru.simplgroupp.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.services.DefInfoClientService;
import ru.simplgroupp.services.DefInfoClientServiceException;
import ru.simplgroupp.services.DefInfoResponse;
import ru.simplgroupp.services.DefInfoRest;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.SettingsKeys;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;

/**
 * Def info client implementation
 */
@Named
@Singleton
public class DefInfoClientServiceImpl implements DefInfoClientService {

    @Inject
    private RulesBeanLocal rulesBean;

    private String baseUrl;

    private String login;

    private String password;

    @PostConstruct
    public void init() {
        Map<String, Object> params = rulesBean.getSiteConstants();
        baseUrl = (String) params.get(SettingsKeys.PHONE_DB_URL);
        login = (String) params.get(SettingsKeys.PHONE_DB_URL_LOGIN);
        password = (String) params.get(SettingsKeys.PHONE_DB_URL_PASSWORD);
    }

    @Override
    public DefInfoRest getInfo(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("Phone is blank");
        }

        if (StringUtils.isEmpty(baseUrl) || StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            throw new DefInfoClientServiceException("Client is not configured");
        }

        ClientRequest request = new ClientRequest(baseUrl + (baseUrl.endsWith("/") ? phone : "/" + phone));
        request.header("Authorization", "Basic "+ HTTPUtils.getBasicAuthorizationString(login, password));
        try {
            ClientResponse<DefInfoResponse> response = request.get(DefInfoResponse.class);
            if (response.getResponseStatus() != OK) {
                throw new DefInfoClientServiceException("Error due get DEF info: " + response.getResponseStatus());
            }
            DefInfoResponse defInfoResponse = response.getEntity();
            if (defInfoResponse.getStatus() != 1) {
                throw new DefInfoClientServiceException("Error due get DEF info: " + defInfoResponse.getError());
            }
            return defInfoResponse.getResult();
        } catch (Exception e) {
            throw new DefInfoClientServiceException("Error due get DEF info.", e);
        }
    }
}
