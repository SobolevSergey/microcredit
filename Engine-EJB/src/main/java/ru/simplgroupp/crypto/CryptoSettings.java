package ru.simplgroupp.crypto;

/**
 * параметры из таблицы Settings 
 *
 */
public class CryptoSettings {

	//КБ
	
	//Русский стандарт
	public static final String RUS_STANDART_CLIENT_TEST="ssl.rusStandart.client.test";//сертификат наш для теста
	public static final String RUS_STANDART_SERVER_TEST="ssl.rusStandart.server";//серверный тестовый сертификат
	public static final String RUS_STANDART_CLIENT_WORK="ssl.rusStandart.client.work";//сертификат наш для продуктива
	public static final String RUS_STANDART_SERVER_WORK="ssl.rusStandart.server";//серверный продуктивный сертификат
	//СКБ	
	public static final String SKB_CLIENT_WORK="ssl.skb.client.work";//сертификат наш для продуктива
	public static final String SKB_SERVER_WORK="ssl.skb.server";//серверный продуктивный сертификат
	//Эквифакс
	public static final String EQUIFAX_SIGN_TEST="sign.equifax.test";//сертификат наш для теста
	public static final String EQUIFAX_CHECK_TEST="check.equifax.test";//сертификат тестовый для шифрования
	public static final String EQUIFAX_SIGN_WORK="sign.equifax.work";//сертификат наш для продуктива
	public static final String EQUIFAX_CHECK_WORK="check.equifax.work";//сертификат продуктивный для шифрования
	//ОКБ		
	public static final String OKB_CLIENT_WORK="ssl.OkbCais.client.work";//сертификат наш для продуктива
	public static final String OKB_SERVER_WORK="ssl.OkbCais.server";//серверный продуктивный сертификат
	public static final String OKB_CLIENT_TEST="ssl.OkbCais.client.test";//сертификат для теста
	public static final String OKB_SERVER_TEST="ssl.OkbCais.server";//серверный продуктивный сертификат
	public static final String OKB_SERVER_ENCRYPT_WORK="encrypt.OkbCais.server";//сертификат продуктивный для шифрования
	public static final String OKB_SERVER_SIGN_TEST="sign.OkbCais.server";//тестовый сертификат ОКБ для подписи
	public static final String OKB_SERVER_SIGN_WORK="sign.OkbCais.server";//продуктивный сертификат ОКБ для подписи
	//НБКИ
	public static final String NBKI_CLIENT_TEST="ssl.nbki.client.test";//тестовый сертификат для подписания
	public static final String NBKI_SERVER_TEST="ssl.nbki.server.test";//тестовый серверный сертификат
	public static final String NBKI_CLIENT_WORK="ssl.nbki.client.work";//рабочий сертификат для подписания
	public static final String NBKI_SERVER_WORK="ssl.nbki.server.work";//рабочий серверный сертификат
	public static final String NBKI_CHECK_TEST="check.nbki.test";//тестовый сертификат для проверки подписи
	public static final String NBKI_CHECK_WORK="check.nbki.work";//рабочий сертификат для проверки подписи
	public static final String NBKI_ENCRYPT_TEST="encrypt.nbki.test";//тестовый сертификат для шифрования - неизвестно, есть ли
	public static final String NBKI_ENCRYPT_WORK="encrypt.nbki.work";//рабочий сертификат для шифрования
	public static final String NBKI_ENCRYPT_CLIENT_WORK="encrypt.nbki.client.work";//наш рабочий сертификат для шифрования
	
	//Платежные системы
    public static final String YANDEX_PAY_SIGN_CLIENT_TEST="sign.yandex-pay.test.client";
    public static final String YANDEX_PAY_SIGN_CLIENT="sign.yandex-pay.client";

    public static final String YANDEX_PAY_CLIENT_TEST="ssl.yandex-pay.test.client";
    public static final String YANDEX_PAY_CLIENT="ssl.yandex-pay.client";

    public static final String YANDEX_PAY_SERVER_TEST="ssl.yandex-pay.test.server";
    public static final String YANDEX_PAY_SERVER="ssl.yandex-pay.server";
    
    public static final String CONTACT_PAY_SIGN_CLIENT_TEST="ssl.contact.client.test";
    public static final String CONTACT_PAY_SIGN_CLIENT="ssl.contact.client.work";

    public static final String CONTACT_PAY_CLIENT_TEST="ssl.contact.client.test";
    public static final String CONTACT_PAY_CLIENT="ssl.contact.client.work";

    public static final String CONTACT_PAY_SERVER_TEST="ssl.contact.server.test";
    public static final String CONTACT_PAY_SERVER="ssl.contact.server.work";

    public static final String PAYONLINE_PAY_CLIENT = "ssl.payonline.client";
    public static final String PAYONLINE_PAY_SERVER = "ssl.payonline.server";
}
