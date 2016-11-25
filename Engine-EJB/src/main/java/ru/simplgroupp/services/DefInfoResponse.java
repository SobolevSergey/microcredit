package ru.simplgroupp.services;

import java.io.Serializable;

/**
 * DEF info response
 */
public class DefInfoResponse implements Serializable {

    private static final long serialVersionUID = 8412338880858456713L;

    private DefInfoRest result;

    private Integer status;

    private String error;

    public DefInfoRest getResult() {
        return result;
    }

    public void setResult(DefInfoRest result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
