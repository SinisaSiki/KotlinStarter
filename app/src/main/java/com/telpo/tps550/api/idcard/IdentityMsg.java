package com.telpo.tps550.api.idcard;

import java.io.Serializable;

/* loaded from: classes.dex */
public class IdentityMsg implements Serializable {
    private static final long serialVersionUID = -7696282392790000305L;
    private String address;
    private String apartment;
    private String born;
    private String card_type;
    private String cn_name;
    private String country;
    private byte[] head_photo;
    private String idcard_version;
    private String name;
    private String no;
    private String period;
    private String reserve;
    private String sex;
    private String nation = null;
    private String passNum = null;
    private String issuesNum = null;
    private String cardSignal = null;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String str) {
        this.sex = str;
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String str) {
        this.nation = str;
    }

    public String getBorn() {
        return this.born;
    }

    public void setBorn(String str) {
        this.born = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getApartment() {
        return this.apartment;
    }

    public void setApartment(String str) {
        this.apartment = str;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String str) {
        this.period = str;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String str) {
        this.no = str;
    }

    public byte[] getHead_photo() {
        return this.head_photo;
    }

    public void setHead_photo(byte[] bArr) {
        this.head_photo = bArr;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public String getCn_name() {
        return this.cn_name;
    }

    public void setCn_name(String str) {
        this.cn_name = str;
    }

    public String getIdcard_version() {
        return this.idcard_version;
    }

    public void setIdcard_version(String str) {
        this.idcard_version = str;
    }

    public String getCard_type() {
        return this.card_type;
    }

    public void setCard_type(String str) {
        this.card_type = str;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String str) {
        this.reserve = str;
    }

    public String getPassNum() {
        return this.passNum;
    }

    public void setPassNum(String str) {
        this.passNum = str;
    }

    public String getIssuesNum() {
        return this.issuesNum;
    }

    public void setIssuesNum(String str) {
        this.issuesNum = str;
    }

    public String getCardSignal() {
        return this.cardSignal;
    }

    public void setCardSignal(String str) {
        this.cardSignal = str;
    }
}
