package com.telpo.tps550.api.printer;

import android.util.Log;
import com.telpo.tps550.api.util.StringUtil;

/* loaded from: classes.dex */
public class ThermalPrinterSY581 {
    public static final int ALGIN_LEFT = 0;
    public static final int ALGIN_MIDDLE = 1;
    public static final int ALGIN_RIGHT = 2;
    static int count;

    public static synchronized void selfCheck() {
        synchronized (ThermalPrinterSY581.class) {
            sendCommand(new byte[]{29, 112, 10, 10, 10, 10, 10, 10, 10});
        }
    }

    public static synchronized void reset() {
        synchronized (ThermalPrinterSY581.class) {
            sendCommand(new byte[]{27, 121});
        }
    }

    public static synchronized byte[] resetCMD() {
        byte[] bArr;
        synchronized (ThermalPrinterSY581.class) {
            bArr = new byte[]{27, 121};
        }
        return bArr;
    }

    public static synchronized void checkStatus() {
        synchronized (ThermalPrinterSY581.class) {
            sendCommand(new byte[]{27, 22});
        }
    }

    public static synchronized void clearSerial() {
        synchronized (ThermalPrinterSY581.class) {
            sendCommand(new byte[]{27, 64});
        }
    }

    public static synchronized byte[] checkStatusCMD() {
        byte[] bArr;
        synchronized (ThermalPrinterSY581.class) {
            bArr = new byte[]{27, 22};
        }
        return bArr;
    }

    public static synchronized void setLineSpace(int i) {
        String str;
        synchronized (ThermalPrinterSY581.class) {
            if (i == 1) {
                str = "01";
            } else if (i == 2) {
                str = "02";
            } else if (i == 3) {
                str = "03";
            } else if (i == 4) {
                str = "04";
            } else if (i == 5) {
                str = "05";
            } else if (i == 6) {
                str = "06";
            } else if (i == 7) {
                str = "07";
            } else if (i == 8) {
                str = "08";
            } else if (i == 9) {
                str = "09";
            } else {
                try {
                    str = new StringBuilder().append(i).toString();
                } catch (Throwable th) {
                    throw th;
                }
            }
            sendCommand(new byte[]{27, 51, parseHexStr2Byte(str)[0]});
        }
    }

    public static synchronized byte[] setLineSpaceCMD(int i) {
        String str;
        byte[] bArr;
        synchronized (ThermalPrinterSY581.class) {
            if (i == 1) {
                str = "01";
            } else if (i == 2) {
                str = "02";
            } else if (i == 3) {
                str = "03";
            } else if (i == 4) {
                str = "04";
            } else if (i == 5) {
                str = "05";
            } else if (i == 6) {
                str = "06";
            } else if (i == 7) {
                str = "07";
            } else if (i == 8) {
                str = "08";
            } else if (i == 9) {
                str = "09";
            } else {
                try {
                    str = new StringBuilder().append(i).toString();
                } catch (Throwable th) {
                    throw th;
                }
            }
            bArr = new byte[]{27, 51, parseHexStr2Byte(str)[0]};
        }
        return bArr;
    }

    public static synchronized void setGray(int i) {
        synchronized (ThermalPrinterSY581.class) {
            String str = "";
            if (i == 0) {
                str = "08";
            } else if (i == 1) {
                str = "07";
            } else if (i == 2) {
                str = "06";
            } else if (i == 3) {
                str = "05";
            } else if (i == 4) {
                str = "04";
            } else if (i == 5) {
                str = "03";
            }
            sendCommand(new byte[]{27, 115, parseHexStr2Byte(str)[0]});
        }
    }

    public static synchronized byte[] setGrayCMD(int i) {
        byte[] bArr;
        synchronized (ThermalPrinterSY581.class) {
            String str = "";
            if (i == 0) {
                str = "08";
            } else if (i == 1) {
                str = "07";
            } else if (i == 2) {
                str = "06";
            } else if (i == 3) {
                str = "05";
            } else if (i == 4) {
                str = "04";
            } else if (i == 5) {
                str = "03";
            }
            bArr = new byte[]{27, 115, parseHexStr2Byte(str)[0]};
        }
        return bArr;
    }

