package ru.simplgroupp.persistence;

import java.io.Serializable;

public class ProductMessagesEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8245341491692849338L;

	protected Integer txVersion = 0;
	
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
    private String subject;
    /**
     * значение
     */
    private String body;
    /**
     * сигнал
     */
    private String signalRef;
    /**
     * продукт
     */
    private ProductsEntity productId;
    /**
     * что кофигурируем (по справочнику)
     */
    private ReferenceEntity configTypeId;
    
    public ProductMessagesEntity(){
    	
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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
    
	public String getSignalRef() {
		return signalRef;
	}

	public void setSignalRef(String signalRef) {
		this.signalRef = signalRef;
	}

	
}
