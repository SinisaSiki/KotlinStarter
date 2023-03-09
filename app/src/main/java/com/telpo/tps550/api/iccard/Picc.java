package com.telpo.tps550.api.iccard;

import android.content.Context;
import android.content.Intent;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.NotSupportYetException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;

/* loaded from: classes.dex */
public class Picc {
    public static final int PICC_CARD_TYPE_CPU = 1;
    public static final int PICC_CARD_TYPE_ERROER = -1;
    public static final int PICC_CARD_TYPE_M1 = 2;
    public static final int PICC_CARD_TYPE_UL = 3;
    public static final int PICC_M1_TYPE_A = 0;
    public static final int PICC_M1_TYPE_B = 1;

    private static native int check_card(byte[] bArr, int[] iArr);

    private static native int check_card_sak(byte[] bArr, int[] iArr, byte[] bArr2, byte[] bArr3);

    private static native int close();

    private static native int enter_cpu_model();

    private static native int get_last_error();

    private static native int get_reader_info(byte[] bArr);

    private static native int halt_card();

    private static native int m1_add_sub(int i, int i2, byte[] bArr);

    private static native int m1_authority(int i, int i2, byte[] bArr);

    private static native int m1_read(int i, byte[] bArr);

    private static native int m1_write(int i, byte[] bArr);

    private static native int open(int i);

    private static native int reset_card();

    private static native int set_baudrate(int i);

    private static native int transmit(byte[] bArr, int i, byte[] bArr2, int[] iArr);

    private static native int ultralight_read(int i, byte[] bArr);

    private static native int ultralight_write(int i, byte[] bArr);

    static {
        System.loadLibrary("picc");
    }

    private static TelpoException getException(int i) {
        int i2 = get_last_error();
        if (i2 == 0) {
            if (i == -10) {
                return new NotSupportYetException();
            }
            if (i == -1) {
                return new DeviceNotOpenException();
            }
            return new InternalErrorException();
        } else if (i2 == 1) {
            return new RemovedCardException("There is no valid card found!");
        } else {
            if (i2 != 4) {
                if (i2 == 27) {
                    return new TimeoutException();
                }
                if (i2 != 9) {
                    if (i2 == 10) {
                        return new NoAuthorityCardException();
                    }
                    return new CommunicationErrorException();
                }
            }
            return new AuthorityFailException();
        }
    }

    public static void openReader() throws TelpoException {
        int open = open(9600);
        if (open == 0) {
            return;
        }
        throw getException(open);
    }

    public static void openReader(int i) throws TelpoException {
        if (i != 4800 && i != 9600 && i != 19200 && i != 38400 && i != 57600 && i != 115200) {
            throw new IllegalArgumentException();
        }
        int open = open(i);
        if (open != 0) {
            throw getException(open);
        }
    }

    public static void openReader(Context context) throws TelpoException {
        int open = open(9600);
        if (open != 0) {
            throw getException(open);
        }
        context.sendBroadcast(new Intent("com.telpo.rfid.picc.start"));
    }

    public static void closeReader() {
        close();
    }

    public static void closeReader(Context context) {
        close();
        context.sendBroadcast(new Intent("com.telpo.rfid.picc.stop"));
    }

    public static int selectCard(byte[] bArr, byte[] bArr2, byte[] bArr3) throws TelpoException {
        int[] iArr = new int[2];
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        int check_card_sak = check_card_sak(bArr, iArr, bArr2, bArr3);
        if (check_card_sak != 0) {
            throw getException(check_card_sak);
        }
        return iArr[0];
    }

    public static void haltCard() throws TelpoException {
        int halt_card = halt_card();
        if (halt_card == 0) {
            return;
        }
        throw getException(halt_card);
    }

    public static void resetReader() throws TelpoException {
        int reset_card = reset_card();
        if (reset_card == 0) {
            return;
        }
        throw getException(reset_card);
    }

    public static int command(byte[] bArr, int i, byte[] bArr2) throws TelpoException {
        int[] iArr = new int[2];
        if (bArr == null || bArr2 == null) {
            throw new IllegalArgumentException();
        }
        int transmit = transmit(bArr, i, bArr2, iArr);
        if (transmit != 0) {
            throw getException(transmit);
        }
        return iArr[0];
    }

    public static void m1Authority(int i, int i2, byte[] bArr) throws TelpoException {
        if ((i != 0 && i != 1) || bArr == null) {
            throw new IllegalArgumentException();
        }
        int m1_authority = m1_authority(i, i2, bArr);
        if (m1_authority != 0) {
            throw getException(m1_authority);
        }
    }

    public static void m1Read(int i, byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        int m1_read = m1_read(i, bArr);
        if (m1_read != 0) {
            throw getException(m1_read);
        }
    }

    public static void m1Write(int i, byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        int m1_write = m1_write(i, bArr);
        if (m1_write != 0) {
            throw getException(m1_write);
        }
    }

    public static void m1Add(int i, byte[] bArr) throws TelpoException {
        int m1_add_sub = m1_add_sub(0, i, bArr);
        if (m1_add_sub == 0) {
            return;
        }
        throw getException(m1_add_sub);
    }

    public static void m1Sub(int i, byte[] bArr) throws TelpoException {
        int m1_add_sub = m1_add_sub(1, i, bArr);
        if (m1_add_sub == 0) {
            return;
        }
        throw getException(m1_add_sub);
    }

    public static void getReaderInfo(byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        int i = get_reader_info(bArr);
        if (i != 0) {
            throw getException(i);
        }
    }

    public static void ultralightRead(int i, byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        int ultralight_read = ultralight_read(i, bArr);
        if (ultralight_read != 0) {
            throw getException(ultralight_read);
        }
    }

    public static void ultralightWrite(int i, byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        int ultralight_write = ultralight_write(i, bArr);
        if (ultralight_write != 0) {
            throw getException(ultralight_write);
        }
    }

    public static void setReaderBaudrate(int i) throws TelpoException {
        if (i != 0 && i != 1 && i != 2 && i != 3 && i != 4 && i != 5) {
            throw new IllegalArgumentException();
        }
        int i2 = set_baudrate(i);
        if (i2 != 0) {
            throw getException(i2);
        }
    }

    public static void enterCpuModel() throws TelpoException {
        int enter_cpu_model = enter_cpu_model();
        if (enter_cpu_model == 0) {
            return;
        }
        throw getException(enter_cpu_model);
    }
}
