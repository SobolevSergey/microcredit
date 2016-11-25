package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

/**
 * стратегии для одобрения займов
 */
public class AIModelEntity extends BaseEntity implements Serializable, Identifiable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1815075869500621880L;
	protected Integer txVersion = 0;
	/**
	 * дата создания
	 */
	private Date dateCreate;
	/**
	 * версия
	 */
	private String version;
	/**
	 * текст стратегии
	 */
	private String body;
	/**
	 * активность
	 */
	private Integer isActive;
	/**
	 * что считаем
	 */
	private String target;
	/**
	 * параметры модели
	 */
	private Set<AIModelParamEntity> params = new HashSet<AIModelParamEntity>(0);
	/**
	 * тип mime
	 */
	private String mimeType;
	/**
	 * дата изменения
	 */
	private Date dateChange;
	/**
     * продукт
     */
    private ProductsEntity productId;
    /**
     * способ подачи заявки
     */
    private ReferenceEntity wayId;
    
	public AIModelEntity(){
		
	}
	
    public AIModelEntity(Integer id){
		this.id=id;
	}

	@Override
	public void init(Set options) {
		
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

   	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	
	public Date getDateChange() {
		return dateChange;
	}

	public void setDateChange(Date dateChange) {
		this.dateChange = dateChange;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public ProductsEntity getProductId() {
		return productId;
	}

	public void setProductId(ProductsEntity productId) {
		this.productId = productId;
	}

	public Set<AIModelParamEntity> getParams() {
		return params;
	}

	public void setParams(Set<AIModelParamEntity> params) {
		this.params = params;
	}

	public ReferenceEntity getWayId() {
		return wayId;
	}

	public void setWayId(ReferenceEntity wayId) {
		this.wayId = wayId;
	}

	
}
