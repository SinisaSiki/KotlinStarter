package com.telpo.tps550.api.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class ReaderUtils {
    static String[] nation_list = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜", "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土", "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒", "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺", "其他", "外国血统中国籍人士"};

    private static String GetFingerName(Context context, int i) {
        switch (i) {
            case 11:
                return "右手拇指";
            case 12:
                return "右手食指";
            case 13:
                return "右手中指";
            case 14:
                return "右手环指";
            case 15:
                return "右手小指";
            case 16:
                return "左手拇指";
            case 17:
                return "左手食指";
            case 18:
                return "左手中指";
            case 19:
                return "左手环指";
            case 20:
                return "左手小指";
            default:
                switch (i) {
                    case 97:
                        return "右手不确定指位";
                    case 98:
                        return "左手不确定指位";
                    case 99:
                        return "其他不确定指位";
                    default:
                        return "指位未知";
                }
        }
    }

    private static String GetFingerStatus(Context context, int i) {
        return i != 1 ? i != 2 ? i != 3 ? "注册状态未知" : "未注册" : "注册失败" : "注册成功";
    }

    public static String get_nation(int i) {
        return nation_list[i];
    }

    public static int count_chinese(String str) {
        int i = 0;
        while (Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str).find()) {
            i++;
        }
        return i;
    }

    public static String get_finger_info(Context context, byte[] bArr) {
        String str;
        if (bArr != null && bArr.length == 1024 && bArr[0] == 67) {
            String str2 = "" + GetFingerName(context, bArr[5]);
            if (bArr[4] == 1) {
                str = String.valueOf(str2) + " 指纹质量= " + String.valueOf((int) bArr[6]);
            } else {
                str = String.valueOf(str2) + GetFingerStatus(context, bArr[4]);
            }
            String str3 = String.valueOf(str) + "  ";
            if (bArr[512] != 67) {
                return str3;
            }
            String str4 = String.valueOf(str3) + GetFingerName(context, bArr[517]);
            if (bArr[516] == 1) {
                return String.valueOf(str4) + " 指纹质量= " + String.valueOf((int) bArr[518]);
            }
            return String.valueOf(str4) + GetFingerStatus(context, bArr[516]);
        }
        return "(指纹未读取或不含指纹)";
    }

    public static boolean check_package(Context context, String str) {
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent().setPackage(str), 32);
        return queryIntentActivities != null && queryIntentActivities.size() >= 1;
    }

    public static byte get_checksum(byte[] bArr, int i, int i2) {
        byte b = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i + i3;
            if (i4 >= i2) {
                break;
            }
            b = (byte) (b ^ bArr[i4]);
        }
        return b;
    }

    public static boolean check_checksum(byte[] bArr, int i, int i2) {
        if (i < 0 || bArr.length < i + i2) {
            return false;
        }
        int i3 = 0;
        byte b = 0;
        while (i3 < i2 - 1) {
            b = (byte) (b ^ bArr[i + i3]);
            i3++;
        }
        return b == bArr[i + i3];
    }

    public static byte[] merge(byte[] bArr, byte[] bArr2) {
        if (bArr == null) {
            return bArr2;
        }
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static String byte2HexString(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = 0; i3 < i2; i3++) {
            String upperCase = Integer.toHexString(bArr[i + i3] & 255).toUpperCase();
            if (upperCase.length() == 1) {
                stringBuffer.append('0');
            }
            stringBuffer.append(upperCase);
        }
        return stringBuffer.toString();
    }

    public static String byte2HexString(byte[] bArr) {
        return bArr == null ? "" : byte2HexString(bArr, 0, bArr.length);
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        if (upperCase.length() % 2 != 0) {
            upperCase = String.valueOf('0') + upperCase;
        }
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

    public static void idcard_poweron() {
        ShellUtils.execCommand("echo 3 > /sys/class/telpoio/power_status", false);
    }

    public static void idcard_poweroff() {
        ShellUtils.execCommand("echo 4 > /sys/class/telpoio/power_status", false);
    }
}
