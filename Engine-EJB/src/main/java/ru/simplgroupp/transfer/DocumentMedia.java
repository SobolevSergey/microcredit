package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Set;

import ru.simplgroupp.persistence.DocumentMediaEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class DocumentMedia extends BaseTransfer<DocumentMediaEntity>  implements Initializable, Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7591819430718094717L;

	public enum Options {
		INIT_DOCUMENT;
	}
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
	static {
    	try {
			wrapConstructor = DocumentMedia.class.getConstructor(DocumentMediaEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	protected Documents document;
	protected PeopleMain peopleMain;
	protected Reference scanType;
	public static final int SCAN_TYPE_SNILS = 4;
	public static final int SCAN_TYPE_INN = 3;
	public static final int SCAN_TYPE_DRIVE = 2;
	public static final int SCAN_TYPE_PASP = 1;
	
	public DocumentMedia() {
		super();
		entity = new DocumentMediaEntity();
	}

	public DocumentMedia(DocumentMediaEntity entity) {
		super(entity);
		if (entity.getPeopleMainId()!=null){
			peopleMain=new PeopleMain(entity.getPeopleMainId());
		}
		if (entity.getScanTypeId()!=null){
			scanType=new Reference(entity.getScanTypeId());
		}
	}

	@Override
	public void init(Set options) {
		if (options != null && options.contains(Options.INIT_DOCUMENT)){
			document=new Documents(entity.getDocumentId());
		}
	}
	
   public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
    	entity.setId(id);
    }

    public Integer getPageNumber() {
        return entity.getPagenumber();
    }

    public void setPageNumber(Integer pagenumber) {
    	entity.setPagenumber(pagenumber);
    }

    public String getMimeType() {
        return entity.getMimetype();
    }

    public void setMimeType(String mimetype) {
    	entity.setMimetype(mimetype);
    }

    public byte[] getMediaData() {
        return entity.getMediadata();
    }
    
    
    public Documents getDocument() {
		return document;
	}

	public void setDocument(Documents document) {
		this.document = document;
	}

	
	public PeopleMain getPeopleMain() {
		return peopleMain;
	}

	public void setPeopleMain(PeopleMain peopleMain) {
		this.peopleMain = peopleMain;
	}

	public Reference getScanType() {
		return scanType;
	}

	public void setScanType(Reference scanType) {
		this.scanType = scanType;
	}

	public void setMediaData(byte[] mediadata) {
    	entity.setMediadata(mediadata);
    }

    public String getMediaPath() {
        return entity.getMediapath();
    }

    public void setMediaPath(String mediapath) {
    	entity.setMediapath(mediapath);
    }	

    public static class PageNumberComparator implements Comparator<DocumentMedia> {

		@Override
		public int compare(DocumentMedia o1, DocumentMedia o2) {
			if (o1.getPageNumber() == null && o2.getPageNumber() == null) {
				return 0;
			}
			if (o1.getPageNumber() == null && o2.getPageNumber() != null) {
				return 1;
			}
			if (o2.getPageNumber() == null && o1.getPageNumber() != null) {
				return -1;
			}
			
			return o1.getPageNumber().compareTo(o2.getPageNumber());
		}
    	
    }
}
