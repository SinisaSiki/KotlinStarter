package com.telpo.tps550.api.reader;

import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public class SmartCardReader extends CardReader {
    public static final int PROTOCOL_NA = 2;
    public static final int PROTOCOL_T0 = 0;
    public static final int PROTOCOL_T1 = 1;
    public static final int SLOT_ICC = 0;
    public static final int SLOT_PSAM1 = 1;
    public static final int SLOT_PSAM2 = 2;
    public static final int SLOT_PSAM3 = 3;
    public static final int SLOT_PSAM4 = 4;
    private static final String TAG = "SmartCardReader";

    public SmartCardReader() {
        this.cardType = 1;
        this.mSlot = 0;
    }

    public SmartCardReader(Context context) {
        this.context = context;
        this.cardType = 1;
        this.mSlot = 0;
        this.mICCardReader = new ICCardReader(context);
    }

    public SmartCardReader(Context context, int i) {
        this.context = context;
        this.cardType = 1;
        this.mSlot = i;
        this.mICCardReader = new ICCardReader(context);
    }

    public SmartCardReader(Context context, int i, int i2) {
        this.context = context;
        this.cardType = 1;
        this.mSlot = i;
        this.calSlot = i2;
        this.mICCardReader = new ICCardReader(context);
    }

    @Override // com.telpo.tps550.api.reader.CardReader
    public int AT24CXX_read(int i, int i2, int i3, byte[] bArr, int[] iArr) {
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            try {
                return this.mICCardReader.AT24CXX_read(i, i2, i3, bArr, iArr);
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override // com.telpo.tps550.api.reader.CardReader
    public int AT24CXX_write(int i, int i2, byte[] bArr, int i3) {
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            try {
                return this.mICCardReader.AT24CXX_write(i, i2, bArr, i3);
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public byte[] transmit(byte[] bArr) throws NullPointerException {
        Objects.requireNonNull(bArr);
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            try {
                return this.mICCardReader.transmit(this.mSlot, bArr, bArr.length);
            } catch (TelpoException e) {
                e.printStackTrace();
                return null;
            }
        }
        byte[] bArr2 = new byte[512];
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            int send_apdu = send_apdu(bArr, bArr2);
            if (send_apdu > 0) {
                return Arrays.copyOf(bArr2, send_apdu);
            }
        } else {
            int[] iArr = new int[1];
            Log.d(TAG, "SmartCardReader > transmit[" + StringUtil.toHexString(bArr) + "]");
            int transmit = this.reader.transmit(bArr, bArr.length, bArr2, iArr);
            if (transmit == 0) {
                byte[] copyOf = Arrays.copyOf(bArr2, iArr[0]);
                Log.d(TAG, "SmartCardReader > transmit returnData[" + StringUtil.toHexString(copyOf) + "]");
                return copyOf;
            }
            Log.e(TAG, "send APDU failed: " + transmit);
        }
        return null;
    }

    public byte[] transmit(byte[] bArr, int i) throws NullPointerException {
        Objects.requireNonNull(bArr);
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            try {
                return this.mICCardReader.transmit(this.mSlot, bArr, bArr.length);
            } catch (TelpoException e) {
                e.printStackTrace();
                return null;
            }
        }
        byte[] bArr2 = new byte[512];
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            int send_apdu = send_apdu(bArr, bArr2);
            if (send_apdu > 0) {
                return Arrays.copyOf(bArr2, send_apdu);
            }
        } else {
            int[] iArr = new int[1];
            int transmit = this.reader.transmit(bArr, bArr.length, bArr2, iArr);
            if (transmit == 0) {
                return Arrays.copyOf(bArr2, iArr[0]);
            }
            Log.e(TAG, "send APDU failed: " + transmit);
        }
        return null;
    }

    public int getProtocol() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            return 0;
        }
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            if (this.mATR != null) {
                if (((this.mATR[1] >> 7) & 1) != 1) {
                    return 0;
                }
                int i = 0;
                for (int i2 = 4; i2 < 7; i2++) {
                    if (((this.mATR[1] >> i2) & 1) != 0) {
                        i++;
                    }
                }
                int i3 = this.mATR[i + 2] & 15;
                if (i3 == 0) {
                    return 0;
                }
                if (i3 == 1) {
                    return 1;
                }
            }
        } else {
            byte[] bArr = new byte[1];
            int protocol = this.reader.getProtocol(bArr);
            if (protocol != 0) {
                Log.e(TAG, "get protocol failed: " + protocol);
            } else if (bArr[0] == 1) {
                return 0;
            } else {
                if (bArr[0] == 2) {
                    return 1;
                }
            }
        }
        return 2;
    }

    public int getProtocol(int i) {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            return 0;
        }
        if (this.reader_type == 2 || this.reader_type == 1 || this.reader_type == 0) {
            if (this.mATR != null) {
                if (((this.mATR[1] >> 7) & 1) != 1) {
                    return 0;
                }
                int i2 = 0;
                for (int i3 = 4; i3 < 7; i3++) {
                    if (((this.mATR[1] >> i3) & 1) != 0) {
                        i2++;
                    }
                }
                int i4 = this.mATR[i2 + 2] & 15;
                if (i4 == 0) {
                    return 0;
                }
                if (i4 == 1) {
                    return 1;
                }
            }
        } else {
            byte[] bArr = new byte[1];
            int protocol = this.reader.getProtocol(bArr);
            if (protocol != 0) {
                Log.e(TAG, "get protocol failed: " + protocol);
            } else if (bArr[0] == 1) {
                return 0;
            } else {
                if (bArr[0] == 2) {
                    return 1;
                }
            }
        }
        return 2;
    }

    public int set_mode(int i, int i2) {
        if (this.mICCardReader != null) {
            try {
                return this.mICCardReader.set_mode(i, i2);
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
