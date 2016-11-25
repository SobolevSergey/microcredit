package ru.simplgroupp.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import ru.simplgroupp.util.HTTPUtils;

/**
 * класс для создания пустой ssl фабрики
 * для посылки писем
 *
 */
public class DummySSLSocketFactory extends SSLSocketFactory{

	private SSLSocketFactory factory;
	
	public DummySSLSocketFactory(){
		SSLContext sslcontext = HTTPUtils.createEmptyTrustSSLContext();
		factory = ( SSLSocketFactory) sslcontext.getSocketFactory();
	}
	
	public static SocketFactory getDefault() {
	      return new DummySSLSocketFactory();
	}
	  
	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean flag)
			throws IOException {
		return factory.createSocket( socket, s, i, flag);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(String s, int i) throws IOException,
			UnknownHostException {
	   return factory.createSocket( s, i);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i) throws IOException {
		return factory.createSocket( inaddr, i);
	}

	@Override
	public Socket createSocket(String s, int i, InetAddress inaddr, int j)
			throws IOException, UnknownHostException {
		return factory.createSocket( s, i, inaddr, j);
	}

	@Override
	public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1,
			int j) throws IOException {
		return factory.createSocket( inaddr, i, inaddr1, j);
	}

}
