package org.admnkz.crypto.app;

import javax.ejb.Remote;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.app.ICryptoService.ParseCMSResult;
import org.admnkz.crypto.data.Settings;

@Remote
public interface IRemoteCryptoService {
	public byte[] sign(final byte[] data, String optkey) throws CryptoException;
	public byte[] sign(final byte[] data, Settings options) throws CryptoException;
	public boolean verify(final byte[] data, byte[] signature, Settings options,boolean bSignatureOnly) throws  CryptoException;
	public boolean verify(final byte[] data, byte[] signature, String optkey, boolean bSignatureOnly) throws  CryptoException;
	public byte[] digest(byte[] source, String optkey) throws CryptoException;
	public byte[] digest(byte[] source, Settings sets) throws CryptoException;
	public byte[] signCMS(byte[] source, String optkey) throws CryptoException;
	public byte[] signCMS(byte[] source, Settings sets) throws CryptoException;
	public boolean verifyCMS(byte[] sig, byte[] data, String optkey, boolean bSignatureOnly) throws CryptoException;
	public boolean verifyCMS(byte[] sig, byte[] data, Settings sets, boolean bSignatureOnly) throws CryptoException;
	
}
