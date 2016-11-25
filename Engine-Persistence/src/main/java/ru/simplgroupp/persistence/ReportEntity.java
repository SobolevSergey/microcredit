package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * отчеты
 */
public class ReportEntity  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2820472233583436165L;
	protected Integer txVersion = 0;
	private Integer id;
	/**
	 * название
	 */
	private String name;
	/**
	 * текст шаблона
	 */
	private String body;
	/**
	 * как исполняем
	 */
	private String reportExecutor;
	/**
	 * тип mime
	 */
	private String mimeType;
	/**
	 * вид отчета
	 */
	private ReportTypeEntity reportTypeId;
	/**
	 * код отчета
	 */
	private String code;
	/**
     * продукт
     */
    private ProductsEntity productId;
	
    public ReportEntity(){
    	
    }
    public ReportEntity(Integer id){
    	this.id=id;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getReportExecutor() {
		return reportExecutor;
	}
	public void setReportExecutor(String reportExecutor) {
		this.reportExecutor = reportExecutor;
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
	public ReportTypeEntity getReportTypeId() {
		return reportTypeId;
	}
	public void setReportTypeId(ReportTypeEntity reportTypeId) {
		this.reportTypeId = reportTypeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	 @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (id != null ? id.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object other) {
	        if (other == null) {
	            return false;
	        }

	        if (other == this) {
	            return true;
	        }

	        if (!(other.getClass() == getClass())) {
	            return false;
	        }

	        ReportEntity entity = (ReportEntity) other;

	        if (this.id == null) {
	            return false;
	        }

	        if (entity.getId() == null) {
	            return false;
	        }

	        return this.id.equals(entity.getId());
	    }


}
