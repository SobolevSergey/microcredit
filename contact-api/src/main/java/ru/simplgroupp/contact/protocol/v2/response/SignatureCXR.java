package ru.simplgroupp.contact.protocol.v2.response;

import javax.xml.bind.annotation.*;

/**
 * Created by aro on 15.09.2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SignatureCXR {
    //<SIGNATURE ALGO="ECR3410">MIAGCSqGSIb3DQEHAqCAMIIBNAIBATEOMAwGCisGAQQBrVkBAgEwgAYJKoZIhvcNAQcBAAAxggEOMIIBCgIBATCBmzCBjTELMAkGA1UEBhMCUlUxDzANBgNVBAcTBk1vc2NvdzEUMBIGA1UEChMLUlVTU0xBVkJBTksxETAPBgNVBAsTCFNlY3VyaXR5MRswGQYDVQQDExJBZG1pbmlzdHJhdG9yIENBIDcxJzAlBgkqhkiG9w0BCQEWGHNlY3VyaXR5QHJ1c3NsYXZiYW5rLmNvbQIJAWYCHb0BCgEdMA4GCisGAQQBrVkBAgEFADAOBgorBgEEAa1ZAQYCBQAERzBFAiEA3Uqu2f84Wt2ahrTL2di/13L19ZmAEPfaiw2Ny56rdVUCIC8iXLn5SeTsKCEZE0qUxf16/pCqHECnvezRwJvkgSZWAAAAAA==</SIGNATURE>
    @XmlAttribute(name = "ALGO")
    private String algo;
    @XmlValue
    private String value;

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