    public static synchronized void setAlign(int i) {
        synchronized (ThermalPrinterSY581.class) {
            byte[] bArr = null;
            try {
                if (i == 0) {
                    bArr = new byte[]{27, 97};
                } else if (i == 1) {
                    bArr = new byte[]{27, 97, 1};
                } else if (i == 2) {
                    bArr = new byte[]{27, 97, 2};
                }
                sendCommand(bArr);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static synchronized byte[] setAlignCMD(int i) {
        byte[] bArr;
        synchronized (ThermalPrinterSY581.class) {
            bArr = null;
            try {
                if (i == 0) {
                    bArr = new byte[]{27, 97};
                } else if (i == 1) {
                    bArr = new byte[]{27, 97, 1};
                } else if (i == 2) {
                    bArr = new byte[]{27, 97, 2};
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return bArr;
    }

    public static synchronized String getVersion() {
        synchronized (ThermalPrinterSY581.class) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendCommand(new byte[]{27, 119});
        }
        return "";
    }

    public static synchronized void setTem(int i) {
        synchronized (ThermalPrinterSY581.class) {
            sendCommand(new byte[]{29, 40, StringUtil.toBytes(Integer.toHexString(i))[0]});
        }
    }

    public static synchronized void setLeftDistance(int i) {
        synchronized (ThermalPrinterSY581.class) {
            if (i < 0 || i > 255) {
                return;
            }
            String hexString = Integer.toHexString(i);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            sendCommand(new byte[]{27, 108, parseHexStr2Byte(hexString)[0]});
        }
    }

    public static synchronized byte[] setLeftDistanceCMD(int i) {
        synchronized (ThermalPrinterSY581.class) {
            if (i < 0 || i > 255) {
                return null;
            }
            String hexString = Integer.toHexString(i);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            return new byte[]{27, 108, parseHexStr2Byte(hexString)[0]};
        }
    }

    public static synchronized void walkPaper(int i) {
        synchronized (ThermalPrinterSY581.class) {
            Log.d("tagg", "walkpaper581");
            if (i <= 0) {
                return;
            }
            String str = "";
            for (int i2 = 0; i2 < (i < 33 ? 1 : i / 33); i2++) {
                str = String.valueOf(str) + "0A";
            }
            byte[] bytes = StringUtil.toBytes(str);
            Log.d("tagg", "send cmd:" + StringUtil.toHexString(bytes));
            sendCommand(bytes);
        }
    }

    public static synchronized byte[] walkPaperCMD(int i) {
        synchronized (ThermalPrinterSY581.class) {
            Log.d("tagg", "walkpaper581");
            if (i <= 0) {
                return null;
            }
            String str = "";
            for (int i2 = 0; i2 < (i < 33 ? 1 : i / 33); i2++) {
                str = String.valueOf(str) + "0A";
            }
            return StringUtil.toBytes(str);
        }
    }

    public static synchronized void cutPaper() {
        synchronized (ThermalPrinterSY581.class) {
            byte[] bytes = StringUtil.toBytes("1D5600");
            Log.d("tagg", "send cmd:" + StringUtil.toHexString(bytes));
            sendCommand(bytes);
        }
    }

    public static synchronized void setFont(int i) {
        synchronized (ThermalPrinterSY581.class) {
            if (i < 0 || i > 7) {
                return;
            }
            try {
                if (i == 0) {
                    setFont(0, 0);
                } else if (i == 1) {
                    setFont(1, 1);
                } else if (i == 2) {
                    setFont(2, 2);
                } else if (i == 3) {
                    setFont(3, 3);
                } else if (i == 4) {
                    setFont(4, 4);
                } else if (i == 5) {
                    setFont(5, 5);
                } else if (i != 6) {
                    if (i == 7) {
                        setFont(7, 7);
                    }
                } else {
                    setFont(6, 6);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static synchronized byte[] setFontCMD(int i) {
        synchronized (ThermalPrinterSY581.class) {
            if (i < 0 || i > 7) {
                return null;
            }
            if (i == 0) {
                return setFontCMD(0, 0);
            } else if (i == 1) {
                return setFontCMD(1, 1);
            } else if (i == 2) {
                return setFontCMD(2, 2);
            } else if (i == 3) {
                return setFontCMD(3, 3);
            } else if (i == 4) {
                return setFontCMD(4, 4);
            } else if (i == 5) {
                return setFontCMD(5, 5);
            } else if (i == 6) {
                return setFontCMD(6, 6);
            } else if (i != 7) {
                return null;
            } else {
                return setFontCMD(7, 7);
            }
        }
    }

    public static synchronized void setFont(int i, int i2) {
        String str;
        synchronized (ThermalPrinterSY581.class) {
            if (i2 < 0 || i2 > 7 || i < 0 || i > 7) {
                return;
            }
            if (i == 0) {
                i = 0;
            } else if (i == 1) {
                i = 10;
            } else if (i == 2) {
                i = 20;
            } else if (i == 3) {
                i = 30;
            } else if (i == 4) {
                i = 40;
            } else if (i == 5) {
                i = 50;
            } else if (i == 6) {
                i = 60;
            } else if (i == 7) {
                i = 70;
            }
            if (i == 0) {
                str = "0" + i2;
            } else {
                str = new StringBuilder().append(i2).append(i).toString();
            }
            sendCommand(str.equals("00") ? new byte[]{28, 33} : new byte[]{29, 33, parseHexStr2Byte(str)[0]});
        }
    }

    public static synchronized byte[] setFontCMD(int i, int i2) {
        String str;
        synchronized (ThermalPrinterSY581.class) {
            if (i2 < 0 || i2 > 7 || i < 0 || i > 7) {
                return null;
            }
            if (i == 0) {
                i = 0;
            } else if (i == 1) {
                i = 10;
            } else if (i == 2) {
                i = 20;
            } else if (i == 3) {
                i = 30;
            } else if (i == 4) {
                i = 40;
            } else if (i == 5) {
                i = 50;
            } else if (i == 6) {
                i = 60;
            } else if (i == 7) {
                i = 70;
            }
            if (i == 0) {
                str = "0" + i2;
            } else {
                str = new StringBuilder().append(i2).append(i).toString();
            }
            return str.equals("00") ? new byte[]{28, 33} : new byte[]{29, 33, parseHexStr2Byte(str)[0]};
        }
    }

    private static synchronized byte[] parseHexStr2Byte(String str) {
        synchronized (ThermalPrinterSY581.class) {
            if (str.length() < 1) {
                return null;
            }
            byte[] bArr = new byte[str.length() / 2];
            for (int i = 0; i < str.length() / 2; i++) {
                int i2 = i * 2;
                int i3 = i2 + 1;
                bArr[i] = (byte) ((Integer.parseInt(str.substring(i2, i3), 16) * 16) + Integer.parseInt(str.substring(i3, i2 + 2), 16));
            }
            return bArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:75:0x00d9 A[Catch: all -> 0x00e5, TRY_ENTER, TRY_LEAVE, TryCatch #7 {, blocks: (B:4:0x0003, B:20:0x0057, B:21:0x005a, B:22:0x005d, B:24:0x0063, B:37:0x0082, B:39:0x0087, B:40:0x008a, B:46:0x0097, B:48:0x009c, B:49:0x009f, B:55:0x00ac, B:57:0x00b1, B:58:0x00b4, B:64:0x00c1, B:66:0x00c6, B:67:0x00c9, B:73:0x00d4, B:75:0x00d9, B:76:0x00dc, B:78:0x00e1, B:79:0x00e4), top: B:98:0x0003, inners: #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00d4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.OutputStream, com.telpo.tps550.api.serial.Serial] */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v15 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v5, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized void sendCommand(byte[] r8) {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinterSY581.sendCommand(byte[]):void");
    }
}
