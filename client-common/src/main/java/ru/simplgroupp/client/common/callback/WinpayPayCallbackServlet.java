package ru.simplgroupp.client.common.callback;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.dao.interfaces.PaymentDAO;
import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.ejb.plugins.payment.WinpayPayPluginConfig;
import ru.simplgroupp.interfaces.ActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.plugins.payment.WinpayPayBeanLocal;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Winpay
 * Запрос на проверку доступности выплаты у партнера
 */
public class WinpayPayCallbackServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WinpayPayCallbackServlet.class);

    private static final String ID_PARAMETER = "ID";

    private static final String DT_PARAMETER = "DT";

    private static final String AMOUNT_PARAMETER = "AMOUNT";

    private static final String RESULT_PARAMETER = "RESULT";

    private static final String DESTINATION_PARAMETER = "DESTINATION";

    private static final String HASH_PARAMETER = "HASH";

    @EJB
    private transient ActionProcessorBeanLocal actionProcessor;

    @EJB
    private PaymentDAO paymentDAO;

    private String secretKey;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ActionContext actionContext = actionProcessor.createActionContext(null, true);
        PluginExecutionContext winpayExecutionContext = PluginExecutionContext.createExecutionContext(actionContext,
                WinpayPayBeanLocal.SYSTEM_NAME);

        WinpayPayPluginConfig winpayWinpayPayPluginConfig = (WinpayPayPluginConfig) winpayExecutionContext.getPluginConfig();
        secretKey = winpayWinpayPayPluginConfig.getSecretKey();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder parametersInfo = new StringBuilder("{");
        for (String parameterName : request.getParameterMap().keySet()) {
            parametersInfo.append(parameterName).append(": ");
            String[] parameterValues = request.getParameterMap().get(parameterName);
            for (String parameterValue : parameterValues) {
                parametersInfo.append(parameterValue).append("; ");
            }
        }
        parametersInfo.append("}");
        logger.info("Winpay pay callback: " + parametersInfo);

        if (!checkSign(request.getParameter(ID_PARAMETER) + request.getParameter(DT_PARAMETER) +
                        request.getParameter(AMOUNT_PARAMETER) + request.getParameter(RESULT_PARAMETER) +
                        request.getParameter(DESTINATION_PARAMETER) + secretKey, request.getParameter(HASH_PARAMETER))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.getWriter().println("OK");
    }

    private boolean checkSign(String data, String sign) {
        return sign.equals(DigestUtils.md5Hex(data));
    }
}
