package org.admnkz.crypto.app;

import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLSecurityContext {
    KeyManagerFactory sslKeyManager;
    TrustManagerFactory sslTrustManager;
    SecureRandom sslSecureRandom;

    PrivateKey myPrivKey = null;
    List<Certificate> myChain = null;

    X509Certificate serverCert;

    public KeyManagerFactory getSslKeyManager() {
        return sslKeyManager;
    }

    public TrustManagerFactory getSslTrustManager() {
        return sslTrustManager;
    }

    public PrivateKey getMyPrivKey() {
        return myPrivKey;
    }

    public List<Certificate> getMyChain() {
        return myChain;
    }

    /**
     * Сертификат сервера, с которым связываемся по SSL
     * @return
     */
    public X509Certificate getServerCert() {
        return serverCert;
    }

    public SecureRandom getSslSecureRandom() {
        return sslSecureRandom;
    }
}
