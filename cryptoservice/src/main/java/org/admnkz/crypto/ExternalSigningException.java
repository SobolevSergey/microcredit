package org.admnkz.crypto;

public class ExternalSigningException extends CryptoException {

	public ExternalSigningException() {
		super();
	}

	public ExternalSigningException(String message) {
		super(message);
	}

	public ExternalSigningException(Throwable cause) {
		super(cause);
	}

	public ExternalSigningException(String message, Throwable cause) {
		super(message, cause);
	}

}
