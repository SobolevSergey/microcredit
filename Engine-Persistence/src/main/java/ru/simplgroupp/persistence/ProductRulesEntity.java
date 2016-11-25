package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * скрипты и правила для продукта
 */
public class ProductRulesEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3999339068676282615L;
	
	protected Integer txVersion = 0;
	
	/**
     * дата начала
     */
    private Date databeg;
    /**
     * дата окончания
     */
    private Date dataend;
    /**
     * активность
     */
    private Integer isActive;
    /**
     * название
     */
    private String name;
    /**
     * описание
     */
    private String description;
    /**
     * константа для knowledge base
     */
    private String kbase;
    /**
     * скрипт (правило)
     */
    private String ruleBody;
    /**
     * вид скрипта
     */
    private Integer scriptTypeId;
    /**
     * продукт
     */
    private ProductsEntity productId;
    /**
     * что кофигурируем (по справочнику)
     */
    private ReferenceEntity configTypeId;
    
    public ProductRulesEntity(){
    	
    }
    
   	public Date getDatabeg() {
		return databeg;
	}
	public void setDatabeg(Date databeg) {
		this.databeg = databeg;
	}
	public Date getDataend() {
		return dataend;
	}
	public void setDataend(Date dataend) {
		this.dataend = dataend;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKbase() {
		return kbase;
	}
	public void setKbase(String kbase) {
		this.kbase = kbase;
	}
	public String getRuleBody() {
		return ruleBody;
	}
	public void setRuleBody(String ruleBody) {
		this.ruleBody = ruleBody;
	}
	public Integer getScriptTypeId() {
		return scriptTypeId;
	}
	public void setScriptTypeId(Integer scriptTypeId) {
		this.scriptTypeId = scriptTypeId;
	}
	public ProductsEntity getProductId() {
		return productId;
	}
	public void setProductId(ProductsEntity productId) {
		this.productId = productId;
	}
	public ReferenceEntity getConfigTypeId() {
		return configTypeId;
	}
	public void setConfigTypeId(ReferenceEntity configTypeId) {
		this.configTypeId = configTypeId;
	}
 
}
