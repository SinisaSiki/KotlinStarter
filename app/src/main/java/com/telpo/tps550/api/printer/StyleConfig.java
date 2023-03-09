package com.telpo.tps550.api.printer;

/* loaded from: classes.dex */
public class StyleConfig {
    public FontFamily fontFamily = FontFamily.DEFAULT;
    public FontSize fontSize = FontSize.F2;
    public FontStyle fontStyle = FontStyle.NORMAL;
    public Align align = Align.LEFT;
    public int gray = 4;
    public int lineSpace = 16;
    public boolean newLine = true;

    /* loaded from: classes.dex */
    public enum Align {
        LEFT,
        CENTER,
        RIGHT
    }

    /* loaded from: classes.dex */
    public enum FontFamily {
        DEFAULT
    }

    /* loaded from: classes.dex */
    public enum FontSize {
        F1,
        F2,
        F3,
        F4
    }

    /* loaded from: classes.dex */
    public enum FontStyle {
        NORMAL,
        BOLD
    }
}
