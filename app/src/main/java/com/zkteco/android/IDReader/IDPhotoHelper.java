package com.zkteco.android.IDReader;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class IDPhotoHelper {
    public static Bitmap Bgr2Bitmap(byte[] bArr) {
        Bitmap createBitmap = Bitmap.createBitmap(102, 126, Bitmap.Config.RGB_565);
        int i = 0;
        int i2 = 101;
        for (int length = bArr.length - 1; length >= 3; length -= 3) {
            int i3 = i2 - 1;
            createBitmap.setPixel(i2, i, (bArr[length] & 255) + ((bArr[length - 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) + ((bArr[length - 2] << 16) & 16711680));
            if (i3 < 0) {
                i++;
                i2 = 101;
            } else {
                i2 = i3;
            }
        }
        return createBitmap;
    }

    public static Bitmap createMyBitmap(byte[] bArr, int i, int i2) {
        int[] convertByteToColor = convertByteToColor(bArr);
        if (convertByteToColor == null) {
            return null;
        }
        try {
            return Bitmap.createBitmap(convertByteToColor, 0, i, i, i2, Bitmap.Config.ARGB_8888);
        } catch (Exception unused) {
            return null;
        }
    }

    private static int[] convertByteToColor(byte[] bArr) {
        int i;
        int length = bArr.length;
        if (length == 0) {
            return null;
        }
        int i2 = 0;
        int i3 = length % 3 != 0 ? 1 : 0;
        int i4 = (length / 3) + i3;
        int[] iArr = new int[i4];
        if (i3 == 0) {
            while (i2 < i4) {
                int i5 = i2 * 3;
                iArr[i2] = (bArr[i5 + 2] & 255) | ((bArr[i5] << 16) & 16711680) | ((bArr[i5 + 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | ViewCompat.MEASURED_STATE_MASK;
                i2++;
            }
        } else {
            while (true) {
                i = i4 - 1;
                if (i2 >= i) {
                    break;
                }
                int i6 = i2 * 3;
                iArr[i2] = (bArr[i6 + 2] & 255) | ((bArr[i6] << 16) & 16711680) | ((bArr[i6 + 1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | ViewCompat.MEASURED_STATE_MASK;
                i2++;
            }
            iArr[i] = -16777216;
        }
        return iArr;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, float f) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (createBitmap.equals(bitmap)) {
            return createBitmap;
        }
        bitmap.recycle();
        return createBitmap;
    }
}
