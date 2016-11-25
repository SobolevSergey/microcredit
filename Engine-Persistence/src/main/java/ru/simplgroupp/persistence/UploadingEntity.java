package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Загрузка данных в КБ
 */
public class UploadingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Integer txVersion = 0;
	
	private Integer id;
	/**
	 * дата загрузки
	 */
    private Date dateUploading;
    /**
     * партнер, кому загружаем
     */
    private PartnersEntity partnersId;
    /**
     * статус загрузки
     */
    private Integer status;
    /**
     * название файла загрузки
     */
    private String name;
    /**
     * строка файла загрузки
     */
    private String info;
    /**
     * отчет о загрузке
     */
    private String report;
    /**
     * всего записей
     */
    private Integer recordsAll;
    /**
     * корректных записей
     */
    private Integer recordsCorrect;
    /**
     * записей с ошибкой
     */
    private Integer recordsError;
    /**
     * номер загрузки
     */
    private Integer uploadId;
    /**
     * номер ответа о загрузке
     */
    private String responseId;
    /**
     * дата ответа о загрузке
     */
    private Date responseDate;
    /**
     * вид загрузки
     */
    private Integer uploadType;
    /**
     * ошибки загрузки
     */
    private List<UploadingErrorEntity> errors=new ArrayList<UploadingErrorEntity>(0);
    /**
     * детали загрузки
     */
    private List<UploadingDetailEntity> details=new ArrayList<UploadingDetailEntity>(0);
    
    public UploadingEntity(){
    	
    }

    public UploadingEntity(Integer id){
    	this.id=id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUploadId() {
        return uploadId;
    }

    public void setUploadId(Integer uploadId) {
        this.uploadId = uploadId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

   
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getRecordsAll() {
        return recordsAll;
    }

    public void setRecordsAll(Integer recordsAll) {
        this.recordsAll = recordsAll;
    }
    
    public Integer getRecordsCorrect() {
        return recordsCorrect;
    }

    public void setRecordsCorrect(Integer recordsCorrect) {
        this.recordsCorrect = recordsCorrect;
    }
    
    public Date getDateUploading() {
        return dateUploading;
    }

    public Integer getRecordsError() {
        return recordsError;
    }

    public void setRecordsError(Integer recordsError) {
        this.recordsError = recordsError;
    }
    
    public void setDateUploading(Date dateUploading) {
        this.dateUploading = dateUploading;
    }
    
    public String getName(){
    	return name;
    }
    
    public void setName(String name){
    	this.name=name;
    }
    
    public String getInfo(){
    	return info;
    }
    
    public void setInfo(String info){
    	this.info=info;
    }
    
    public String getResponseId(){
    	return responseId;
    }
    
    public void setResponseId(String responseId){
    	this.responseId=responseId;
    }
    
    public String getReport(){
    	return report;
    }
    
    public void setReport(String report){
    	this.report=report;
    }
    
    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }
    
    public List<UploadingErrorEntity> getErrors() {
    	return errors;
    }
    
    public void setErrors(List<UploadingErrorEntity> errors){
    	this.errors=errors;
    }
    
    public List<UploadingDetailEntity> getDetails() {
		return details;
	}

	public void setDetails(List<UploadingDetailEntity> details) {
		this.details = details;
	}

	
	public Integer getUploadType() {
		return uploadType;
	}

	public void setUploadType(Integer uploadType) {
		this.uploadType = uploadType;
	}

	public Integer getHasErrors(){
    	if (getErrors().size()>0){
    		return getErrors().size();
    	}
    	return 0;
    }
	
	public Integer getHasDetails(){
    	if (getDetails().size()>0){
    		return getDetails().size();
    	}
    	return 0;
    }
	
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
   	 if (other == null) return false;
     
	    if (other == this) return true;
	       
	    if (! (other.getClass() ==  getClass()))
	    	return false;
	    
	    UploadingEntity cast = (UploadingEntity) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (this.id.intValue() != cast.getId().intValue())
	    	return false;
	    
	    return true;
    }
}
