package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.UploadingErrorEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class UploadingError extends BaseTransfer<UploadingErrorEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5635534905121393820L;

	protected static Constructor<? extends BaseTransfer> wrapConstructor;
	public static Constructor<? extends BaseTransfer> getWrapConstructor() {
	    	return wrapConstructor;
	} 	
	static {
	    	try {
				wrapConstructor = UploadingError.class.getConstructor(UploadingErrorEntity.class);
			} catch (NoSuchMethodException | SecurityException e) {
				wrapConstructor = null;
			}
	}	
		
	protected CreditRequest creditRequest;
	protected Credit credit;
	protected Uploading uploading;
	
	public UploadingError(){
		super();
		entity=new UploadingErrorEntity();
	}
	
	public UploadingError(UploadingErrorEntity entity){
		super(entity);
		if (entity.getCreditRequestId()!=null) {
    		creditRequest=new CreditRequest(entity.getCreditRequestId());
    	}
		if (entity.getCreditId()!=null) {
    		credit=new Credit(entity.getCreditId());
    	}
		uploading=new Uploading(entity.getUploadingId());
	}
	
	public Integer getId() {
		return entity.getId();
	}

	public void setId(Integer id){
		entity.setId(id);
	}
	
	public String getDescription(){
		return entity.getDescription();
	}
	
	public void setDescription(String description){
		entity.setDescription(description);
	}
	
	public CreditRequest getCreditRequest() {
		return creditRequest;
	}

	public void setCreditRequest(CreditRequest creditRequest) {
		this.creditRequest = creditRequest;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public Uploading getUploading() {
		return uploading;
	}

	public void setUploading(Uploading uploading) {
		this.uploading = uploading;
	}

	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}

}
