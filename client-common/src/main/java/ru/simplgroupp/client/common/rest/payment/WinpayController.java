package ru.simplgroupp.client.common.rest.payment;

import ru.simplgroupp.client.common.facade.payment.WinpayConfig;
import ru.simplgroupp.client.common.facade.payment.WinpayFacade;
import ru.simplgroupp.client.common.facade.payment.WinpayRequest;

import javax.ejb.ObjectNotFoundException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.AuthenticationException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Winpay controller
 */
@Produces(MediaType.APPLICATION_JSON)
@Named
public class WinpayController {

    private WinpayFacade winpayFacade;

    public WinpayController() {
    }

    @Inject
    public WinpayController(WinpayFacade winpayFacade) {
        this.winpayFacade = winpayFacade;
    }

    @Path("create")
    @POST
    public WinpayPaymentResponse createPayment(WinpayPaymentRequest paymentRequest) throws AuthenticationException,
            ObjectNotFoundException {
        WinpayRequest winpayRequest = winpayFacade.createPaymentRequest(paymentRequest.getSum(),
                paymentRequest.getSuccessUrl(), paymentRequest.getFailureUrl());

        WinpayConfig config = winpayFacade.getWinpayConfig();

        return new WinpayPaymentResponse(config.getPaymentUrl(), winpayRequest.getData(), winpayRequest.getSign());
    }
}
