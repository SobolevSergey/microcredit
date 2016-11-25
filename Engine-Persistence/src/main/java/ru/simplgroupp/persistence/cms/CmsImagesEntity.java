package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Картинки для страничек под управлением CMS
 */
public class CmsImagesEntity implements Serializable{

    /**
     * идентификатор
     */
    private Integer id;
    /**
     * Страничка
     */
    private CmsPageEntity cmsPage;

    /**
     * Название файла
     */
    private String fileName;
    /**
     * Файл
     */
    private byte[] fileData;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public CmsPageEntity getCmsPage() {
        return cmsPage;
    }


    public void setCmsPage(CmsPageEntity cmsPage) {
        this.cmsPage = cmsPage;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public byte[] getFileData() {
        return fileData;
    }


    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmsImagesEntity that = (CmsImagesEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (cmsPage != null ? !cmsPage.equals(that.cmsPage) : that.cmsPage != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        return Arrays.equals(fileData, that.fileData);

    }


    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cmsPage != null ? cmsPage.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (fileData != null ? Arrays.hashCode(fileData) : 0);
        return result;
    }
}
