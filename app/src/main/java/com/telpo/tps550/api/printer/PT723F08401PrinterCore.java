package com.telpo.tps550.api.printer;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PT723F08401PrinterCore {
    public static int BitmapWidth;
    private static int LBlank;
    public static int PrintDataHeight;
    private static int RBlank;

    public static byte[] PrintDataFormat(Bitmap bitmap) {
        try {
            return CompressPrintData(CreatePrintBitmpaData(bitmap));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] CompressPrintData(byte[] bArr) {
        try {
            byte[] bArr2 = new byte[BitmapWidth];
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (int i = 0; i < PrintDataHeight; i++) {
                int i2 = BitmapWidth;
                int i3 = i * i2;
                byte[] bArr3 = new byte[i2];
                boolean z = true;
                int i4 = 0;
                int i5 = 0;
                for (int i6 = 0; i6 < BitmapWidth; i6++) {
                    byte b = bArr[i3 + i6];
                    if (b != 0) {
                        if (i6 == 0) {
                            i4 = 0;
                        } else if (i4 > i5) {
                            i4 = i5;
                        }
                        z = false;
                        i5 = i6;
                    }
                    bArr3[i6] = b;
                }
                if (!z) {
                    int i7 = LBlank;
                    if (i7 == 0) {
                        LBlank = i4;
                    } else {
                        if (i7 < i4) {
                            i4 = i7;
                        }
                        LBlank = i4;
                    }
                    int i8 = RBlank;
                    if (i8 >= i5) {
                        i5 = i8;
                    }
                    RBlank = i5;
                    int size = arrayList3.size();
                    if (size > 0) {
                        if (size > 24) {
                            if (arrayList2.size() > 0) {
                                arrayList.add(TrimBitmapBlank(arrayList2));
                            }
                            arrayList.add(CreateFeedLineCMD(arrayList3));
                            arrayList2 = new ArrayList();
                        } else {
                            arrayList2.addAll(arrayList3);
                        }
                        arrayList3 = new ArrayList();
                    }
                    arrayList2.add(bArr3);
                    if (arrayList2.size() == 100) {
                        arrayList.add(TrimBitmapBlank(arrayList2));
                        arrayList2 = new ArrayList();
                    }
                } else {
                    arrayList3.add(bArr3);
                }
            }
            int size2 = arrayList3.size();
            if (size2 <= 0) {
                arrayList.add(TrimBitmapBlank(arrayList2));
            } else if (size2 > 24) {
                if (arrayList2.size() > 0) {
                    arrayList.add(TrimBitmapBlank(arrayList2));
                }
                arrayList.add(CreateFeedLineCMD(arrayList3));
            } else {
                arrayList2.addAll(arrayList3);
                arrayList.add(TrimBitmapBlank(arrayList2));
            }
            return sysCopy(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] CreateFeedLineCMD(List<byte[]> list) {
        try {
            ArrayList arrayList = new ArrayList();
            int size = list.size();
            for (int i = 0; i < size; i += 240) {
                byte[] bArr = new byte[3];
                bArr[0] = 27;
                bArr[1] = 74;
                int i2 = size - i;
                if (i2 > 240) {
                    bArr[2] = -16;
                } else {
                    bArr[2] = (byte) ((i2 * 8) / 8);
                }
                arrayList.add(bArr);
            }
            return sysCopy(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] TrimBitmapBlank(List<byte[]> list) {
        try {
            int i = (RBlank - LBlank) + 1;
            int size = list.size();
            byte[] bArr = new byte[(i * size) + ((size % 2300 > 0 ? (size / 2300) + 1 : size / 2300) * 8)];
            int i2 = 0;
            int i3 = 0;
            while (i2 < size) {
                int i4 = i2 + 2300;
                int i5 = i4 < size ? 2300 : size - i2;
                int i6 = 2308 * i3;
                bArr[i6] = 29;
                bArr[i6 + 1] = 118;
                bArr[i6 + 2] = 48;
                bArr[i6 + 3] = 0;
                bArr[i6 + 4] = (byte) (i % 256);
                bArr[i6 + 5] = (byte) (i / 256);
                bArr[i6 + 6] = (byte) (i5 % 256);
                bArr[i6 + 7] = (byte) (i5 / 256);
                while (i2 < size) {
                    System.arraycopy(list.get(i2), LBlank, bArr, (i2 * i) + i6 + 8, i);
                    i2++;
                }
                i3++;
                i2 = i4;
            }
            LBlank = 0;
            RBlank = 0;
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] CreatePrintBitmpaData(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            PrintDataHeight = height;
            int i = (width % 8 == 0 ? width : ((width / 8) + 1) * 8) / 8;
            BitmapWidth = i;
            int i2 = i * height;
            byte[] bArr = new byte[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                bArr[i3] = 0;
            }
            int i4 = 0;
            int i5 = 0;
            while (i4 < height) {
                int[] iArr = new int[width];
                bitmap.getPixels(iArr, 0, width, 0, i4, width, 1);
                int i6 = 0;
                for (int i7 = 0; i7 < width; i7++) {
                    i6++;
                    int i8 = iArr[i7];
                    if (i6 > 8) {
                        i5++;
                        i6 = 1;
                    }
                    if (i8 != -1) {
                        int i9 = 1 << (8 - i6);
                        int red = Color.red(i8);
                        int green = Color.green(i8);
                        int blue = Color.blue(i8);
                        if (red < 128 || green < 128 || blue < 128) {
                            bArr[i5] = (byte) (bArr[i5] | i9);
                        }
                    }
                }
                i4++;
                i5 = BitmapWidth * i4;
            }
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] sysCopy(List<byte[]> list) {
        int i = 0;
        for (byte[] bArr : list) {
            i += bArr.length;
        }
        byte[] bArr2 = new byte[i];
        int i2 = 0;
        for (byte[] bArr3 : list) {
            System.arraycopy(bArr3, 0, bArr2, i2, bArr3.length);
            i2 += bArr3.length;
        }
        return bArr2;
    }
}
