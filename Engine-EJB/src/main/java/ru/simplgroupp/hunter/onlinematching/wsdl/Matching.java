
package ru.simplgroupp.hunter.onlinematching.wsdl;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "Matching", targetNamespace = "http://www.mclsoftware.co.uk/HunterII/WebServices", wsdlLocation = "https://nh-test.rb-ei.com/OnlineMatchingService/OnlineMatching.asmx?wsdl")
public class Matching
    extends Service
{

    private final static URL MATCHING_WSDL_LOCATION;
    private final static WebServiceException MATCHING_EXCEPTION;
    private final static QName MATCHING_QNAME = new QName("http://www.mclsoftware.co.uk/HunterII/WebServices", "Matching");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://nh-test.rb-ei.com/OnlineMatchingService/OnlineMatching.asmx?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        MATCHING_WSDL_LOCATION = url;
        MATCHING_EXCEPTION = e;
    }

    public Matching() {
        super(__getWsdlLocation(), MATCHING_QNAME);
    }

    public Matching(WebServiceFeature... features) {
        super(__getWsdlLocation(), MATCHING_QNAME, features);
    }

    public Matching(URL wsdlLocation) {
        super(wsdlLocation, MATCHING_QNAME);
    }

    public Matching(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, MATCHING_QNAME, features);
    }

    public Matching(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Matching(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns MatchingSoap
     */
    @WebEndpoint(name = "MatchingSoap")
    public MatchingSoap getMatchingSoap() {
        return super.getPort(new QName("http://www.mclsoftware.co.uk/HunterII/WebServices", "MatchingSoap"), MatchingSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MatchingSoap
     */
    @WebEndpoint(name = "MatchingSoap")
    public MatchingSoap getMatchingSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.mclsoftware.co.uk/HunterII/WebServices", "MatchingSoap"), MatchingSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MATCHING_EXCEPTION!= null) {
            throw MATCHING_EXCEPTION;
        }
        return MATCHING_WSDL_LOCATION;
    }

}
