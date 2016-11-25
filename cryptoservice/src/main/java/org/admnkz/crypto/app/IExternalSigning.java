package org.admnkz.crypto.app;

import java.util.List;

import org.admnkz.crypto.ExternalSigningException;

public interface IExternalSigning 
{
	/**
	 * Подписывает данные на внешнем клиенте, используя заданный алгоритм
	 * @param data - данные для подписания
	 * @param sigAlg - название алгоритма подписи 
	 * @return подпись в формате CMS
	 * @throws ExternalSigningException
	 */
	public List<byte[]> signCMS(List<byte[]> data, String sigAlg) throws ExternalSigningException;
}
