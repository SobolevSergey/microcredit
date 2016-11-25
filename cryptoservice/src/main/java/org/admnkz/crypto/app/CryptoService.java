package org.admnkz.crypto.app;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Null;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

import org.admnkz.common.IOptions;
import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.data.Certificate;
import org.admnkz.crypto.data.Settings;
import org.admnkz.crypto.data.Status;
import org.admnkz.crypto.data.SubjectType;
import org.admnkz.crypto.requestspep.SignatureTool;
import org.admnkz.crypto.requestspep.VerificationResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.token.X509Security;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.asymmetric.ec.KeyPairGenerator;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.CryptoPro.CAdES.EnvelopedSignature;
import ru.CryptoPro.Crypto.CryptoProvider;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CMSVersion;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CertificateChoices;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.CertificateSet;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentEncryptionAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.ContentType;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.DigestAlgorithmIdentifiers;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.EncapsulatedContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.EncryptedContent;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.EncryptedContentInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.EncryptedKey;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.EnvelopedData;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.IssuerAndSerialNumber;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.KeyEncryptionAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.KeyTransRecipientInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.RecipientIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.RecipientInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.RecipientInfos;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignatureAlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignatureValue;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignedData;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerIdentifier;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfo;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.SignerInfos;
import ru.CryptoPro.JCP.ASN.Gost28147_89_EncryptionSyntax.Gost28147_89_EncryptedKey;
import ru.CryptoPro.JCP.ASN.Gost28147_89_EncryptionSyntax.Gost28147_89_IV;
import ru.CryptoPro.JCP.ASN.Gost28147_89_EncryptionSyntax.Gost28147_89_ParamSet;
import ru.CryptoPro.JCP.ASN.Gost28147_89_EncryptionSyntax.Gost28147_89_Parameters;
import ru.CryptoPro.JCP.ASN.Gost28147_89_EncryptionSyntax._Gost28147_89_EncryptionSyntaxValues;
import ru.CryptoPro.JCP.ASN.GostR3410_EncryptionSyntax.GostR3410_KeyTransport;
import ru.CryptoPro.JCP.ASN.GostR3410_EncryptionSyntax.GostR3410_TransportParameters;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.AlgorithmIdentifier;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateSerialNumber;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Name;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.RDNSequence;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.SubjectPublicKeyInfo;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.Key.GostPublicKey;
import ru.CryptoPro.JCP.KeyStore.HDImage.HDImageStore;
import ru.CryptoPro.JCP.params.AlgIdInterface;
import ru.CryptoPro.JCP.params.AlgIdSpec;
import ru.CryptoPro.JCP.params.CryptParamsSpec;
import ru.CryptoPro.JCP.params.OID;
import ru.CryptoPro.JCP.params.ParamsInterface;
import ru.CryptoPro.JCP.spec.GostCipherSpec;
import ru.CryptoPro.JCP.tools.Array;
import ru.signalcom.crypto.cms.SignedDataGenerator;
import ru.signalcom.crypto.provider.PKCS8EncryptedPrivateKeySpec;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.security.auth.x500.X500Principal;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.CertStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

