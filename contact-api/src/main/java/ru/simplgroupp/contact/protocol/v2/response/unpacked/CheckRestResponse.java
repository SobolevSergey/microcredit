package ru.simplgroupp.contact.protocol.v2.response.unpacked;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlAccessorType(XmlAccessType.NONE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RESPONSE")
public class CheckRestResponse extends BaseResponse {
    @XmlAttribute
    private Double AVAIL_REST;
    @XmlAttribute
    private Double AVAIL_REST_CURR;
    @XmlAttribute
    private String AVAIL_REST_CURR_CODE;

    public Double getAVAIL_REST() {
        return AVAIL_REST;
    }

    public void setAVAIL_REST(Double AVAIL_REST) {
        this.AVAIL_REST = AVAIL_REST;
    }

    public Double getAVAIL_REST_CURR() {
        return AVAIL_REST_CURR;
    }

    public void setAVAIL_REST_CURR(Double AVAIL_REST_CURR) {
        this.AVAIL_REST_CURR = AVAIL_REST_CURR;
    }

    public String getAVAIL_REST_CURR_CODE() {
        return AVAIL_REST_CURR_CODE;
    }

    public void setAVAIL_REST_CURR_CODE(String AVAIL_REST_CURR_CODE) {
        this.AVAIL_REST_CURR_CODE = AVAIL_REST_CURR_CODE;
    }
}
