package ru.simplgroupp.ssl;

import javax.net.ssl.SSLContext;

import org.admnkz.crypto.app.ICryptoService;
import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;

/**
 * Создаёт соединения SSL между указанным клиентом и сервером. Клиент - 0, сервер - 1
 * @author irina
 *
 */
public class SSLContextFactory extends BaseKeyedPoolableObjectFactory<String[], SSLContext> {
	
	ICryptoService cryptoServ;

	public SSLContextFactory(ICryptoService svc) {
		super();
		cryptoServ = svc;
	}
	
	@Override
	public SSLContext makeObject(String[] settings) throws Exception {
		SSLContext ctx = cryptoServ.createTrustedSSLContext(settings[0], settings[1]);
		return ctx;
	}

}
