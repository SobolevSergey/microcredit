package ru.simplgroupp.client.common.rest.payment;

import ru.simplgroupp.client.common.facade.payment.PayonlineConfig;
import ru.simplgroupp.client.common.facade.payment.PayonlineFacade;
import ru.simplgroupp.persistence.PaymentEntity;

import javax.ejb.ObjectNotFoundException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.AuthenticationException;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;

/**
 * Payonline controller
 */
@Produces(MediaType.APPLICATION_JSON)
@Named
public class PayonlineController {

    private static final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

    static {
        decimalFormatSymbols.setDecimalSeparator('.');
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00", decimalFormatSymbols);

    private PayonlineFacade payonlineFacade;

    public PayonlineController() {
    }

    @Inject
    public PayonlineController(PayonlineFacade payonlineFacade) {
        this.payonlineFacade = payonlineFacade;
    }

    @Path("create")
    @POST
    public PayonlinePaymentResponse createPayment(PaymentRequest paymentRequest)
            throws AuthenticationException, ObjectNotFoundException {
        PaymentEntity payment = payonlineFacade.createPayment(paymentRequest.getSum());

        PayonlineConfig config = payonlineFacade.getPayonlineConfig();

        String amount = decimalFormat.format(paymentRequest.getSum());
        String securityKey = payonlineFacade.signPaymentRequest(payment.getId().toString(), amount);

        return new PayonlinePaymentResponse(payment.getId().toString(), config.getMerchantId(), amount,
                config.getCardUrl(), securityKey);
    }

    @Path("verification")
    @POST
    public PayonlinePaymentResponse verification(PaymentRequest paymentRequest)
            throws AuthenticationException, ObjectNotFoundException {
        String orderId = UUID.randomUUID().toString();

        PayonlineConfig config = payonlineFacade.getPayonlineConfig();
        String securityKey = payonlineFacade.signVerificationRequest(orderId);

        return new PayonlinePaymentResponse(orderId, config.getVerificationMerchantId(), orderId, config.getVerificationUrl(),
                securityKey);
    }

    @Path("verification/{guid}")
    @POST
    public Response checkAmount(@PathParam("guid") String guid, PayonlineCheckAmountRequest checkAmountRequest)
            throws AuthenticationException, ObjectNotFoundException {
        boolean amountValid = payonlineFacade.checkAmount(guid, checkAmountRequest.getAmount());

        return amountValid ? Response.ok().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Path("verification/{guid}")
    @DELETE
    public Response deleteVerification(@PathParam("guid") String guid)
            throws AuthenticationException, ObjectNotFoundException {
        payonlineFacade.deleteVerification(guid);

        return Response.ok().build();
    }
}
