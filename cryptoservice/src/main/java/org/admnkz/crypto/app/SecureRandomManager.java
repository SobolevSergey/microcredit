package org.admnkz.crypto.app;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.admnkz.common.IOptions;
import org.admnkz.crypto.data.Settings;
import org.apache.commons.codec.binary.Base64;

@Singleton
@Named
public class SecureRandomManager 
{
	
    @EJB
    protected IOptions options;	
    
    @PersistenceContext(unitName="CryptoPU")
    protected EntityManager em;    

	protected Map<String, SecureRandom> randoms = new HashMap<String, SecureRandom>(3); 
	
	public synchronized SecureRandom getRandom(String cid, Settings sets) throws IOException, NoSuchAlgorithmException, NoSuchProviderException 
	{
		SecureRandom random = randoms.get(cid);
		if (random != null) 
			return random;
		
		String sdir = options.getValue("cryptoservice.securerandom.path") + cid;
		File fdir = new File(sdir);
		if (! fdir.exists() || fdir.list().length <= 1) {
			Query qry = em.createNamedQuery("selectSecureRandom");
			qry.setParameter("ID", cid);
			List lst = qry.getResultList();
			if (lst.size() > 0) {
				String srtext = (String) lst.get(0);
				byte[] csecrzip = Base64.decodeBase64(srtext.getBytes());
				
				if (fdir.exists())
					fdir.delete();
				fdir.mkdir();				
				CryptoUtils.unzipTo(csecrzip, fdir);
			}
		}
		
		random = SecureRandom.getInstance(sets.getSecureRandomAlg(), sets.getProviderName());			
		random.setSeed(sdir.getBytes());
		randoms.put(cid, random);
		return random;
		
	}
	
	public synchronized SecureRandom getRandom(Settings sets) throws IOException, NoSuchAlgorithmException, NoSuchProviderException 
	{
		SecureRandom random = randoms.get(sets.getID());
		if (random != null) 
			return random;
		
		String sdir = options.getValue("cryptoservice.securerandom.path") + sets.getID();
		File fdir = new File(sdir);
		if (! fdir.exists() || fdir.list().length <= 1) {
			if (fdir.exists())
				fdir.delete();
			fdir.mkdir();							
		}
		
		random = SecureRandom.getInstance(sets.getSecureRandomAlg(), sets.getProviderName());			
		random.setSeed(sdir.getBytes());
		randoms.put(sets.getID(), random);
		return random;
		
	}	
}
