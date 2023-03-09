package com.common.sdk.idcard;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class IdCardInfo implements Parcelable {
    public static final Parcelable.Creator<IdCardInfo> CREATOR = new Parcelable.Creator<IdCardInfo>() { // from class: com.common.sdk.idcard.IdCardInfo.1
        @Override // android.os.Parcelable.Creator
        public IdCardInfo createFromParcel(Parcel parcel) {
            return new IdCardInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public IdCardInfo[] newArray(int i) {
            return new IdCardInfo[i];
        }
    };
    private String adress;
    private String born;
    byte[] img;
    private String name;
    private String nation;
    private String number;
    private String office;
    private String sex;
    private String term;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public IdCardInfo() {
        this.img = new byte[1024];
    }

    public IdCardInfo(Parcel parcel) {
        this.img = new byte[1024];
        this.name = parcel.readString();
        this.sex = parcel.readString();
        this.nation = parcel.readString();
        this.born = parcel.readString();
        this.adress = parcel.readString();
        this.number = parcel.readString();
        this.office = parcel.readString();
        this.term = parcel.readString();
        parcel.readByteArray(this.img);
    }

    public String getName() {
        return this.name;
    }

    public String getSex() {
        return this.sex;
    }

    public String getNation() {
        return this.nation;
    }

    public String getBorn() {
        return this.born;
    }

    public String getAdress() {
        return this.adress;
    }

    public String getNumber() {
        return this.number;
    }

    public String getOffice() {
        return this.office;
    }

    public String getTerm() {
        return this.term;
    }

    public byte[] getImg() {
        return this.img;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setSex(String str) {
        this.sex = str;
    }

    public void setNation(String str) {
        this.nation = str;
    }

    public void setBorn(String str) {
        this.born = str;
    }

    public void setAdress(String str) {
        this.adress = str;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public void setOffice(String str) {
        this.office = str;
    }

    public void setTerm(String str) {
        this.term = str;
    }

    public void setImg(byte[] bArr) {
        this.img = bArr;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.sex);
        parcel.writeString(this.nation);
        parcel.writeString(this.born);
        parcel.writeString(this.adress);
        parcel.writeString(this.number);
        parcel.writeString(this.office);
        parcel.writeString(this.term);
        parcel.writeByteArray(this.img);
    }
}
