package com.telpo.tps550.api.reader;

import amlib.ccid.Reader4428;
import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public class SLE4428Reader extends CardReader {
    private static final String TAG = "SLE4428Reader";

    public SLE4428Reader() {
        this.cardType = 3;
        this.mSlot = 1;
    }

    public SLE4428Reader(Context context) {
        this.context = context;
        this.cardType = 3;
        this.mSlot = 1;
        this.mICCardReader = new ICCardReader(context);
    }

    public byte[] readMainMemory(int i, int i2) throws InvalidParameterException {
        int i3 = i;
        byte[] bArr = null;
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            byte[] bArr2 = new byte[i2];
            if (this.mICCardReader.readmMinMemory(this.mSlot, i, i2, bArr2, new int[i2]) != 0) {
                return null;
            }
            return bArr2;
        } else if (i3 < 0 || i3 > 1023 || i2 < 0 || i2 > 1024 || i3 + i2 > 1024) {
            Log.e(TAG, "readMainMemory invalid parameter");
            return null;
        } else if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            return read_main_mem(this.cardType, i3, i2);
        } else {
            Reader4428 reader4428 = this.reader;
            byte[] bArr3 = new byte[i2];
            int[] iArr = new int[1];
            byte[] bArr4 = new byte[123];
            int i4 = i2 / 123;
            int i5 = i2 % 123;
            int i6 = 0;
            int i7 = 0;
            while (i6 < i4) {
                int SLE4428Cmd_Read8Bits = reader4428.SLE4428Cmd_Read8Bits(i3, 123, bArr4, iArr);
                if (SLE4428Cmd_Read8Bits != 0 || iArr[0] != 123) {
                    Log.e(TAG, "4428 read 8 bits failed: " + SLE4428Cmd_Read8Bits);
                    return null;
                }
                System.arraycopy(bArr4, 0, bArr3, i7, 123);
                i7 += 123;
                i3 += 123;
                i6++;
                bArr = null;
            }
            if (i5 != 0) {
                int SLE4428Cmd_Read8Bits2 = reader4428.SLE4428Cmd_Read8Bits(i3, i5, bArr4, iArr);
                if (SLE4428Cmd_Read8Bits2 != 0 || iArr[0] != i5) {
                    Log.e(TAG, "4428 read 8 bits failed: " + SLE4428Cmd_Read8Bits2);
                    return bArr;
                }
                System.arraycopy(bArr4, 0, bArr3, i7, i5);
            }
            return bArr3;
        }
    }

    public boolean pscVerify(byte[] bArr) throws InvalidParameterException, NullPointerException {
        Objects.requireNonNull(bArr);
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            return this.mICCardReader.verifyPsc(this.mSlot, bArr) == 0;
        } else if (bArr.length != 2) {
            throw new InvalidParameterException();
        } else {
            if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
                if (psc_verify(this.cardType, bArr) < 0) {
                    return false;
                }
            } else {
                int SLE4428Cmd_VerifyPSCAndEraseErrorCounter = this.reader.SLE4428Cmd_VerifyPSCAndEraseErrorCounter(bArr[0], bArr[1], new int[1]);
                if (SLE4428Cmd_VerifyPSCAndEraseErrorCounter != 0) {
                    Log.e(TAG, "4428 verify psc and erase error counter failed: " + SLE4428Cmd_VerifyPSCAndEraseErrorCounter);
                    return false;
                }
            }
            this.correct_psc_verification = true;
            return true;
        }
    }

    public boolean updateMainMemory(int i, byte[] bArr) throws InvalidParameterException, NullPointerException {
        Objects.requireNonNull(bArr);
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            return this.mICCardReader.writeMainMemory(this.mSlot, i, bArr, bArr.length) == 0;
        } else if (i < 0 || i > 1020 || bArr.length < 0 || bArr.length > 1021 || bArr.length + i > 1021) {
            throw new InvalidParameterException();
        } else {
            if (!this.correct_psc_verification) {
                return false;
            }
            if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
                if (update_main_mem(this.cardType, i, bArr) != bArr.length) {
                    return false;
                }
            } else {
                Reader4428 reader4428 = this.reader;
                byte[] bArr2 = new byte[bArr.length];
                byte[] bArr3 = new byte[bArr.length];
                int[] iArr = new int[1];
                if (reader4428.SLE4428Cmd_Read9Bits(i, bArr.length, bArr2, bArr3, iArr) == 0) {
                    for (int i2 = 0; i2 < iArr[0]; i2++) {
                        if (bArr3[i2] == 0) {
                            Log.e(TAG, "The 4428 protected data byte can not be changed");
                            return false;
                        }
                    }
                }
                int i3 = i;
                int i4 = 0;
                while (i4 < bArr.length) {
                    reader4428.SLE4428Cmd_WriteEraseWithoutPB(i3, bArr[i4]);
                    i4++;
                    i3++;
                }
            }
            byte[] readMainMemory = readMainMemory(i, bArr.length);
            if (readMainMemory != null && Arrays.equals(bArr, readMainMemory)) {
                return true;
            }
            Log.e(TAG, "The read data is not consistent with the writen data");
            return false;
        }
    }

    public boolean pscModify(byte[] bArr) throws InvalidParameterException, NullPointerException {
        Objects.requireNonNull(bArr);
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            return this.mICCardReader.updatePsc(this.mSlot, bArr) == 0;
        } else if (bArr.length != 2) {
            throw new InvalidParameterException();
        } else {
            if (!this.correct_psc_verification) {
                return false;
            }
            if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
                if (psc_modify(this.cardType, bArr) != 0) {
                    return false;
                }
            } else {
                Reader4428 reader4428 = this.reader;
                int i = 0;
                int i2 = 1022;
                while (i < 2) {
                    reader4428.SLE4428Cmd_WriteEraseWithoutPB(i2, bArr[i]);
                    i++;
                    i2++;
                }
                byte[] readMainMemory = readMainMemory(1022, 2);
                if (readMainMemory == null || !Arrays.equals(bArr, readMainMemory)) {
                    Log.e(TAG, "The read data is not consistent with the writen data");
                    return false;
                }
            }
            return true;
        }
    }

    public byte[] getUserCode() {
        byte[] readMainMemory;
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal() || (readMainMemory = readMainMemory(21, 6)) == null || readMainMemory.length != 6) {
            return null;
        }
        return readMainMemory;
    }
}
