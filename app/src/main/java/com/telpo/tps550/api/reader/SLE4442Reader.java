package com.telpo.tps550.api.reader;

import amlib.ccid.Reader4442;
import android.content.Context;
import android.util.Log;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public class SLE4442Reader extends CardReader {
    private static final String TAG = "SLE4442Reader";

    public SLE4442Reader() {
        this.cardType = 2;
    }

    public SLE4442Reader(Context context) {
        this.context = context;
        this.cardType = 2;
    }

    public byte[] readMainMemory(int i, int i2) {
        if (i < 0 || i > 255 || i2 < 0 || i2 > 256 || i + i2 > 256) {
            Log.e(TAG, "readMainMemory invalid parameter");
            return null;
        } else if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            return read_main_mem(this.cardType, i, i2);
        } else {
            Reader4442 reader4442 = this.reader;
            byte[] bArr = new byte[i2];
            int[] iArr = new int[1];
            if (i2 == 256) {
                int SLE4442Cmd_ReadMainMemory = reader4442.SLE4442Cmd_ReadMainMemory((byte) i, (byte) -1, bArr, iArr);
                if (SLE4442Cmd_ReadMainMemory == 0) {
                    if (iArr[0] == 255) {
                        byte[] bArr2 = new byte[1];
                        if (reader4442.SLE4442Cmd_ReadMainMemory((byte) -1, (byte) 1, bArr2, iArr) == 0) {
                            bArr[255] = bArr2[0];
                        }
                        return bArr;
                    }
                    return Arrays.copyOf(bArr, iArr[0]);
                }
                Log.e(TAG, "read main memory failed: " + SLE4442Cmd_ReadMainMemory);
            } else {
                int SLE4442Cmd_ReadMainMemory2 = reader4442.SLE4442Cmd_ReadMainMemory((byte) i, (byte) i2, bArr, iArr);
                if (SLE4442Cmd_ReadMainMemory2 == 0) {
                    return Arrays.copyOf(bArr, iArr[0]);
                }
                Log.e(TAG, "read main memory failed: " + SLE4442Cmd_ReadMainMemory2);
            }
            return null;
        }
    }

    public boolean updateMainMemory(int i, byte[] bArr) throws InvalidParameterException, NullPointerException {
        byte[] readProtectionMemory;
        Objects.requireNonNull(bArr);
        if (i < 0 || i > 255 || bArr.length < 0 || bArr.length > 256 || bArr.length + i > 256) {
            throw new InvalidParameterException();
        }
        if (!this.correct_psc_verification) {
            return false;
        }
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            if (update_main_mem(this.cardType, i, bArr) != bArr.length) {
                return false;
            }
        } else {
            Reader4442 reader4442 = this.reader;
            if (i < 32 && (readProtectionMemory = readProtectionMemory()) != null && readProtectionMemory.length == 4) {
                int i2 = (255 & readProtectionMemory[0]) | ((readProtectionMemory[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | ((readProtectionMemory[2] << 16) & 16711680) | ((readProtectionMemory[3] << 24) & ViewCompat.MEASURED_STATE_MASK);
                int length = bArr.length + i > 32 ? 32 - i : bArr.length;
                int i3 = i;
                while (true) {
                    int i4 = i3 + 1;
                    if (((i2 >> i3) & 1) == 0) {
                        Log.e(TAG, "The 4442 protected data byte can not be changed");
                        return false;
                    }
                    length--;
                    if (length <= 0) {
                        break;
                    }
                    i3 = i4;
                }
            }
            int i5 = i;
            int i6 = 0;
            while (i6 < bArr.length) {
                reader4442.SLE4442Cmd_UpdateMainMemory((byte) i5, bArr[i6]);
                i6++;
                i5++;
            }
        }
        byte[] readMainMemory = readMainMemory(i, bArr.length);
        if (readMainMemory != null && Arrays.equals(bArr, readMainMemory)) {
            return true;
        }
        Log.e(TAG, "The read data is not consistent with the writen data");
        return false;
    }

    public boolean pscVerify(byte[] bArr) throws InvalidParameterException, NullPointerException {
        Objects.requireNonNull(bArr);
        if (bArr.length != 3) {
            throw new InvalidParameterException();
        }
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            if (psc_verify(this.cardType, bArr) < 0) {
                return false;
            }
        } else {
            Reader4442 reader4442 = this.reader;
            byte[] bArr2 = new byte[1];
            int[] iArr = new int[1];
            int SLE4442Cmd_ReadSecurityMemory = reader4442.SLE4442Cmd_ReadSecurityMemory((byte) 1, bArr2, iArr);
            if (SLE4442Cmd_ReadSecurityMemory != 0) {
                Log.e(TAG, "read 4442 error counter failed: " + SLE4442Cmd_ReadSecurityMemory);
                return false;
            }
            Log.i(TAG, "4442 error counter: " + ((int) bArr2[0]));
            if ((bArr2[0] & 7) == 0) {
                Log.e(TAG, "4442 error counter no free bits");
                return false;
            }
            byte b = bArr2[0];
            int i = 0;
            while (true) {
                if (i >= 3) {
                    break;
                } else if (((b >> i) & 1) == 1) {
                    int SLE4442Cmd_UpdateSecurityMemory = reader4442.SLE4442Cmd_UpdateSecurityMemory((byte) 0, (byte) (b & (~(1 << i))));
                    if (SLE4442Cmd_UpdateSecurityMemory != 0) {
                        Log.e(TAG, "4442 error counter write free bit failed: " + SLE4442Cmd_UpdateSecurityMemory);
                        return false;
                    }
                } else {
                    i++;
                }
            }
            reader4442.SLE4442Cmd_CompareVerificationData((byte) 1, bArr[0]);
            reader4442.SLE4442Cmd_CompareVerificationData((byte) 2, bArr[1]);
            reader4442.SLE4442Cmd_CompareVerificationData((byte) 3, bArr[2]);
            reader4442.SLE4442Cmd_UpdateSecurityMemory((byte) 0, (byte) -1);
            bArr2[0] = 0;
            reader4442.SLE4442Cmd_ReadSecurityMemory((byte) 1, bArr2, iArr);
            if ((bArr2[0] & 7) != 7) {
                Log.e(TAG, "4442 psc verification failed");
                return false;
            }
        }
        this.correct_psc_verification = true;
        return true;
    }

    public boolean pscModify(byte[] bArr) throws InvalidParameterException, NullPointerException {
        Objects.requireNonNull(bArr);
        if (bArr.length != 3) {
            throw new InvalidParameterException();
        }
        if (!this.correct_psc_verification) {
            return false;
        }
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            if (psc_modify(this.cardType, bArr) != 0) {
                return false;
            }
        } else {
            Reader4442 reader4442 = this.reader;
            int i = 0;
            while (i < 3) {
                int i2 = i + 1;
                int SLE4442Cmd_UpdateSecurityMemory = reader4442.SLE4442Cmd_UpdateSecurityMemory((byte) i2, bArr[i]);
                if (SLE4442Cmd_UpdateSecurityMemory != 0) {
                    Log.e(TAG, "4442 update psc failed: " + SLE4442Cmd_UpdateSecurityMemory);
                    return false;
                }
                i = i2;
            }
        }
        return true;
    }

    public byte[] readProtectionMemory() {
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            return null;
        }
        byte[] bArr = new byte[4];
        int[] iArr = new int[1];
        int SLE4442Cmd_ReadProtectionMemory = this.reader.SLE4442Cmd_ReadProtectionMemory((byte) 4, bArr, iArr);
        if (SLE4442Cmd_ReadProtectionMemory != 0) {
            Log.e(TAG, "4442 read protection memory failed: " + SLE4442Cmd_ReadProtectionMemory);
            return null;
        }
        return Arrays.copyOf(bArr, iArr[0]);
    }

    public byte[] getUserCode() {
        byte[] readMainMemory = readMainMemory(21, 6);
        if (readMainMemory == null || readMainMemory.length != 6) {
            return null;
        }
        return readMainMemory;
    }
}
