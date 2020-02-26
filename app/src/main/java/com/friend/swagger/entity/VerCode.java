package com.friend.swagger.entity;

import java.util.Date;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-26 14:34
 **/
public class VerCode {
    private int codeId;
    private String codePhone;
    private String codeValue;
    private Date codeCreatetime;

    public VerCode() {
    }

    public VerCode(String codePhone, String codeValue, Date codeCreatetime) {
        this.codePhone = codePhone;
        this.codeValue = codeValue;
        this.codeCreatetime = codeCreatetime;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getCodePhone() {
        return codePhone;
    }

    public void setCodePhone(String codePhone) {
        this.codePhone = codePhone;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public Date getCodeCreatetime() {
        return codeCreatetime;
    }

    public void setCodeCreatetime(Date codeCreatetime) {
        this.codeCreatetime = codeCreatetime;
    }

    @Override
    public String toString() {
        return "VerCode{" +
                "codeId=" + codeId +
                ", codePhone='" + codePhone + '\'' +
                ", codeValue='" + codeValue + '\'' +
                ", codeCreatetime=" + codeCreatetime +
                '}';
    }
}
