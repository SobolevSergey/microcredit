
package ru.simplgroupp.hunter.hashing.in;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;
import ru.simplgroupp.hunter.hashing.ATY;
import ru.simplgroupp.hunter.hashing.CTR;
import ru.simplgroupp.hunter.hashing.DOT;
import ru.simplgroupp.hunter.hashing.ENS;
import ru.simplgroupp.hunter.hashing.ENUM_YES;
import ru.simplgroupp.hunter.hashing.JOT;
import ru.simplgroupp.hunter.hashing.PRODUCT_CODES;
import ru.simplgroupp.hunter.hashing.RGC;
import ru.simplgroupp.hunter.hashing.SCL;


/**
 * <p>Java class for BATCH element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="BATCH"&gt;
 *   &lt;complexType&gt;
 *     &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="HEADER"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="COUNT" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                     &lt;element name="ORIGINATOR" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                     &lt;element name="SUPPRESS" type="{}ENUM_YES" minOccurs="0"/&gt;
 *                     &lt;element name="RESULT" type="{}ENUM_YES" minOccurs="0"/&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="SUBMISSIONS"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="SUBMISSION" maxOccurs="500"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="SUB_HEADER" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;all&gt;
 *                                         &lt;element name="NOTES" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="NOTE" maxOccurs="unbounded"&gt;
 *                                                     &lt;complexType&gt;
 *                                                       &lt;simpleContent&gt;
 *                                                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                                                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                                           &lt;attribute name="privacyLevel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                                         &lt;/extension&gt;
 *                                                       &lt;/simpleContent&gt;
 *                                                     &lt;/complexType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                       &lt;/all&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="IDENTIFIER" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                               &lt;element name="PRODUCT" type="{}PRODUCT_CODES"/&gt;
 *                               &lt;element name="CLASSIFICATION" type="{}SCL" minOccurs="0"/&gt;
 *                               &lt;element name="DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                               &lt;element name="APP_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                               &lt;element name="MA"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                         &lt;element name="FNAME"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;minLength value="1"/&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PNAME" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="LNAME"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;minLength value="1"/&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                                         &lt;element name="PER_INN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="12"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PREV_FN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PREV_PN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PREV_LN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="INC_TM" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                               &lt;totalDigits value="13"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="INC_DOC" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                               &lt;totalDigits value="13"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="INC_ADD" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                               &lt;totalDigits value="13"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_PAS"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="DOC_NUMBER"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="12"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_DOC" maxOccurs="2" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="DOC_NUMBER"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="32"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                                                   &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_SPO" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="FNAME"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;minLength value="1"/&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="PNAME" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="LNAME"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;minLength value="1"/&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_RAD"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_RTEL" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_CAD" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_CTEL" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_TAD" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_MTEL" maxOccurs="2" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_EMP" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="ORG_NAME"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="200"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="INN" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="12"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_ETEL" maxOccurs="2" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_EAD" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_EMA" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="EMAIL"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="MA_VEH" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="CAR_REG"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="9"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="VIN" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="21"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                       &lt;/sequence&gt;
 *                                       &lt;attribute name="entID"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/attribute&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                               &lt;element name="AP" maxOccurs="6" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                         &lt;element name="FNAME"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PNAME" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="LNAME"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                                         &lt;element name="PER_INN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="12"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PREV_FN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PREV_PN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="PREV_LN" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                               &lt;maxLength value="50"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="INC_TM" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                               &lt;totalDigits value="13"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="INC_DOC" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                               &lt;totalDigits value="13"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="INC_ADD" minOccurs="0"&gt;
 *                                           &lt;simpleType&gt;
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                               &lt;totalDigits value="13"/&gt;
 *                                             &lt;/restriction&gt;
 *                                           &lt;/simpleType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="APP_TYPE" type="{}ATY" minOccurs="0"/&gt;
 *                                         &lt;element name="A_PAS"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="DOC_NUMBER"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="12"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_DOC" maxOccurs="2" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="DOC_NUMBER"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="32"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                                                   &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_SPO" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="FNAME"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="PNAME" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="LNAME" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_RAD"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_RTEL" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_CAD" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_CTEL" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_TAD" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_MTEL" maxOccurs="2" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_EMP" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="ORG_NAME"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="200"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="INN" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="12"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_ETEL" maxOccurs="2" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="TEL_NUM"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="18"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_EAD" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
 *                                                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                                                   &lt;element name="REGION" type="{}RGC"/&gt;
 *                                                   &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CITY" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="STREET" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="50"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="HOUSE" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="BUILD" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="CONSTR" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="20"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="APART" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_EMA" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="EMAIL"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="64"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                         &lt;element name="A_VEH" minOccurs="0"&gt;
 *                                           &lt;complexType&gt;
 *                                             &lt;complexContent&gt;
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                                 &lt;sequence&gt;
 *                                                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
 *                                                   &lt;element name="CAR_REG"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="9"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                   &lt;element name="VIN" minOccurs="0"&gt;
 *                                                     &lt;simpleType&gt;
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                         &lt;maxLength value="21"/&gt;
 *                                                       &lt;/restriction&gt;
 *                                                     &lt;/simpleType&gt;
 *                                                   &lt;/element&gt;
 *                                                 &lt;/sequence&gt;
 *                                                 &lt;attribute name="entID"&gt;
 *                                                   &lt;simpleType&gt;
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                                     &lt;/restriction&gt;
 *                                                   &lt;/simpleType&gt;
 *                                                 &lt;/attribute&gt;
 *                                               &lt;/restriction&gt;
 *                                             &lt;/complexContent&gt;
 *                                           &lt;/complexType&gt;
 *                                         &lt;/element&gt;
 *                                       &lt;/sequence&gt;
 *                                       &lt;attribute name="entID"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/attribute&gt;
 *                                     &lt;/restriction&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                             &lt;/sequence&gt;
 *                             &lt;attribute name="NotificationRqd"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"&gt;
 *                                   &lt;totalDigits value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/attribute&gt;
 *                             &lt;attribute name="subID"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/attribute&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *     &lt;/complexContent&gt;
 *   &lt;/complexType&gt;
 * &lt;/element&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "submissions"
})
@XmlRootElement(name = "BATCH")
public class BATCH_IN {

    @XmlElement(name = "HEADER", required = true)
    protected BATCH_IN.HEADER header;
    @XmlElement(name = "SUBMISSIONS", required = true)
    protected BATCH_IN.SUBMISSIONS submissions;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link BATCH_IN.HEADER }
     *     
     */
    public BATCH_IN.HEADER getHEADER() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link BATCH_IN.HEADER }
     *     
     */
    public void setHEADER(BATCH_IN.HEADER value) {
        this.header = value;
    }

    /**
     * Gets the value of the submissions property.
     * 
     * @return
     *     possible object is
     *     {@link BATCH_IN.SUBMISSIONS }
     *     
     */
    public BATCH_IN.SUBMISSIONS getSUBMISSIONS() {
        return submissions;
    }

    /**
     * Sets the value of the submissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link BATCH_IN.SUBMISSIONS }
     *     
     */
    public void setSUBMISSIONS(BATCH_IN.SUBMISSIONS value) {
        this.submissions = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="COUNT" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="ORIGINATOR" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="SUPPRESS" type="{}ENUM_YES" minOccurs="0"/&gt;
     *         &lt;element name="RESULT" type="{}ENUM_YES" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "count",
        "originator",
        "suppress",
        "result"
    })
    public static class HEADER {

        @XmlElement(name = "COUNT", required = true)
        protected BigDecimal count;
        @XmlElement(name = "ORIGINATOR", required = true)
        protected String originator;
        @XmlElement(name = "SUPPRESS")
        @XmlSchemaType(name = "string")
        protected ENUM_YES suppress;
        @XmlElement(name = "RESULT")
        @XmlSchemaType(name = "string")
        protected ENUM_YES result;

        /**
         * Gets the value of the count property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCOUNT() {
            return count;
        }

        /**
         * Sets the value of the count property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCOUNT(BigDecimal value) {
            this.count = value;
        }

        /**
         * Gets the value of the originator property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORIGINATOR() {
            return originator;
        }

        /**
         * Sets the value of the originator property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORIGINATOR(String value) {
            this.originator = value;
        }

        /**
         * Gets the value of the suppress property.
         * 
         * @return
         *     possible object is
         *     {@link ENUM_YES }
         *     
         */
        public ENUM_YES getSUPPRESS() {
            return suppress;
        }

        /**
         * Sets the value of the suppress property.
         * 
         * @param value
         *     allowed object is
         *     {@link ENUM_YES }
         *     
         */
        public void setSUPPRESS(ENUM_YES value) {
            this.suppress = value;
        }

        /**
         * Gets the value of the result property.
         * 
         * @return
         *     possible object is
         *     {@link ENUM_YES }
         *     
         */
        public ENUM_YES getRESULT() {
            return result;
        }

        /**
         * Sets the value of the result property.
         * 
         * @param value
         *     allowed object is
         *     {@link ENUM_YES }
         *     
         */
        public void setRESULT(ENUM_YES value) {
            this.result = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="SUBMISSION" maxOccurs="500"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="SUB_HEADER" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;all&gt;
     *                             &lt;element name="NOTES" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="NOTE" maxOccurs="unbounded"&gt;
     *                                         &lt;complexType&gt;
     *                                           &lt;simpleContent&gt;
     *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
     *                                               &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                                               &lt;attribute name="privacyLevel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                                             &lt;/extension&gt;
     *                                           &lt;/simpleContent&gt;
     *                                         &lt;/complexType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/all&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="IDENTIFIER" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="PRODUCT" type="{}PRODUCT_CODES"/&gt;
     *                   &lt;element name="CLASSIFICATION" type="{}SCL" minOccurs="0"/&gt;
     *                   &lt;element name="DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                   &lt;element name="APP_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                   &lt;element name="MA"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                             &lt;element name="FNAME"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;minLength value="1"/&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PNAME" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="LNAME"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;minLength value="1"/&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                             &lt;element name="PER_INN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="12"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PREV_FN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PREV_PN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PREV_LN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="INC_TM" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="13"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="INC_DOC" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="13"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="INC_ADD" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="13"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_PAS"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="DOC_NUMBER"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="12"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_DOC" maxOccurs="2" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="DOC_NUMBER"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="32"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                                       &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_SPO" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="FNAME"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;minLength value="1"/&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="PNAME" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="LNAME"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;minLength value="1"/&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_RAD"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_RTEL" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_CAD" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_CTEL" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_TAD" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_MTEL" maxOccurs="2" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_EMP" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="ORG_NAME"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="200"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="INN" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="12"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_ETEL" maxOccurs="2" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_EAD" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_EMA" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="EMAIL"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MA_VEH" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="CAR_REG"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="9"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="VIN" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="21"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                           &lt;attribute name="entID"&gt;
     *                             &lt;simpleType&gt;
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                               &lt;/restriction&gt;
     *                             &lt;/simpleType&gt;
     *                           &lt;/attribute&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="AP" maxOccurs="6" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                             &lt;element name="FNAME"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PNAME" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="LNAME"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                             &lt;element name="PER_INN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="12"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PREV_FN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PREV_PN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PREV_LN" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="50"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="INC_TM" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="13"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="INC_DOC" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="13"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="INC_ADD" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="13"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="APP_TYPE" type="{}ATY" minOccurs="0"/&gt;
     *                             &lt;element name="A_PAS"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="DOC_NUMBER"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="12"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_DOC" maxOccurs="2" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="DOC_NUMBER"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="32"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                                       &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_SPO" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="FNAME"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="PNAME" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="LNAME" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_RAD"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_RTEL" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_CAD" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_CTEL" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_TAD" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_MTEL" maxOccurs="2" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_EMP" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="ORG_NAME"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="200"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="INN" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="12"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_ETEL" maxOccurs="2" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="TEL_NUM"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="18"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_EAD" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
     *                                       &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                                       &lt;element name="REGION" type="{}RGC"/&gt;
     *                                       &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CITY" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="SETTLEMENT" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="STREET" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="50"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="HOUSE" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="BUILD" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="CONSTR" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="20"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="APART" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_EMA" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="EMAIL"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="64"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="A_VEH" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;complexContent&gt;
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                                     &lt;sequence&gt;
     *                                       &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
     *                                       &lt;element name="CAR_REG"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="9"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                       &lt;element name="VIN" minOccurs="0"&gt;
     *                                         &lt;simpleType&gt;
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                             &lt;maxLength value="21"/&gt;
     *                                           &lt;/restriction&gt;
     *                                         &lt;/simpleType&gt;
     *                                       &lt;/element&gt;
     *                                     &lt;/sequence&gt;
     *                                     &lt;attribute name="entID"&gt;
     *                                       &lt;simpleType&gt;
     *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                         &lt;/restriction&gt;
     *                                       &lt;/simpleType&gt;
     *                                     &lt;/attribute&gt;
     *                                   &lt;/restriction&gt;
     *                                 &lt;/complexContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                           &lt;attribute name="entID"&gt;
     *                             &lt;simpleType&gt;
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                               &lt;/restriction&gt;
     *                             &lt;/simpleType&gt;
     *                           &lt;/attribute&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *                 &lt;attribute name="NotificationRqd"&gt;
     *                   &lt;simpleType&gt;
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"&gt;
     *                       &lt;totalDigits value="1"/&gt;
     *                     &lt;/restriction&gt;
     *                   &lt;/simpleType&gt;
     *                 &lt;/attribute&gt;
     *                 &lt;attribute name="subID"&gt;
     *                   &lt;simpleType&gt;
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                     &lt;/restriction&gt;
     *                   &lt;/simpleType&gt;
     *                 &lt;/attribute&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "submission"
    })
    public static class SUBMISSIONS {

        @XmlElement(name = "SUBMISSION", required = true)
        protected List<BATCH_IN.SUBMISSIONS.SUBMISSION> submission;

        /**
         * Gets the value of the submission property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the submission property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSUBMISSION().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BATCH_IN.SUBMISSIONS.SUBMISSION }
         * 
         * 
         */
        public List<BATCH_IN.SUBMISSIONS.SUBMISSION> getSUBMISSION() {
            if (submission == null) {
                submission = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION>();
            }
            return this.submission;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="SUB_HEADER" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;all&gt;
         *                   &lt;element name="NOTES" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="NOTE" maxOccurs="unbounded"&gt;
         *                               &lt;complexType&gt;
         *                                 &lt;simpleContent&gt;
         *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
         *                                     &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                                     &lt;attribute name="privacyLevel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                                   &lt;/extension&gt;
         *                                 &lt;/simpleContent&gt;
         *                               &lt;/complexType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/all&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="IDENTIFIER" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="PRODUCT" type="{}PRODUCT_CODES"/&gt;
         *         &lt;element name="CLASSIFICATION" type="{}SCL" minOccurs="0"/&gt;
         *         &lt;element name="DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *         &lt;element name="APP_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *         &lt;element name="MA"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                   &lt;element name="FNAME"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;minLength value="1"/&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PNAME" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="LNAME"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;minLength value="1"/&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *                   &lt;element name="PER_INN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="12"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PREV_FN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PREV_PN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PREV_LN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="INC_TM" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="13"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="INC_DOC" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="13"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="INC_ADD" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="13"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_PAS"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="DOC_NUMBER"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="12"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_DOC" maxOccurs="2" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="DOC_NUMBER"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="32"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *                             &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_SPO" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="FNAME"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;minLength value="1"/&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="PNAME" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="LNAME"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;minLength value="1"/&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_RAD"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_RTEL" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_CAD" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_CTEL" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_TAD" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_MTEL" maxOccurs="2" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_EMP" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="ORG_NAME"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="200"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="INN" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="12"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_ETEL" maxOccurs="2" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_EAD" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_EMA" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="EMAIL"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MA_VEH" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="CAR_REG"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="9"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="VIN" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="21"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *                 &lt;attribute name="entID"&gt;
         *                   &lt;simpleType&gt;
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                     &lt;/restriction&gt;
         *                   &lt;/simpleType&gt;
         *                 &lt;/attribute&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="AP" maxOccurs="6" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                   &lt;element name="FNAME"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PNAME" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="LNAME"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *                   &lt;element name="PER_INN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="12"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PREV_FN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PREV_PN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PREV_LN" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="50"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="INC_TM" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="13"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="INC_DOC" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="13"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="INC_ADD" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="13"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="APP_TYPE" type="{}ATY" minOccurs="0"/&gt;
         *                   &lt;element name="A_PAS"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="DOC_NUMBER"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="12"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_DOC" maxOccurs="2" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="DOC_NUMBER"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="32"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *                             &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_SPO" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="FNAME"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="PNAME" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="LNAME" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_RAD"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_RTEL" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_CAD" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_CTEL" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_TAD" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_MTEL" maxOccurs="2" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_EMP" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="ORG_NAME"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="200"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="INN" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="12"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_ETEL" maxOccurs="2" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="TEL_NUM"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="18"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_EAD" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
         *                             &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *                             &lt;element name="REGION" type="{}RGC"/&gt;
         *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CITY" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="SETTLEMENT" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="STREET" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="50"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="HOUSE" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="BUILD" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="CONSTR" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="20"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="APART" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_EMA" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="EMAIL"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="64"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="A_VEH" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;complexContent&gt;
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                           &lt;sequence&gt;
         *                             &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
         *                             &lt;element name="CAR_REG"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="9"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                             &lt;element name="VIN" minOccurs="0"&gt;
         *                               &lt;simpleType&gt;
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                                   &lt;maxLength value="21"/&gt;
         *                                 &lt;/restriction&gt;
         *                               &lt;/simpleType&gt;
         *                             &lt;/element&gt;
         *                           &lt;/sequence&gt;
         *                           &lt;attribute name="entID"&gt;
         *                             &lt;simpleType&gt;
         *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                               &lt;/restriction&gt;
         *                             &lt;/simpleType&gt;
         *                           &lt;/attribute&gt;
         *                         &lt;/restriction&gt;
         *                       &lt;/complexContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *                 &lt;attribute name="entID"&gt;
         *                   &lt;simpleType&gt;
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                     &lt;/restriction&gt;
         *                   &lt;/simpleType&gt;
         *                 &lt;/attribute&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *       &lt;attribute name="NotificationRqd"&gt;
         *         &lt;simpleType&gt;
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"&gt;
         *             &lt;totalDigits value="1"/&gt;
         *           &lt;/restriction&gt;
         *         &lt;/simpleType&gt;
         *       &lt;/attribute&gt;
         *       &lt;attribute name="subID"&gt;
         *         &lt;simpleType&gt;
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *           &lt;/restriction&gt;
         *         &lt;/simpleType&gt;
         *       &lt;/attribute&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "sub_HEADER",
            "identifier",
            "product",
            "classification",
            "date",
            "app_DATE",
            "ma",
            "ap"
        })
        public static class SUBMISSION {

            @XmlElement(name = "SUB_HEADER")
            protected BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER sub_HEADER;
            @XmlElement(name = "IDENTIFIER", required = true)
            protected String identifier;
            @XmlElement(name = "PRODUCT", required = true)
            @XmlSchemaType(name = "string")
            protected PRODUCT_CODES product;
            @XmlElement(name = "CLASSIFICATION")
            @XmlSchemaType(name = "string")
            protected SCL classification;
            @XmlElement(name = "DATE", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar date;
            @XmlElement(name = "APP_DATE", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar app_DATE;
            @XmlElement(name = "MA", required = true)
            protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA ma;
            @XmlElement(name = "AP")
            protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP> ap;
            @XmlAttribute(name = "NotificationRqd")
            protected BigInteger notificationRqd;
            @XmlAttribute(name = "subID")
            protected String subID;

            /**
             * Gets the value of the sub_HEADER property.
             * 
             * @return
             *     possible object is
             *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER }
             *     
             */
            public BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER getSUB_HEADER() {
                return sub_HEADER;
            }

            /**
             * Sets the value of the sub_HEADER property.
             * 
             * @param value
             *     allowed object is
             *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER }
             *     
             */
            public void setSUB_HEADER(BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER value) {
                this.sub_HEADER = value;
            }

            /**
             * Gets the value of the identifier property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIDENTIFIER() {
                return identifier;
            }

            /**
             * Sets the value of the identifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIDENTIFIER(String value) {
                this.identifier = value;
            }

            /**
             * Gets the value of the product property.
             * 
             * @return
             *     possible object is
             *     {@link PRODUCT_CODES }
             *     
             */
            public PRODUCT_CODES getPRODUCT() {
                return product;
            }

            /**
             * Sets the value of the product property.
             * 
             * @param value
             *     allowed object is
             *     {@link PRODUCT_CODES }
             *     
             */
            public void setPRODUCT(PRODUCT_CODES value) {
                this.product = value;
            }

            /**
             * Gets the value of the classification property.
             * 
             * @return
             *     possible object is
             *     {@link SCL }
             *     
             */
            public SCL getCLASSIFICATION() {
                return classification;
            }

            /**
             * Sets the value of the classification property.
             * 
             * @param value
             *     allowed object is
             *     {@link SCL }
             *     
             */
            public void setCLASSIFICATION(SCL value) {
                this.classification = value;
            }

            /**
             * Gets the value of the date property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getDATE() {
                return date;
            }

            /**
             * Sets the value of the date property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setDATE(XMLGregorianCalendar value) {
                this.date = value;
            }

            /**
             * Gets the value of the app_DATE property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getAPP_DATE() {
                return app_DATE;
            }

            /**
             * Sets the value of the app_DATE property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setAPP_DATE(XMLGregorianCalendar value) {
                this.app_DATE = value;
            }

            /**
             * Gets the value of the ma property.
             * 
             * @return
             *     possible object is
             *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA }
             *     
             */
            public BATCH_IN.SUBMISSIONS.SUBMISSION.MA getMA() {
                return ma;
            }

            /**
             * Sets the value of the ma property.
             * 
             * @param value
             *     allowed object is
             *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA }
             *     
             */
            public void setMA(BATCH_IN.SUBMISSIONS.SUBMISSION.MA value) {
                this.ma = value;
            }

            /**
             * Gets the value of the ap property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the ap property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAP().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP }
             * 
             * 
             */
            public List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP> getAP() {
                if (ap == null) {
                    ap = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.AP>();
                }
                return this.ap;
            }

            /**
             * Gets the value of the notificationRqd property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNotificationRqd() {
                return notificationRqd;
            }

            /**
             * Sets the value of the notificationRqd property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNotificationRqd(BigInteger value) {
                this.notificationRqd = value;
            }

            /**
             * Gets the value of the subID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSubID() {
                return subID;
            }

            /**
             * Sets the value of the subID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSubID(String value) {
                this.subID = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *         &lt;element name="FNAME"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PNAME" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="LNAME"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
             *         &lt;element name="PER_INN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="12"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PREV_FN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PREV_PN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PREV_LN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="INC_TM" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="13"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="INC_DOC" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="13"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="INC_ADD" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="13"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="APP_TYPE" type="{}ATY" minOccurs="0"/&gt;
             *         &lt;element name="A_PAS"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="DOC_NUMBER"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="12"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_DOC" maxOccurs="2" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="DOC_NUMBER"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="32"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
             *                   &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_SPO" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="FNAME"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="PNAME" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="LNAME" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_RAD"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_RTEL" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_CAD" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_CTEL" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_TAD" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_MTEL" maxOccurs="2" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_EMP" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="ORG_NAME"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="200"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="INN" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="12"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_ETEL" maxOccurs="2" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_EAD" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_EMA" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="EMAIL"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="A_VEH" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="CAR_REG"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="9"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="VIN" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="21"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *       &lt;attribute name="entID"&gt;
             *         &lt;simpleType&gt;
             *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *           &lt;/restriction&gt;
             *         &lt;/simpleType&gt;
             *       &lt;/attribute&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "status",
                "fname",
                "pname",
                "lname",
                "dob",
                "per_INN",
                "prev_FN",
                "prev_PN",
                "prev_LN",
                "inc_TM",
                "inc_DOC",
                "inc_ADD",
                "app_TYPE",
                "a_PAS",
                "a_DOC",
                "a_SPO",
                "a_RAD",
                "a_RTEL",
                "a_CAD",
                "a_CTEL",
                "a_TAD",
                "a_MTEL",
                "a_EMP",
                "a_ETEL",
                "a_EAD",
                "a_EMA",
                "a_VEH"
            })
            public static class AP {

                @XmlElement(name = "STATUS")
                @XmlSchemaType(name = "string")
                protected ENS status;
                @XmlElement(name = "FNAME", required = true)
                protected String fname;
                @XmlElement(name = "PNAME")
                protected String pname;
                @XmlElement(name = "LNAME", required = true)
                protected String lname;
                @XmlElement(name = "DOB", required = true)
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar dob;
                @XmlElement(name = "PER_INN")
                protected String per_INN;
                @XmlElement(name = "PREV_FN")
                protected String prev_FN;
                @XmlElement(name = "PREV_PN")
                protected String prev_PN;
                @XmlElement(name = "PREV_LN")
                protected String prev_LN;
                @XmlElement(name = "INC_TM")
                protected BigInteger inc_TM;
                @XmlElement(name = "INC_DOC")
                protected BigInteger inc_DOC;
                @XmlElement(name = "INC_ADD")
                protected BigInteger inc_ADD;
                @XmlElement(name = "APP_TYPE")
                @XmlSchemaType(name = "string")
                protected ATY app_TYPE;
                @XmlElement(name = "A_PAS", required = true)
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_PAS a_PAS;
                @XmlElement(name = "A_DOC")
                protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_DOC> a_DOC;
                @XmlElement(name = "A_SPO")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_SPO a_SPO;
                @XmlElement(name = "A_RAD", required = true)
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RAD a_RAD;
                @XmlElement(name = "A_RTEL")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RTEL a_RTEL;
                @XmlElement(name = "A_CAD")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CAD a_CAD;
                @XmlElement(name = "A_CTEL")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CTEL a_CTEL;
                @XmlElement(name = "A_TAD")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_TAD a_TAD;
                @XmlElement(name = "A_MTEL")
                protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_MTEL> a_MTEL;
                @XmlElement(name = "A_EMP")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMP a_EMP;
                @XmlElement(name = "A_ETEL")
                protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_ETEL> a_ETEL;
                @XmlElement(name = "A_EAD")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EAD a_EAD;
                @XmlElement(name = "A_EMA")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMA a_EMA;
                @XmlElement(name = "A_VEH")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_VEH a_VEH;
                @XmlAttribute(name = "entID")
                protected String entID;

                /**
                 * Gets the value of the status property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ENS }
                 *     
                 */
                public ENS getSTATUS() {
                    return status;
                }

                /**
                 * Sets the value of the status property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ENS }
                 *     
                 */
                public void setSTATUS(ENS value) {
                    this.status = value;
                }

                /**
                 * Gets the value of the fname property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getFNAME() {
                    return fname;
                }

                /**
                 * Sets the value of the fname property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setFNAME(String value) {
                    this.fname = value;
                }

                /**
                 * Gets the value of the pname property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPNAME() {
                    return pname;
                }

                /**
                 * Sets the value of the pname property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPNAME(String value) {
                    this.pname = value;
                }

                /**
                 * Gets the value of the lname property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getLNAME() {
                    return lname;
                }

                /**
                 * Sets the value of the lname property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setLNAME(String value) {
                    this.lname = value;
                }

                /**
                 * Gets the value of the dob property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDOB() {
                    return dob;
                }

                /**
                 * Sets the value of the dob property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDOB(XMLGregorianCalendar value) {
                    this.dob = value;
                }

                /**
                 * Gets the value of the per_INN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPER_INN() {
                    return per_INN;
                }

                /**
                 * Sets the value of the per_INN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPER_INN(String value) {
                    this.per_INN = value;
                }

                /**
                 * Gets the value of the prev_FN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPREV_FN() {
                    return prev_FN;
                }

                /**
                 * Sets the value of the prev_FN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPREV_FN(String value) {
                    this.prev_FN = value;
                }

                /**
                 * Gets the value of the prev_PN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPREV_PN() {
                    return prev_PN;
                }

                /**
                 * Sets the value of the prev_PN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPREV_PN(String value) {
                    this.prev_PN = value;
                }

                /**
                 * Gets the value of the prev_LN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPREV_LN() {
                    return prev_LN;
                }

                /**
                 * Sets the value of the prev_LN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPREV_LN(String value) {
                    this.prev_LN = value;
                }

                /**
                 * Gets the value of the inc_TM property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getINC_TM() {
                    return inc_TM;
                }

                /**
                 * Sets the value of the inc_TM property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setINC_TM(BigInteger value) {
                    this.inc_TM = value;
                }

                /**
                 * Gets the value of the inc_DOC property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getINC_DOC() {
                    return inc_DOC;
                }

                /**
                 * Sets the value of the inc_DOC property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setINC_DOC(BigInteger value) {
                    this.inc_DOC = value;
                }

                /**
                 * Gets the value of the inc_ADD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getINC_ADD() {
                    return inc_ADD;
                }

                /**
                 * Sets the value of the inc_ADD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setINC_ADD(BigInteger value) {
                    this.inc_ADD = value;
                }

                /**
                 * Gets the value of the app_TYPE property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ATY }
                 *     
                 */
                public ATY getAPP_TYPE() {
                    return app_TYPE;
                }

                /**
                 * Sets the value of the app_TYPE property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ATY }
                 *     
                 */
                public void setAPP_TYPE(ATY value) {
                    this.app_TYPE = value;
                }

                /**
                 * Gets the value of the a_PAS property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_PAS }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_PAS getA_PAS() {
                    return a_PAS;
                }

                /**
                 * Sets the value of the a_PAS property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_PAS }
                 *     
                 */
                public void setA_PAS(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_PAS value) {
                    this.a_PAS = value;
                }

                /**
                 * Gets the value of the aDOC property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the aDOC property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getA_DOC().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_DOC }
                 * 
                 * 
                 */
                public List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_DOC> getA_DOC() {
                    if (a_DOC == null) {
                        a_DOC = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_DOC>();
                    }
                    return this.a_DOC;
                }

                /**
                 * Gets the value of the a_SPO property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_SPO }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_SPO getA_SPO() {
                    return a_SPO;
                }

                /**
                 * Sets the value of the a_SPO property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_SPO }
                 *     
                 */
                public void setA_SPO(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_SPO value) {
                    this.a_SPO = value;
                }

                /**
                 * Gets the value of the a_RAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RAD getA_RAD() {
                    return a_RAD;
                }

                /**
                 * Sets the value of the a_RAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RAD }
                 *     
                 */
                public void setA_RAD(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RAD value) {
                    this.a_RAD = value;
                }

                /**
                 * Gets the value of the a_RTEL property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RTEL }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RTEL getA_RTEL() {
                    return a_RTEL;
                }

                /**
                 * Sets the value of the a_RTEL property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RTEL }
                 *     
                 */
                public void setA_RTEL(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_RTEL value) {
                    this.a_RTEL = value;
                }

                /**
                 * Gets the value of the a_CAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CAD getA_CAD() {
                    return a_CAD;
                }

                /**
                 * Sets the value of the a_CAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CAD }
                 *     
                 */
                public void setA_CAD(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CAD value) {
                    this.a_CAD = value;
                }

                /**
                 * Gets the value of the a_CTEL property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CTEL }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CTEL getA_CTEL() {
                    return a_CTEL;
                }

                /**
                 * Sets the value of the a_CTEL property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CTEL }
                 *     
                 */
                public void setA_CTEL(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_CTEL value) {
                    this.a_CTEL = value;
                }

                /**
                 * Gets the value of the a_TAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_TAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_TAD getA_TAD() {
                    return a_TAD;
                }

                /**
                 * Sets the value of the a_TAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_TAD }
                 *     
                 */
                public void setA_TAD(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_TAD value) {
                    this.a_TAD = value;
                }

                /**
                 * Gets the value of the aMTEL property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the aMTEL property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getA_MTEL().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_MTEL }
                 * 
                 * 
                 */
                public List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_MTEL> getA_MTEL() {
                    if (a_MTEL == null) {
                        a_MTEL = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_MTEL>();
                    }
                    return this.a_MTEL;
                }

                /**
                 * Gets the value of the a_EMP property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMP }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMP getA_EMP() {
                    return a_EMP;
                }

                /**
                 * Sets the value of the a_EMP property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMP }
                 *     
                 */
                public void setA_EMP(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMP value) {
                    this.a_EMP = value;
                }

                /**
                 * Gets the value of the aETEL property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the aETEL property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getA_ETEL().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_ETEL }
                 * 
                 * 
                 */
                public List<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_ETEL> getA_ETEL() {
                    if (a_ETEL == null) {
                        a_ETEL = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_ETEL>();
                    }
                    return this.a_ETEL;
                }

                /**
                 * Gets the value of the a_EAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EAD getA_EAD() {
                    return a_EAD;
                }

                /**
                 * Sets the value of the a_EAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EAD }
                 *     
                 */
                public void setA_EAD(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EAD value) {
                    this.a_EAD = value;
                }

                /**
                 * Gets the value of the a_EMA property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMA }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMA getA_EMA() {
                    return a_EMA;
                }

                /**
                 * Sets the value of the a_EMA property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMA }
                 *     
                 */
                public void setA_EMA(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_EMA value) {
                    this.a_EMA = value;
                }

                /**
                 * Gets the value of the a_VEH property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_VEH }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_VEH getA_VEH() {
                    return a_VEH;
                }

                /**
                 * Sets the value of the a_VEH property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_VEH }
                 *     
                 */
                public void setA_VEH(BATCH_IN.SUBMISSIONS.SUBMISSION.AP.A_VEH value) {
                    this.a_VEH = value;
                }

                /**
                 * Gets the value of the entID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEntID() {
                    return entID;
                }

                /**
                 * Sets the value of the entID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEntID(String value) {
                    this.entID = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class A_CAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class A_CTEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="DOC_NUMBER"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="32"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
                 *         &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "doc_NUMBER",
                    "doc_DATE",
                    "dot"
                })
                public static class A_DOC {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "DOC_NUMBER", required = true)
                    protected String doc_NUMBER;
                    @XmlElement(name = "DOC_DATE")
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar doc_DATE;
                    @XmlElement(name = "DOT")
                    @XmlSchemaType(name = "string")
                    protected DOT dot;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the doc_NUMBER property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDOC_NUMBER() {
                        return doc_NUMBER;
                    }

                    /**
                     * Sets the value of the doc_NUMBER property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDOC_NUMBER(String value) {
                        this.doc_NUMBER = value;
                    }

                    /**
                     * Gets the value of the doc_DATE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDOC_DATE() {
                        return doc_DATE;
                    }

                    /**
                     * Sets the value of the doc_DATE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDOC_DATE(XMLGregorianCalendar value) {
                        this.doc_DATE = value;
                    }

                    /**
                     * Gets the value of the dot property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link DOT }
                     *     
                     */
                    public DOT getDOT() {
                        return dot;
                    }

                    /**
                     * Sets the value of the dot property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link DOT }
                     *     
                     */
                    public void setDOT(DOT value) {
                        this.dot = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class A_EAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="EMAIL"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "email"
                })
                public static class A_EMA {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "EMAIL", required = true)
                    protected String email;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the email property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEMAIL() {
                        return email;
                    }

                    /**
                     * Sets the value of the email property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEMAIL(String value) {
                        this.email = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="ORG_NAME"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="200"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="INN" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="12"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "org_NAME",
                    "inn",
                    "jot"
                })
                public static class A_EMP {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "ORG_NAME", required = true)
                    protected String org_NAME;
                    @XmlElement(name = "INN")
                    protected String inn;
                    @XmlElement(name = "JOT")
                    @XmlSchemaType(name = "string")
                    protected JOT jot;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the org_NAME property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getORG_NAME() {
                        return org_NAME;
                    }

                    /**
                     * Sets the value of the org_NAME property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setORG_NAME(String value) {
                        this.org_NAME = value;
                    }

                    /**
                     * Gets the value of the inn property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getINN() {
                        return inn;
                    }

                    /**
                     * Sets the value of the inn property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setINN(String value) {
                        this.inn = value;
                    }

                    /**
                     * Gets the value of the jot property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link JOT }
                     *     
                     */
                    public JOT getJOT() {
                        return jot;
                    }

                    /**
                     * Sets the value of the jot property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link JOT }
                     *     
                     */
                    public void setJOT(JOT value) {
                        this.jot = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class A_ETEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class A_MTEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="DOC_NUMBER"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="12"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "doc_NUMBER",
                    "doc_DATE"
                })
                public static class A_PAS {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "DOC_NUMBER", required = true)
                    protected String doc_NUMBER;
                    @XmlElement(name = "DOC_DATE", required = true)
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar doc_DATE;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the doc_NUMBER property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDOC_NUMBER() {
                        return doc_NUMBER;
                    }

                    /**
                     * Sets the value of the doc_NUMBER property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDOC_NUMBER(String value) {
                        this.doc_NUMBER = value;
                    }

                    /**
                     * Gets the value of the doc_DATE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDOC_DATE() {
                        return doc_DATE;
                    }

                    /**
                     * Sets the value of the doc_DATE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDOC_DATE(XMLGregorianCalendar value) {
                        this.doc_DATE = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class A_RAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class A_RTEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="FNAME"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="PNAME" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="LNAME" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "fname",
                    "pname",
                    "lname",
                    "dob"
                })
                public static class A_SPO {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "FNAME", required = true)
                    protected String fname;
                    @XmlElement(name = "PNAME")
                    protected String pname;
                    @XmlElement(name = "LNAME")
                    protected String lname;
                    @XmlElement(name = "DOB", required = true)
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar dob;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the fname property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getFNAME() {
                        return fname;
                    }

                    /**
                     * Sets the value of the fname property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setFNAME(String value) {
                        this.fname = value;
                    }

                    /**
                     * Gets the value of the pname property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPNAME() {
                        return pname;
                    }

                    /**
                     * Sets the value of the pname property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPNAME(String value) {
                        this.pname = value;
                    }

                    /**
                     * Gets the value of the lname property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getLNAME() {
                        return lname;
                    }

                    /**
                     * Sets the value of the lname property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setLNAME(String value) {
                        this.lname = value;
                    }

                    /**
                     * Gets the value of the dob property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDOB() {
                        return dob;
                    }

                    /**
                     * Sets the value of the dob property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDOB(XMLGregorianCalendar value) {
                        this.dob = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class A_TAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="CAR_REG"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="9"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="VIN" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="21"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "car_REG",
                    "vin"
                })
                public static class A_VEH {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "CAR_REG", required = true)
                    protected String car_REG;
                    @XmlElement(name = "VIN")
                    protected String vin;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the car_REG property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCAR_REG() {
                        return car_REG;
                    }

                    /**
                     * Sets the value of the car_REG property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCAR_REG(String value) {
                        this.car_REG = value;
                    }

                    /**
                     * Gets the value of the vin property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getVIN() {
                        return vin;
                    }

                    /**
                     * Sets the value of the vin property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setVIN(String value) {
                        this.vin = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *         &lt;element name="FNAME"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;minLength value="1"/&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PNAME" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="LNAME"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;minLength value="1"/&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
             *         &lt;element name="PER_INN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="12"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PREV_FN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PREV_PN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PREV_LN" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="50"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="INC_TM" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="13"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="INC_DOC" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="13"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="INC_ADD" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="13"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_PAS"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="DOC_NUMBER"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="12"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_DOC" maxOccurs="2" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="DOC_NUMBER"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="32"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
             *                   &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_SPO" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="FNAME"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;minLength value="1"/&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="PNAME" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="LNAME"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;minLength value="1"/&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_RAD"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_RTEL" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_CAD" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_CTEL" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_TAD" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_MTEL" maxOccurs="2" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_EMP" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="ORG_NAME"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="200"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="INN" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="12"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_ETEL" maxOccurs="2" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="TEL_NUM"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="18"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_EAD" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
             *                   &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
             *                   &lt;element name="REGION" type="{}RGC"/&gt;
             *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CITY" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="SETTLEMENT" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="STREET" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="50"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="HOUSE" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="BUILD" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="CONSTR" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="20"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="APART" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_EMA" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="EMAIL"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="64"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MA_VEH" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
             *                   &lt;element name="CAR_REG"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="9"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                   &lt;element name="VIN" minOccurs="0"&gt;
             *                     &lt;simpleType&gt;
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                         &lt;maxLength value="21"/&gt;
             *                       &lt;/restriction&gt;
             *                     &lt;/simpleType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *                 &lt;attribute name="entID"&gt;
             *                   &lt;simpleType&gt;
             *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *                     &lt;/restriction&gt;
             *                   &lt;/simpleType&gt;
             *                 &lt;/attribute&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *       &lt;attribute name="entID"&gt;
             *         &lt;simpleType&gt;
             *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *           &lt;/restriction&gt;
             *         &lt;/simpleType&gt;
             *       &lt;/attribute&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "status",
                "fname",
                "pname",
                "lname",
                "dob",
                "per_INN",
                "prev_FN",
                "prev_PN",
                "prev_LN",
                "inc_TM",
                "inc_DOC",
                "inc_ADD",
                "ma_PAS",
                "ma_DOC",
                "ma_SPO",
                "ma_RAD",
                "ma_RTEL",
                "ma_CAD",
                "ma_CTEL",
                "ma_TAD",
                "ma_MTEL",
                "ma_EMP",
                "ma_ETEL",
                "ma_EAD",
                "ma_EMA",
                "ma_VEH"
            })
            public static class MA {

                @XmlElement(name = "STATUS")
                @XmlSchemaType(name = "string")
                protected ENS status;
                @XmlElement(name = "FNAME", required = true)
                protected String fname;
                @XmlElement(name = "PNAME")
                protected String pname;
                @XmlElement(name = "LNAME", required = true)
                protected String lname;
                @XmlElement(name = "DOB", required = true)
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar dob;
                @XmlElement(name = "PER_INN")
                protected String per_INN;
                @XmlElement(name = "PREV_FN")
                protected String prev_FN;
                @XmlElement(name = "PREV_PN")
                protected String prev_PN;
                @XmlElement(name = "PREV_LN")
                protected String prev_LN;
                @XmlElement(name = "INC_TM")
                protected BigInteger inc_TM;
                @XmlElement(name = "INC_DOC")
                protected BigInteger inc_DOC;
                @XmlElement(name = "INC_ADD")
                protected BigInteger inc_ADD;
                @XmlElement(name = "MA_PAS", required = true)
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS ma_PAS;
                @XmlElement(name = "MA_DOC")
                protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_DOC> ma_DOC;
                @XmlElement(name = "MA_SPO")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_SPO ma_SPO;
                @XmlElement(name = "MA_RAD", required = true)
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD ma_RAD;
                @XmlElement(name = "MA_RTEL")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL ma_RTEL;
                @XmlElement(name = "MA_CAD")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD ma_CAD;
                @XmlElement(name = "MA_CTEL")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL ma_CTEL;
                @XmlElement(name = "MA_TAD")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_TAD ma_TAD;
                @XmlElement(name = "MA_MTEL")
                protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_MTEL> ma_MTEL;
                @XmlElement(name = "MA_EMP")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP ma_EMP;
                @XmlElement(name = "MA_ETEL")
                protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_ETEL> ma_ETEL;
                @XmlElement(name = "MA_EAD")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD ma_EAD;
                @XmlElement(name = "MA_EMA")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA ma_EMA;
                @XmlElement(name = "MA_VEH")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_VEH ma_VEH;
                @XmlAttribute(name = "entID")
                protected String entID;

                /**
                 * Gets the value of the status property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ENS }
                 *     
                 */
                public ENS getSTATUS() {
                    return status;
                }

                /**
                 * Sets the value of the status property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ENS }
                 *     
                 */
                public void setSTATUS(ENS value) {
                    this.status = value;
                }

                /**
                 * Gets the value of the fname property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getFNAME() {
                    return fname;
                }

                /**
                 * Sets the value of the fname property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setFNAME(String value) {
                    this.fname = value;
                }

                /**
                 * Gets the value of the pname property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPNAME() {
                    return pname;
                }

                /**
                 * Sets the value of the pname property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPNAME(String value) {
                    this.pname = value;
                }

                /**
                 * Gets the value of the lname property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getLNAME() {
                    return lname;
                }

                /**
                 * Sets the value of the lname property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setLNAME(String value) {
                    this.lname = value;
                }

                /**
                 * Gets the value of the dob property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDOB() {
                    return dob;
                }

                /**
                 * Sets the value of the dob property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDOB(XMLGregorianCalendar value) {
                    this.dob = value;
                }

                /**
                 * Gets the value of the per_INN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPER_INN() {
                    return per_INN;
                }

                /**
                 * Sets the value of the per_INN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPER_INN(String value) {
                    this.per_INN = value;
                }

                /**
                 * Gets the value of the prev_FN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPREV_FN() {
                    return prev_FN;
                }

                /**
                 * Sets the value of the prev_FN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPREV_FN(String value) {
                    this.prev_FN = value;
                }

                /**
                 * Gets the value of the prev_PN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPREV_PN() {
                    return prev_PN;
                }

                /**
                 * Sets the value of the prev_PN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPREV_PN(String value) {
                    this.prev_PN = value;
                }

                /**
                 * Gets the value of the prev_LN property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPREV_LN() {
                    return prev_LN;
                }

                /**
                 * Sets the value of the prev_LN property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPREV_LN(String value) {
                    this.prev_LN = value;
                }

                /**
                 * Gets the value of the inc_TM property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getINC_TM() {
                    return inc_TM;
                }

                /**
                 * Sets the value of the inc_TM property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setINC_TM(BigInteger value) {
                    this.inc_TM = value;
                }

                /**
                 * Gets the value of the inc_DOC property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getINC_DOC() {
                    return inc_DOC;
                }

                /**
                 * Sets the value of the inc_DOC property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setINC_DOC(BigInteger value) {
                    this.inc_DOC = value;
                }

                /**
                 * Gets the value of the inc_ADD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getINC_ADD() {
                    return inc_ADD;
                }

                /**
                 * Sets the value of the inc_ADD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setINC_ADD(BigInteger value) {
                    this.inc_ADD = value;
                }

                /**
                 * Gets the value of the ma_PAS property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS getMA_PAS() {
                    return ma_PAS;
                }

                /**
                 * Sets the value of the ma_PAS property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS }
                 *     
                 */
                public void setMA_PAS(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_PAS value) {
                    this.ma_PAS = value;
                }

                /**
                 * Gets the value of the maDOC property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the maDOC property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getMA_DOC().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_DOC }
                 * 
                 * 
                 */
                public List<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_DOC> getMA_DOC() {
                    if (ma_DOC == null) {
                        ma_DOC = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_DOC>();
                    }
                    return this.ma_DOC;
                }

                /**
                 * Gets the value of the ma_SPO property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_SPO }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_SPO getMA_SPO() {
                    return ma_SPO;
                }

                /**
                 * Sets the value of the ma_SPO property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_SPO }
                 *     
                 */
                public void setMA_SPO(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_SPO value) {
                    this.ma_SPO = value;
                }

                /**
                 * Gets the value of the ma_RAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD getMA_RAD() {
                    return ma_RAD;
                }

                /**
                 * Sets the value of the ma_RAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD }
                 *     
                 */
                public void setMA_RAD(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RAD value) {
                    this.ma_RAD = value;
                }

                /**
                 * Gets the value of the ma_RTEL property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL getMA_RTEL() {
                    return ma_RTEL;
                }

                /**
                 * Sets the value of the ma_RTEL property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL }
                 *     
                 */
                public void setMA_RTEL(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_RTEL value) {
                    this.ma_RTEL = value;
                }

                /**
                 * Gets the value of the ma_CAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD getMA_CAD() {
                    return ma_CAD;
                }

                /**
                 * Sets the value of the ma_CAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD }
                 *     
                 */
                public void setMA_CAD(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CAD value) {
                    this.ma_CAD = value;
                }

                /**
                 * Gets the value of the ma_CTEL property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL getMA_CTEL() {
                    return ma_CTEL;
                }

                /**
                 * Sets the value of the ma_CTEL property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL }
                 *     
                 */
                public void setMA_CTEL(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_CTEL value) {
                    this.ma_CTEL = value;
                }

                /**
                 * Gets the value of the ma_TAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_TAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_TAD getMA_TAD() {
                    return ma_TAD;
                }

                /**
                 * Sets the value of the ma_TAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_TAD }
                 *     
                 */
                public void setMA_TAD(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_TAD value) {
                    this.ma_TAD = value;
                }

                /**
                 * Gets the value of the maMTEL property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the maMTEL property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getMA_MTEL().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_MTEL }
                 * 
                 * 
                 */
                public List<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_MTEL> getMA_MTEL() {
                    if (ma_MTEL == null) {
                        ma_MTEL = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_MTEL>();
                    }
                    return this.ma_MTEL;
                }

                /**
                 * Gets the value of the ma_EMP property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP getMA_EMP() {
                    return ma_EMP;
                }

                /**
                 * Sets the value of the ma_EMP property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP }
                 *     
                 */
                public void setMA_EMP(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMP value) {
                    this.ma_EMP = value;
                }

                /**
                 * Gets the value of the maETEL property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the maETEL property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getMA_ETEL().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_ETEL }
                 * 
                 * 
                 */
                public List<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_ETEL> getMA_ETEL() {
                    if (ma_ETEL == null) {
                        ma_ETEL = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_ETEL>();
                    }
                    return this.ma_ETEL;
                }

                /**
                 * Gets the value of the ma_EAD property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD getMA_EAD() {
                    return ma_EAD;
                }

                /**
                 * Sets the value of the ma_EAD property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD }
                 *     
                 */
                public void setMA_EAD(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EAD value) {
                    this.ma_EAD = value;
                }

                /**
                 * Gets the value of the ma_EMA property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA getMA_EMA() {
                    return ma_EMA;
                }

                /**
                 * Sets the value of the ma_EMA property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA }
                 *     
                 */
                public void setMA_EMA(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_EMA value) {
                    this.ma_EMA = value;
                }

                /**
                 * Gets the value of the ma_VEH property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_VEH }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_VEH getMA_VEH() {
                    return ma_VEH;
                }

                /**
                 * Sets the value of the ma_VEH property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_VEH }
                 *     
                 */
                public void setMA_VEH(BATCH_IN.SUBMISSIONS.SUBMISSION.MA.MA_VEH value) {
                    this.ma_VEH = value;
                }

                /**
                 * Gets the value of the entID property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getEntID() {
                    return entID;
                }

                /**
                 * Sets the value of the entID property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setEntID(String value) {
                    this.entID = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class MA_CAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class MA_CTEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="DOC_NUMBER"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="32"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
                 *         &lt;element name="DOT" type="{}DOT" minOccurs="0"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "doc_NUMBER",
                    "doc_DATE",
                    "dot"
                })
                public static class MA_DOC {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "DOC_NUMBER", required = true)
                    protected String doc_NUMBER;
                    @XmlElement(name = "DOC_DATE")
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar doc_DATE;
                    @XmlElement(name = "DOT")
                    @XmlSchemaType(name = "string")
                    protected DOT dot;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the doc_NUMBER property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDOC_NUMBER() {
                        return doc_NUMBER;
                    }

                    /**
                     * Sets the value of the doc_NUMBER property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDOC_NUMBER(String value) {
                        this.doc_NUMBER = value;
                    }

                    /**
                     * Gets the value of the doc_DATE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDOC_DATE() {
                        return doc_DATE;
                    }

                    /**
                     * Sets the value of the doc_DATE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDOC_DATE(XMLGregorianCalendar value) {
                        this.doc_DATE = value;
                    }

                    /**
                     * Gets the value of the dot property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link DOT }
                     *     
                     */
                    public DOT getDOT() {
                        return dot;
                    }

                    /**
                     * Sets the value of the dot property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link DOT }
                     *     
                     */
                    public void setDOT(DOT value) {
                        this.dot = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class MA_EAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="EMAIL"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "email"
                })
                public static class MA_EMA {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "EMAIL", required = true)
                    protected String email;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the email property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEMAIL() {
                        return email;
                    }

                    /**
                     * Sets the value of the email property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEMAIL(String value) {
                        this.email = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="ORG_NAME"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="200"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="INN" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="12"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="JOT" type="{}JOT" minOccurs="0"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "org_NAME",
                    "inn",
                    "jot"
                })
                public static class MA_EMP {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "ORG_NAME", required = true)
                    protected String org_NAME;
                    @XmlElement(name = "INN")
                    protected String inn;
                    @XmlElement(name = "JOT")
                    @XmlSchemaType(name = "string")
                    protected JOT jot;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the org_NAME property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getORG_NAME() {
                        return org_NAME;
                    }

                    /**
                     * Sets the value of the org_NAME property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setORG_NAME(String value) {
                        this.org_NAME = value;
                    }

                    /**
                     * Gets the value of the inn property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getINN() {
                        return inn;
                    }

                    /**
                     * Sets the value of the inn property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setINN(String value) {
                        this.inn = value;
                    }

                    /**
                     * Gets the value of the jot property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link JOT }
                     *     
                     */
                    public JOT getJOT() {
                        return jot;
                    }

                    /**
                     * Sets the value of the jot property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link JOT }
                     *     
                     */
                    public void setJOT(JOT value) {
                        this.jot = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class MA_ETEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class MA_MTEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="DOC_NUMBER"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="12"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "doc_NUMBER",
                    "doc_DATE"
                })
                public static class MA_PAS {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "DOC_NUMBER", required = true)
                    protected String doc_NUMBER;
                    @XmlElement(name = "DOC_DATE", required = true)
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar doc_DATE;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the doc_NUMBER property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDOC_NUMBER() {
                        return doc_NUMBER;
                    }

                    /**
                     * Sets the value of the doc_NUMBER property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDOC_NUMBER(String value) {
                        this.doc_NUMBER = value;
                    }

                    /**
                     * Gets the value of the doc_DATE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDOC_DATE() {
                        return doc_DATE;
                    }

                    /**
                     * Sets the value of the doc_DATE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDOC_DATE(XMLGregorianCalendar value) {
                        this.doc_DATE = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class MA_RAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="TEL_NUM"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="18"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "tel_NUM"
                })
                public static class MA_RTEL {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "TEL_NUM", required = true)
                    protected String tel_NUM;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the tel_NUM property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTEL_NUM() {
                        return tel_NUM;
                    }

                    /**
                     * Sets the value of the tel_NUM property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTEL_NUM(String value) {
                        this.tel_NUM = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="FNAME"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;minLength value="1"/&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="PNAME" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="LNAME"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;minLength value="1"/&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "fname",
                    "pname",
                    "lname",
                    "dob"
                })
                public static class MA_SPO {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "FNAME", required = true)
                    protected String fname;
                    @XmlElement(name = "PNAME")
                    protected String pname;
                    @XmlElement(name = "LNAME", required = true)
                    protected String lname;
                    @XmlElement(name = "DOB", required = true)
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar dob;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the fname property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getFNAME() {
                        return fname;
                    }

                    /**
                     * Sets the value of the fname property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setFNAME(String value) {
                        this.fname = value;
                    }

                    /**
                     * Gets the value of the pname property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPNAME() {
                        return pname;
                    }

                    /**
                     * Sets the value of the pname property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPNAME(String value) {
                        this.pname = value;
                    }

                    /**
                     * Gets the value of the lname property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getLNAME() {
                        return lname;
                    }

                    /**
                     * Sets the value of the lname property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setLNAME(String value) {
                        this.lname = value;
                    }

                    /**
                     * Gets the value of the dob property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDOB() {
                        return dob;
                    }

                    /**
                     * Sets the value of the dob property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDOB(XMLGregorianCalendar value) {
                        this.dob = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="COUNTRY" type="{}CTR" minOccurs="0"/&gt;
                 *         &lt;element name="POST_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
                 *         &lt;element name="REGION" type="{}RGC"/&gt;
                 *         &lt;element name="DISTRICT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CITY" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="SETTLEMENT" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="STREET" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="50"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="HOUSE" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="BUILD" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="CONSTR" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="20"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="APART" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="64"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "country",
                    "post_CODE",
                    "region",
                    "district",
                    "city",
                    "settlement",
                    "street",
                    "house",
                    "build",
                    "constr",
                    "apart"
                })
                public static class MA_TAD {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "COUNTRY")
                    @XmlSchemaType(name = "string")
                    protected CTR country;
                    @XmlElement(name = "POST_CODE")
                    protected String post_CODE;
                    @XmlElement(name = "REGION", required = true)
                    @XmlSchemaType(name = "string")
                    protected RGC region;
                    @XmlElement(name = "DISTRICT")
                    protected String district;
                    @XmlElement(name = "CITY")
                    protected String city;
                    @XmlElement(name = "SETTLEMENT")
                    protected String settlement;
                    @XmlElement(name = "STREET")
                    protected String street;
                    @XmlElement(name = "HOUSE")
                    protected String house;
                    @XmlElement(name = "BUILD")
                    protected String build;
                    @XmlElement(name = "CONSTR")
                    protected String constr;
                    @XmlElement(name = "APART")
                    protected String apart;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the country property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link CTR }
                     *     
                     */
                    public CTR getCOUNTRY() {
                        return country;
                    }

                    /**
                     * Sets the value of the country property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link CTR }
                     *     
                     */
                    public void setCOUNTRY(CTR value) {
                        this.country = value;
                    }

                    /**
                     * Gets the value of the post_CODE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPOST_CODE() {
                        return post_CODE;
                    }

                    /**
                     * Sets the value of the post_CODE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPOST_CODE(String value) {
                        this.post_CODE = value;
                    }

                    /**
                     * Gets the value of the region property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RGC }
                     *     
                     */
                    public RGC getREGION() {
                        return region;
                    }

                    /**
                     * Sets the value of the region property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RGC }
                     *     
                     */
                    public void setREGION(RGC value) {
                        this.region = value;
                    }

                    /**
                     * Gets the value of the district property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getDISTRICT() {
                        return district;
                    }

                    /**
                     * Sets the value of the district property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setDISTRICT(String value) {
                        this.district = value;
                    }

                    /**
                     * Gets the value of the city property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCITY() {
                        return city;
                    }

                    /**
                     * Sets the value of the city property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCITY(String value) {
                        this.city = value;
                    }

                    /**
                     * Gets the value of the settlement property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSETTLEMENT() {
                        return settlement;
                    }

                    /**
                     * Sets the value of the settlement property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSETTLEMENT(String value) {
                        this.settlement = value;
                    }

                    /**
                     * Gets the value of the street property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSTREET() {
                        return street;
                    }

                    /**
                     * Sets the value of the street property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSTREET(String value) {
                        this.street = value;
                    }

                    /**
                     * Gets the value of the house property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getHOUSE() {
                        return house;
                    }

                    /**
                     * Sets the value of the house property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setHOUSE(String value) {
                        this.house = value;
                    }

                    /**
                     * Gets the value of the build property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getBUILD() {
                        return build;
                    }

                    /**
                     * Sets the value of the build property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setBUILD(String value) {
                        this.build = value;
                    }

                    /**
                     * Gets the value of the constr property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCONSTR() {
                        return constr;
                    }

                    /**
                     * Sets the value of the constr property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCONSTR(String value) {
                        this.constr = value;
                    }

                    /**
                     * Gets the value of the apart property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getAPART() {
                        return apart;
                    }

                    /**
                     * Sets the value of the apart property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setAPART(String value) {
                        this.apart = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="STATUS" type="{}ENS" minOccurs="0"/&gt;
                 *         &lt;element name="CAR_REG"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="9"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *         &lt;element name="VIN" minOccurs="0"&gt;
                 *           &lt;simpleType&gt;
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *               &lt;maxLength value="21"/&gt;
                 *             &lt;/restriction&gt;
                 *           &lt;/simpleType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *       &lt;attribute name="entID"&gt;
                 *         &lt;simpleType&gt;
                 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
                 *           &lt;/restriction&gt;
                 *         &lt;/simpleType&gt;
                 *       &lt;/attribute&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "status",
                    "car_REG",
                    "vin"
                })
                public static class MA_VEH {

                    @XmlElement(name = "STATUS")
                    @XmlSchemaType(name = "string")
                    protected ENS status;
                    @XmlElement(name = "CAR_REG", required = true)
                    protected String car_REG;
                    @XmlElement(name = "VIN")
                    protected String vin;
                    @XmlAttribute(name = "entID")
                    protected String entID;

                    /**
                     * Gets the value of the status property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ENS }
                     *     
                     */
                    public ENS getSTATUS() {
                        return status;
                    }

                    /**
                     * Sets the value of the status property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ENS }
                     *     
                     */
                    public void setSTATUS(ENS value) {
                        this.status = value;
                    }

                    /**
                     * Gets the value of the car_REG property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getCAR_REG() {
                        return car_REG;
                    }

                    /**
                     * Sets the value of the car_REG property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setCAR_REG(String value) {
                        this.car_REG = value;
                    }

                    /**
                     * Gets the value of the vin property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getVIN() {
                        return vin;
                    }

                    /**
                     * Sets the value of the vin property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setVIN(String value) {
                        this.vin = value;
                    }

                    /**
                     * Gets the value of the entID property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getEntID() {
                        return entID;
                    }

                    /**
                     * Sets the value of the entID property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setEntID(String value) {
                        this.entID = value;
                    }

                }

            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;all&gt;
             *         &lt;element name="NOTES" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;complexContent&gt;
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *                 &lt;sequence&gt;
             *                   &lt;element name="NOTE" maxOccurs="unbounded"&gt;
             *                     &lt;complexType&gt;
             *                       &lt;simpleContent&gt;
             *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
             *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *                           &lt;attribute name="privacyLevel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *                         &lt;/extension&gt;
             *                       &lt;/simpleContent&gt;
             *                     &lt;/complexType&gt;
             *                   &lt;/element&gt;
             *                 &lt;/sequence&gt;
             *               &lt;/restriction&gt;
             *             &lt;/complexContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
             *       &lt;/all&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {

            })
            public static class SUB_HEADER {

                @XmlElement(name = "NOTES")
                protected BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES notes;

                /**
                 * Gets the value of the notes property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES }
                 *     
                 */
                public BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES getNOTES() {
                    return notes;
                }

                /**
                 * Sets the value of the notes property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES }
                 *     
                 */
                public void setNOTES(BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES value) {
                    this.notes = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;complexContent&gt;
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
                 *       &lt;sequence&gt;
                 *         &lt;element name="NOTE" maxOccurs="unbounded"&gt;
                 *           &lt;complexType&gt;
                 *             &lt;simpleContent&gt;
                 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
                 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
                 *                 &lt;attribute name="privacyLevel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
                 *               &lt;/extension&gt;
                 *             &lt;/simpleContent&gt;
                 *           &lt;/complexType&gt;
                 *         &lt;/element&gt;
                 *       &lt;/sequence&gt;
                 *     &lt;/restriction&gt;
                 *   &lt;/complexContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "note"
                })
                public static class NOTES {

                    @XmlElement(name = "NOTE", required = true)
                    protected List<BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES.NOTE> note;

                    /**
                     * Gets the value of the note property.
                     * 
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the note property.
                     * 
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     *    getNOTE().add(newItem);
                     * </pre>
                     * 
                     * 
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES.NOTE }
                     * 
                     * 
                     */
                    public List<BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES.NOTE> getNOTE() {
                        if (note == null) {
                            note = new ArrayList<BATCH_IN.SUBMISSIONS.SUBMISSION.SUB_HEADER.NOTES.NOTE>();
                        }
                        return this.note;
                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType&gt;
                     *   &lt;simpleContent&gt;
                     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
                     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
                     *       &lt;attribute name="privacyLevel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
                     *     &lt;/extension&gt;
                     *   &lt;/simpleContent&gt;
                     * &lt;/complexType&gt;
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "value"
                    })
                    public static class NOTE {

                        @XmlValue
                        protected String value;
                        @XmlAttribute(name = "type")
                        protected String type;
                        @XmlAttribute(name = "privacyLevel")
                        protected String privacyLevel;

                        /**
                         * Gets the value of the value property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getValue() {
                            return value;
                        }

                        /**
                         * Sets the value of the value property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setValue(String value) {
                            this.value = value;
                        }

                        /**
                         * Gets the value of the type property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getType() {
                            return type;
                        }

                        /**
                         * Sets the value of the type property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setType(String value) {
                            this.type = value;
                        }

                        /**
                         * Gets the value of the privacyLevel property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getPrivacyLevel() {
                            return privacyLevel;
                        }

                        /**
                         * Sets the value of the privacyLevel property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setPrivacyLevel(String value) {
                            this.privacyLevel = value;
                        }

                    }

                }

            }

        }

    }

}