@TransactionManagement(TransactionManagementType.CONTAINER)
@Stateless
public class CryptoService implements ICryptoService, IRemoteCryptoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoService.class.getName());

    @PersistenceContext(unitName = "CryptoPU")
    protected EntityManager em;

    @EJB
    protected IOptions options;
    @EJB
    protected SecureRandomManager srManager;

    private Map<String, CertificateFactory> certFactories = new HashMap<String, CertificateFactory>(1);
    private MessageDigest mdSha1;

    private XPathFactory xpFactory;
    private XPathExpression xpX509Cert;
    private XPathExpression xpWsuCert;
    private XPathExpression xpX509Data;
    private XPathExpression xpSignature;
    private XPathExpression xpKeyInfo;


    private SignatureTool spepServ;

    private void checkInit() {
        if (mdSha1 != null)
            return;
        org.apache.xml.security.Init.init();
        xpFactory = XPathFactory.newInstance();
//		com.sun.org.apache.xml.internal.security.Init.init();

        try {
            xpWsuCert = xpFactory.newXPath().compile("//*[@wsu:Id='SenderCertificate']");
            xpX509Cert = xpFactory.newXPath().compile("//ds:X509Certificate");
            xpX509Data = xpFactory.newXPath().compile("//ds:X509Data");
            xpSignature = xpFactory.newXPath().compile("//ds:Signature");
            xpKeyInfo = xpFactory.newXPath().compile("//ds:KeyInfo");
/*			
			xpSignature = xpFactory.newXPath().compile("//ds:Signature");
			xpKeyInfo = xpFactory.newXPath().compile("//ds:KeyInfo");
			xpX509Data = xpFactory.newXPath().compile("//ds:X509Data");
			xpWSec = xpFactory.newXPath().compile("//wsse:Security");
			xpWBST = xpFactory.newXPath().compile("//wsse:BinarySecurityToken[1]");
*/
        } catch (XPathExpressionException e1) {
            LOGGER.error(e1.getMessage(), e1);
            xpWsuCert = null;
            xpX509Cert = null;
            xpX509Data = null;
            xpSignature = null;
            xpKeyInfo = null;
            xpFactory = null;
/*			
			xpSignature = null;
			xpKeyInfo = null;
			xpX509Data = null;
			xpWSec = null;
			xpWBST = null;
*/
            return;
        }

        try {
            mdSha1 = MessageDigest.getInstance("SHA1");
            ru.CryptoPro.CAdES.tools.Utility.initJCPAlgorithms();

//			secEngine = new WSSecurityEngine();
/*			
			Properties props = new Properties();
			props.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
			props.put("org.apache.ws.security.crypto.merlin.cert.provider", "SC");
			crypto = CryptoFactory.getInstance(props);
			crypto.setCertificateFactory("org.apache.ws.security.components.crypto.Merlin", certFactory);
*/
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
//			certFactory = null;
            mdSha1 = null;
        }

    }

    private CertificateFactory retrieveCertFactory(String providerName) {
        CertificateFactory cf = certFactories.get(providerName);
        if (cf == null) {
            try {
                if (StringUtils.isEmpty(providerName))
                    cf = CertificateFactory.getInstance("X.509");
                else {
                    try {
                        cf = CertificateFactory.getInstance("X.509", providerName);
                    } catch (Exception ex) {
                        cf = CertificateFactory.getInstance("X.509");
                    }
                }
                certFactories.put(providerName, cf);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return cf;
    }

    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Element signXML(Element esource, String setid) throws CryptoException {
        Settings sets = getSettings(setid);
        return signXML(esource, sets);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Element signXML(Element esource, String setid, String defRefId) throws CryptoException {
        Settings sets = getSettings(setid);
        return signXML(esource, sets, defRefId);
    }

    /**
     * Подписывает XMLDSig указанный элемент и возвращает элемент ds:Signature. Элемент никуда не присоединён.
     *
     * @param esource - элемент для подписания
     * @throws CryptoException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Element signXML(Element esource, Settings sets) throws CryptoException {
        return internalsignXML(esource, sets, "AppData1");
    }

    /**
     * Подписывает XMLDSig указанный элемент и возвращает элемент ds:Signature. Элемент никуда не присоединён.
     *
     * @param esource  - элемент для подписания
     * @param defRefId - #id reference по умолчанию
     * @throws CryptoException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Element signXML(Element esource, Settings sets, String defRefId) throws CryptoException {
        return internalsignXML(esource, sets, defRefId);
    }

    private Element internalsignXML(Element esource, Settings sets, String defRefId) throws CryptoException {
        checkInit();

        Certificate cc = sets.getCertificate();

        byte[] cbody;
        cbody = Base64.decodeBase64(cc.getBody());

        Map<String, String> mcaps = getProviderCapabities(sets.getProviderName());
        X509Certificate xcert = makeCert(cbody, cc.getProviderName());

        try {
            byte[] cpriv = null;
            if (cc.hasPrivateKey())
                cpriv = Base64.decodeBase64(cc.getPrivateKey());

            SecureRandom random = retrieveSecureRandom(cc.getId(), sets, mcaps);

            String refid = esource.getAttribute("Id");

            if (StringUtils.isEmpty(refid)) {
                refid = defRefId;
                if (!StringUtils.isEmpty(refid)) {
                    esource.setAttribute("Id", refid);
                    esource.setIdAttribute("Id", true);
                }
            }

            Provider xmlDP = (Provider) Class.forName(sets.getXmlDSigProviderName()).newInstance();
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", xmlDP);

            List<Transform> transformList = new ArrayList<Transform>();
            Transform transform = fac.newTransform(Transform.ENVELOPED, (XMLStructure) null);
            Transform transformC14N = fac.newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS, (XMLStructure) null);
            transformList.add(transform);
            transformList.add(transformC14N);

            Reference ref = fac.newReference((StringUtils.isEmpty(refid)) ? "" : "#" + refid, fac.newDigestMethod(sets.getXmlDigestAlg(), null), transformList, null, null);
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
                            (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(sets.getXmlSignatureAlg(), null),
                    Collections.singletonList(ref));

            // Prepare key information to verify signature in future on other side
            KeyInfoFactory kif = fac.getKeyInfoFactory();
            X509Data x509d = kif.newX509Data(Collections.singletonList(xcert));
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

            // Create signature and sign by private key
            PrivateKey pvk1 = decodePrivateKey(cpriv, cc.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), cc.getPrivateKeyPassword());
            javax.xml.crypto.dsig.XMLSignature sig = fac.newXMLSignature(si, ki);
            DOMSignContext signContext = new DOMSignContext(pvk1, esource);
            signContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");
            signContext.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", Security.getProvider(sets.getProviderName()));
            signContext.setProperty("org.jcp.xml.dsig.internal.dom.SecureRandom", random);
            signContext.setProperty("org.jcp.xml.dsig.internal.dom.DigestProvider", Security.getProvider(sets.getDigestProviderName()));
            sig.sign(signContext);

            Element edsig = (Element) CryptoUtils.findNodeByNameNS((Element) signContext.getParent(), "ds", "Signature");
            // TODO
            return edsig;
        } catch (Exception ex) {
            throw new CryptoException("Не удалось создать подпись XMLDsig", ex);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SOAPMessage signXMLAsDoc(SOAPMessage source, String ns, String localName, String setid) throws CryptoException {
        Settings sets = getSettings(setid);
        return internalSignXMLAsDoc(source, ns, localName, sets);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SOAPMessage signXMLAsDoc(SOAPMessage source, String ns, String localName, Settings sets) throws CryptoException {
        return internalSignXMLAsDoc(source, ns, localName, sets);
    }

    private SOAPMessage internalSignXMLAsDoc(SOAPMessage source, String ns, String localName, Settings sets) throws CryptoException {
        try {
            String ssource = CryptoUtils.SOAPMessageToString(source);
            final Document docSource = CryptoUtils.createDocument(ssource);
            Element esource = (Element) CryptoUtils.findNodeByNameNS(docSource, ns, localName);

            final Document docSign = CryptoUtils.createDocument();
            docSign.appendChild(docSign.adoptNode(esource.cloneNode(true)));
            Element edsig = internalsignXML(docSign.getDocumentElement(), sets, "");
//            boolean bValid = verifyXML(docSign.getDocumentElement(), sets, true);

            esource.appendChild(docSource.adoptNode(edsig.cloneNode(true)));
//            bValid = verifyXML(esource, sets, true);

            return CryptoUtils.getSOAPMessageFromDocument(docSource);
        } catch (Exception ex) {
            throw new CryptoException("Не удалось создать подпись XMLDsig", ex);
        }
    }

    private Element internalsignXMLAsDoc(Element esource, Settings sets, String defRefId) throws CryptoException {
        checkInit();

        Certificate cc = sets.getCertificate();

        byte[] cbody;
        cbody = Base64.decodeBase64(cc.getBody());

        Map<String, String> mcaps = getProviderCapabities(sets.getProviderName());
        X509Certificate xcert = makeCert(cbody, cc.getProviderName());

        try {
            byte[] cpriv = null;
            if (cc.hasPrivateKey())
                cpriv = Base64.decodeBase64(cc.getPrivateKey());

            SecureRandom random = retrieveSecureRandom(cc.getId(), sets, mcaps);

            String refid = esource.getAttribute("Id");

            if (StringUtils.isEmpty(refid)) {
                refid = defRefId;
                if (!StringUtils.isEmpty(refid)) {
                    esource.setAttribute("Id", refid);
                    esource.setIdAttribute("Id", true);
                }
            }

            Document docSource = esource.getOwnerDocument();
            Document signDoc = CryptoUtils.createDocument((Element) esource.cloneNode(true));
/*            
            Element ndClone = (Element) signDoc.adoptNode(esource.cloneNode(true));
            signDoc.appendChild(ndClone);
*/
            Provider xmlDP = (Provider) Class.forName(sets.getXmlDSigProviderName()).newInstance();
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", xmlDP);

            List<Transform> transformList = new ArrayList<Transform>();
            Transform transform = fac.newTransform(Transform.ENVELOPED, (XMLStructure) null);
            Transform transformC14N = fac.newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS, (XMLStructure) null);
            transformList.add(transform);
            transformList.add(transformC14N);

            Reference ref = fac.newReference((StringUtils.isEmpty(refid)) ? "" : "#" + refid, fac.newDigestMethod(sets.getXmlDigestAlg(), null), transformList, null, null);
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
                            (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(sets.getXmlSignatureAlg(), null),
                    Collections.singletonList(ref));

            // Prepare key information to verify signature in future on other side
            KeyInfoFactory kif = fac.getKeyInfoFactory();
            X509Data x509d = kif.newX509Data(Collections.singletonList(xcert));
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

            // Create signature and sign by private key
            PrivateKey pvk1 = decodePrivateKey(cpriv, cc.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), cc.getPrivateKeyPassword());
            javax.xml.crypto.dsig.XMLSignature sig = fac.newXMLSignature(si, ki);
            DOMSignContext signContext = new DOMSignContext(pvk1, signDoc);
            signContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");
            signContext.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", Security.getProvider(sets.getProviderName()));
            signContext.setProperty("org.jcp.xml.dsig.internal.dom.SecureRandom", random);
            signContext.setProperty("org.jcp.xml.dsig.internal.dom.DigestProvider", Security.getProvider(sets.getDigestProviderName()));
            sig.sign(signContext);
            Element edsig = (Element) sig.getObjects().get(0);

            // добавление в нужный узел исходного документа узла подписи
            esource.appendChild(docSource.adoptNode(edsig.cloneNode(true)));

            // Element edsig = (Element) CryptoUtils.findNodeByNameNS((Element) signContext.getParent(), "ds","Signature");
            edsig = (Element) CryptoUtils.findNodeByNameNS((Element) esource, "ds", "Signature");
            // TODO
            return edsig;
        } catch (Exception ex) {
            throw new CryptoException("Не удалось создать подпись XMLDsig", ex);
        }
    }

    protected SOAPMessage signSOAPMessage(SOAPMessage mf, X509Certificate cert, byte[] privkey, String privKeyAlias, SecureRandom random, Settings sets)
            throws SOAPException, WSSecurityException, NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, MarshalException, XMLSignatureException, TransformerException, TransformationException, CryptoException, InstantiationException, IllegalAccessException, ClassNotFoundException, XPathExpressionException, CanonicalizationException, InvalidCanonicalizerException {
        mf.getSOAPPart().getEnvelope().addNamespaceDeclaration("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        mf.getSOAPPart().getEnvelope().addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        mf.getSOAPPart().getEnvelope().addNamespaceDeclaration("ds", "http://www.w3.org/2000/09/xmldsig#");
        mf.getSOAPBody().setAttributeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu:Id", "body");

        WSSecHeader header = new WSSecHeader();
        header.setActor("http://smev.gosuslugi.ru/actors/smev");
        header.setMustUnderstand(false);

        Element sec = header.insertSecurityHeader(mf.getSOAPPart());
        Document doc = mf.getSOAPPart().getEnvelope().getOwnerDocument();

        Element token = (Element) sec.appendChild(doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse:BinarySecurityToken"));
        token.setAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        token.setAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
        token.setAttribute("wsu:Id", "CertId");

        Provider xmlDSigProvider = (Provider) Class.forName(sets.getXmlDSigProviderName()).newInstance();

        // Преобразования над документом.
        final Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", xmlDSigProvider);

        List<Transform> transformList = new ArrayList<Transform>();
        Transform transform = fac.newTransform(Transform.ENVELOPED, (XMLStructure) null);
        Transform transformC14N = fac.newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS, (XMLStructure) null);
        transformList.add(transform);
        transformList.add(transformC14N);

        // Ссылка на подписываемые данные.
        Reference ref = fac.newReference("#body", fac.newDigestMethod("http://www.w3.org/2001/04/xmldsig-more#gostr3411", null),
                transformList, null, null);

        // Make link to signing element
        SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
                        (C14NMethodParameterSpec) null),
                fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411", null),
                Collections.singletonList(ref));

        // Prepare key information to verify signature in future on other side
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509Data x509d = kif.newX509Data(Collections.singletonList(cert));
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

        // Create signature and sign by private key
        javax.xml.crypto.dsig.XMLSignature sig = fac.newXMLSignature(si, ki);

        Element l = doc.getElementById("body");
        PrivateKey privateKey = decodePrivateKey(privkey, privKeyAlias, sets.getProviderName(), sets.getPrivateKeyAlg(), sets.getCertificate().getPrivateKeyPassword());
        DOMSignContext signContext = new DOMSignContext(privateKey, token);
        signContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");
        signContext.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", Security.getProvider(sets.getProviderName()));
        signContext.setProperty("org.jcp.xml.dsig.internal.dom.SecureRandom", random);
        signContext.setProperty("org.jcp.xml.dsig.internal.dom.DigestProvider", Security.getProvider(sets.getDigestProviderName()));
        sig.sign(signContext);

        // Insert signature node in document

        Element sigE = evalSingleElement(javax.xml.crypto.dsig.XMLSignature.XMLNS, "Signature", (Element) signContext.getParent());

        Element keyE = evalSingleElement(javax.xml.crypto.dsig.XMLSignature.XMLNS, "KeyInfo", sigE);
        Element elmcert = evalSingleElement(javax.xml.crypto.dsig.XMLSignature.XMLNS, "X509Certificate", keyE);
        token.appendChild(doc.createTextNode(elmcert.getFirstChild().getNodeValue()));
        Element elmd = evalSingleElement(javax.xml.crypto.dsig.XMLSignature.XMLNS, "X509Data", keyE);
        keyE.removeChild(elmd);
        NodeList chl = keyE.getChildNodes();

        for (int i = 0; i < chl.getLength(); i++) {
            keyE.removeChild(chl.item(i));
        }

        Node str = keyE.appendChild(doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse:SecurityTokenReference"));
        Element strRef = (Element) str.appendChild(doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse:Reference"));

        strRef.setAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
        strRef.setAttribute("URI", "#CertId");
        header.getSecurityHeader().appendChild(sigE);

        return mf;
    }

    private Node evalSingleNode(String ns, String tagName, Element parent) {
        NodeList ndl = parent.getElementsByTagNameNS(ns, tagName);
        if (ndl != null && ndl.getLength() > 0) {
            return (Element) ndl.item(0);
        } else
            return null;
    }

    private Element evalSingleElement(String ns, String tagName, Element parent) {
        Node nd = evalSingleNode(ns, tagName, parent);
        return (Element) nd;
    }

    private void removeExtraAttributes(Element sec, String aname) {
        NamedNodeMap nmp = sec.getAttributes();
        while (nmp.getNamedItem(aname) != null) {
            sec.removeAttribute(aname);
            nmp = sec.getAttributes();
        }
    }

    private Node findNodeByNameNS(Node parent, String ns, String name) {
        NodeList lst = parent.getChildNodes();
        for (int i = 0; i < lst.getLength(); i++) {
            Node nd = lst.item(i);
            if (!(nd instanceof Element))
                continue;

            Element elmND = (Element) nd;
            if (ns == null) {
                if (name.equals(elmND.getLocalName())) {
                    return elmND;
                }
            } else {
                if ((ns + ":" + name).equals(elmND.getTagName())) {
                    return elmND;
                }
            }
            if (elmND.hasChildNodes()) {
                Node ndf = findNodeByNameNS(elmND, ns, name);
                if (ndf != null)
                    return ndf;
            }
        }
        return null;

    }

    public List<Settings> listSettings(String templateID) {
        String sql = "from org.admnkz.crypto.app.Settings where id like :tpl";
        Query qry = em.createQuery(sql);
        qry.setParameter("tpl", templateID);
        List<Settings> lst = qry.getResultList();
        return lst;
    }

    public Settings getSettings(String setID) {
        return em.find(Settings.class, setID);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifyCertificate(String certID) throws CryptoException {
        Certificate cert = getCertificate(certID);
        return verifyCertificate(cert);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifyCertificate(Certificate cert) throws CryptoException {
        checkSpep();

        VerificationResult vres = spepServ.getSignatureToolSoap().verifyCertificate(Base64.decodeBase64(cert.getBody().getBytes()));
        cert.setValidationCode(vres.getCode());
        cert.setValidationMessage(vres.getDescription());
        cert.setValidationLastTime(new Date());

        switch (vres.getCode()) {
            case SignatureTool.VER_INTERNAL_ERROR:
            case SignatureTool.VER_INVALID_SERVICE_CONFIG:
            case SignatureTool.VER_REVOK_UNKNOWN:
                throw new CryptoException("Не удалось проверить сертификат " + cert.getId());
            case SignatureTool.VER_OK:
                cert.setStatus(Status.ACTIVE);
                break;
            default:
                cert.setStatus(Status.OUT_OF_DATE);
                break;
        }

        return (cert.getStatus().equals(Status.ACTIVE));
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifyCertificate(byte[] xcert) throws CryptoException {
        checkSpep();

        VerificationResult vres = spepServ.getSignatureToolSoap().verifyCertificate(xcert);

        switch (vres.getCode()) {
            case SignatureTool.VER_INTERNAL_ERROR:
            case SignatureTool.VER_INVALID_SERVICE_CONFIG:
            case SignatureTool.VER_REVOK_UNKNOWN:
                throw new CryptoException("Не удалось проверить сертификат");
            case SignatureTool.VER_OK:
                return true;
            default:
                return false;
        }

    }

    protected void checkSpep() throws CryptoException {
        if (spepServ != null)
            return;

        SignatureTool.setWsdlLocation(options.getValue("cryptoservice.spep.url"));
        spepServ = new SignatureTool();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifySOAP(SOAPMessage axisMessage, String setID, boolean bSignatureOnly) throws CryptoException {
        Settings sets = getSettings(setID);
        try {
            return verifySOAPMessage(axisMessage, sets, bSignatureOnly);
        } catch (Exception e) {
            throw new CryptoException("Не удалось проверить сообщение", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifySOAP(String signedSOAP, String setID, boolean bSignatureOnly) throws CryptoException {
        Settings sets = getSettings(setID);
        return verifySOAP(signedSOAP, sets, bSignatureOnly);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifySOAP(String signedSOAP, Settings sets, boolean bSignatureOnly) throws CryptoException {
        SOAPMessage axisMessage = CryptoUtils.getSOAPMessageFromString(signedSOAP);
        try {
            return verifySOAPMessage(axisMessage, sets, bSignatureOnly);
        } catch (Exception e) {
            throw new CryptoException("Не удалось проверить сообщение", e);
        }
    }

    public void removeCertificate(String certID, boolean bRemoveChilden) {
        Certificate acert = getCertificate(certID);
        if (acert.getSubjectType() != SubjectType.END_USER.getID()) {
            if (bRemoveChilden) {
                Query qry = em.createNamedQuery("removeChilds");
                qry.setParameter("signerID", acert.getId());
                qry.executeUpdate();
            } else {
                Query qry = em.createNamedQuery("nullChilds");
                qry.setParameter("signerID", acert.getId());
                qry.executeUpdate();
            }
        }
        em.remove(acert);
    }

    @Override
    public boolean verifyXML(Element source, String setID, boolean bSignatureOnly)
            throws CryptoException {
        Settings sets = getSettings(setID);
        return verifyXML(source, sets, bSignatureOnly);
    }

    @Override
    public boolean verifyXML(Element source, Settings sets, boolean bSignatureOnly)
            throws CryptoException {
        boolean bValidAll = true;
        try {
            Provider xmlDP = (Provider) Class.forName(sets.getXmlDSigProviderName()).newInstance();
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", xmlDP);

            // ищем сертификат
            Element r = (Element) CryptoUtils.findNodeByNameNS(source, null, "X509Certificate");
            if (r == null)
                throw new CryptoException("Ошибочный формат подписи. Не найден заголовок X509Certificate");

            String certString = r.getTextContent();
            byte[] bxcert = Base64.decodeBase64(certString);
            final X509Certificate cert = (X509Certificate) retrieveCertFactory(sets.getProviderName()).generateCertificate(new ByteArrayInputStream(bxcert));

            if (cert == null)
                throw new CryptoException("Cannot find certificate to verify signature");

            // Set public key
            Element elmSig = (Element) CryptoUtils.findNodeByNameNS(source, null, "Signature");
            DOMValidateContext valContext = new DOMValidateContext(KeySelector.singletonKeySelector(cert.getPublicKey()), elmSig);
            valContext.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", Security.getProvider(sets.getProviderName()));
            valContext.setProperty("org.jcp.xml.dsig.internal.dom.DigestProvider", Security.getProvider(sets.getDigestProviderName()));

            javax.xml.crypto.dsig.XMLSignature signature = fac.unmarshalXMLSignature(valContext);

            // Verify signature
            boolean bValid = signature.validate(valContext);

            if (!bSignatureOnly) {
                // Verify certificate
                bValid = bValid && verifyCertificate(bxcert);
            }

            if (!bValid) {
                bValidAll = false;
                return bValidAll;
            }

            return bValidAll;
        } catch (Throwable ex) {
            throw new CryptoException("Не удалось проверить подпись", ex);
        }
    }

    protected boolean verifySOAPMessage(SOAPMessage axisMessage, Settings sets, boolean bSignatureOnly)
            throws CryptoException, SOAPException, TransformerException, WSSecurityException, CertificateException, MarshalException, XMLSignatureException, InstantiationException, IllegalAccessException, ClassNotFoundException, XPathExpressionException {
        checkInit();

        Provider xmlDP = (Provider) Class.forName(sets.getXmlDSigProviderName()).newInstance();
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", xmlDP);

        // Extract some nodes to verify document
        Document doc = axisMessage.getSOAPPart().getEnvelope().getOwnerDocument();
        final Element wssecontext = doc.createElementNS(null, "namespaceContext");
        wssecontext.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + "wsse".trim(), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
//        NodeList secnodeList = XPathAPI.selectNodeList(doc.getDocumentElement(), "//wsse:Security");
//        NodeList secnodeList = (NodeList) xpWSec.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
//        List<Node> secnodeList = findNodeListByNameNS(doc.getDocumentElement(), "wsse", "Security");
        NodeList secnodeList = doc.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");

        Element r = null;
        Element el = null;
        boolean bValidAll = true;
        if (secnodeList != null && secnodeList.getLength() > 0) {
            // проходим по всем заголовкам wsse:Security
            for (int i = 0; i < secnodeList.getLength(); i++) {
                el = (Element) secnodeList.item(i);
                String actorAttr = el.getAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "actor");
                if (actorAttr != null) {
//        			r = (Element)XPathAPI.selectSingleNode(el, "//wsse:BinarySecurityToken[1]", wssecontext);
//        			r = (Element) xpWBST.evaluate(el, XPathConstants.NODE);
                    r = (Element) CryptoUtils.findNodeByNameNS(el, null, "BinarySecurityToken");
                    if (r == null)
                        throw new CryptoException("Ошибочный формат подписи. Не найден заголовок BinarySecurityToken");

                    final X509Security x509 = new X509Security(r, true);
                    if (x509 == null) {
                        bValidAll = false;
                        break;
                    }

                    // Extract certificate
                    byte[] bxcert = x509.getToken();
                    final X509Certificate cert = (X509Certificate) retrieveCertFactory(sets.getProviderName()).generateCertificate(new ByteArrayInputStream(bxcert));

                    if (cert == null)
                        throw new CryptoException("Cannot find certificate to verify signature");

                    // Get signature node

                    NodeList nl = el.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
//        	       	NodeList nl = doc.getElementsByTagName("ds:Signature");
                    if (nl.getLength() == 0) {
                        throw new CryptoException("Cannot find Signature element");
                    }

                    // Set public key
                    DOMValidateContext valContext = new DOMValidateContext(KeySelector.singletonKeySelector(cert.getPublicKey()), nl.item(0));
                    valContext.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", Security.getProvider(sets.getProviderName()));
                    valContext.setProperty("org.jcp.xml.dsig.internal.dom.DigestProvider", Security.getProvider(sets.getDigestProviderName()));

                    javax.xml.crypto.dsig.XMLSignature signature = fac.unmarshalXMLSignature(valContext);

                    // Verify signature
                    boolean bValid = signature.validate(valContext);

                    if (!bSignatureOnly) {
                        // Verify certificate
                        bValid = bValid && verifyCertificate(bxcert);
                    }

                    if (!bValid) {
                        bValidAll = false;
                        break;
                    }

                }
            }
        }

        return bValidAll;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String signSOAP1(SOAPMessage axisMessage, String settingsID) throws CryptoException {
        Settings sets = getSettings(settingsID);
        return signSOAP1(axisMessage, sets);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String signSOAP1(SOAPMessage axisMessage, Settings sets) throws CryptoException {
        SOAPMessage rMessage = signSOAP(axisMessage, sets);
        String sxml = null;
        try {
            sxml = CryptoUtils.SOAPMessageToString(axisMessage);
        } catch (SOAPException e) {
            throw new CryptoException("Не удалось сохранить подписанное сообщение в виде строки", e);
        }
        return sxml;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String signSOAP(String unsignedSOAP, String settingsID) throws CryptoException {
        Settings sets = getSettings(settingsID);
        return signSOAP(unsignedSOAP, null, sets);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SOAPMessage signSOAP(SOAPMessage unsignedSOAP, String settingsID) throws CryptoException {
        Settings options = getSettings(settingsID);
        return signSOAP(unsignedSOAP, options);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String signSOAP(String unsignedSOAP, Settings sets) throws CryptoException {
        return signSOAP(unsignedSOAP, null, sets);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String signSOAP(String unsignedSOAP, String cid, Settings sets) throws CryptoException {
        SOAPMessage axisMessage = CryptoUtils.getSOAPMessageFromString(unsignedSOAP);
        axisMessage = signSOAP(axisMessage, sets);
        String sxml = null;
        try {
            sxml = CryptoUtils.SOAPMessageToString(axisMessage);
        } catch (SOAPException e) {
            throw new CryptoException("Не удалось сохранить подписанное сообщение в виде строки", e);
        }
        return sxml;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SOAPMessage signSOAP(SOAPMessage axisMessage, Settings sets) throws CryptoException {
        checkInit();

        Certificate cc = sets.getCertificate();

        byte[] cbody;
        cbody = Base64.decodeBase64(cc.getBody());
        X509Certificate acert = makeCert(cbody, cc.getProviderName());

        SecureRandom random = retrieveSecureRandom(cc.getId(), sets, null);

        try {
            byte[] cpriv = null;
            if (cc.hasPrivateKey())
                cpriv = Base64.decodeBase64(cc.getPrivateKey());
            SOAPMessage mres = signSOAPMessage(axisMessage, acert, cpriv, cc.getPrivateKeyPath(), random, sets);
            return mres;
        } catch (Exception e) {
            throw new CryptoException("Не удалось подписать сообщение", e);
        }
    }

    public String getServiceRootPath() {
        return options.getValue("cryptoservice.securerandom.path");
    }

    protected String checkServiceRoot(String forID) throws CryptoException {
        File fdir = null;
        if (forID == null)
            fdir = new File(getServiceRootPath());
        else
            fdir = new File(getServiceRootPath() + forID.toString());
        try {
            FileUtils.forceMkdir(fdir);
            return fdir.getCanonicalPath();
        } catch (IOException e) {
            throw new CryptoException("Не инициализирована служебная папка " + fdir.getAbsolutePath(), e);
        }
    }

    public List<Status> listStatuses() {
        ArrayList<Status> lst = new ArrayList<Status>(4);
        lst.add(Status.UNDEFINED);
        lst.add(Status.ACTIVE);
        lst.add(Status.OUT_OF_DATE);
        lst.add(Status.CLOSED);
        return lst;
    }

    public List<SubjectType> listSubjectTypes() {
        ArrayList<SubjectType> lst = new ArrayList<SubjectType>(3);
        lst.add(SubjectType.END_USER);
        lst.add(SubjectType.SUB_CA);
        lst.add(SubjectType.ROOT_CA);
        return lst;
    }

    public Certificate getCertificate(String aid) {
        return em.find(Certificate.class, aid);
    }

    public Certificate getCertificate(String aid, Set options) {
        Certificate acert = em.find(Certificate.class, aid);
        if (acert!=null) {
            acert.init(options);
        }
        return acert;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] sign(final byte[] data, String optkey) throws CryptoException {
        Settings options = getSettings(optkey);
        return sign(data, options);
    }

    private boolean needSecureRandom(Map<String, String> mcaps) {
        return CAP_VALUE_REQUIRED.equals(mcaps.get(CAP_SECURE_RANDOM));
    }

    private SecureRandom retrieveSecureRandom(String certID, Settings sets, Map<String, String> mcaps)
            throws CryptoException {
        if (mcaps == null)
            mcaps = getProviderCapabities(sets.getProviderName());
        if (!needSecureRandom(mcaps))
            return null;

        try {
            if ("SC".equalsIgnoreCase(sets.getProviderName())) {
                if (certID == null) {
                    return srManager.getRandom(sets);
                } else {
                    return srManager.getRandom(certID, sets);
                }
            } else
                return null;
        } catch (Exception ex) {
        	LOGGER.error("Не удалось инициализаровать SecureRandom для " + certID, ex);
            throw new CryptoException("Не удалось инициализаровать SecureRandom для " + certID, ex);
        }
        // TODO для других провайдеров
    }

    /**
     * Проверяет, подходят ли друг к другу сертификат и закрытый ключ.
     *
     * @param cc
     * @return
     * @throws CryptoException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean checkIsPair(Certificate cc)
            throws CryptoException {
        checkInit();
        Map<String, String> mcaps = getProviderCapabities(cc.getProviderName());

        Settings sets = new Settings();
        sets.setProviderName(cc.getProviderName());
        String[] sigAlgs = CryptoUtils.listSigAlgs(cc.getProviderName());
        sets.setSignatureAlg(sigAlgs[0]);
        String[] pvkAlgs = CryptoUtils.listPrivKeyAlgs(cc.getProviderName());
        sets.setPrivateKeyAlg(pvkAlgs[0]);
        String[] secRands = CryptoUtils.listSecRandAlgs(cc.getProviderName());
        sets.setSecureRandomAlg(secRands[0]);

        // Формирование подписи
        try {
            SecureRandom random = retrieveSecureRandom(cc.getId(), sets, mcaps);
            X509Certificate xcert = makeCert(Base64.decodeBase64(cc.getBody()), cc.getProviderName());

            byte[] data = "test".getBytes();

            byte[] cpriv = null;
            if (cc.hasPrivateKey())
                cpriv = Base64.decodeBase64(cc.getPrivateKey());
            PrivateKey pvk1 = decodePrivateKey(cpriv, cc.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), cc.getPrivateKeyPassword());
            Signature sign = Signature.getInstance(sets.getSignatureAlg(), sets.getProviderName());
            sign.initSign(pvk1, random);
            sign.update(data);
            byte[] signature = sign.sign();

            sign.initVerify(xcert);
            sign.update(data);
            boolean bver = sign.verify(signature);
            return bver;
        } catch (Exception ex) {
            throw new CryptoException("Не удалось проверить соответствие для " + cc.getId(), ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] sign(final byte[] data, Settings options) throws CryptoException {
        checkInit();

        String cid = options.getCertificate().getId();
        Certificate cc = getCertificate(cid);
        Map<String, String> mcaps = getProviderCapabities(options.getProviderName());

        SecureRandom random = null;
        byte[] cpriv = null;
        PrivateKey pvk1 = null;
        // создаём SecureRandom
        if (options.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {

            try {
                random = SecureRandom.getInstance(JCP.CP_RANDOM, CryptoUtils.PROVIDER_CRYPTOPRO);
                pvk1 = decodePrivateKeyJCP(cc.getPrivateKeyAlias(), cc.getPrivateKeyPassword());
            } catch (Exception e) {
                throw new CryptoException("Не удалось создать secure random " + e);
            }
        } else {
            random = retrieveSecureRandom(options.getCertificate().getId(), options, mcaps);
            if (cc.hasPrivateKey())
                cpriv = Base64.decodeBase64(cc.getPrivateKey());
            pvk1 = decodePrivateKey(cpriv, cc.getPrivateKeyPath(), options.getProviderName(), options.getPrivateKeyAlg(), cc.getPrivateKeyPassword());
        }

        //	SecureRandom random = retrieveSecureRandom(cid, options, mcaps);

        // Формирование подписи
        try {

            Signature sign = Signature.getInstance(options.getSignatureAlg(), options.getProviderName());
            sign.initSign(pvk1, random);
            sign.update(data);
            byte[] signature = sign.sign();
            return signature;
        } catch (Exception ex) {
            throw new CryptoException("Не удалось сформировать подпись для " + cc.getId(), ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] convertPrivateKey(Settings setsFrom, Settings setsTo) throws CryptoException {
        checkInit();

        String cid = setsFrom.getCertificate().getId();
        Certificate cc = getCertificate(cid);
        Map<String, String> mcaps = getProviderCapabities(setsFrom.getProviderName());

        SecureRandom random = retrieveSecureRandom(cid, setsFrom, mcaps);

        // Формирование подписи
        try {
            byte[] cpriv = null;
            if (cc.hasPrivateKey())
                cpriv = Base64.decodeBase64(cc.getPrivateKey());
            PrivateKey pvk1 = decodePrivateKey(cpriv, cc.getPrivateKeyPath(), setsFrom.getProviderName(), setsFrom.getPrivateKeyAlg(), cc.getPrivateKeyPassword());

            PrivateKey pvk2 = null;
            KeyFactory keyFac8 = KeyFactory.getInstance("GOST3410DH", "JCP");
            PKCS8EncodedKeySpec pvks = new PKCS8EncodedKeySpec(cpriv);
            pvk2 = keyFac8.generatePrivate(pvks);
            return pvk2.getEncoded();
        } catch (Exception ex) {
            throw new CryptoException("Не удалось сформировать подпись для " + cc.getId(), ex);
        }

    }

    protected PrivateKey decodePrivateKey(byte[] cpriv, String alias, String providerName, String privateKeyAlg, String pvkPassword) throws CryptoException {

        if (CryptoUtils.PROVIDER_CRYPTOPRO.equals(providerName))
            try {
                return decodePrivateKeyJCP(alias, pvkPassword);
            } catch (Exception e) {
            	LOGGER.error("Не удалось прочитать закрытый ключ " + alias + " с помощью " + providerName, e);
                throw new CryptoException("Не удалось прочитать закрытый ключ " + alias + " с помощью " + providerName, e);
            }

        PrivateKey pvk1 = null;

        if (CryptoUtils.PROVIDER_SIGNALCOM.equals(providerName) && "PKCS#8".equals(privateKeyAlg)) {
            try {
                KeyFactory keyFac8 = KeyFactory.getInstance(privateKeyAlg, providerName);
                PKCS8EncryptedPrivateKeySpec pvks = null;
                if (StringUtils.isBlank(pvkPassword)) {
                    pvks = new PKCS8EncryptedPrivateKeySpec(cpriv);
                } else {
                    pvks = new PKCS8EncryptedPrivateKeySpec(cpriv, pvkPassword.toCharArray());
                }
                pvk1 = keyFac8.generatePrivate(pvks);
            } catch (Exception e) {
            	LOGGER.error("Не удалось прочитать закрытый ключ с помощью " + providerName, e);
                throw new CryptoException("Не удалось прочитать закрытый ключ с помощью " + providerName, e);
            }

            return pvk1;
        }

        if (CryptoUtils.PROVIDER_SIGNALCOM.equals(providerName) && "ECGOST3410".equals(privateKeyAlg)) {
            try {
                KeyFactory keyFac8 = KeyFactory.getInstance(privateKeyAlg, providerName);
//				 ECGOST3410PrivateKeySpec  pvks = new  ECGOST3410PrivateKeySpec (cpriv, "SECP256R1");
                PKCS8EncodedKeySpec pvks = null;
                pvks = new PKCS8EncodedKeySpec(cpriv);
                pvk1 = keyFac8.generatePrivate(pvks);
            } catch (Exception e) {
            	LOGGER.error("Не удалось прочитать закрытый ключ с помощью " + providerName, e);
                throw new CryptoException("Не удалось прочитать закрытый ключ с помощью " + providerName, e);
            }

            return pvk1;
        }

        try {
            KeyFactory keyFac8 = KeyFactory.getInstance(privateKeyAlg, providerName);
            PKCS8EncodedKeySpec pvks = new PKCS8EncodedKeySpec(cpriv);
            pvk1 = keyFac8.generatePrivate(pvks);
        } catch (Exception e) {
        	LOGGER.error("Не удалось прочитать закрытый ключ с помощью " + providerName, e);
            throw new CryptoException("Не удалось прочитать закрытый ключ с помощью " + providerName, e);
        }

        return pvk1;
    }

    /**
     * вытаскивает закрытый ключ из хранилища
     * только для работы с крипто-про jcp
     * @param alias       - альяс ключа в хранилище
     * @param pvkPassword - пароль
     * @return - закрытый ключ
     */
    private PrivateKey decodePrivateKeyJCP(String alias, String pvkPassword) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
        PrivateKey pvk1 = null;
        if (StringUtils.isBlank(pvkPassword)) {
            pvkPassword = null;
        }

        KeyStore hdPriv = KeyStore.getInstance(CryptoUtils.JCP_HDSTORE, CryptoUtils.PROVIDER_CRYPTOPRO);
        //смотрим, есть ли путь к HDImageStore в БД
        String dir = options.getValue(CryptoUtils.KEYS_DIR);
        //String dir="c:\\Users\\helen1\\1\\";
        LOGGER.info(dir);
        if (StringUtils.isNotEmpty(dir)) {
            HDImageStore.setDir(dir);
        }
        Key akey = null;

        //ищем ключ в хранилище по альясу и паролю
        if (pvkPassword != null) {
            LOGGER.info("Пытаемся зайти в хранилище");
            hdPriv.load(null, pvkPassword.toCharArray());
            Enumeration e=hdPriv.aliases();
            while (e.hasMoreElements()){
            	String st=e.nextElement().toString();
            	LOGGER.info("Альяс " + st);
            }
            LOGGER.info("Пароль " + pvkPassword);
            LOGGER.info("Алиас " + alias);
            akey = hdPriv.getKey(alias, pvkPassword.toCharArray());
            LOGGER.info("Зашли в хранилище, взяли ключ");
        } else {
            LOGGER.info("Пытаемся зайти в хранилище");
            hdPriv.load(null, null);
            LOGGER.info("Алиас " + alias);
            Enumeration e=hdPriv.aliases();
            while (e.hasMoreElements()){
            	String st=e.nextElement().toString();
            	LOGGER.info("Альяс " + st);
            }
            akey = hdPriv.getKey(alias, null);
            LOGGER.info("Зашли в хранилище, взяли ключ");
        }

        pvk1 = (PrivateKey) akey;
        if (pvk1 != null) {
            LOGGER.info("Ключ нашелся");
        }
        return pvk1;
    }

    /**
     * вытаскивает сертификат из хранилища
     * только для работы с крипто-про jcp
     * @param alias       - альяс ключа в хранилище
     * @param pvkPassword - пароль
     * @return - закрытый ключ
     */
    private X509Certificate decodeCertificateJCP(String alias, String pvkPassword) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
    	X509Certificate xcert = null;
        if (StringUtils.isBlank(pvkPassword)) {
            pvkPassword = null;
        }

        KeyStore hdPriv = KeyStore.getInstance(CryptoUtils.JCP_HDSTORE, CryptoUtils.PROVIDER_CRYPTOPRO);
        //смотрим, есть ли путь к HDImageStore в БД
        String dir = options.getValue(CryptoUtils.KEYS_DIR);
        LOGGER.info(dir);
        if (StringUtils.isNotEmpty(dir)) {
            HDImageStore.setDir(dir);
        }
       
        //ищем сертификат в хранилище по альясу и паролю
        if (pvkPassword != null) {
            LOGGER.info("Пытаемся зайти в хранилище");
            hdPriv.load(null, pvkPassword.toCharArray());
            LOGGER.info("Пароль " + pvkPassword);
            LOGGER.info("Алиас " + alias);
            xcert = (X509Certificate) hdPriv.getCertificate(alias);
            LOGGER.info("Зашли в хранилище, взяли сертификат");
        } else {
            LOGGER.info("Пытаемся зайти в хранилище");
            hdPriv.load(null, null);
            LOGGER.info("Алиас " + alias);
            xcert = (X509Certificate) hdPriv.getCertificate(alias);
            LOGGER.info("Зашли в хранилище, взяли сертификат");
        }

        if (xcert != null) {
            LOGGER.info("Сертификат нашелся");
        }
        return xcert;
    }

      
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verify(final byte[] data, byte[] signature, String optkey, boolean bSignatureOnly) throws CryptoException {
        Settings options = getSettings(optkey);
        return verify(data, signature, options, bSignatureOnly);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verify(final byte[] data, byte[] signature, Settings options, boolean bSignatureOnly) throws CryptoException {
        checkInit();

        String cid = options.getCertificate().getId();
        Certificate cc = getCertificate(cid);
        boolean bres = false;
        try {
            X509Certificate acert = makeCert(Base64.decodeBase64(cc.getBody()), options.getProviderName());
            Signature sign = Signature.getInstance(options.getSignatureAlg(), options.getProviderName());
            sign.initVerify(acert);
            sign.update(data);
            bres = sign.verify(signature);
            if (!bSignatureOnly) {
                bres = bres && verifyCertificate(cc);
                em.persist(cc);
            }
            return bres;
        } catch (Exception ex) {
        	LOGGER.error("Не удалось проверить подпись", ex);
            throw new CryptoException("Не удалось проверить подпись", ex);
        }
    }

 
    private java.security.cert.X509Certificate makeCert(byte[] cbody, String providerName) throws CryptoException {
        java.security.cert.X509Certificate acert = null;
        ByteArrayInputStream stm = new ByteArrayInputStream(cbody);
        BufferedInputStream bis = new BufferedInputStream(stm);
        try {
            try {
                CertificateFactory cf = retrieveCertFactory(providerName);
                acert = (X509Certificate) cf.generateCertificate(bis);
            } finally {
                bis.close();
                stm.close();
            }
        } catch (Exception e) {
        	LOGGER.error("Не удалось загрузить X509-сертификат", e);
            throw new CryptoException("Не удалось загрузить X509-сертификат", e);
        }
        return acert;
    }

    public Map<String, String> getProviderCapabities(String providerName) {
        HashMap<String, String> mp = new HashMap<String, String>(0);
        if (CryptoUtils.PROVIDER_SIGNALCOM.equalsIgnoreCase(providerName)) {
            mp.put(CAP_SECURE_RANDOM, CAP_VALUE_REQUIRED);
            mp.put(CAP_PRIVATE_KEY_EXPORT, CAP_VALUE_SUPPORTS);
        } else if (CryptoUtils.PROVIDER_CRYPTOPRO.equalsIgnoreCase(providerName)) {
            mp.put(CAP_SECURE_RANDOM, CAP_VALUE_REQUIRED);
            mp.put(CAP_PRIVATE_KEY_EXPORT, CAP_VALUE_NEVER);
        } else {
            mp.put(CAP_SECURE_RANDOM, CAP_VALUE_NEVER);
            mp.put(CAP_PRIVATE_KEY_EXPORT, CAP_VALUE_SUPPORTS);
        }
        // TODO
        return mp;
    }

    public Set<String> listDSigAlgorithms() {
        return Security.getAlgorithms("Signature");
    }

    public String[] listDSigProviders() {
        return listProvidersFor("Signature.", false);
    }

    public String[] listProvidersFor(String prefix, boolean bExact) {
        Provider[] provs = Security.getProviders();

        String[] res = new String[provs.length];
        int i = 0;
        for (Provider prov : provs) {
            for (Enumeration e = prov.keys(); e.hasMoreElements(); ) {
                String skey = e.nextElement().toString();
                if (bExact) {
                    if (skey.equals(prefix)) {
                        res[i] = prov.getName();
                        i++;
                        break;
                    }
                } else {
                    if (skey.startsWith(prefix)) {
                        res[i] = prov.getName();
                        i++;
                        break;
                    }
                }
            }
        }
        if (i == 0)
            return new String[0];
        else
            return Arrays.copyOf(res, i);
    }

    protected Certificate findIssuerFor(X509Certificate xcert) throws CryptoException {
        String sql = "from org.admnkz.crypto.data.Certificate where (Status.ID != " + Status.CLOSED.getID().toString() + " ) and (SubjectType in (" + SubjectType.ROOT_CA.getID().toString() + "," + SubjectType.SUB_CA.getID().toString() + " ))";
        Query qry = em.createQuery(sql);
        List<Certificate> lst = qry.getResultList();
        if (lst.size() > 0) {
            for (Certificate cert : lst) {
                X509Certificate xcertIssuer = extractX509Cert(cert);
                if (CryptoUtils.isIssuerFor(xcert, xcertIssuer, cert.getProviderName()))
                    return cert;
            }
            return null;
        } else
            return null;
    }

    protected Certificate findBySubjectDN(String subjectDN) {
        String sql = "from org.admnkz.crypto.data.Certificate c where (SubjectDN = :subjectDN)";
        Query qry = em.createQuery(sql);
        qry.setParameter("subjectDN", subjectDN);
        List<Certificate> lst = qry.getResultList();
        if (lst.size() > 0)
            return lst.get(0);
        else
            return null;
    }

    protected List<Settings> findBySettingsId(String settingsId) {
        String sql = "from org.admnkz.crypto.data.Settings  where (ID like :settingsId)";
        Query qry = em.createQuery(sql);
        qry.setParameter("settingsId", settingsId+"%");
        List<Settings> lst = qry.getResultList();
        return lst;
    }
    
    protected X509Certificate extractX509Cert(Certificate cert) throws CryptoException {
        java.security.cert.X509Certificate acert = makeCert(Base64.decodeBase64(cert.getBody()), cert.getProviderName());
        return acert;
    }

    @Override
    public Certificate addCertificate(Certificate asigner, String sprovname, byte[] cbody, String cprivPath, String privPassword)
            throws Exception {
        checkInit();

        ParseCertResult res = parseCertificate(cbody);

        if (StringUtils.isEmpty(sprovname))
            sprovname = res.providerName;
        if (CryptoUtils.PROVIDER_CRYPTOPRO.equalsIgnoreCase(sprovname) && StringUtils.isBlank(cprivPath)) {
            cprivPath = CryptoUtils.JCP_HDSTORE;
        }
        java.security.cert.X509Certificate acert = res.certificate;

        String sid = res.sha1hash;
        Certificate cc = getCertificate(sid);
        if (cc == null) {
            cc = new Certificate();
            cc.setId(sid);
        }
        cc.setStatus(Status.UNDEFINED);
        cc.setBody(new String(Base64.encodeBase64(cbody)));
        if (!StringUtils.isEmpty(cprivPath))
            cc.setPrivateKeyPath(cprivPath);
        if (!StringUtils.isEmpty(privPassword))
            cc.setPrivateKeyPassword(privPassword);
        cc.setProviderName(sprovname);
        int nType = CryptoUtils.calcCertType(acert);
        cc.setSubjectType(nType);
        cc.setSubjectCN(CryptoUtils.extractCN(acert));
        cc.setSubjectDN(acert.getSubjectX500Principal().getName());
        cc.setDateStart(acert.getNotBefore());
        cc.setDateFinish(acert.getNotAfter());
        if (nType == SubjectType.ROOT_CA.getID())
            cc.setSigner(null);
        else {
            if (asigner == null)
                asigner = findBySubjectDN(acert.getIssuerX500Principal().getName());
            cc.setSigner(asigner);
        }
        updateStatus(cc, new Date());
        em.persist(cc);

        afterAdd(cc);
        return cc;
    }

    @Override
    public Certificate addCertificate(Certificate asigner, String providerName, byte[] cbody)
            throws Exception {
        return addCertificate(asigner, providerName, cbody, null, null, null);
    }

    @Override
    public Certificate addCertificate(Certificate asigner, String providerName, byte[] cbody, byte[] cpriv, byte[] csecrzip, String privPassword)
            throws Exception {
        checkInit();

        ParseCertResult res = parseCertificate(cbody);

        if (StringUtils.isEmpty(providerName))
            providerName = res.providerName;
        java.security.cert.X509Certificate acert = res.certificate;
/*		
		// конвертируем закрытый ключ
		byte[] cpvk1;
		if (cpriv == null)
			 cpvk1 = cpriv;
		else {
			PKCS8EncodedKeySpec pvks = new PKCS8EncodedKeySpec(cpriv);
			PrivateKey pvk1 = keyFac8.generatePrivate(pvks);
			cpvk1 = pvk1.getEncoded();
		}
*/
        String sid = res.sha1hash;
        Certificate cc = getCertificate(sid);
        if (cc == null) {
            cc = new Certificate();
            cc.setId(sid);
        }
        cc.setStatus(Status.UNDEFINED);
        cc.setBody(new String(Base64.encodeBase64(acert.getEncoded())));
        cc.setProviderName(providerName);
        if (cpriv != null)
            cc.setPrivateKey(new String(Base64.encodeBase64(cpriv)));
        int nType = CryptoUtils.calcCertType(acert);
        cc.setSubjectType(nType);
        cc.setSubjectCN(CryptoUtils.extractCN(acert));
        cc.setSubjectDN(CryptoUtils.extractDN(acert));
        cc.setDateStart(acert.getNotBefore());
        cc.setDateFinish(acert.getNotAfter());
        if (!StringUtils.isEmpty(privPassword))
            cc.setPrivateKeyPassword(privPassword);
        if (nType == SubjectType.ROOT_CA.getID())
            cc.setSigner(null);
        else {
            if (asigner == null)
                asigner = findIssuerFor(acert);
            cc.setSigner(asigner);
        }
        // secure random
        if (cpriv != null && csecrzip != null) {
            cc.setSecureRandom(new String(Base64.encodeBase64(csecrzip)));
            String sdir = checkServiceRoot(cc.getId());
            CryptoUtils.unzipTo(csecrzip, new File(sdir));
        }
        updateStatus(cc, new Date());
        em.persist(cc);

        afterAdd(cc);

        return cc;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Settings addSettings(Certificate cert, String sid) {
        Settings sets = new Settings();
        sets.setID(sid);
        sets.setCertificate(cert);
        sets.setProviderName(cert.getProviderName());
        sets.setXmlDigestAlg(CryptoUtils.XML_DIGEST_ALG);
        sets.setXmlDSigProviderName(CryptoUtils.XML_PROVIDER_NAME);
        sets.setXmlSignatureAlg(CryptoUtils.XML_SIGNATURE_ALG);
        sets.setDigestProviderName(CryptoUtils.PROVIDER_BC);
        if (cert.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)||cert.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_SIGNALCOM)) {
        	sets.setSignatureAlg(CryptoUtils.SIGNATURE_ALG_GOST);
        	sets.setDigestAlg(CryptoUtils.DIGEST_ALG_GOST);
        	sets.setSecureRandomAlg(CryptoUtils.SECURE_RANDOM_ALG_GOST);
        	sets.setPrivateKeyAlg(CryptoUtils.PRIVATE_KEY_ALG_GOST);
        	sets.setSslProtocol(CryptoUtils.SSL_PROTOCOL_GOST);
        	sets.setJsseProviderName(CryptoUtils.JSSE_PROVIDER_NAME_GOST);
        } else {
        	sets.setSignatureAlg(CryptoUtils.SIGNATURE_ALG_SHA);
        	sets.setDigestAlg(CryptoUtils.DIGEST_ALG_SHA);
        	sets.setSecureRandomAlg(CryptoUtils.SECURE_RANDOM_ALG_SHA);
        	sets.setPrivateKeyAlg(CryptoUtils.PRIVATE_KEY_ALG_RSA);
        	sets.setSslProtocol(CryptoUtils.SSL_PROTOCOL_SUN);
        	sets.setJsseProviderName(CryptoUtils.JSSE_PROVIDER_NAME_SUN);
        }
        if (cert.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)){
        	sets.setKeyManagerAlg(CryptoUtils.KEY_MANAGER_ALG_GOST_JCP);
        } else if (cert.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_SIGNALCOM)){
        	sets.setKeyManagerAlg(CryptoUtils.KEY_MANAGER_ALG_GOST_SC);
        } else {
        	sets.setKeyManagerAlg(CryptoUtils.KEY_MANAGER_ALG_GOST_SUN);
        }
       // cert.getSettings().add(sets);
        em.persist(sets);
        //em.persist(cert);
        return sets;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Settings addSettings(Certificate cert) {
    	return addSettings(cert,UUID.randomUUID().toString());
    }
    
    public void removeSettings(Certificate cert, String setsID) {
        Settings asets = null;
        for (Settings sets : cert.getSettings()) {
            if (setsID.equals(sets.getID())) {
                asets = sets;
                break;
            }
        }
        cert.getSettings().remove(asets);
        em.persist(cert);
        em.remove(asets);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeSettings(String setsID) {
       String sql="delete from settings where id=:id";
       Query qry = em.createNativeQuery(sql);
       qry.setParameter("id", setsID);
       qry.executeUpdate();
    }
    
    protected void afterAdd(Certificate cert)
            throws CryptoException {
        if (cert.getSubjectType() == SubjectType.ROOT_CA.getID() || cert.getSubjectType() == SubjectType.SUB_CA.getID()) {
            X509Certificate xcertCA = extractX509Cert(cert);
            String sql = "from org.admnkz.crypto.data.Certificate where (Status.ID != " + Status.CLOSED.getID().toString() + " ) and (Signer is null) and (SubjectType != " + SubjectType.ROOT_CA.getID().toString() + ")";
            Query qry = em.createQuery(sql);
            List<Certificate> lst = qry.getResultList();
            for (Certificate cc : lst) {
                if (cc.getSigner() != null)
                    continue;
                if (cc.getId().equals(cert.getId()))
                    continue;

                try {
                    X509Certificate xcert = extractX509Cert(cc);
//					String idn = CryptoUtils.extractIssuerDN(xcert);
                    if (CryptoUtils.isIssuerFor(xcert, xcertCA, cc.getProviderName())) ;
                    {
                        cc.setSigner(cert);
                        em.persist(cc);
                    }
                } catch (Exception e) {
                	LOGGER.error("Не удалось записать издателя для сертификата", e);
                    throw new CryptoException("Не удалось записать издателя для сертификата ",e);
                }
            }
        }
    }

    private void updateStatus(Certificate acert, Date timeNow) {
        if (acert.getStatus().equals(Status.CLOSED))
            return;

        if (timeNow.after(acert.getDateStart()) && timeNow.before(acert.getDateFinish())) {
            acert.setStatus(Status.ACTIVE);
        } else {
            acert.setStatus(Status.OUT_OF_DATE);
        }
    }

    public void updateStatuses(Date timeNow) {
        String sql = "from org.admnkz.crypto.data.Certificate where (Status.ID != " + Status.CLOSED.getID().toString() + " )";
        Query qry = em.createQuery(sql);
        List<Certificate> lst = qry.getResultList();
        for (Certificate cert : lst) {
            updateStatus(cert, timeNow);
            em.persist(cert);
        }
    }


    public void updateSelfCheck(Date timeNow) {
        String sql = "from org.admnkz.crypto.data.Certificate";
        Query qry = em.createQuery(sql);
        List<Certificate> lst = qry.getResultList();
        for (Certificate cert : lst) {
            try {
                X509Certificate xcert = extractX509Cert(cert);
                cert.setSubjectDN(CryptoUtils.extractDN(xcert));
                em.persist(cert);
            } catch (Exception ex) {

            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Certificate saveCertificate(Certificate acert) {
        acert = em.merge(acert);
        em.persist(acert);
        return acert;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Settings saveSettings(Settings sets) {
        sets = em.merge(sets);
        em.persist(sets);
        return sets;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] digest(byte[] source, String optkey)
            throws CryptoException {
        Settings sets = getSettings(optkey);
        return digest(source, sets);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] digest(byte[] source, Settings sets)
            throws CryptoException {
        return digest(source, sets.getProviderName(), sets.getDigestAlg());
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] digest(byte[] source, String providerName, String digestAlg) throws CryptoException {
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlg, providerName);
            md.update(source);
            byte[] dig = md.digest();
            return dig;
        } catch (Exception e) {
            throw new CryptoException("Could not make message digest ", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] signCMS(byte[] source, String optkey)
            throws CryptoException {
        Settings sets = getSettings(optkey);
        return signCMS(source, sets);
    }

    /*
	 * Возвращает все сертификаты в цепочке. Конечный сертификат в начале списка.
	 */
    public List<Certificate> listCertPath(Certificate endCert) {
        ArrayList<Certificate> lst = new ArrayList<Certificate>(1);
        lst.add(endCert);

        Certificate cert = endCert;
        while (cert.getSigner() != null) {
            cert = cert.getSigner();
            lst.add(cert);
        }

        return lst;
    }

    private List<X509Certificate> listX509CertPath(Certificate endCert) throws Base64DecodingException, CryptoException {
        ArrayList<X509Certificate> lst = new ArrayList<X509Certificate>(1);
        lst.add(makeCert(Base64.decodeBase64(endCert.getBody()), endCert.getProviderName()));

        Certificate cert = endCert;
        while (cert.getSigner() != null) {
            cert = cert.getSigner();
            lst.add(makeCert(Base64.decodeBase64(cert.getBody()), cert.getProviderName()));
        }

        return lst;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] signCMS(byte[] source, Settings sets)
            throws CryptoException {
        return signCMS(source, sets, false);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] signCMS(byte[] source, String optkey, boolean detached) throws CryptoException {
        Settings sets = getSettings(optkey);
        return signCMS(source, sets, detached);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] signCMS(byte[] source, String optkey, boolean detached,boolean addChainCerts) throws CryptoException{
    	Settings sets = getSettings(optkey);
    	return signCMS(source, sets, detached,addChainCerts);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public byte[] signCMS(byte[] source, Settings sets, boolean detached) 
		throws CryptoException{
    	return signCMS(source, sets, detached,true);
    }
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public byte[] signCMS(byte[] source, Settings sets, boolean detached, boolean addChainCerts) 
		throws CryptoException
	{
		checkInit();
		
		Certificate endCert = sets.getCertificate(); 
		if (endCert == null)
			throw new CryptoException("В настройках " + sets.getID() + " не указан сертификат для подписания");
		if (! endCert.hasPrivateKey())
			throw new CryptoException("У выбранного вами сертификата нет закрытого ключа");
		
		Map<String,String> mcaps = getProviderCapabities(sets.getProviderName());
		byte[] asign = null;
		
		try {			
			SecureRandom rnd = retrieveSecureRandom(sets.getCertificate().getId(), sets, mcaps);

			// make cert store with signer certificate and all CAs			
			List<X509Certificate> certList = listX509CertPath(endCert);
			Store certs = new JcaCertStore(certList);
			
			// make signer from alias My
			PrivateKey pvk =null;
			if (endCert.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO))	{
				pvk=decodePrivateKeyJCP(endCert.getPrivateKeyAlias(),endCert.getPrivateKeyPassword());
			}
			else {
			    pvk = decodePrivateKey(Base64.decodeBase64(endCert.getPrivateKey()), endCert.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), endCert.getPrivateKeyPassword());
			}
			
			X509Certificate signCert = certList.get(0);
			
			JcaContentSignerBuilder cbuild = new JcaContentSignerBuilder(sets.getSignatureAlg());
			cbuild.setProvider(sets.getProviderName());
			if (rnd != null)
				cbuild.setSecureRandom(rnd);
			ContentSigner asigner = cbuild.build(pvk);			
			
			CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
			
			JcaSignerInfoGeneratorBuilder bgen = new JcaSignerInfoGeneratorBuilder( new JcaDigestCalculatorProviderBuilder().setProvider(sets.getProviderName()).build());
			bgen.setDirectSignature(true);			
			gen.addSignerInfoGenerator(bgen.build(asigner, signCert));
			
			if (addChainCerts){
			  gen.addCertificates(certs);
			}
			
			CMSProcessableByteArray adata = new CMSProcessableByteArray(source);
			CMSSignedData sigData = gen.generate(adata, detached);
			
			asign = sigData.getEncoded();
		} catch (Exception e) {
			 throw new CryptoException(e);
		}
		
		return asign;
		
	}

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] signYandexCMS(byte[] source, String optkey)
            throws CryptoException {
        Settings sets = getSettings(optkey);
        Certificate endCert = sets.getCertificate();
        if (endCert == null)
            throw new CryptoException("В настройках " + sets.getID() + " не указан сертификат для подписания");
        if (! endCert.hasPrivateKey())
            throw new CryptoException("У выбранного вами сертификата нет закрытого ключа");

        Map<String,String> mcaps = getProviderCapabities(sets.getProviderName());
        byte[] asign = null;

        try {
            SecureRandom rnd = retrieveSecureRandom(sets.getCertificate().getId(), sets, mcaps);

            // make cert store with signer certificate and all CAs
            List<X509Certificate> certList = listX509CertPath(endCert);
            Store certs = new JcaCertStore(certList);

            // make signer from alias My
            PrivateKey pvk = decodePrivateKey(Base64.decodeBase64(endCert.getPrivateKey()), endCert.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), endCert.getPrivateKeyPassword());
            X509Certificate signCert = certList.get(0);

            JcaContentSignerBuilder cbuild = new JcaContentSignerBuilder(sets.getSignatureAlg());
            cbuild.setProvider(sets.getProviderName());
            if (rnd != null)
                cbuild.setSecureRandom(rnd);
            ContentSigner asigner = cbuild.build(pvk);

            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

            JcaSignerInfoGeneratorBuilder bgen = new JcaSignerInfoGeneratorBuilder( new JcaDigestCalculatorProviderBuilder().setProvider(sets.getProviderName()).build());
            bgen.setDirectSignature(true);
            gen.addSignerInfoGenerator(bgen.build(asigner, signCert));
//            gen.addCertificates(certs);

            CMSProcessableByteArray adata = new CMSProcessableByteArray(source);
            CMSSignedData sigData = gen.generate(adata, true);

            asign = sigData.getEncoded();
        } catch (Exception e) {
            throw new CryptoException(e);
        }

        return asign;

    }

    @Override
    public byte[] createJCPCMS(byte[] data, String settingId,  boolean detached)
            throws Exception {
        return createJCPCMS(data,settingId,detached,null);
    }

    @Override
    public byte[] createJCPCMS(byte[] data, String settingId,  boolean detached,
    		String filename) throws Exception {

        Settings settings = getSettings(settingId);
        Certificate endCert = getSettings(settingId).getCertificate();

        if (endCert == null) {
            throw new CryptoException("В настройках " + settings.getID() + " не указан сертификат для подписания");
        }
        if (! endCert.hasPrivateKey()) {
            throw new CryptoException("У выбранного вами сертификата нет закрытого ключа");
        }

        List<X509Certificate> certList = listX509CertPath(endCert);
        //чем подписываем
        X509Certificate signCert = certList.get(0);

        //нашли ключ в хранилище
        PrivateKey pvk =null;
        try { 
            pvk = decodePrivateKeyJCP(endCert.getPrivateKeyAlias(),endCert.getPrivateKeyPassword());
        } catch (Exception e) {
        	LOGGER.error("Не удалось прочитать закрытый ключ " + endCert.getPrivateKeyAlias() + " с помощью " +
                    JCP.PROVIDER_NAME, e);
            throw new CryptoException("Не удалось прочитать закрытый ключ " + endCert.getPrivateKeyAlias() + " с помощью " + JCP.PROVIDER_NAME, e);
        }

        if (pvk==null){
        	 throw new CryptoException("Закрытый ключ не нашелся в хранилище");
        }
        
        final Signature signature = Signature.getInstance(JCP.GOST_DHEL_SIGN_NAME, JCP.PROVIDER_NAME);
        signature.initSign(pvk);
        signature.update(data);

        final byte[] sign = signature.sign();
        //создали cms
        byte[] cms= createCMS(signCert,data,sign,detached);
        //запишем файл
        if (StringUtils.isNotEmpty(filename)){
            Array.writeFile(filename, cms);
        }
        return cms;
    
    }
    
    @Override
    public boolean verifyJCPCMS(byte[] data, String settingId) throws Exception {

        Certificate endCert = getSettings(settingId).getCertificate();

        List<X509Certificate> certList = listX509CertPath(endCert);
        X509Certificate cert = certList.get(0);

        int i;
        final Asn1BerDecodeBuffer asnBuf = new Asn1BerDecodeBuffer(data);
        final ContentInfo all = new ContentInfo();
        all.decode(asnBuf);

        if (!new OID("1.2.840.113549.1.7.2").eq(all.contentType.value)) {
            throw new Exception("Not supported");
        } // if

        final SignedData cms = (SignedData) all.content;
        if (cms.version.value != 1) {
            throw new Exception("Incorrect version");
        } // if

        if (!new OID("1.2.840.113549.1.7.1").eq(
                cms.encapContentInfo.eContentType.value)) {
            throw new Exception("Nested not supported");
        } // if

        byte[] text = null;
        if (data != null) {
            text = data;
        } // if
        else if (cms.encapContentInfo.eContent != null) {
            text = cms.encapContentInfo.eContent.value;
        } // else

        if (text == null) {
            throw new Exception("No content");
        } // if

        OID digestOid = null;
        DigestAlgorithmIdentifier a = new DigestAlgorithmIdentifier(
                new OID(JCP.GOST_DIGEST_OID).value);

        for (i = 0; i < cms.digestAlgorithms.elements.length; i++) {
            if (cms.digestAlgorithms.elements[i].algorithm.equals(a.algorithm)) {
                digestOid = new OID(cms.digestAlgorithms.elements[i].algorithm.value);
                break;
            } // if
        } // for

        if (digestOid == null) {
            throw new Exception("Unknown digest");
        } // if

        int pos = -1;

        if (cms.certificates != null) {

            for (i = 0; i < cms.certificates.elements.length; i++) {

                final Asn1BerEncodeBuffer encBuf = new Asn1BerEncodeBuffer();
                cms.certificates.elements[i].encode(encBuf);
                final byte[] in = encBuf.getMsgCopy();

                if (Arrays.equals(in, cert.getEncoded())) {
                    LOGGER.info("Certificate: " + ((X509Certificate) cert).getSubjectDN());
                    pos = i;
                    break;
                } // if

            } // for

            if (pos == -1) {
                throw new Exception("Not signed on certificate.");
            } // if

        }
        else if (cert == null) {
            throw new Exception("No certificate found.");
        } // else
        else {
            // Если задан {@link #cert}, то пробуем проверить
            // первую же подпись на нем.
            pos = 0;
        } // else

        final SignerInfo info = cms.signerInfos.elements[pos];
        if (info.version.value != 1) {
            throw new Exception("Incorrect version");
        } // if

        if (!digestOid.equals(new OID(info.digestAlgorithm.algorithm.value))) {
            throw new Exception("Not signed on certificate.");
        } // if

        final byte[] sign = info.signature.value;

        // check
        final Signature signature = Signature.getInstance(JCP.GOST_EL_SIGN_NAME, JCP.PROVIDER_NAME);
        signature.initVerify(cert);
        signature.update(text);

        return signature.verify(sign);
    }

    /**
	 * Выдаёт список сертификатов по условиям
	 * @param orderby - null, если не нужно сортировать. Если нужна сортировка, то он содержит 
	 * строки вида "#{certAlias}SubjectCN asc". В строке может присутствовать макроподстановка: #{certAlias}    
	 */
	public List<Certificate> listCertificates(int nFirstRow, int nRows, String subjectCN, Integer subjectTypeID, Integer statusID, 
			String providerName, String settingsName, DateRange dateStart, DateRange dateFinish,
			String signerID, String[] orderby, Set options) 
	{
		String sql = "from org.admnkz.crypto.data.Certificate c where (1=1)";
		if (! StringUtils.isEmpty(subjectCN)){
			sql = sql + " and (upper(c.SubjectCN) like :subjectCN)";
		}
		if (subjectTypeID != null&&subjectTypeID!=0) {
			sql = sql + " and (c.SubjectType = :subjectTypeID)";
		}
		if (statusID != null) {
			sql = sql + " and (c.Status.ID = :statusID)";
		}
		if (StringUtils.isNotEmpty(providerName)) {
			sql = sql + " and (c.ProviderName = :providerName)";
		}
		if (StringUtils.isNotEmpty(signerID)) {
			sql = sql + " and (c.Signer.ID = :signerID)";
		}
		if (dateStart != null && dateStart.getFrom() != null) {
	        sql+= " and (DateStart >= :dateStartFrom) ";
	    }
	    if (dateStart != null && dateStart.getTo() != null) {
	    	sql+= " and (DateStart < :dateStartTo) ";
	    }
	    if (dateFinish != null && dateFinish.getFrom() != null) {
	        sql+= " and (DateFinish >= :dateFinishFrom) ";
	    }
	    if (dateFinish != null && dateFinish.getTo() != null) {
	    	sql+= " and (DateFinish < :dateFinishTo) ";
	    }
		if (StringUtils.isNotEmpty(settingsName)){
			sql = sql + " and ( (select count(*) from c.settings as s where (upper(s.id) like:settingsName) ) > 0 )";
		}
		if (orderby != null && orderby.length > 0) 
		{
			sql = sql + " order by ";
			for (String ord: orderby) {
				sql = sql + ord.replace("#{certAlias}", "c.") + ",";
			}
			if (sql.endsWith(","))
				sql = sql.substring(0, sql.length()-1);
		} else {
			sql+=" order by DateFinish ";
		}
		Query qry = em.createQuery(sql);
		if (StringUtils.isNotEmpty(subjectCN)) {
			qry.setParameter("subjectCN", "%"+subjectCN.toUpperCase()+"%");
		}
		if (subjectTypeID != null&&subjectTypeID!=0) {
			qry.setParameter("subjectTypeID", subjectTypeID);
		}
		if (statusID != null) {
			qry.setParameter("statusID", statusID);
		}
		if (providerName != null) {
			qry.setParameter("providerName", providerName);
		}
		if (signerID != null) {
			qry.setParameter("signerID", signerID);
		}
		if (dateStart != null && dateStart.getFrom() != null) {
			qry.setParameter("dateStartFrom", dateStart.getFrom(),TemporalType.DATE);
	    }
	    if (dateStart != null && dateStart.getTo() != null) {
	    	qry.setParameter("dateStartTo", DateUtils.addDays(dateStart.getTo(),1),TemporalType.DATE);
	    }
	    if (dateFinish != null && dateFinish.getFrom() != null) {
	    	qry.setParameter("dateFinishFrom", dateFinish.getFrom(),TemporalType.DATE);
	    }
	    if (dateFinish != null && dateFinish.getTo() != null) {
	    	qry.setParameter("dateFinishTo", DateUtils.addDays(dateFinish.getTo(),1),TemporalType.DATE);
	    }
		if (StringUtils.isNotEmpty(settingsName)){
			qry.setParameter("settingsName", "%"+settingsName.toUpperCase()+"%");
		}
		if (nFirstRow >= 0) {
			qry.setFirstResult(nFirstRow);
		}
		if (nRows > 0) {
			qry.setMaxResults(nRows);
		}
		
		if (options != null && options.size() > 0) {
			List<Certificate> lstRes = qry.getResultList();
			Utils.initCollection(lstRes, options);
			return lstRes;
		} else			
			return qry.getResultList();
	}
	
	public int countCertificates(String subjectCN, Integer subjectTypeID, Integer statusID, String providerName, 
			String settingsName, DateRange dateStart, DateRange dateFinish,String signerID) 
	{
		String sql = "select count(c.id) from org.admnkz.crypto.data.Certificate c where (1=1)";
		if (StringUtils.isNotEmpty(subjectCN)) {
			sql = sql + " and (upper(c.SubjectCN) like :subjectCN)";
		}
		if (subjectTypeID != null&&subjectTypeID!=0) {
			sql = sql + " and (c.SubjectType = :subjectTypeID)";
		}
		if (statusID != null) {
			sql = sql + " and (c.Status.ID = :statusID)";
		}
		if (StringUtils.isNotEmpty(providerName)) {
			sql = sql + " and (c.ProviderName = :providerName)";
		}
		if (StringUtils.isNotEmpty(signerID)) {
			sql = sql + " and (c.Signer.ID = :signerID)";	
		}
		if (dateStart != null && dateStart.getFrom() != null) {
	        sql+= " and (DateStart >= :dateStartFrom) ";
	    }
	    if (dateStart != null && dateStart.getTo() != null) {
	    	sql+= " and (DateStart < :dateStartTo) ";
	    }
	    if (dateFinish != null && dateFinish.getFrom() != null) {
	        sql+= " and (DateFinish >= :dateFinishFrom) ";
	    }
	    if (dateFinish != null && dateFinish.getTo() != null) {
	    	sql+= " and (DateFinish < :dateFinishTo) ";
	    }
		if (StringUtils.isNotEmpty(settingsName)){
			sql = sql + " and ( (select count(*) from c.settings as s where (upper(s.id) like:settingsName) ) > 0 )";
		}
		
		Query qry = em.createQuery(sql);
		if (! StringUtils.isEmpty(subjectCN)){
			qry.setParameter("subjectCN", "%"+subjectCN.toUpperCase()+"%");
		}
		if (subjectTypeID != null&&subjectTypeID!=0) {
			qry.setParameter("subjectTypeID", subjectTypeID);
		}
		if (statusID != null) {
			qry.setParameter("statusID", statusID);
		}
		if (providerName != null) {
			qry.setParameter("providerName", providerName);
		}
		if (signerID != null) {
			qry.setParameter("signerID", signerID);		
		}
		if (dateStart != null && dateStart.getFrom() != null) {
			qry.setParameter("dateStartFrom", dateStart.getFrom(),TemporalType.DATE);
	    }
	    if (dateStart != null && dateStart.getTo() != null) {
	    	qry.setParameter("dateStartTo", DateUtils.addDays(dateStart.getTo(),1),TemporalType.DATE);
	    }
	    if (dateFinish != null && dateFinish.getFrom() != null) {
	    	qry.setParameter("dateFinishFrom", dateFinish.getFrom(),TemporalType.DATE);
	    }
	    if (dateFinish != null && dateFinish.getTo() != null) {
	    	qry.setParameter("dateFinishTo", DateUtils.addDays(dateFinish.getTo(),1),TemporalType.DATE);
	    }
		if (StringUtils.isNotEmpty(settingsName)){
			qry.setParameter("settingsName", "%"+settingsName.toUpperCase()+"%");
		}
		Number nres = (Number) qry.getSingleResult();
		if (nres == null)
			return 0;
		else
			return nres.intValue();
	}	

	public String getDefaultProviderName() {
		return options.getValue("cryptoservice.provider.default");
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verifyCMS(byte[] sig, byte[] data, String optkey, boolean bSignatureOnly)
		throws CryptoException
	{
		Settings sets = getSettings(optkey);
		return verifyCMS(sig, data, sets, bSignatureOnly);
	}
	
	@Override
	public SSLContext createSSLContext(String clentSettingsID, String serverSettingsID) throws CryptoException {
		Settings clientSets = getSettings(clentSettingsID);
		Settings serverSets = getSettings(serverSettingsID);
		return createSSLContext(clientSets, serverSets);
	}

    @Override
    public SSLContext createX509SSLContext(String clentSettingsID, String serverSettingsID) throws CryptoException {
        Settings s = getSettings(serverSettingsID);
        org.admnkz.crypto.data.Certificate ca = s.getCertificate();

        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(Base64
                    .decodeBase64(ca.getBody())));

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustks = KeyStore.getInstance("JKS");
            trustks.load(null);

            trustks.setCertificateEntry("ca", cert);

            tmf.init(trustks);

            SSLContext context = SSLContext.getInstance("SSLv3");

            s = getSettings(clentSettingsID);
            org.admnkz.crypto.data.Certificate certificate = s.getCertificate();

            cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64
                    (certificate.getBody())));

            KeyFactory keyFac8 = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec pvks = new PKCS8EncodedKeySpec(Base64.decodeBase64(certificate.getPrivateKey()));
            PrivateKey key = keyFac8.generatePrivate(pvks);

            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(null);
            keystore.setCertificateEntry("alias", cert);
            char[] keyPassword = certificate.getPrivateKeyPassword() == null ? "".toCharArray() : certificate
                    .getPrivateKeyPassword().toCharArray();
            keystore.setKeyEntry("key-alias", key, keyPassword, new X509Certificate[]{cert});

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keystore, "".toCharArray());

            KeyManager[] km = kmf.getKeyManagers();

            context.init(km, tmf.getTrustManagers(), null);
            return context;
        } catch (Exception e) {
            LOGGER.error("Не удалось создать SSLContext", e);
            throw new CryptoException("Не удалось создать SSLContext " + e, e);
        }
    }

    @Override
	public SSLContext createTrustedSSLContext(String clentSettingsID, String serverSettingsID) throws CryptoException {
		Settings clientSets = getSettings(clentSettingsID);
		Settings serverSets = getSettings(serverSettingsID);
		return createSSLContext(clientSets, serverSets,"JKS","JKS",true);
	}
	
	/**
	 * добавляем сертификат для закрытого ключа
	 * @param ks - хранилище
	 * @param endCert - сертификат из бд
	 * @param sets - настройки сертификата
	 * @param password - пароль
	 * @param bAddChain - добавляем цепочку или нет
	 */
	private void addPrivateCert(KeyStore ks, Certificate endCert, Settings sets, String password, boolean bAddChain) throws CryptoException, KeyStoreException {
		X509Certificate xcert = makeCert(Base64.decodeBase64(endCert.getBody()), sets.getProviderName());
		/*X509Certificate xcert=null;
		try {
			xcert = decodeCertificateJCP(endCert.getPrivateKeyAlias(),endCert.getPrivateKeyPassword());
		} catch (Exception e) {
			LOGGER.error("Ошибка при вытаскивании сертификата "+e);
		} */
		
		if (StringUtils.isNotEmpty(endCert.getPrivateKeyAlias())) {
            LOGGER.info("Сертификат " + endCert.getPrivateKeyAlias());
			ks.setCertificateEntry(endCert.getPrivateKeyAlias(), xcert);
		} else {
            LOGGER.info("Сертификат " + xcert.getSubjectDN().toString());
			 ks.setCertificateEntry(xcert.getSubjectDN().toString(), xcert);
		}
		
		
		X509Certificate[] xarray = new X509Certificate[20];
		int narray = 0;
		xarray[narray] = xcert;
		
		//если добавляем цепочку сертификатов
		if (bAddChain) {
			Certificate cert = endCert; 
			while (cert.getSigner() != null) 
			{
				cert = cert.getSigner();
				xcert = makeCert(Base64.decodeBase64(cert.getBody()), cert.getProviderName());
				
				if (StringUtils.isNotEmpty(cert.getPrivateKeyAlias()))
				{
					ks.setCertificateEntry(cert.getPrivateKeyAlias(), xcert);
					LOGGER.info("Сертификат " + cert.getPrivateKeyAlias());
				}
				else
				{
				    ks.setCertificateEntry(xcert.getSubjectDN().toString(), xcert);
				    LOGGER.info("Сертификат " + xcert.getSubjectDN().toString());
				}
				narray++;
				xarray[narray] = xcert;
			}
		}
		
		xarray = Arrays.copyOf(xarray, narray+1);
        LOGGER.info("Будем искать закрытый ключ");
        
		PrivateKey pvk = decodePrivateKey(Base64.decodeBase64(endCert.getPrivateKey()), endCert.getPrivateKeyAlias(), sets.getProviderName(), sets.getPrivateKeyAlg(), endCert.getPrivateKeyPassword() );
      
		if (pvk == null) {
            throw new CryptoException("Не удалось найти закрытый ключ");
        } else {
            LOGGER.info("Нашли закрытый ключ");
        }
        //PrivateKey pvk = decodePrivateKey(Base64.decodeBase64(endCert.getPrivateKey()), endCert.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), endCert.getPrivateKeyPassword() );
		if (password==null) {
            ks.setKeyEntry(endCert.getPrivateKeyAlias(), pvk, null, xarray);
        } else {
            ks.setKeyEntry(endCert.getPrivateKeyAlias(), pvk, password.toCharArray(), xarray);
        }
        LOGGER.info("Добавили в хранилище");
	
	}
	
	private void addCert(KeyStore ks, Certificate endCert, Settings sets, int[] subjectTypes, String password, boolean bAddChain) throws CryptoException, KeyStoreException {
		X509Certificate xcert = null;
		if (subjectTypes == null || Arrays.binarySearch(subjectTypes, endCert.getSubjectType().intValue()) >= 0) {
			xcert = makeCert(Base64.decodeBase64(endCert.getBody()), endCert.getProviderName());
			ks.setCertificateEntry(xcert.getSubjectDN().toString(), xcert);
            LOGGER.info("Сертификат trust store " + xcert.getSubjectDN().toString());
		}
		
		List<Settings> lstSett=findBySettingsId(sets.getID()+".");
		if (lstSett.size()>0){
			for (Settings sett:lstSett){
			  	
			  xcert = makeCert(Base64.decodeBase64(sett.getCertificate().getBody()), sett.getCertificate().getProviderName());
			  ks.setCertificateEntry(xcert.getSubjectDN().toString(), xcert);
			}
		}
		
		if (bAddChain) {
			int n = 0;
			Certificate cert = endCert; 
			while (cert.getSigner() != null) 
			{
				cert = cert.getSigner();
				if (subjectTypes == null || Arrays.binarySearch(subjectTypes, cert.getSubjectType().intValue()) >= 0) {
					n++;
					xcert = makeCert(Base64.decodeBase64(cert.getBody()), cert.getProviderName());
					ks.setCertificateEntry(xcert.getSubjectDN().toString(), xcert);
                    LOGGER.info("Сертификат trust store " + xcert.getSubjectDN().toString());
				}
			}		
		}
	}
	
	protected KeyStore createKeyStore(String storeType, Settings sets, int[] subjectTypes, String password) throws KeyStoreException, NoSuchProviderException, CryptoException, NoSuchAlgorithmException, CertificateException, IOException {
				
		KeyStore ks = KeyStore.getInstance(storeType, sets.getProviderName());
        if (password==null)
        	ks.load(null, null);
        else
		    ks.load(null, password.toCharArray());
		
		// добавляем конечный сертификат в хранилище
		Certificate endCert = getCertificate(sets.getCertificate().getId());
		if (endCert.hasPrivateKey()&&sets.getID().contains("client") ) {
			addPrivateCert(ks, endCert, sets, password, true);
		} else {
			addCert(ks, endCert, sets, subjectTypes, password, true);
		}
		
		return ks;
	}
	
	@Override
	public byte[] generateKeyStore(String storeType, String settingsID, int[] subjectTypes, String password) throws CryptoException {
		Settings sets = getSettings(settingsID);
		return generateKeyStore(storeType, sets, subjectTypes, password);
	}
	
	@Override
	public byte[] generateKeyStore(String storeType, Settings sets, int[] subjectTypes, String password) throws CryptoException {
		try {
			byte[] res =  null;
			KeyStore keyStore = createKeyStore(storeType, sets, subjectTypes, password);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				if (password == null) {
					keyStore.store(stream, null);					
				} else {
					keyStore.store(stream, password.toCharArray());
				}
				res = stream.toByteArray();
			} finally {
				IOUtils.closeQuietly(stream);
			}
			return res;
		} catch (Exception e) {
			throw new CryptoException("Не удалось создать хранилище", e);
		}
	}
	
	/**
	 * создаем ssl context
	 * @param clientSets - настройки сертификата клиента из бд
	 * @param serverSets - настройки сертификата сервера из бд
	 * @param clientStoreType - вид хранилища клиентского сертификата
	 * @param serverStoreType - вид хранилища серверного сертификата
	 * @param bTrustAll - доверяем всем
	 * @throws CryptoException
	 */
	protected SSLContext createSSLContext(Settings clientSets, Settings serverSets, String clientStoreType, String serverStoreType, boolean bTrustAll) throws CryptoException {
		
		try {
			// создаём SecureRandom
			Map<String,String> mcaps = getProviderCapabities(clientSets.getProviderName());	
			SecureRandom random =null; 
					
			//инициализируем фабрику крипто-про и гостовские алгоритмы, создаём SecureRandom
			if (clientSets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
				setSSLProperties();
                random = SecureRandom.getInstance(JCP.CP_RANDOM, CryptoUtils.PROVIDER_CRYPTOPRO);
                LOGGER.info("properites and random JCP");
			} else { 
				if (clientSets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_SIGNALCOM)){
				  
				}
                random = retrieveSecureRandom(clientSets.getCertificate().getId(), clientSets, mcaps);
            }
            LOGGER.info("Установили проперти для SSL");
			
			// создаём KeyStore из цепочки клиентских сертификатов и клиентского закрытого ключа
			String clientPassword = clientSets.getCertificate().getPrivateKeyPassword();
			if (StringUtils.isEmpty(clientPassword)) {
				clientPassword = null;
			}
			KeyStore keyStore = null;
			KeyManagerFactory kmf = null;
			//инициализируем keystore для закрытого ключа
			if (clientSets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
				 keyStore = createKeyStore(clientSets.getCertificate().getPrivateKeyPath(), clientSets, null, clientPassword);
				 kmf = KeyManagerFactory.getInstance(clientSets.getKeyManagerAlg());

                LOGGER.info("Проинициализировали ключ JCP");
			} else {
				 keyStore = createKeyStore(clientStoreType, clientSets, null, clientPassword);
				 kmf = KeyManagerFactory.getInstance(clientSets.getKeyManagerAlg(), clientSets.getJsseProviderName());
				 LOGGER.info("Проинициализировали ключ SC");
			}
			
			if (clientPassword==null){
				kmf.init(keyStore,null);
			} else {
			    kmf.init(keyStore, clientPassword.toCharArray());
			}
			
			SSLContext ctx = null;
			//если доверяем всем
			if (bTrustAll) {
							
				TrustManager[] trustAllCerts = trustAll();
				if (clientSets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)){
					ctx= SSLContext.getInstance(serverSets.getSslProtocol());
				} else {
				    ctx = SSLContext.getInstance(serverSets.getSslProtocol(), serverSets.getJsseProviderName());
				}
				ctx.init(kmf.getKeyManagers(), trustAllCerts, random);
				LOGGER.info("Закончили процедуру инициализации SSL context");
			} else {
				// создаём TrustStore из цепочки серверных сертификатов
				String serverPassword = serverSets.getCertificate().getPrivateKeyPassword();
				if (StringUtils.isEmpty(serverPassword)) {
					serverPassword = null;
				}
				String ss=serverSets.getCertificate().getPrivateKeyPath();
				KeyStore trustStore =null;
				TrustManagerFactory tmf =null;
				//инициализируем truststore для корневых сертификатов
				if (serverSets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
				   trustStore = createKeyStore(ss, serverSets, null, serverPassword);
				   tmf = TrustManagerFactory.getInstance(serverSets.getKeyManagerAlg());

                    LOGGER.info("Проинициализировали trust store JCP");
				} else {
				   trustStore = createKeyStore(serverStoreType, serverSets, null, "");
				   tmf = TrustManagerFactory.getInstance(serverSets.getKeyManagerAlg(), serverSets.getJsseProviderName());
				   LOGGER.info("Проинициализировали trust store SC");
				}
				tmf.init(trustStore);
				// создаем контекст
				if (clientSets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)){
					ctx= SSLContext.getInstance(serverSets.getSslProtocol());
				} else {
				    ctx = SSLContext.getInstance(serverSets.getSslProtocol(), serverSets.getJsseProviderName());
				}
				
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), random);	
                LOGGER.info("Закончили процедуру инициализации SSL context");
			}
			return ctx;
		} catch (KeyStoreException | NoSuchProviderException | UnrecoverableKeyException | KeyManagementException
                | CertificateException | IOException | NoSuchAlgorithmException e) {
			LOGGER.error("Не удалось создать SSLContext", e);
			throw new CryptoException("Не удалось создать SSLContext "+e, e);
		}
    }
	
	@Override
	public SSLContext createSSLContext(Settings clientSets, Settings serverSets) throws CryptoException {
		return createSSLContext(clientSets, serverSets, "JKS", "JKS", false);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verifyCMS(byte[] sig, byte[] data, Settings sets, boolean bSignatureOnly)
		throws CryptoException
	{
		checkInit();
		
	    try{
			ParseCMSResult res = new ParseCMSResult();
        	res.signature = sig;
        	
	        CMSSignedData cms = new CMSSignedData(sig);
	        if (cms.getSignedContent() == null)
	        	res.data = data;
	        else {
	        	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        	try {
	        		cms.getSignedContent().write(bout);
	        	} finally {
	        		bout.close();
	        	}
	        	res.data = bout.toByteArray();
	        }	    	
	    	
	        cms = new CMSSignedData(new CMSProcessableByteArray(res.data), sig);
	        Store                   certStore = cms.getCertificates();
	        SignerInformationStore  signers = cms.getSignerInfos();
	        Collection              c = signers.getSigners();
	        Iterator                it = c.iterator();
	        
	        int verified = 0;
	        while (it.hasNext())
	        {
	            SignerInformation   signer = (SignerInformation)it.next();
	            Collection          certCollection = certStore.getMatches(signer.getSID());

	            Iterator              certIt = certCollection.iterator();
	            X509CertificateHolder cert = (X509CertificateHolder)certIt.next();
	        
	            JcaSimpleSignerInfoVerifierBuilder bld = new JcaSimpleSignerInfoVerifierBuilder();
	            bld.setProvider(sets.getProviderName());
	            SignerInformationVerifier siv = bld.build(cert);
	            
	            boolean bValid = signer.verify(siv);
	            if (! bValid)
	                return false;
	            if (! bSignatureOnly) {
	            	bValid = bValid && verifyCertificate(cert.getEncoded());
	            }
	            if (! bValid)
	                return false;
	        }

	        return true;
	    } catch (Exception e) {
	    	throw new CryptoException("Не удалось проверить подпись PKCS#7", e);
	    }

	}

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] decryptCMS(byte[] sig, String setId)
            throws CryptoException {
        checkInit();
        Settings sets = getSettings(setId);
        try{
            ParseCMSResult res = new ParseCMSResult();
            res.signature = sig;

            CMSSignedData cms = new CMSSignedData(sig);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            try {
                cms.getSignedContent().write(bout);
            } finally {
                bout.close();
            }
            res.data = bout.toByteArray();

            org.admnkz.crypto.data.Certificate certificate = sets.getCertificate();
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate clientCert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(certificate.getBody())));


            cms = new CMSSignedData(new CMSProcessableByteArray(res.data), sig);
            Store                   certStore = cms.getCertificates();
            SignerInformationStore  signers = cms.getSignerInfos();
            Collection              c = signers.getSigners();
            Iterator                it = c.iterator();

            SignerInformation signerInfo = (SignerInformation)signers.getSigners().iterator().next();
            boolean verified = signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(clientCert.getPublicKey()));
            if (!verified) {
                throw new CryptoException("подпись PKCS#7 не верна");
            }

            return (byte[]) cms.getSignedContent().getContent();
        } catch (Exception e) {
            throw new CryptoException("Не удалось проверить подпись PKCS#7", e);
        }

    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MimeMultipart signMime(MimeBodyPart source, String optkey) throws CryptoException
	{
		Settings sets = getSettings(optkey);
		return signMime(source, sets);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MimeMultipart signMime(MimeBodyPart source, Settings sets) throws CryptoException
	{
		Certificate endCert = sets.getCertificate(); 
		if (endCert == null)
			throw new CryptoException("В настройках " + sets.getID() + " не указан сертификат для подписания");
		if (! endCert.hasPrivateKey())
			throw new CryptoException("У выбранного вами сертификата нет закрытого ключа");
		
		Map<String,String> mcaps = getProviderCapabities(sets.getProviderName());

		MimeMultipart mm = null;
		try {
			SecureRandom rnd = retrieveSecureRandom(sets.getCertificate().getId(), sets, mcaps);

			// make cert store with signer certificate and all CAs			
			List<X509Certificate> certList = listX509CertPath(endCert);
			Store certs = new JcaCertStore(certList);
			
			// make signer from alias My 
			PrivateKey pvk = decodePrivateKey(Base64.decodeBase64(endCert.getPrivateKey()), endCert.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), endCert.getPrivateKeyPassword());
			X509Certificate signCert = certList.get(0);			
			
			mm = createMultipartWithSignature(pvk, signCert, certs, source, sets.getProviderName(), sets.getSignatureAlg(), rnd);
		} catch (Exception e) {
			 throw new CryptoException(e);
		}
		return mm;
	}	
	
    public static MimeMultipart createMultipartWithSignature(
            PrivateKey      key,
            X509Certificate cert,
            Store       certsAndCRLs,
            MimeBodyPart    dataPart, 
            String providerName, String signatureAlg, SecureRandom rnd) 
        throws Exception
    {
        // create some smime capabilities in case someone wants to respond
        ASN1EncodableVector         signedAttrs = new ASN1EncodableVector();
        SMIMECapabilityVector       caps = new SMIMECapabilityVector();

        caps.addCapability(SMIMECapability.dES_EDE3_CBC);
        caps.addCapability(SMIMECapability.rC2_CBC, 128);
        caps.addCapability(SMIMECapability.dES_CBC);

        signedAttrs.add(new SMIMECapabilitiesAttribute(caps));
        signedAttrs.add(new SMIMEEncryptionKeyPreferenceAttribute(SMIMEUtil.createIssuerAndSerialNumberFor(cert)));

        // set up the generator
        SMIMESignedGenerator gen = new SMIMESignedGenerator();

		JcaContentSignerBuilder cbuild = new JcaContentSignerBuilder(signatureAlg);
		cbuild.setProvider(providerName);
		if (rnd != null)
			cbuild.setSecureRandom(rnd);
		ContentSigner asigner = cbuild.build(key);	
		
		JcaSignerInfoGeneratorBuilder bgen = new JcaSignerInfoGeneratorBuilder( new JcaDigestCalculatorProviderBuilder().setProvider(providerName).build());
		bgen.setDirectSignature(true);			
		gen.addSignerInfoGenerator(bgen.build(asigner, cert));			

		gen.addCertificates(certsAndCRLs);
    
        MimeMultipart mm = gen.generate(dataPart);
        return mm;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifyMime(MimeMultipart source, String optkey, boolean bSignatureOnly)
    	throws CryptoException
    {
    	Settings sets = getSettings(optkey);
    	return verifyMime(source, sets, bSignatureOnly);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean verifyMime(MimeMultipart source, Settings sets, boolean bSignatureOnly)
            throws CryptoException {
        try {
            SMIMESigned cms = new SMIMESigned(source);
            Store certStore = cms.getCertificates();
            SignerInformationStore signers = cms.getSignerInfos();
            Collection c = signers.getSigners();
            Iterator it = c.iterator();

            while (it.hasNext()) {
                SignerInformation signer = (SignerInformation) it.next();
                Collection certCollection = certStore.getMatches(signer.getSID());

                Iterator certIt = certCollection.iterator();
                X509CertificateHolder cert = (X509CertificateHolder) certIt.next();

                JcaSimpleSignerInfoVerifierBuilder bld = new JcaSimpleSignerInfoVerifierBuilder();
                bld.setProvider(sets.getProviderName());
                SignerInformationVerifier siv = bld.build(cert);

                boolean bValid = signer.verify(siv);
                if (!bValid) {
                    return false;
                }
                if (bSignatureOnly) continue;
                bValid = bValid && verifyCertificate(cert.getEncoded());
                if (!bValid)
                    return false;
            }
            return true;
        } catch (Exception e) {
            throw new CryptoException("Не удалось проверить подпись сообщения MIME", e);
        }

    }
    
    public Set<TrustAnchor> extractTrustAnchors() throws Base64DecodingException, CryptoException 
    {
    	List<Certificate> lstRoots = listRootCAs();
    	HashSet<TrustAnchor> res = new HashSet<TrustAnchor>( lstRoots.size());
    	
    	for (Certificate cert: lstRoots) 
    	{
    		X509Certificate xcert = makeCert(Base64.decodeBase64(cert.getBody()), cert.getProviderName());
    		TrustAnchor ta = new TrustAnchor(xcert, null);
    		res.add(ta);
    	}
    	return res;
    }
    
    protected List<Certificate> listRootCAs() {
    	return this.listCertificates(0, -1, null, SubjectType.ROOT_CA.getID(), Status.ACTIVE.getID(), null, null, null, null,null,null, null);
    }

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Element signXML(Element esource, String setid, IExternalSigning isign)
			throws CryptoException 
	{
		Settings sets = getSettings(setid);
		return signXML(esource, sets, isign);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Element signXML(Element esource, Settings sets, 	IExternalSigning isign) throws CryptoException 
	{
		checkInit();
		
		Certificate cc = sets.getCertificate();
		
		byte[] cbody;
		cbody = Base64.decodeBase64( cc.getBody());
		
		Map<String,String> mcaps = getProviderCapabities(sets.getProviderName());
		X509Certificate xcert = makeCert( cbody, cc.getProviderName());
		
		try {
        	byte[] cpriv = null;
        	if (cc.hasPrivateKey())
        		cpriv = Base64.decodeBase64(cc.getPrivateKey());
        	
    		SecureRandom random = retrieveSecureRandom(cc.getId(), sets, mcaps);
			
    		String refid = esource.getAttribute("Id");
			if ( StringUtils.isEmpty(refid)) {
				refid = "AppData1";
				esource.setAttribute("Id", refid);
				esource.setIdAttribute("Id", true);
			}
			
			Provider xmlDP = (Provider) Class.forName(sets.getXmlDSigProviderName()).newInstance();
	        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",xmlDP);

		    List<Transform> transformList = new ArrayList<Transform>();
		    Transform transform = fac.newTransform(Transform.ENVELOPED, (XMLStructure) null);
		    Transform transformC14N = fac.newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS, (XMLStructure) null);
		    transformList.add(transform);
		    transformList.add(transformC14N);		

	        Reference ref = fac.newReference("#AppData1", fac.newDigestMethod(sets.getXmlDigestAlg(), null), transformList, null, null);
			SignedInfo si = fac.newSignedInfo( fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, 
						(C14NMethodParameterSpec) null),
				   fac.newSignatureMethod(sets.getXmlSignatureAlg(), null),
				   		Collections.singletonList(ref));
			
			// Prepare key information to verify signature in future on other side
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			X509Data x509d = kif.newX509Data(Collections.singletonList(xcert));
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));
			
			// Create signature and sign by private key
			PrivateKey pvk1 = decodePrivateKey(cpriv, cc.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), cc.getPrivateKeyPassword());
			javax.xml.crypto.dsig.XMLSignature sig = fac.newXMLSignature(si, ki);
			DOMSignContext signContext = new DOMSignContext(pvk1, esource);
			signContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");
	        signContext.setProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider", Security.getProvider(sets.getProviderName()));
	        signContext.setProperty("org.jcp.xml.dsig.internal.dom.SecureRandom", random);
	        signContext.setProperty("org.jcp.xml.dsig.internal.dom.DigestProvider", Security.getProvider(sets.getDigestProviderName()));  
			sig.sign(signContext);
			
			Element edsig = (Element) CryptoUtils.findNodeByNameNS((Element) signContext.getParent(), "ds","Signature");
			// TODO
			return edsig;
		} catch (Exception ex) {
			throw new CryptoException("Не удалось создать подпись XMLDsig", ex);
		}
	}

	@Override
	public ParseCMSResult parseCMS(byte[] sig) throws CryptoException 
	{
		checkInit();
		
		try {
			ParseCMSResult res = new ParseCMSResult();
        	res.signature = sig;
        	
	        CMSSignedData cms = new CMSSignedData(sig);
	        if (cms.getSignedContent() != null) {
	        	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        	try {
	        		cms.getSignedContent().write(bout);
	        	} finally {
	        		bout.close();
	        	}
	        	res.data = bout.toByteArray();
	        }
	        
	        Store                   certStore = cms.getCertificates();
	        SignerInformationStore  signers = cms.getSignerInfos();
	        Collection              c = signers.getSigners();
	        Iterator                it = c.iterator();
	        
	        int verified = 0;
	        while (it.hasNext())
	        {
	            SignerInformation   signer = (SignerInformation)it.next();
	            res.digestAlg = signer.getDigestAlgorithmID().getAlgorithm().toString();
	            Collection          certCollection = certStore.getMatches(signer.getSID());

	            Iterator              certIt = certCollection.iterator();
	            while (certIt.hasNext()) {
	            	X509CertificateHolder cert = (X509CertificateHolder)certIt.next();
	            	res.certs.add(cert.getEncoded());
	            }
	            
	        }
			return res;
	    } catch (Exception e) {
	    	LOGGER.error("Не удалось извлечь cms result PKCS#7", e);
	    	throw new CryptoException("Не удалось извлечь cms result  PKCS#7", e);
	    }
	}

	@Override
	public ParseCertResult parseCertificate(byte[] cbody)
		throws CryptoException 
	{
		checkInit();
		
		try {
			ParseCertResult res = new ParseCertResult();
			res.certificate = makeCert(cbody, "");

			// ищем подходящий криптопровайдер, который поддерживает алгоритмы этого сертификата			
			String[] provs = listProvidersFor("Alg.Alias.Signature." + res.certificate.getSigAlgOID(), false);
			if ( provs == null || provs.length == 0)
				throw new CryptoException("Не найден подходящий провайдер для алгоритма " + res.certificate.getSigAlgOID());
			if (ArrayUtils.contains(provs, getDefaultProviderName()))
				res.providerName = getDefaultProviderName();
			else
				res.providerName = provs[0];
			res.certificate = makeCert(cbody, res.providerName);
			res.sha1hash =  CryptoUtils.extractSha1(res.certificate, mdSha1);
			return res;
		} catch (CertificateEncodingException e) {
			throw new CryptoException(e);
		}
		
	}
	
	@Override
	public byte[] exportChain(List<byte[]> certs) throws CryptoException
	{
		checkInit();
		
		List<X509Certificate> lst = new ArrayList<X509Certificate>(certs.size()); 
		
		
		for (byte[] ares: certs) {
			X509Certificate xcert = makeCert(ares, getDefaultProviderName());
			lst.add(xcert);
		}
		
		try {
			CertificateFactory cf = retrieveCertFactory(getDefaultProviderName());
			CertPath cpath = cf.generateCertPath(lst);
			byte[] res = cpath.getEncoded("PKCS7");
			return res;
		} catch (Exception ex) {
			throw new CryptoException(ex);
		}		
	}
	
	@Override
	public byte[] exportChain(String certID) throws CryptoException
	{
		checkInit();
		
		try {
			Certificate cert = getCertificate(certID);
			List<X509Certificate> lst = listX509CertPath(cert);
			
			CertificateFactory cf = retrieveCertFactory(getDefaultProviderName());
			CertPath cpath = cf.generateCertPath(lst);
			byte[] res = cpath.getEncoded("PKCS7");
			return res;
		} catch (Exception ex) {
			throw new CryptoException(ex);
		}
	}

	@Override
    public void setSSLProperties() {
        Security.setProperty("ssl.SocketFactory.provider", "ru.CryptoPro.ssl.SSLSocketFactoryImpl");
        LOGGER.info("ru.CryptoPro.ssl.SSLSocketFactoryImpl");
        Security.setProperty("ocsp.enable", "false");
        System.setProperty("javax.net.ssl.supportGVO","true");
        //System.setProperty("javax.net.debug", "ssl");
        //System.setProperty("com.sun.security.enableCRLDP", "true");
        //System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        //System.setProperty("sun.security.ssl.allowLegacyHelloMessages","true");
        System.setProperty("ru.CryptoPro.ssl.allowUnsafeRenegotiation", "true");
        LOGGER.info("renegotiation");
        System.setProperty("ru.CryptoPro.ssl.allowLegacyHelloMessages", "true");
        LOGGER.info("hellomessages");
       // ru.CryptoPro.CAdES.tools.Utility.initJCPAlgorithms();
    }

	@Override
	public void setSystemCryptoProperties(String clentSettingsID,String serverSettingsID,String trustStoreName) throws SecurityException{
		Settings clientSets = getSettings(clentSettingsID);
		Settings serverSets = getSettings(serverSettingsID);
		System.setProperty("javax.net.ssl.supportGVO","true");
		Properties systemProps = System.getProperties();
		//key store
		LOGGER.info("инициализируем key store " + clientSets.getCertificate().getPrivateKeyPath());
		systemProps.put("javax.net.ssl.keyStoreType",clientSets.getCertificate().getPrivateKeyPath());
		String clientPassword = clientSets.getCertificate().getPrivateKeyPassword();
		if (StringUtils.isNotEmpty(clientPassword)) {
			systemProps.put("javax.net.ssl.keyStorePassword", clientPassword.toCharArray());
		}
		String dir=options.getValue(CryptoUtils.KEYS_DIR);
		
		systemProps.put("javax.net.ssl.trustStoreType", serverSets.getCertificate().getPrivateKeyPath());
		
		if (StringUtils.isNotEmpty(dir))
		{
		  systemProps.put("javax.net.ssl.keyStore",	dir);
		  systemProps.put("javax.net.ssl.trustStore", dir+trustStoreName);
		  LOGGER.info("путь к trust store " + dir + trustStoreName);
		}
		//trust store
		LOGGER.info("инициализируем trust store " + serverSets.getCertificate().getPrivateKeyPath());
		String serverPassword = serverSets.getCertificate().getPrivateKeyPassword();
	   
	    systemProps.put("javax.net.ssl.trustStorePassword", serverPassword.toCharArray());
	    System.setProperties(systemProps);
	    
	}

	@Override
	public boolean verifyJCPCms(byte[] data, String settingId) throws CryptoException {

		checkInit();

		Certificate endCert = getSettings(settingId).getCertificate();

		X509Certificate acert = makeCert(Base64.decodeBase64( endCert.getBody()),  getSettings(settingId).getProviderName());

		if (data==null){
			throw new CryptoException("Пустые данные");
		}
		
	    CMSSignedData cms=null;
		try {
			cms = new CMSSignedData(data);
		} catch (Exception e) {
            throw new CryptoException("Не удалось обновить cms "+e);
		}

	     ByteArrayOutputStream stm = new ByteArrayOutputStream();
	     try {
			cms.getSignedContent().write(stm);
		} catch (IOException e) {
			throw new CryptoException("Не удалось обновить cms "+e);
		} catch (CMSException e) {
			throw new CryptoException("Не удалось обновить cms "+e);
		}
	     CertificateFactory bcCertFact =null;
	     try {
			 bcCertFact = CertificateFactory.getInstance("X.509", "BC");
		} catch (CertificateException e) {
			throw new CryptoException("Не удалось создать фабрику сертификатов "+e);
		} catch (NoSuchProviderException e) {
			throw new CryptoException("Не удалось создать фабрику сертификатов - нет алгоритма "+e);
		}

	     int verified = 0;

         Store certStore = cms.getCertificates();
         SignerInformationStore signers = cms.getSignerInfos();
         Collection c = signers.getSigners();
         Iterator it = c.iterator();

         while (it.hasNext())
         {
             SignerInformation signer = (SignerInformation) it.next();
             Collection certCollection = certStore.getMatches(signer.getSID());

             Iterator certIt = certCollection.iterator();
             X509CertificateHolder certs = null;
             if (certCollection.size()>0)
                certs = (X509CertificateHolder) certIt.next();

             boolean bVerified = false;
             try {

                 bVerified = signer.verify(acert.getPublicKey(), "JCP");
             } catch (Throwable ex) {
            	 if (bcCertFact!=null&&certs!=null)
            	 {
                  X509Certificate xcert=null;
				  try {
					  xcert = (X509Certificate) bcCertFact.generateCertificate(new ByteArrayInputStream(
					         certs.getEncoded()));
				  } catch (CertificateException e) {
					  throw new CryptoException("Не удалось создать сертификат для проверки "+e);
				  } catch (IOException e) {
					  throw new CryptoException("Не удалось создать сертификат для проверки "+e);
				  }
                   try {
                       bVerified = signer.verify(xcert.getPublicKey(), "JCP");
                   } catch (Throwable ex1) {
                	   throw new CryptoException("Не удалось проверить сообщение "+ex1);
                  }
            	 }
            	 else {
            	   throw new CryptoException("Не удалось проверить сообщение "+ex);
            	 }
             }
             if (bVerified){
                 verified++;
             }

         }

		return (verified == c.size());
	}
		
    private SecureRandom createSecureRandom(String privateKeyDirectory, String pkPass){
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("GOST28147PRNG", CryptoUtils.PROVIDER_SIGNALCOM);
            random.setSeed(new String(privateKeyDirectory + ";NonInteractive;Password=" + pkPass).getBytes());
        } catch (Exception e) {
            try {
                throw new SignatureException("Cannot init class secure random", e);
            } catch (SignatureException e1) {
                LOGGER.error("Cannot init class secure random", e1);
            }
        }
        return random;
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] signContactRequest(byte[] source, String passwdPk, String alias, String keyStoreName, String testKeysDir) throws CryptoException{
       
        String testKeyString = null;
        try {

            testKeyString = options.getValue(CryptoUtils.KEYS_DIR) +"/ContactKeys/" + keyStoreName;
        } catch (Exception e) {
            LOGGER.error("ошибка при создании пути к закрытому ключу "+e);
        }
        if(testKeyString == null){
            testKeyString=testKeysDir+"/ContactKeys/" + keyStoreName;
        }
        String storeString = testKeyString + "/contactStore.pfx";
        LOGGER.info(
                "*******DEBUG************signContactRequest; testKeyString=" + testKeyString + "; passwdPk=" + passwdPk);
        SecureRandom random = createSecureRandom(testKeyString, passwdPk);
        FileInputStream in;
        OutputStream sigOut;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream inp;
        byte[] sig = null;
        try {
            KeyStore keyStore = null;
            try {
                File fin = new File(storeString);
                in = new FileInputStream(fin);
                keyStore = KeyStore.getInstance("PKCS#12", CryptoUtils.PROVIDER_SIGNALCOM);
                keyStore.load(in, passwdPk.toCharArray());
            } catch (Exception e) {
            	 LOGGER.error("Ошибка при загрузке ключа из хранилища ",e);
            	 throw new CryptoException("Ошибка при загрузке ключа из хранилища ",e);
            } 
            List certs = new ArrayList();
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

            //указываем, каким сертификатом будем подписывать
            certs.add(cert);
            PrivateKey priv = (PrivateKey) keyStore.getKey(alias, passwdPk.toCharArray());


            SignedDataGenerator generator = new SignedDataGenerator(out);
            ru.signalcom.crypto.cms.Signer signer = null;
            try {
                signer = new ru.signalcom.crypto.cms.Signer(priv, cert, random);
            } catch (Throwable e) {
            	LOGGER.error("Ошибка при установке криптопровайдера для подписания ",e);
            	throw new CryptoException("Ошибка при установке криптопровайдера для подписания ",e);
            }
            // экземпляр объекта SecureRandom должен быть предварительно проинициализирован!
            generator.addSigner(signer);
            generator.addCertificatesAndCRLs(CertStore.getInstance("Collection", new CollectionCertStoreParameters(certs)));
            //для создания отсоединенной подписи, т.е такой, которая не включает в себя данные, ставим true
            generator.setDetached(true);
            sigOut = generator.open();
            inp = new ByteArrayInputStream(source);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inp.read(buf)) > 0) {
                sigOut.write(buf, 0, len);
            }
            generator.close();
            sig = out.toByteArray();
        } catch (Exception e) {
        	LOGGER.error("Не удалось подписать CMS сообщение", e);
            throw new CryptoException("Не удалось подписать CMS сообщение", e);
        } finally {

        }
        return sig;
    }

	@Override
	public int runCryptcp(String sets, String certName,
			String inputFileName, String outputFileName, boolean isDer) throws CryptoException{
		String keysDir = options.getValue(CryptoUtils.KEYS_DIR);
		String dir = options.getValue(CryptoUtils.CRYPTCP_DIR);
		if (StringUtils.isNotEmpty(dir)){
			Runtime runtime = Runtime.getRuntime();
	  		Process process = null;
	  		try {
	  			String der=isDer?"-der ":"";
	  			process=runtime.exec(dir+" -encr -f "+keysDir+certName+" "+sets+" "+der+inputFileName+" "+outputFileName);
	  			process.waitFor();
	  		} catch (Exception e){
	  			LOGGER.error("Не удалось зашифровать сообщение", e);
				throw new CryptoException("Не удалось зашифровать сообщение "+e.getMessage());
	  		}
	  		return process.exitValue();
		} else {
			LOGGER.error("Не удалось найти путь к утилите cryptcp");
			throw new CryptoException("Не удалось найти путь к утилите cryptcp");
		}
		
	}

	@Override
	public String makePKCS7Encrypted(byte[] source,boolean inStr) throws CryptoException {
		StringBuilder sb = new StringBuilder();
		try {
        	sb.append("----- BEGIN PKCS7 ENCRYPTED -----"); 
        	if (!inStr){
        		sb.append("\r\n");
        	}
			sb.append(new String(Base64.encodeBase64(source)));
			if (!inStr){
        		sb.append("\r\n");
        	}
			sb.append("----- END PKCS7 ENCRYPTED -----");
			return sb.toString();
		 } catch (Exception e) {
			LOGGER.error("Не удалось перекодировать файл", e);
			throw new CryptoException("Не удалось перекодировать файл "+e);
		 }
	}

	@Override
	public String makePKCS7Signed(byte[] source,boolean inStr) throws CryptoException {
		StringBuilder sb = new StringBuilder();
		try {
        	sb.append("-----BEGIN PKCS7-----"); 
        	if (!inStr){
        		sb.append("\r\n");
        	}
			sb.append(new String(Base64.encodeBase64(source)));
			if (!inStr){
        		sb.append("\r\n");
        	}
			sb.append("-----END PKCS7-----");
			return sb.toString();
		 } catch (Exception e) {
			LOGGER.error("Не удалось перекодировать файл", e);
			throw new CryptoException("Не удалось перекодировать файл "+e);
		 }
	}

	private byte[] wrap(SecretKey secretKey, Key recipientKey) throws Exception {

		// Генерация эфемерной пары.

		java.security.KeyPairGenerator kgp =  KeyPairGenerator.getInstance(
			JCP.GOST_EL_DH_EPH_NAME,
			CryptoProvider.PROVIDER_NAME
		);

		// Устанавливаем нужные параметры, как у получателя.

		GostPublicKey repPublicKey =  (GostPublicKey) recipientKey;
		ParamsInterface repPublicKeySpec = repPublicKey.getSpec().getParams();
		kgp.initialize(repPublicKeySpec);

		// Генерируем эфемерную пару.

		KeyPair ephPair = kgp.generateKeyPair();

		PrivateKey privateKey = ephPair.getPrivate();
		PublicKey publicKey = ephPair.getPublic();

		byte[] syncro = new byte[8];
		SecureRandom random = SecureRandom.getInstance(JCP.CP_RANDOM, JCP.PROVIDER_NAME);
		random.nextBytes(syncro);

		IvParameterSpec iv = new IvParameterSpec(syncro);
		OID oid = CryptParamsSpec.OID_Crypt_VerbaO;

		// Выработка ключа согласования.

		KeyAgreement ka = KeyAgreement.getInstance(privateKey.getAlgorithm(), CryptoProvider.PROVIDER_NAME);
		ka.init(privateKey, iv);

		ka.doPhase(recipientKey, true);
		Key dh = ka.generateSecret("GOST28147");

		//Зашифрование симметричного ключа на ключе согласования
		// отправителя.

		final Cipher cipher = Cipher.getInstance("GOST28147", CryptoProvider.PROVIDER_NAME);
		cipher.init(Cipher.WRAP_MODE, dh, (SecureRandom) null);

		final byte[] wrappedKey = cipher.wrap(secretKey);

		// Упаковка параметров и ключа.

		Gost28147_89_EncryptedKey encryptedKey = new Gost28147_89_EncryptedKey();
		Asn1BerDecodeBuffer decoder = new Asn1BerDecodeBuffer(wrappedKey);
		encryptedKey.decode(decoder);

		byte[] imita = encryptedKey.macKey.value;
		byte[] wrapperKeyBytes = encryptedKey.encryptedKey.value;

		// Кодирование открытого ключа в SubjectPublicKeyInfo.

		byte[] publicKeyBytes = publicKey.getEncoded();
		SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo();

		decoder = new Asn1BerDecodeBuffer(publicKeyBytes);
		publicKeyInfo.decode(decoder);

		// Кодирование GostR3410_KeyTransport.

		GostR3410_KeyTransport keyTransport = new GostR3410_KeyTransport();
		Asn1BerEncodeBuffer encoder = new Asn1BerEncodeBuffer();

		keyTransport.sessionEncryptedKey = new Gost28147_89_EncryptedKey(wrapperKeyBytes, imita);
		keyTransport.transportParameters = new GostR3410_TransportParameters(
			new Gost28147_89_ParamSet(oid.value),
			publicKeyInfo,
			new Asn1OctetString(iv.getIV()));

		keyTransport.encode(encoder);
		return encoder.getMsgCopy();

	}


	@Override
	public byte[] encryptPKCS7(byte[] data, String serverId,String filename) throws Exception {
		// Сертификат получателя.
		Certificate serverCert = getSettings(serverId).getCertificate();
		X509Certificate acert = makeCert(Base64.decodeBase64( serverCert.getBody()),  getSettings(serverId).getProviderName());
		final PublicKey recipientPublic = acert.getPublicKey();

		// Генерирование симметричного ключа с параметрами
		// шифрования из контрольной панели.

	//	KeyGenerator kg = KeyGenerator.getInstance("GOST28147", CryptoProvider.PROVIDER_NAME);
		final ParamsInterface paramss = AlgIdSpec.getDefaultCryptParams();
     
		//***для отладки, потом убрать!!!
        Enumeration e=paramss.getOIDs();
        while (e.hasMoreElements()){
        	String st=e.nextElement().toString();
        	LOGGER.info("Параметр " + st);
        }
        //***
         		
	//	kg.init(paramss);
		
		KeyGenerator kg = KeyGenerator.getInstance("GOST28147");
		final SecretKey simm = kg.generateKey();
			    
		// Зашифрование текста на симметричном ключе.

		Cipher cipher = Cipher.getInstance("GOST28147/CFB/NoPadding", CryptoProvider.PROVIDER_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, simm, (SecureRandom) null);

		final byte[] iv = cipher.getIV();
		final byte[] text = cipher.doFinal(data, 0, data.length);

		// Зашифрование симметричного ключа.

		final byte[] keyTransport = wrap(simm, recipientPublic);

	   // Формирование CMS-сообщения.

		final ContentInfo all = new ContentInfo();
		all.contentType = new Asn1ObjectIdentifier(new OID("1.2.840.113549.1.7.3").value);

		final EnvelopedData cms = new EnvelopedData();

		all.content = cms;
	    cms.version = new CMSVersion(0);

		cms.recipientInfos = new RecipientInfos(1);
		cms.recipientInfos.elements = new RecipientInfo[1];
		cms.recipientInfos.elements[0] = new RecipientInfo();

		final KeyTransRecipientInfo keytrans = new KeyTransRecipientInfo();
		keytrans.version = new CMSVersion(0);

		final Asn1BerEncodeBuffer ebuf = new Asn1BerEncodeBuffer();

		final AlgIdInterface algid = new AlgIdSpec(JCP.GOST_EL_KEY_OID);
		final AlgorithmIdentifier id = (AlgorithmIdentifier) algid.getDecoded();
		id.encode(ebuf);

		Asn1BerDecodeBuffer dbuf = new Asn1BerDecodeBuffer(ebuf.getMsgCopy());
		keytrans.keyEncryptionAlgorithm = new KeyEncryptionAlgorithmIdentifier();
		keytrans.keyEncryptionAlgorithm.decode(dbuf);

		ebuf.reset();
		dbuf.reset();
			    
		keytrans.rid = new RecipientIdentifier();
		final IssuerAndSerialNumber issuer = new IssuerAndSerialNumber();

		final X500Principal issuerName = acert.getIssuerX500Principal();
		dbuf = new Asn1BerDecodeBuffer(issuerName.getEncoded());
			    
		issuer.issuer = new Name();
		final RDNSequence rnd = new RDNSequence();
		rnd.decode(dbuf);
			    
		issuer.issuer.set_rdnSequence(rnd);
		issuer.serialNumber = new CertificateSerialNumber(acert.getSerialNumber());

		keytrans.rid.set_issuerAndSerialNumber(issuer);
		dbuf.reset();

		keytrans.encryptedKey = new EncryptedKey(keyTransport);
		ebuf.reset();
			    
		cms.recipientInfos.elements[0].set_ktri(keytrans);
		cms.encryptedContentInfo = new EncryptedContentInfo();

		final OID contentType = new OID("1.2.840.113549.1.7.1");
		cms.encryptedContentInfo.contentType = new ContentType(contentType.value);

		final Gost28147_89_Parameters params = new Gost28147_89_Parameters();
		params.iv = new Gost28147_89_IV(iv);

		params.encryptionParamSet = new Gost28147_89_ParamSet(paramss.getOID().value);
		cms.encryptedContentInfo.contentEncryptionAlgorithm = new ContentEncryptionAlgorithmIdentifier(
			_Gost28147_89_EncryptionSyntaxValues.id_Gost28147_89, params);

		cms.encryptedContentInfo.encryptedContent = new EncryptedContent(text);
		all.encode(ebuf);
		if (StringUtils.isNotEmpty(filename)){
	           Array.writeFile(filename, ebuf.getMsgCopy());
	    }
		return ebuf.getMsgCopy();

	}

	@Override
	public  boolean decryptPKCS7(String clientId, byte[] pkcs7Env, byte[] inData, 
			boolean detached, String outDataFile) throws CryptoException {
		
		boolean verified = false;
		
		try{
		// Разбор CMS-сообщения
		Asn1BerDecodeBuffer dbuf = new Asn1BerDecodeBuffer(pkcs7Env);
		final ContentInfo all = new ContentInfo();
		all.decode(dbuf);
		dbuf.reset();
		
		final EnvelopedData cms = (EnvelopedData) all.content;

		KeyTransRecipientInfo keytrans = new KeyTransRecipientInfo();
		if (cms.recipientInfos.elements[0].getChoiceID() == RecipientInfo._KTRI)
	        keytrans = (KeyTransRecipientInfo) (cms.recipientInfos.elements[0].getElement());
		
		final Asn1BerEncodeBuffer ebuf = new Asn1BerEncodeBuffer();
		dbuf = new Asn1BerDecodeBuffer(keytrans.encryptedKey.value);
		final GostR3410_KeyTransport encrKey = new GostR3410_KeyTransport();
		encrKey.decode(dbuf);
		dbuf.reset();
		
		encrKey.sessionEncryptedKey.encode(ebuf);
		final byte[] wrapKey = ebuf.getMsgCopy();
		ebuf.reset();
		
		encrKey.transportParameters.ephemeralPublicKey.encode(ebuf);
		final byte[] encodedPub = ebuf.getMsgCopy();
		ebuf.reset();
		
		final byte[] sv = encrKey.transportParameters.ukm.value;
		final Gost28147_89_Parameters params = (Gost28147_89_Parameters)
            cms.encryptedContentInfo.contentEncryptionAlgorithm.parameters;
		final byte[] iv = params.iv.value;
		final OID cipherOID = new OID(params.encryptionParamSet.value);
		final byte[] text = cms.encryptedContentInfo.encryptedContent.value;

        Settings sets=getSettings(clientId);
		
		//наш сертификат
		Certificate clientCert = sets.getCertificate();
	    PrivateKey recipientKey=null;
		X509Certificate ccert = makeCert(Base64.decodeBase64( clientCert.getBody()),  getSettings(clientId).getProviderName());
			
		//закрытый ключ наш
        //для крипто-про
        if (sets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
        	try {
        		recipientKey=decodePrivateKeyJCP(clientCert.getPrivateKeyAlias(),clientCert.getPrivateKeyPassword());
			} catch (Exception e) {
			   LOGGER.error("Не удалось инициализировать закрытый ключ", e);
			   throw new CryptoException("Не удалось инициализировать закрытый ключ "+e);
			}
        }
        else {
        	recipientKey=decodePrivateKey(Base64.decodeBase64(clientCert.getPrivateKey()), clientCert.getPrivateKeyPath(), sets.getProviderName(), sets.getPrivateKeyAlg(), clientCert.getPrivateKeyPassword());
        }
        
		
		// Отправитель - открытый ключ из cms
		final X509EncodedKeySpec pspec = new X509EncodedKeySpec(encodedPub);
		final KeyFactory kf = KeyFactory.getInstance(JCP.GOST_EL_DH_NAME);
		final PublicKey senderPublic = kf.generatePublic(pspec);

		// Выработка ключа согласования получателем
		final KeyAgreement recipientKeyAgree = KeyAgreement.getInstance(JCP.GOST_EL_DH_NAME);
		recipientKeyAgree.init(recipientKey, new IvParameterSpec(sv), null);
		recipientKeyAgree.doPhase(senderPublic, true);
		final SecretKey secret = recipientKeyAgree.generateSecret("GOST28147");

		// Расшифрование симметричного ключа.
		final Cipher cipher = Cipher.getInstance("GOST28147/CFB/NoPadding");
		cipher.init(Cipher.UNWRAP_MODE, secret, (SecureRandom) null);
		final SecretKey simmKey = (SecretKey) cipher.unwrap(wrapKey, null, Cipher.SECRET_KEY);

		// Расшифрование текста на симметричном ключе.
		final GostCipherSpec spec = new GostCipherSpec(iv, cipherOID);
		cipher.init(Cipher.DECRYPT_MODE, simmKey, spec, null);
		final byte[] result = cipher.doFinal(text, 0, text.length);
	    
		// Проверка подписи PKCS7.
		// TODO проверка подписи
		verified = true;

		// Извлечение текста, если он есть (для attached).
		final Asn1BerDecodeBuffer asnBuf = new Asn1BerDecodeBuffer(result);
		final ContentInfo cInfo = new ContentInfo();
		cInfo.decode(asnBuf);
		final SignedData pkcs7 = (SignedData) cInfo.content;
		               
		// Сохранение текста в файл (для attached).
		if (pkcs7.encapContentInfo.eContent != null) {
			byte[] data = pkcs7.encapContentInfo.eContent.value;
			Array.writeFile(outDataFile, data);
		}
		
		
		} catch (Exception e1){
			LOGGER.error("Не удалось проверить расшифровать сообщение, ошибка", e1);
			throw new CryptoException("Не удалось проверить расшифровать сообщение, ошибка "+e1);
		}
		return verified;
	}

	@Override
	public String extractXmlFromPkcs7(byte[] source, String encoding)
			throws CryptoException {

		String xmlString ="";  
		try{
		  CMSSignedData cms = new CMSSignedData(source);
	      ByteArrayOutputStream stream = new ByteArrayOutputStream();
	      cms.getSignedContent().write(stream);
	      xmlString = new String(stream.toByteArray(), encoding);
		} catch (Exception e){
			LOGGER.error("Произошла ошибка при попытке вытащить данные из сообщения", e);
			throw new CryptoException("Произошла ошибка при попытке вытащить данные из сообщения "+e);
		}

		return xmlString;
	}

	@Override
	public SSLContext createTrustedEmptyGostSSLContext() {
		setSSLProperties();
		SecureRandom random =null;
		try {
			random = SecureRandom.getInstance(JCP.CP_RANDOM, CryptoUtils.PROVIDER_CRYPTOPRO);
		} catch (Exception e) {
			LOGGER.error("Ошибка при инициализации secure random", e);
		}
        LOGGER.info("properites and random JCP");
        SSLContext ctx = null;
        try {
        	ctx= SSLContext.getInstance("GostTLS");
			ctx.init(null, trustAll(), random);
		} catch (Exception e) {
			LOGGER.error("Ошибка при создании SSL Context", e);
		}
		return ctx;
	}

	@Override
	public TrustManager[] trustAll() {
		return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
	}
	
	@Override
	public byte[] decryptMessage(String clientId,File encodedMessage) throws CryptoException{
	
		byte[] enc;
		try {
			enc = Array.readFile(encodedMessage);
		} catch (IOException e2) {
			LOGGER.error("Произошла ошибка при попытке раскодировать сообщение "+e2);
			return null;
		}
		return decryptMessage(clientId,enc);
	}

	@Override
	public byte[] decryptMessage(String clientId,byte[] encodedMessage) throws CryptoException{
		
		ByteArrayOutputStream decryptedByteDataStream = new ByteArrayOutputStream();
	
		EnvelopedSignature signature;
		try {
			signature = new EnvelopedSignature(new ByteArrayInputStream(encodedMessage));
		} catch (Exception e1) {
			LOGGER.error("Произошла ошибка при попытке раскодировать сообщение "+e1);
			return null;
		}
        Settings sets=getSettings(clientId);
		
		//наш сертификат
		Certificate clientCert = sets.getCertificate();
	    PrivateKey pvk=null;
		X509Certificate ccert = makeCert(Base64.decodeBase64( clientCert.getBody()),  getSettings(clientId).getProviderName());
			
		//закрытый ключ наш
        //для крипто-про
        if (sets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
        	try {
				pvk=decodePrivateKeyJCP(clientCert.getPrivateKeyAlias(),clientCert.getPrivateKeyPassword());
			} catch (Exception e) {
			   LOGGER.error("Не удалось инициализировать закрытый ключ", e);
			   throw new CryptoException("Не удалось инициализировать закрытый ключ "+e);
			}
        }
		try {
			signature.decrypt(ccert, pvk, decryptedByteDataStream);
		} catch (Exception e) {
			LOGGER.error("Произошла ошибка при попытке раскодировать сообщение "+e);
			return null;
		} 
		
		byte[] decryptedData = decryptedByteDataStream.toByteArray();
		return decryptedData;
	
	}
	
	@Override
    public  byte[] encryptCMS(String clientId,
        String serverId, byte[] data,String filename) throws CryptoException {

        ByteArrayOutputStream envelopedByteArrayOutStream = new ByteArrayOutputStream();

        Settings sets=getSettings(clientId);
        //наш сертификат
        Certificate clientCert = sets.getCertificate();
        Settings serverSets=getSettings(serverId);
		//сертификат получателя
		Certificate serverCert = serverSets.getCertificate();
	    PrivateKey pvk=null;
		X509Certificate ccert = makeCert(Base64.decodeBase64( clientCert.getBody()),  getSettings(clientId).getProviderName());
		X509Certificate scert = makeCert(Base64.decodeBase64( serverCert.getBody()),  getSettings(serverId).getProviderName());
		//закрытый ключ наш
        //для крипто-про
        if (sets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
        	try {
				pvk=decodePrivateKeyJCP(clientCert.getPrivateKeyAlias(),clientCert.getPrivateKeyPassword());
			} catch (Exception e) {
			   LOGGER.error("Не удалось инициализировать закрытый ключ", e);
			   throw new CryptoException("Не удалось инициализировать закрытый ключ "+e);
			}
        }
        EnvelopedSignature signature = new EnvelopedSignature(pvk, ccert);

        try {
			signature.addRecipient(scert);
		} catch (Exception e) {
			LOGGER.error("Не удалось добавить получателя", e);
			throw new CryptoException("Не удалось добавить получателя", e);
		}

        try {
			signature.open(envelopedByteArrayOutStream);
		} catch (Exception e) {
			LOGGER.error("Не удалось открыть подпись", e);
			throw new CryptoException("Не удалось открыть подпись", e);
		}
        
        try {
			signature.update(data);
		} catch (Exception e) {
			LOGGER.error("Не удалось обновить подпись", e);
			throw new CryptoException("Не удалось обновить подпись", e);
		}

        try {
			signature.close();
		} catch (Exception e) {
			LOGGER.error("Не удалось закрыть подпись", e);
			throw new CryptoException("Не удалось закрыть подпись", e);
		}
        if (StringUtils.isNotEmpty(filename)){
	         try {
				Array.writeFile(filename,envelopedByteArrayOutStream.toByteArray());
			} catch (IOException e) {
				LOGGER.error("Не удалось записать в файл", e);
				throw new CryptoException("Не удалось записать в файл", e);
			}
	    }
      
        return envelopedByteArrayOutStream.toByteArray();

    }

	@Override
	public  byte[] signDetachedCMS(String clientId, File inputFile,String filename) throws CryptoException {
		    Settings sets=getSettings(clientId);
	        //наш сертификат
	        Certificate clientCert = sets.getCertificate();
	   
			PrivateKey pvk=null;
			X509Certificate ccert = makeCert(Base64.decodeBase64( clientCert.getBody()),  getSettings(clientId).getProviderName());
			//закрытый ключ наш
	        //для крипто-про
	        if (sets.getProviderName().equalsIgnoreCase(CryptoUtils.PROVIDER_CRYPTOPRO)) {
	        	try {
					pvk=decodePrivateKeyJCP(clientCert.getPrivateKeyAlias(),clientCert.getPrivateKeyPassword());
				} catch (Exception e) {
				   LOGGER.error("Не удалось инициализировать закрытый ключ", e);
				   throw new CryptoException("Не удалось инициализировать закрытый ключ "+e);
				}
	        }
	        Signature signature = null;
	        try {
	            signature = Signature.getInstance(JCP.GOST_DHEL_SIGN_NAME);
	            signature.initSign(pvk);
	            FileInputStream fData = new FileInputStream(inputFile);

	            int read;
	            while ( (read = fData.read()) != -1) {
	                signature.update((byte)read);
	            }

	            fData.close();
	            final byte[] sign = signature.sign();
	            byte[] cms =createCMS(ccert,null,sign,true);
	            if (StringUtils.isNotEmpty(filename)){
	                Array.writeFile(filename, cms);
	            }
                return cms;
	        } catch (Exception e){
	        	LOGGER.error("Не удалось подписать файл "+e);
	        	throw new CryptoException(e);
	        }
		        
	}
	
	/**
	 * создаем cms
	 * @param signCert - наш сертификат
	 * @param data - данные для cms
	 * @param sign - подпись
	 * @param detached - отсоединенная подпись или нет
	 * @return
	 * @throws Exception
	 */
	protected byte[] createCMS(X509Certificate signCert,byte[] data,byte[] sign,boolean detached) throws Exception{
	    final ContentInfo all = new ContentInfo();
        all.contentType = new Asn1ObjectIdentifier(
                new OID("1.2.840.113549.1.7.2").value);

        final SignedData cms = new SignedData();
        all.content = cms;
        cms.version = new CMSVersion(1);

        // digest
        cms.digestAlgorithms = new DigestAlgorithmIdentifiers(1);
        final DigestAlgorithmIdentifier a = new DigestAlgorithmIdentifier(
                new OID(JCP.GOST_DIGEST_OID).value);

        a.parameters = new Asn1Null();
        cms.digestAlgorithms.elements[0] = a;

        if (detached) {
            cms.encapContentInfo = new EncapsulatedContentInfo(
                    new Asn1ObjectIdentifier(
                            new OID("1.2.840.113549.1.7.1").value), null);
        } // if
        else {
            cms.encapContentInfo =
                    new EncapsulatedContentInfo(new Asn1ObjectIdentifier(
                            new OID("1.2.840.113549.1.7.1").value),
                            new Asn1OctetString(data));
        } // else

        // certificate
        cms.certificates = new CertificateSet(1);
        final ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate certificate =
                new ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate();
        final Asn1BerDecodeBuffer decodeBuffer =
                new Asn1BerDecodeBuffer(signCert.getEncoded());
        certificate.decode(decodeBuffer);

        cms.certificates.elements = new CertificateChoices[1];
        cms.certificates.elements[0] = new CertificateChoices();
        cms.certificates.elements[0].set_certificate(certificate);

        cms.signerInfos = new SignerInfos(1);
        cms.signerInfos.elements[0] = new SignerInfo();
        cms.signerInfos.elements[0].version = new CMSVersion(1);
        cms.signerInfos.elements[0].sid = new SignerIdentifier();

        final byte[] encodedName = ((X509Certificate) signCert)
                .getIssuerX500Principal().getEncoded();
        final Asn1BerDecodeBuffer nameBuf = new Asn1BerDecodeBuffer(encodedName);
        final Name name = new Name();
        name.decode(nameBuf);

        final CertificateSerialNumber num = new CertificateSerialNumber(
                ((X509Certificate) signCert).getSerialNumber());
        cms.signerInfos.elements[0].sid.set_issuerAndSerialNumber(
                new IssuerAndSerialNumber(name, num));
        cms.signerInfos.elements[0].digestAlgorithm =
                new DigestAlgorithmIdentifier(new OID(JCP.GOST_DIGEST_OID).value);
        cms.signerInfos.elements[0].digestAlgorithm.parameters = new Asn1Null();
        cms.signerInfos.elements[0].signatureAlgorithm =
                new SignatureAlgorithmIdentifier(new OID(JCP.GOST_EL_KEY_OID).value);
        cms.signerInfos.elements[0].signatureAlgorithm.parameters = new Asn1Null();
        cms.signerInfos.elements[0].signature = new SignatureValue(sign);

        // encode
        final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
        all.encode(asnBuf, true);
        return asnBuf.getMsgCopy();
	}

}
