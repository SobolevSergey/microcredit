package ru.simplgroupp.contact.protocol.v2.response.unpacked;

/**
 * Created by aro on 17.09.2014.
 */
public class Dics {
    private BanksDic banksDic;
    private KFMInfoDic kfmInfoDic;
    private BankServDic bankServDic;
    private ServDic servDic;
    private BadDocDic badDocDic;
    private Integer total = 0;
    private Integer currentPart = 0;
    private String bookId = "";

    public BanksDic getBanksDic() {
        return banksDic;
    }

    public void setBanksDic(BanksDic banksDic) {
        this.banksDic = banksDic;
    }

    public KFMInfoDic getKfmInfoDic() {
        return kfmInfoDic;
    }

    public void setKfmInfoDic(KFMInfoDic kfmInfoDic) {
        this.kfmInfoDic = kfmInfoDic;
    }

    public BankServDic getBankServDic() {
        return bankServDic;
    }

    public void setBankServDic(BankServDic bankServDic) {
        this.bankServDic = bankServDic;
    }

    public ServDic getServDic() {
        return servDic;
    }

    public void setServDic(ServDic servDic) {
        this.servDic = servDic;
    }

    public BadDocDic getBadDocDic() {
        return badDocDic;
    }

    public void setBadDocDic(BadDocDic badDocDic) {
        this.badDocDic = badDocDic;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrentPart() {
        return currentPart;
    }

    public void setCurrentPart(Integer currentPart) {
        this.currentPart = currentPart;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
