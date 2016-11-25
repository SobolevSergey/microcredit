package ru.simplgroupp.contact.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.contact.protocol.v2.elements.BaseElement;
import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.*;
import ru.simplgroupp.toolkit.common.Convertor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * Created by aro on 12.09.2014.
 */
public class AUtils {
    private static final Logger logger = Logger.getLogger(AUtils.class.getName());
    
    public static final DateFormat CONTACT_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    public static final String CHARSET_1251 = "windows-1251";


    public static String createStamp(){
        return CONTACT_STAMP_FORMAT.format(new Date());
    }


    public static BaseElement createSignatureElement(byte[] sig){
        String encSig = "";
        try {
            encSig = new String(Base64.encodeBase64(sig), BaseDoc.ENCODING_VALUE_WIN1251);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String,String> attrMap = new HashMap<String, String>();
        attrMap.put(BaseDoc.ALGO,BaseDoc.ECR3410_ALGO_VALUE);
        return new BaseElement(BaseDoc.SIGNATURE,encSig,attrMap);
    }


    public static Dics parseDics(CdtrnCXR cdtrn){
        Dics dics = new Dics();
        if(cdtrn!=null && cdtrn.getData() !=null && cdtrn.getData().getResponseAsXml()!=null){
            String xml = cdtrn.getData().getResponseAsXml();
            try {
            
            	
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputStream inputSource = new ByteArrayInputStream(xml.getBytes(CHARSET_1251));
                org.w3c.dom.Document d = db.parse(inputSource);

                //banksDic;
                org.w3c.dom.Node dicNode = d.getElementsByTagName("BANKS").item(0);
                JAXBContext jaxbContext = JAXBContext.newInstance(BanksDic.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                BanksDic banksDic = (BanksDic) jaxbUnmarshaller.unmarshal(dicNode);
                dics.setBanksDic(banksDic);
                //badDocDic;
                dicNode = d.getElementsByTagName("BAD_DOC").item(0);
                jaxbContext = JAXBContext.newInstance(BadDocDic.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                BadDocDic badDocDic = (BadDocDic) jaxbUnmarshaller.unmarshal(dicNode);
                dics.setBadDocDic(badDocDic);
                //bankServDic;
                dicNode = d.getElementsByTagName("BANKSERV").item(0);
                jaxbContext = JAXBContext.newInstance(BankServDic.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                BankServDic bankServDic = (BankServDic) jaxbUnmarshaller.unmarshal(dicNode);
                dics.setBankServDic(bankServDic);
                //kfmInfoDic;
                dicNode = d.getElementsByTagName("KFM_INFO").item(0);
                jaxbContext = JAXBContext.newInstance(KFMInfoDic.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                KFMInfoDic kfmInfoDic = (KFMInfoDic) jaxbUnmarshaller.unmarshal(dicNode);
                dics.setKfmInfoDic(kfmInfoDic);
                //servDic;
                dicNode = d.getElementsByTagName("SERV").item(0);
                jaxbContext = JAXBContext.newInstance(ServDic.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                ServDic servDic = (ServDic) jaxbUnmarshaller.unmarshal(dicNode);
                dics.setServDic(servDic);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (SAXException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }else{
            logger.severe("Пустой xml со словарями,ошибка!");
        }
        return dics;
    }

    public static Dics parseDicsV2(CdtrnCXR cdtrn){
        Dics dics = new Dics();
        if(cdtrn!=null && cdtrn.getData() !=null && cdtrn.getData().getResponseAsXml()!=null){
            String xml = cdtrn.getData().getResponseAsXml();
            try {
                //new String(ZlibUtils.decompress(Base64.decodeBase64("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")),AUtils.CHARSET_1251)
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputStream inputSource = new ByteArrayInputStream(xml.getBytes(CHARSET_1251));
                org.w3c.dom.Document d = db.parse(inputSource);

                //banksDic;
                DataPacketUnp dataPack = extractDicContentV2(d,"BANKS");
                Dic dic = new BanksDic();
                dic.setDataPacket(dataPack);
                dics.setBanksDic((BanksDic)dic);
                //dics.setBanksDic((BanksDic)extractDicContent(BanksDic.class,d,"BANKS"));

                //badDocDic;
                dataPack = extractDicContentV2(d,"BAD_DOC");
                dic = new BadDocDic();
                dic.setDataPacket(dataPack);
                dics.setBadDocDic((BadDocDic) dic);
                //dics.setBadDocDic((BadDocDic)extractDicContent(BadDocDic.class,d,"BAD_DOC"));

                //bankServDic;
                dataPack = extractDicContentV2(d,"BANKSERV");
                dic = new BankServDic();
                dic.setDataPacket(dataPack);
                dics.setBankServDic((BankServDic) dic);
                //dics.setBankServDic((BankServDic)extractDicContent(BankServDic.class,d,"BANKSERV"));

                //kfmInfoDic;
                dataPack = extractDicContentV2(d,"KFM_INFO");
                dic = new KFMInfoDic();
                dic.setDataPacket(dataPack);
                dics.setKfmInfoDic((KFMInfoDic) dic);
                //dics.setKfmInfoDic((KFMInfoDic)extractDicContent(KFMInfoDic.class,d,"KFM_INFO"));

                //servDic;
                dataPack = extractDicContentV2(d,"SERV");
                dic = new ServDic();
                dic.setDataPacket(dataPack);
                dics.setServDic((ServDic) dic);
                //dics.setServDic((ServDic)extractDicContent(ServDic.class,d,"SERV"));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (SAXException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Не парсятся словари", e);
            }
        }else{
            logger.severe("Пустой xml со словарями,ошибка!");
        }
        return dics;
    }


    public static Dics parseDicsPartialV2(CdtrnCXR cdtrn){
        Dics dics = new Dics();
        if(cdtrn!=null && cdtrn.getData() !=null && cdtrn.getData().getResponseAsXml()!=null){
            String xml = cdtrn.getData().getResponseAsXml();
            try {
                //new String(ZlibUtils.decompress(Base64.decodeBase64("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")),AUtils.CHARSET_1251)
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputStream inputSource = new ByteArrayInputStream(xml.getBytes(CHARSET_1251));
                org.w3c.dom.Document d = db.parse(inputSource);
                org.w3c.dom.Node mainTag = d.getElementsByTagName("RESPONSE").item(0);
                if(mainTag!=null){
                    Element e = (Element)mainTag;
                    String str = e.getAttribute("TOTAL");
                    if(str!=null){
                        dics.setTotal(Convertor.toInteger(str));
                    }
                    str = e.getAttribute("PART");
                    if(str!=null){
                        dics.setCurrentPart(Convertor.toInteger(str));
                    }

                    str = e.getAttribute("BOOK_ID");
                    if(str!=null){
                        dics.setBookId(str);
                    }


                    //banksDic;
                    Dic dic = null;
                    DataPacketUnp dataPack = extractDicContentV2(d,"BANKS");
                    if(dataPack!=null){
                        dic = new BanksDic();
                        dic.setDataPacket(dataPack);
                        dics.setBanksDic((BanksDic)dic);
                        //dics.setBanksDic((BanksDic)extractDicContent(BanksDic.class,d,"BANKS"));
                    }

                    //badDocDic;
                    dataPack = extractDicContentV2(d,"BAD_DOC");
                    if(dataPack!=null) {
                        dic = new BadDocDic();
                        dic.setDataPacket(dataPack);
                        dics.setBadDocDic((BadDocDic) dic);
                        //dics.setBadDocDic((BadDocDic)extractDicContent(BadDocDic.class,d,"BAD_DOC"));
                    }
                    //bankServDic;
                    dataPack = extractDicContentV2(d,"BANKSERV");
                    if(dataPack!=null) {
                        dic = new BankServDic();
                        dic.setDataPacket(dataPack);
                        dics.setBankServDic((BankServDic) dic);
                        //dics.setBankServDic((BankServDic)extractDicContent(BankServDic.class,d,"BANKSERV"));
                    }

                    //kfmInfoDic;
                    dataPack = extractDicContentV2(d,"KFM_INFO");
                    if(dataPack!=null) {
                        dic = new KFMInfoDic();
                        dic.setDataPacket(dataPack);
                        dics.setKfmInfoDic((KFMInfoDic) dic);
                        //dics.setKfmInfoDic((KFMInfoDic)extractDicContent(KFMInfoDic.class,d,"KFM_INFO"));
                    }

                    //servDic;
                    dataPack = extractDicContentV2(d,"SERV");
                    if(dataPack!=null) {
                        dic = new ServDic();
                        dic.setDataPacket(dataPack);
                        dics.setServDic((ServDic) dic);
                        //dics.setServDic((ServDic)extractDicContent(ServDic.class,d,"SERV"));
                    }

                }




            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (SAXException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсятся словари", e);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Не парсятся словари", e);
            }
        }else{
            logger.severe("Пустой xml со словарями,ошибка!");
        }
        return dics;
    }



    private static DataPacketUnp extractDicContentV2(org.w3c.dom.Document d,String elemName){
        org.w3c.dom.Node dicNode = d.getElementsByTagName(elemName).item(0);
        if(dicNode != null){
            if(dicNode.getTextContent() !=null){

                try {
                    ZlibUtils.decompressIntoFileV2(Base64.decodeBase64(dicNode.getTextContent()), elemName);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(DataPacketUnp.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                InputStream fis = null;
                try {
                    fis = new FileInputStream(elemName+"_utf8");
                    return (DataPacketUnp)jaxbUnmarshaller.unmarshal(fis);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    if(fis!=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return null;

    }







    public static String parsePhoneToContactForm(String phone){
        //это маска телефона в Контакте ^(\d{1,3}-\d{1,4}-\d{1,12})$
        //сделаю просто и тупо, лишь бы вписывалось в маску 7-9-123456789

        if(StringUtils.isEmpty(phone)){
            return phone;
        }
        try {
            phone = phone.replaceAll("\\D","");
            StringBuilder sb = new StringBuilder(phone.substring(0,1));
            sb.append("-").append(phone.substring(1,2));
            sb.append("-").append(phone.substring(2));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return phone;
        }
    }

    public static String readFile(String path, String encoding) throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void transform(File source, String srcEncoding, File target, String tgtEncoding) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(source),srcEncoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));
            char[] buffer = new char[16384];
            int read;
            while ((read = br.read(buffer)) != -1)
                bw.write(buffer, 0, read);
        } finally {
            try {
                if (br != null)
                    br.close();
            } finally {
                if (bw != null)
                    bw.close();
            }
        }
    }

}
