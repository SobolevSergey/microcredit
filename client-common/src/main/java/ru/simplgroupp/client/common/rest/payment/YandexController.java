package ru.simplgroupp.client.common.rest.payment;

import ru.simplgroupp.client.common.facade.payment.YandexConfig;
import ru.simplgroupp.client.common.facade.payment.YandexFacade;
import ru.simplgroupp.persistence.PaymentEntity;

import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.AuthenticationException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Yandex controller
 */
@Produces(MediaType.APPLICATION_JSON)
@Named
public class YandexController {

    private YandexFacade yandexFacade;

    public YandexController() {
    }

    @Inject
    public YandexController(YandexFacade yandexFacade) {
        this.yandexFacade = yandexFacade;
    }

    @Path("create")
    @POST
    public YandexPaymentResponse createPayment(PaymentRequest paymentRequest) throws AuthenticationException,
            ObjectNotFoundException {
        PaymentEntity payment = yandexFacade.createPayment(paymentRequest.getSum());

        YandexConfig config = yandexFacade.getYandexConfig();

        return new YandexPaymentResponse(config.getShopId(), config.getScid(), config.getCustomerNumber(), config
                .getPaymentUrl(), payment.getId());
    }
}
