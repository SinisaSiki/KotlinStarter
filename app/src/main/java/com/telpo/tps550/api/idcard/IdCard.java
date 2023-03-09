package com.telpo.tps550.api.idcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.Handler;
import com.telpo.tps550.api.DeviceNotFoundException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import com.telpo.tps550.api.serial.Serial;
import com.telpo.tps550.api.util.ReaderUtils;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class IdCard {
    private static final int CHECK_ADDR = 4;
    private static final int DEFAULT_BARTRATE = 115200;
    public static final int IDREADER_TYPE_UART = 0;
    public static final int IDREADER_TYPE_USB = 1;
    private static final int PID = 770;
    private static final int PID_110 = 772;
    public static final int READER_PID_BIG = 50010;
    public static final int READER_PID_SMALL = 22352;
    public static final int READER_PID_WINDOWS = 650;
    public static final int READER_VID_BIG = 1024;
    public static final int READER_VID_SMALL = 1155;
    public static final int READER_VID_WINDOWS = 10473;
    private static final int STEP_CHECK = 1;
    private static final int STEP_READ = 3;
    private static final int STEP_SELECT = 2;
    private static final String TAG = "TELPO_SDK";
    private static final int VID = 6997;
    private static int contentLength;
    private static int fplength;
    public static byte[] idcardData;
    private static int imageDatalength;
    private static IdentityMsg info;
    private static int now_PID;
    static T2OReader t2oReader;
    private long endTime;
    private Handler handler;
    private UsbDevice idcard_reader;
    private ReadThread mReadThread;
    private UsbManager mUsbManager;
    Serial serial;
    private long startTime;
    private long totalEndTime;
    private long totalStartTime;
    private static final byte[] byLicData = {5, 0, 1, 0, 91, 3, 51, 1, 90, -77, 30};
    private static String[] nation_list = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜", "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土", "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒", "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺", "其他", "外国血统中国籍人士"};
    private UsbIdCard mUsbIdCard = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private byte[] idData = null;
    private int step = -1;
    private boolean allStep = false;
    private boolean circle = false;
    private boolean decodeDataSuccess = false;
    private int findCardSuccess = 0;
    private int selectCardSuccess = 0;
    private int readCardSuccess = 0;
    private boolean throwException = false;

    private static native int check_find(int i);

    private static native Object check_idcard(int i, int[] iArr);

    private static native int check_read(int i);

    private static native int check_select(int i);

    private static native boolean connect_idcard(int i, int i2);

    private static native boolean connected_idcard(int i, int i2, String str);

    private static native boolean disconnect_idcard();

    private static native byte[] get_fringerprint();

    private static native byte[] get_image();

    private static native int get_imageusb(byte[] bArr, String str);

    private static native byte[] get_sam();

    public static native int getimage(byte[] bArr, byte[] bArr2);

    public static native int resetModule(int i);

    static {
        System.loadLibrary("idcard");
    }

    private IdCard() {
    }

    public IdCard(int i, Context context) throws TelpoException {
        if (t2oReader == null) {
            t2oReader = new T2OReader();
        }
        if (i == 1) {
            t2oReader.openReader(context);
        } else if (i != 0) {
        } else {
            t2oReader.openReader(selectSerial());
        }
    }

    public IdCard(int i) {
        if (i == 0 && t2oReader == null) {
            t2oReader = new T2OReader();
        }
        t2oReader.openReader(selectSerial());
    }

    public IdCard(String str) {
        if (t2oReader == null) {
            t2oReader = new T2OReader();
        }
        t2oReader.openReader(str);
    }

    public IdCard(Context context) {
        if (t2oReader == null) {
            t2oReader = new T2OReader();
        }
        if (t2oReader.isUSBReader(context)) {
            t2oReader.openReader(context);
        } else {
            t2oReader.openReader(selectSerial());
        }
    }

    public boolean isSerialReader() throws TelpoException {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            throw new TelpoException();
        }
        return t2OReader.isSerialIDReader();
    }

    public static synchronized void open() throws TelpoException {
        synchronized (IdCard.class) {
            File file = new File(Environment.getExternalStorageDirectory() + "/wltlib");
            if (!file.exists() && !file.mkdir()) {
                throw new IdCardInitFailException("Failed to find idcard library directory!");
            }
            if (!isFileExists(Environment.getExternalStorageDirectory() + "/wltlib/base.dat") && !copyFile("/system/usr/base.dat", Environment.getExternalStorageDirectory() + "/wltlib/base.dat")) {
                throw new IdCardInitFailException("Failed to find idcard library data file!");
            }
            if (!isFileExists(Environment.getExternalStorageDirectory() + "/wltlib/license.lic") && !copyFile("/system/usr/license.lic", Environment.getExternalStorageDirectory() + "/wltlib/license.lic")) {
                throw new IdCardInitFailException("Failed to find idcard library license file!");
            }
            if (!connect_idcard(0, DEFAULT_BARTRATE)) {
                throw new DeviceNotFoundException();
            }
        }
    }

    public static synchronized void open(Context context) throws TelpoException {
        synchronized (IdCard.class) {
            File file = new File(Environment.getExternalStorageDirectory() + "/wltlib");
            if (!file.exists()) {
                file.mkdir();
            }
            if (!isFileExists(Environment.getExternalStorageDirectory() + "/wltlib/base.dat")) {
                copyFile("/system/usr/base.dat", Environment.getExternalStorageDirectory() + "/wltlib/base.dat");
            }
            if (!isFileExists(Environment.getExternalStorageDirectory() + "/wltlib/license.lic")) {
                copyFile("/system/usr/license.lic", Environment.getExternalStorageDirectory() + "/wltlib/license.lic");
            }
            if (!connect_idcard(0, DEFAULT_BARTRATE)) {
                throw new DeviceNotFoundException();
            }
        }
    }

    public static synchronized void open(int i, int i2) throws TelpoException {
        synchronized (IdCard.class) {
            if (i != 0 && i != 1) {
                throw new IllegalArgumentException("Idcard reader type is invalid!");
            }
            File file = new File(Environment.getExternalStorageDirectory() + "/wltlib");
            if (!file.exists() && !file.mkdir()) {
                throw new IdCardInitFailException("Failed to find idcard library directory!");
            }
            if (!isFileExists(Environment.getExternalStorageDirectory() + "/wltlib/base.dat") && !copyFile("/system/usr/base.dat", Environment.getExternalStorageDirectory() + "/wltlib/base.dat")) {
                throw new IdCardInitFailException("Failed to find idcard library data file!");
            }
            if (!isFileExists(Environment.getExternalStorageDirectory() + "/wltlib/license.lic") && !copyFile("/system/usr/license.lic", Environment.getExternalStorageDirectory() + "/wltlib/license.lic")) {
                throw new IdCardInitFailException("Failed to find idcard library license file!");
            }
            if (!connect_idcard(i, i2)) {
                throw new DeviceNotFoundException();
            }
        }
    }

    public static synchronized void open(int i, String str) throws TelpoException {
        synchronized (IdCard.class) {
            File file = new File(Environment.getExternalStorageDirectory() + "/wltlib");
            if (!file.exists() && !file.mkdir()) {
                throw new IdCardInitFailException("Failed to find idcard library directory!");
            }
            connected_idcard(0, i, str);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ce, code lost:
        r1 = com.telpo.tps550.api.util.ShellUtils.execCommand("chmod -R 777 /dev/bus/usb/", true);
        android.util.Log.w("result", java.lang.String.valueOf(r1.result) + r1.errorMsg);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized void open(int r5) throws com.telpo.tps550.api.TelpoException {
        /*
            java.lang.Class<com.telpo.tps550.api.idcard.IdCard> r0 = com.telpo.tps550.api.idcard.IdCard.class
            monitor-enter(r0)
            r1 = 1
            if (r5 == 0) goto L11
            if (r5 != r1) goto L9
            goto L11
        L9:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lff
            java.lang.String r1 = "Idcard reader type is invalid!"
            r5.<init>(r1)     // Catch: java.lang.Throwable -> Lff
            throw r5     // Catch: java.lang.Throwable -> Lff
        L11:
            java.io.File r2 = new java.io.File     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lff
            r3.<init>()     // Catch: java.lang.Throwable -> Lff
            java.io.File r4 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r4 = "/wltlib"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> Lff
            r2.<init>(r3)     // Catch: java.lang.Throwable -> Lff
            boolean r3 = r2.exists()     // Catch: java.lang.Throwable -> Lff
            if (r3 != 0) goto L42
            boolean r2 = r2.mkdir()     // Catch: java.lang.Throwable -> Lff
            if (r2 == 0) goto L3a
            goto L42
        L3a:
            com.telpo.tps550.api.idcard.IdCardInitFailException r5 = new com.telpo.tps550.api.idcard.IdCardInitFailException     // Catch: java.lang.Throwable -> Lff
            java.lang.String r1 = "Failed to find idcard library directory!"
            r5.<init>(r1)     // Catch: java.lang.Throwable -> Lff
            throw r5     // Catch: java.lang.Throwable -> Lff
        L42:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lff
            r2.<init>()     // Catch: java.lang.Throwable -> Lff
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r3 = "/wltlib/base.dat"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> Lff
            boolean r2 = isFileExists(r2)     // Catch: java.lang.Throwable -> Lff
            if (r2 != 0) goto L87
            java.lang.String r2 = "/system/usr/base.dat"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lff
            r3.<init>()     // Catch: java.lang.Throwable -> Lff
            java.io.File r4 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r4 = "/wltlib/base.dat"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> Lff
            boolean r2 = copyFile(r2, r3)     // Catch: java.lang.Throwable -> Lff
            if (r2 == 0) goto L7f
            goto L87
        L7f:
            com.telpo.tps550.api.idcard.IdCardInitFailException r5 = new com.telpo.tps550.api.idcard.IdCardInitFailException     // Catch: java.lang.Throwable -> Lff
            java.lang.String r1 = "Failed to find idcard library data file!"
            r5.<init>(r1)     // Catch: java.lang.Throwable -> Lff
            throw r5     // Catch: java.lang.Throwable -> Lff
        L87:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lff
            r2.<init>()     // Catch: java.lang.Throwable -> Lff
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r3 = "/wltlib/license.lic"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> Lff
            boolean r2 = isFileExists(r2)     // Catch: java.lang.Throwable -> Lff
            if (r2 != 0) goto Lcc
            java.lang.String r2 = "/system/usr/license.lic"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lff
            r3.<init>()     // Catch: java.lang.Throwable -> Lff
            java.io.File r4 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r4 = "/wltlib/license.lic"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> Lff
            boolean r2 = copyFile(r2, r3)     // Catch: java.lang.Throwable -> Lff
            if (r2 == 0) goto Lc4
            goto Lcc
        Lc4:
            com.telpo.tps550.api.idcard.IdCardInitFailException r5 = new com.telpo.tps550.api.idcard.IdCardInitFailException     // Catch: java.lang.Throwable -> Lff
            java.lang.String r1 = "Failed to find idcard library license file!"
            r5.<init>(r1)     // Catch: java.lang.Throwable -> Lff
            throw r5     // Catch: java.lang.Throwable -> Lff
        Lcc:
            if (r5 != r1) goto Lee
            java.lang.String r2 = "chmod -R 777 /dev/bus/usb/"
            com.telpo.tps550.api.util.ShellUtils$CommandResult r1 = com.telpo.tps550.api.util.ShellUtils.execCommand(r2, r1)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r2 = "result"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lff
            int r4 = r1.result     // Catch: java.lang.Throwable -> Lff
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch: java.lang.Throwable -> Lff
            r3.<init>(r4)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r1 = r1.errorMsg     // Catch: java.lang.Throwable -> Lff
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch: java.lang.Throwable -> Lff
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Lff
            android.util.Log.w(r2, r1)     // Catch: java.lang.Throwable -> Lff
        Lee:
            r1 = 115200(0x1c200, float:1.6143E-40)
            boolean r5 = connect_idcard(r5, r1)     // Catch: java.lang.Throwable -> Lff
            if (r5 == 0) goto Lf9
            monitor-exit(r0)
            return
        Lf9:
            com.telpo.tps550.api.DeviceNotFoundException r5 = new com.telpo.tps550.api.DeviceNotFoundException     // Catch: java.lang.Throwable -> Lff
            r5.<init>()     // Catch: java.lang.Throwable -> Lff
            throw r5     // Catch: java.lang.Throwable -> Lff
        Lff:
            r5 = move-exception
            monitor-exit(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.IdCard.open(int):void");
    }

    public static synchronized int open(int i, Context context) {
        synchronized (IdCard.class) {
            if (i == 1) {
                if (t2oReader == null) {
                    t2oReader = new T2OReader();
                }
                if (!t2oReader.isUSBReader(context)) {
                    return -99;
                }
                t2oReader.openReader(context);
                return 0;
            }
            return 1;
        }
    }

    public static synchronized IdentityMsg checkIdCard(int i) throws TelpoException {
        IdentityMsg identityMsg;
        String trim;
        synchronized (IdCard.class) {
            int[] iArr = new int[2];
            identityMsg = (IdentityMsg) check_idcard(i, iArr);
            if (identityMsg == null) {
                if (iArr[0] == -5) {
                    close();
                    throw new DeviceNotFoundException();
                }
                throw new TimeoutException();
            }
            StringBuilder sb = new StringBuilder();
            String trim2 = identityMsg.getName().trim();
            if (countChinese(trim2) != 0) {
                if (trim2.length() <= 4) {
                    for (char c : trim2.toCharArray()) {
                        sb.append(c);
                        sb.append(" ");
                    }
                } else if (trim2.length() > 14) {
                    sb.append(trim2.substring(0, 14));
                    sb.append("\n\t\t\t");
                    sb.append(trim2.substring(14));
                }
                if (!sb.toString().equals("")) {
                    identityMsg.setName(sb.toString());
                }
            } else {
                int i2 = 26;
                if (trim2.length() > 26) {
                    String substring = trim2.substring(0, 26);
                    int lastIndexOf = substring.lastIndexOf(" ");
                    int lastIndexOf2 = substring.lastIndexOf(",");
                    if (lastIndexOf != -1 || lastIndexOf2 != -1) {
                        i2 = lastIndexOf > lastIndexOf2 ? lastIndexOf : lastIndexOf2;
                    }
                    int i3 = i2 + 1;
                    sb.append(trim2.substring(0, i3));
                    sb.append("\n\t\t\t");
                    sb.append(trim2.substring(i3));
                    identityMsg.setName(sb.toString());
                }
            }
            String sex = identityMsg.getSex();
            if ("男".equals(sex)) {
                identityMsg.setSex(String.valueOf(sex) + " / M");
            } else if ("女".equals(sex)) {
                identityMsg.setSex(String.valueOf(sex) + " / F");
            }
            String trim3 = identityMsg.getBorn().trim();
            if (trim3.length() >= 8) {
                identityMsg.setBorn(formatDate(trim3));
            }
            if (identityMsg.getPeriod().trim().length() >= 17) {
                identityMsg.setPeriod(String.valueOf(formatDate(trim.substring(0, 8))) + " - " + formatDate(trim.substring(9)));
            }
            if ("I".equals(identityMsg.getCard_type())) {
                identityMsg.setApartment("公安部/Ministry of Public Security");
            }
        }
        return identityMsg;
    }

    public IdentityMsg checkIdCardOverseas(int i) throws TelpoException {
        return checkIdCardOverseas();
    }

    public IdentityMsg checkIdCardOverseas() throws TelpoException {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return null;
        }
        idcardData = null;
        IdentityMsg checkIDCard = t2OReader.checkIDCard();
        info = checkIDCard;
        return checkIDCard;
    }

    public String readUIDTypeA() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return null;
        }
        return t2OReader.readUIDTypeA();
    }

    public boolean passwordCheckTypeA(String str, String str2) {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        return t2OReader.passwordCheckTypeA(str, str2);
    }

    public boolean passwordCheckTypeA(String str, String str2, int i) {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        return t2OReader.passwordCheckTypeA(str, str2, i);
    }

    public String readDataTYPEA() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return null;
        }
        return t2OReader.readDataTYPEA();
    }

    public boolean writeDataTypeA(String str) {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        return t2OReader.writeDataTypeA(str);
    }

    public void checkID_IC(ReadCallBack readCallBack) {
        T2OReader t2OReader = t2oReader;
        if (t2OReader != null) {
            t2OReader.checkID_IC(readCallBack);
        }
    }

    public boolean resetModule() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        return t2OReader.resetModule();
    }

    public static synchronized byte[] getIdCardImage() throws TelpoException {
        byte[] bArr;
        synchronized (IdCard.class) {
            bArr = get_image();
            if (bArr == null) {
                throw new IdCardNotCheckException();
            }
        }
        return bArr;
    }

    public static int saveIdCardImage(byte[] bArr, String str) {
        return get_imageusb(bArr, str);
    }

    public byte[] getIdCardImageOverseas(IdentityMsg identityMsg) throws TelpoException {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return null;
        }
        return t2OReader.getIDImage(identityMsg);
    }

    public static synchronized byte[] getFringerPrint() throws TelpoException {
        synchronized (IdCard.class) {
            if (SystemUtil.getDeviceType() != StringUtil.DeviceModelEnum.TPS900.ordinal() && SystemUtil.getDeviceType() != StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                byte[] bArr = get_fringerprint();
                if (bArr != null) {
                    return bArr;
                }
                throw new IdCardNotCheckException();
            }
            return null;
        }
    }

    public byte[] getFringerPrintOverseas(IdentityMsg identityMsg) throws TelpoException {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return null;
        }
        return t2OReader.getIDFinger(identityMsg);
    }

    public static Bitmap decodeIdCardImage(byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new ImageDecodeException();
        }
        return t2oReader.decodeIDImage(bArr);
    }

    public Bitmap decodeIdCardImageOverseas(byte[] bArr) throws TelpoException {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return null;
        }
        return t2OReader.decodeIDImage(bArr);
    }

    public static synchronized String getSAM() throws TelpoException {
        synchronized (IdCard.class) {
            T2OReader t2OReader = t2oReader;
            if (t2OReader == null) {
                return null;
            }
            return t2OReader.getIDSam();
        }
    }

    public static synchronized String getVersion() throws TelpoException {
        synchronized (IdCard.class) {
        }
        return null;
    }

    public static synchronized byte[] getPhyAddr() throws TelpoException {
        synchronized (IdCard.class) {
            T2OReader t2OReader = t2oReader;
            if (t2OReader == null) {
                return null;
            }
            return t2OReader.getIDPhyAddr();
        }
    }

    public static int close() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return -1;
        }
        t2OReader.closeReader();
        return 0;
    }

    public void usbClose() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader != null) {
            t2OReader.closeReader();
        }
    }

    private static String formatDate(String str) {
        return str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8);
    }

    private static String bytearray2Str(byte[] bArr, int i, int i2, int i3) {
        int i4;
        if (bArr.length < i + i2) {
            return "";
        }
        long j = 0;
        for (int i5 = 1; i5 <= i2; i5++) {
            j = (j * 256) + (bArr[i4 - i5] & 255);
        }
        return String.format("%0" + i3 + "d", Long.valueOf(j));
    }

    private static int countChinese(String str) {
        int i = 0;
        while (Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str).find()) {
            i++;
        }
        return i;
    }

    private static boolean isChinese(String str) {
        return Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str).find();
    }

    private static boolean isSimpleNumber(String str) {
        return Pattern.compile("[\\u0030-\\u0039]").matcher(str).find();
    }

    private static boolean isAllNumber(String str) {
        return Pattern.compile("[\\uff10-\\uff19]").matcher(str).find();
    }

    private static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static boolean copyFile(String str, String str2) {
        boolean z = false;
        try {
            File file = new File(str);
            if (file.exists() && file.canRead()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                File file2 = new File(str2);
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.close();
                        fileInputStream.close();
                        z = true;
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return z;
        }
    }

    private static boolean isFileExists(String str) {
        try {
            return new File(str).exists();
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x03ee  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x043f  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0395 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x017e  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x034e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.telpo.tps550.api.idcard.IdentityMsg decodeIdCardBaseInfo(byte[] r63) {
        /*
            Method dump skipped, instructions count: 1094
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.IdCard.decodeIdCardBaseInfo(byte[]):com.telpo.tps550.api.idcard.IdentityMsg");
    }

    public static boolean isNumeric(String str) {
        int length = str.length();
        do {
            length--;
            if (length < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(length)));
        return false;
    }

    public void closeStream() {
        t2oReader.closeReader();
    }

    private void sendCommand(byte[] bArr, int i) {
        this.step = i;
        this.idData = null;
        this.startTime = System.currentTimeMillis();
        if (this.allStep) {
            this.totalStartTime = System.currentTimeMillis();
        }
        for (byte b : bArr) {
            try {
                this.mOutputStream.write(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (SecurityException e3) {
                e3.printStackTrace();
            }
        }
        if (i == 1) {
            this.handler.sendEmptyMessageDelayed(2, 50L);
        } else if (i == 2) {
            this.handler.sendEmptyMessageDelayed(3, 50L);
        } else if (i == 3) {
            this.handler.sendEmptyMessageDelayed(4, 1300L);
        }
    }

    public void allStep() {
        if (this.serial == null) {
            this.throwException = true;
            return;
        }
        this.allStep = true;
        this.decodeDataSuccess = false;
        sendCommand(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 1, 34}, 1);
    }

    /* loaded from: classes.dex */
    private class ReadThread extends Thread {
        IdCardCalBack calback;

        public ReadThread(IdCardCalBack idCardCalBack) {
            IdCard.this = r1;
            this.calback = idCardCalBack;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] bArr = new byte[1024];
                    byte[] copyOfRange = Arrays.copyOfRange(bArr, 0, IdCard.this.mInputStream.read(bArr));
                    IdCard idCard = IdCard.this;
                    idCard.idData = ReaderUtils.merge(idCard.idData, copyOfRange);
                    if (IdCard.this.idData != null) {
                        if (IdCard.this.step == 3) {
                            if (IdCard.this.idData.length > 10 && ((StringUtil.toHexString(Arrays.copyOfRange(IdCard.this.idData, 0, 11)).equals("AAAAAA9669000400004145") || StringUtil.toHexString(Arrays.copyOfRange(IdCard.this.idData, 0, 12)).equals("AAAAAA966900040000101400") || StringUtil.toHexString(Arrays.copyOfRange(IdCard.this.idData, 0, 11)).equals("AAAAAA9669000400001014")) && this.calback != null)) {
                                IdCard.this.readCardSuccess = 2;
                                this.calback.readCard(false);
                            }
                            if (IdCard.this.idData != null && IdCard.this.idData.length > 6) {
                                if (IdCard.this.idData[5] == 9) {
                                    if (IdCard.this.idData.length == 2321 && this.calback != null) {
                                        IdCard.this.readCardSuccess = 1;
                                        this.calback.readCard(true);
                                    }
                                } else if (IdCard.this.idData[5] == 5 && IdCard.this.idData.length == 1297 && this.calback != null) {
                                    IdCard.this.readCardSuccess = 1;
                                    this.calback.readCard(true);
                                }
                            }
                            IdCard.this.endTime = System.currentTimeMillis();
                            if (IdCard.this.endTime - IdCard.this.startTime > 1300 && this.calback != null) {
                                IdCard.this.readCardSuccess = 2;
                                this.calback.readCard(false);
                            }
                        } else {
                            String hexString = StringUtil.toHexString(IdCard.this.idData);
                            if (hexString.equals("AAAAAA9669000800009F0000000097")) {
                                if (this.calback != null) {
                                    IdCard.this.findCardSuccess = 1;
                                    this.calback.findCard(true);
                                }
                            } else if (hexString.equals("AAAAAA9669000C00009000000000000000009C")) {
                                if (this.calback != null) {
                                    IdCard.this.selectCardSuccess = 1;
                                    this.calback.selectCard(true);
                                }
                            } else {
                                if (!hexString.equals("AAAAAA9669000400008084") && !hexString.equals("AAAAAA966900040000101400") && !hexString.equals("AAAAAA9669000400001014") && !hexString.equals("AAAAAA9669090400001014")) {
                                    if (hexString.equals("AAAAAA9669000400008185") && this.calback != null) {
                                        IdCard.this.selectCardSuccess = 2;
                                        this.calback.selectCard(false);
                                    }
                                }
                                if (this.calback != null) {
                                    IdCard.this.findCardSuccess = 2;
                                    this.calback.findCard(false);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public boolean findCard() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        return t2OReader.findIDCard();
    }

    public boolean selectCard() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        return t2OReader.selectIDCard();
    }

    public boolean readCard() {
        T2OReader t2OReader = t2oReader;
        if (t2OReader == null) {
            return false;
        }
        int length = t2OReader.readIDCard().length;
        return length == 1297 || length == 2321;
    }

    public int usb_idcard_find() {
        UsbIdCard usbIdCard = this.mUsbIdCard;
        if (usbIdCard != null) {
            try {
                byte[] bArr = {-86, -86, -86, -106, 105, 0, 8, 0, 0, -97, 0, 0, 0, 0, -105};
                byte[] findCard = usbIdCard.findCard();
                if (findCard == null) {
                    return 1;
                }
                return !StringUtil.toHexString(findCard).equals(StringUtil.toHexString(bArr)) ? 1 : 0;
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -99;
    }

    public int usb_idcard_select() {
        UsbIdCard usbIdCard = this.mUsbIdCard;
        if (usbIdCard != null) {
            try {
                byte[] bArr = new byte[19];
                bArr[0] = -86;
                bArr[1] = -86;
                bArr[2] = -86;
                bArr[3] = -106;
                bArr[4] = 105;
                bArr[6] = 12;
                bArr[9] = -112;
                bArr[18] = -100;
                byte[] selectCard = usbIdCard.selectCard();
                if (selectCard == null) {
                    return 1;
                }
                return !StringUtil.toHexString(selectCard).equals(StringUtil.toHexString(bArr)) ? 1 : 0;
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -99;
    }

    public byte[] usb_idcard_read() {
        UsbIdCard usbIdCard = this.mUsbIdCard;
        if (usbIdCard != null) {
            try {
                return usbIdCard.readCard();
            } catch (TelpoException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static double div(double d, double d2, int i) throws IllegalAccessException {
        if (i < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        return new BigDecimal(Double.toString(d)).divide(new BigDecimal(Double.toString(d2)), i, 4).doubleValue();
    }

    private String selectSerial() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS550.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS550A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS580A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510A_NHW.ordinal() || "TPS980P".equals(SystemUtil.getInternalModel()) || "TPS990H".equals(SystemUtil.getInternalModel())) {
            return "/dev/ttyS0";
        }
        if (deviceType == StringUtil.DeviceModelEnum.TPS580.ordinal()) {
            return "/dev/ttyS2";
        }
        if ("F10".equals(SystemUtil.getInternalModel())) {
            return "/dev/ttyHSL1";
        }
        if (deviceType == StringUtil.DeviceModelEnum.TPS550MTK.ordinal()) {
            return "/dev/ttyMT3";
        }
        if (deviceType != StringUtil.DeviceModelEnum.TPS462.ordinal() && deviceType != StringUtil.DeviceModelEnum.TPS468.ordinal()) {
            return null;
        }
        return "/dev/ttyS3";
    }
}
