package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.UploadingDetailEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class UploadingDetail extends BaseTransfer<UploadingDetailEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3928416497436988551L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = UploadingDetail.class.getConstructor(UploadingDetailEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	protected CreditRequest creditRequest;
	protected Credit credit;
	protected Uploading uploading;
	
	public UploadingDetail(){
		super();
		entity=new UploadingDetailEntity();
	}
	
	public UploadingDetail(UploadingDetailEntity entity){
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
	
	public Integer getStatus(){
		return entity.getStatus();
	}
	
	public void setStatus(Integer status) {
		entity.setStatus(status);
	}

	public Uploading getUploading() {
		return uploading;
	}

	public void setUploading(Uploading uploading) {
		this.uploading = uploading;
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

	@Override
	public void init(Set options) {
		// TODO Auto-generated method stub
		
	}
	
}
