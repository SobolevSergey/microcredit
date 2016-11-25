
package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Страницы документов
 */
public class DocumentMediaEntity extends BaseEntity implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8313579148730951376L;

	protected Integer txVersion = 0;
    
    /**
     * номер страницы
     */
    private Integer pagenumber;
    /**
     * тип данных
     */
    private String mimetype;
    /**
     * данные
     */
    private byte[] mediadata;
    /**
     * путь к данным
     */
    private String mediapath;
    /**
     * документ
     */
    private DocumentEntity documentId;
    /**
     * вид скана документа по справочнику
     */
    private ReferenceEntity scanTypeId;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    
    public DocumentMediaEntity() {
    }

    public Integer getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(Integer pagenumber) {
        this.pagenumber = pagenumber;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public byte[] getMediadata() {
        return mediadata;
    }

    public void setMediadata(byte[] mediadata) {
        this.mediadata = mediadata;
    }

    public String getMediapath() {
        return mediapath;
    }

    public void setMediapath(String mediapath) {
        this.mediapath = mediapath;
    }

    public ReferenceEntity getScanTypeId() {
		return scanTypeId;
	}

	public void setScanTypeId(ReferenceEntity scanTypeId) {
		this.scanTypeId = scanTypeId;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public DocumentEntity getDocumentId() {
        return documentId;
    }

    public void setDocumentId(DocumentEntity documentId) {
        this.documentId = documentId;
    }
    
}
