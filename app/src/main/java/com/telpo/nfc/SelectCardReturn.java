package com.telpo.nfc;

/* loaded from: classes.dex */
public class SelectCardReturn {
    private byte[] cardNum;
    private CardTypeEnum cardType;

    public void setCardType(CardTypeEnum cardTypeEnum) {
        this.cardType = cardTypeEnum;
    }

    public CardTypeEnum getCardType() {
        return this.cardType;
    }

    public void setCardNum(byte[] bArr) {
        this.cardNum = bArr;
    }

    public byte[] getCardNum() {
        return this.cardNum;
    }
}
