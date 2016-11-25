
package ru.simplgroupp.hunter.batch;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.simplgroupp.hunter.batch package. 
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

    private final static QName _ResultBlockType_QNAME = new QName("", "ResultBlockType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.simplgroupp.hunter.batch
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ScoreValueType }
     * 
     */
    public ScoreValueType createScoreValueType() {
        return new ScoreValueType();
    }

    /**
     * Create an instance of {@link ErrorType }
     * 
     */
    public ErrorType createErrorType() {
        return new ErrorType();
    }

    /**
     * Create an instance of {@link RulesType }
     * 
     */
    public RulesType createRulesType() {
        return new RulesType();
    }

    /**
     * Create an instance of {@link ResultBlockType }
     * 
     */
    public ResultBlockType createResultBlockType() {
        return new ResultBlockType();
    }

    /**
     * Create an instance of {@link ErrorWarningType }
     * 
     */
    public ErrorWarningType createErrorWarningType() {
        return new ErrorWarningType();
    }

    /**
     * Create an instance of {@link RuleIDType }
     * 
     */
    public RuleIDType createRuleIDType() {
        return new RuleIDType();
    }

    /**
     * Create an instance of {@link SchemeIDType }
     * 
     */
    public SchemeIDType createSchemeIDType() {
        return new SchemeIDType();
    }

    /**
     * Create an instance of {@link MatchSummaryTypeBase }
     * 
     */
    public MatchSummaryTypeBase createMatchSummaryTypeBase() {
        return new MatchSummaryTypeBase();
    }

    /**
     * Create an instance of {@link Scheme }
     * 
     */
    public Scheme createScheme() {
        return new Scheme();
    }

    /**
     * Create an instance of {@link MatchSchemeType }
     * 
     */
    public MatchSchemeType createMatchSchemeType() {
        return new MatchSchemeType();
    }

    /**
     * Create an instance of {@link SubmissionIDType }
     * 
     */
    public SubmissionIDType createSubmissionIDType() {
        return new SubmissionIDType();
    }

    /**
     * Create an instance of {@link ScoreTypeResult }
     * 
     */
    public ScoreTypeResult createScoreTypeResult() {
        return new ScoreTypeResult();
    }

    /**
     * Create an instance of {@link ErrorsType }
     * 
     */
    public ErrorsType createErrorsType() {
        return new ErrorsType();
    }

    /**
     * Create an instance of {@link WarningsType }
     * 
     */
    public WarningsType createWarningsType() {
        return new WarningsType();
    }

    /**
     * Create an instance of {@link MatchSummaryType }
     * 
     */
    public MatchSummaryType createMatchSummaryType() {
        return new MatchSummaryType();
    }

    /**
     * Create an instance of {@link SubmissionType }
     * 
     */
    public SubmissionType createSubmissionType() {
        return new SubmissionType();
    }

    /**
     * Create an instance of {@link SubmissionsType }
     * 
     */
    public SubmissionsType createSubmissionsType() {
        return new SubmissionsType();
    }

    /**
     * Create an instance of {@link ScoreValueType.ScoreValue }
     * 
     */
    public ScoreValueType.ScoreValue createScoreValueTypeScoreValue() {
        return new ScoreValueType.ScoreValue();
    }

    /**
     * Create an instance of {@link ErrorType.Values }
     * 
     */
    public ErrorType.Values createErrorTypeValues() {
        return new ErrorType.Values();
    }

    /**
     * Create an instance of {@link RulesType.Rule }
     * 
     */
    public RulesType.Rule createRulesTypeRule() {
        return new RulesType.Rule();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultBlockType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ResultBlockType")
    public JAXBElement<ResultBlockType> createResultBlockType(ResultBlockType value) {
        return new JAXBElement<ResultBlockType>(_ResultBlockType_QNAME, ResultBlockType.class, null, value);
    }

}
