package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * конфигурация продукта
 *
 */
public class ProductConfigEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1082150756549693953L;

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
     * тип данных
     */
    private String dataType;
    /**
     * значение
     */
    private String dataValue;
    /**
     * продукт
     */
    private ProductsEntity productId;
    /**
     * что кофигурируем (по справочнику)
     */
    private ReferenceEntity configTypeId;
    
    public ProductConfigEntity(){
    	
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
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
