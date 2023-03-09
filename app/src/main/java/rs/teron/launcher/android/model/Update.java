package rs.teron.launcher.android.model;

import java.io.PrintStream;

/* loaded from: classes.dex */
public class Update {
    public String id;
    public String name;
    public Type type;
    public String url;
    public String version;

    /* loaded from: classes.dex */
    public enum Type {
        Application,
        Firmware
    }

    public Update() {
        this.type = Type.Application;
    }

    public Update(String str, String str2, String str3, Type type) {
        Type type2 = Type.Application;
        this.id = str;
        this.name = str2;
        this.version = str3;
        this.type = type;
    }

    public boolean isEnabled() {
        try {
            if (this.type != Type.Firmware) {
                if (Integer.parseInt(this.version) <= 0) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNewer(Update update) {
        try {
            return Integer.parseInt(this.version) > Integer.parseInt(update.version);
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("Exception: " + e);
            e.printStackTrace();
            return false;
        }
    }
}
