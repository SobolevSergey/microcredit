package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.persistence.UploadingErrorEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Uploading extends BaseTransfer<UploadingEntity> implements Serializable, Initializable,Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5389598426968054692L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Uploading.class.getConstructor(UploadingEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public enum Options {
		INIT_DETAIL,
		INIT_ERROR;
	}
	
	//константы для ОКБ
	public static final int FUNCTION_UPLOAD_CAIS=21;
	public static final int FLAG_UPLOAD_CAIS=7;
	
	protected Partner partner;
	protected List<UploadingDetail> uploadingDetails;
	protected List<UploadingError> uploadingErrors;
	
	public Uploading(){
		super();
		entity=new UploadingEntity();
	}
	
	public Uploading(UploadingEntity entity){
		super(entity);
		partner=new Partner(entity.getPartnersId());
		uploadingDetails=new ArrayList<UploadingDetail>(0);
		uploadingErrors=new ArrayList<UploadingError>(0);
	}
	
	public Integer getId() {
		return entity.getId();
	}

	public void setId(Integer id){
		entity.setId(id);
	}
	
	public Date getDateUploading(){
		return entity.getDateUploading();
	}
	
	public void setDateUploading(Date dateUploading){
		entity.setDateUploading(dateUploading);
	}
	
	public Integer getStatus(){
		return entity.getStatus();
	}
	
	public void setStatus(Integer status) {
		entity.setStatus(status);
	}
	
	public String getName(){
		return entity.getName();
	}
	
	public void setName(String name){
		entity.setName(name);
	}
	
	public String getInfo(){
		return entity.getInfo();
	}
	
	public void setInfo(String info){
		entity.setInfo(info);
	}
	
	public String getReport(){
		return entity.getReport();
	}
	
	public void setReport(String report){
		entity.setReport(report);
	}
	
	public String getResponseId(){
		return entity.getResponseId();
	}
	
	public void setResponseId(String responseId){
		entity.setResponseId(responseId);
	}
	
	public Date getResponseDate(){
		return entity.getResponseDate();
	}
	
	public void setResponseDate(Date responseDate){
		entity.setResponseDate(responseDate);
	}
	
	public Integer getRecordsAll(){
		return entity.getRecordsAll();
	}
	
	public void setRecordsAll(Integer recordsAll){
		entity.setRecordsAll(recordsAll);
	}
	
	public Integer getRecordsCorrect(){
		return entity.getRecordsCorrect();
	}
	
	public void setRecordsCorrect(Integer recordsCorrect){
		entity.setRecordsCorrect(recordsCorrect);
	}
	
	public Integer getRecordsError(){
		return entity.getRecordsError();
	}
	
	public void setRecordsError(Integer recordsError){
		entity.setRecordsError(recordsError);
	}
	
	public Integer getUploadId(){
		return entity.getUploadId();
	}
	
	public void setUploadId(Integer uploadId){
		entity.setUploadId(uploadId);
	}
	
	public Integer getUploadType(){
		return entity.getUploadType();
	}
	
	public void setUploadType(Integer uploadType){
		entity.setUploadType(uploadType);
	}
	
	public Partner getPartner() {
	    return partner;
	}

	public void setPartner(Partner partner) {
	    this.partner=partner;
	}
	
	public List<UploadingDetail> getUploadingDetails(){
		return uploadingDetails;
	}
	
	public List<UploadingError> getUploadingErrors() {
		return uploadingErrors;
	}

	public int getHasDetails(){
		return entity.getHasDetails();
	}
	
	public int getHasErrors(){
		return entity.getHasErrors();
	}
	
	@Override
	public void init(Set options) {
		  //записи выгруженных кредитов
		  if (options != null && options.contains(Options.INIT_DETAIL)) {
	            Utils.initCollection(entity.getDetails(), options);
	            for (UploadingDetailEntity ent : entity.getDetails()) {
	                UploadingDetail detail = new UploadingDetail(ent);
	                detail.init(options);
	                uploadingDetails.add(detail);
	            }
	        }
		  
		  //ошибки выгруженных кредитов
		  if (options != null && options.contains(Options.INIT_ERROR)) {
	            Utils.initCollection(entity.getErrors(), options);
	            for (UploadingErrorEntity ent : entity.getErrors()) {
	                UploadingError error = new UploadingError(ent);
	                error.init(options);
	                uploadingErrors.add(error);
	            }
	        }
	}

}
