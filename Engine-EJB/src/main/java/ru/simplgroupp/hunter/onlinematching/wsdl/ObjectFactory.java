
package ru.simplgroupp.hunter.onlinematching.wsdl;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.simplgroupp.hunter.onlinematching.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.simplgroupp.hunter.onlinematching.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MatchResponse }
     * 
     */
    public MatchResponse createMatchResponse() {
        return new MatchResponse();
    }

    /**
     * Create an instance of {@link Match }
     * 
     */
    public Match createMatch() {
        return new Match();
    }

    /**
     * Create an instance of {@link PrePopulateCache }
     * 
     */
    public PrePopulateCache createPrePopulateCache() {
        return new PrePopulateCache();
    }

    /**
     * Create an instance of {@link PrePopulateCacheResponse }
     * 
     */
    public PrePopulateCacheResponse createPrePopulateCacheResponse() {
        return new PrePopulateCacheResponse();
    }

    /**
     * Create an instance of {@link ClearCacheResponse }
     * 
     */
    public ClearCacheResponse createClearCacheResponse() {
        return new ClearCacheResponse();
    }

    /**
     * Create an instance of {@link ClearCache }
     * 
     */
    public ClearCache createClearCache() {
        return new ClearCache();
    }

}
