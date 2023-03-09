package androidx.constraintlayout.core.parser;

import androidx.core.location.LocationRequestCompat;
import java.io.PrintStream;

/* loaded from: classes.dex */
public class CLElement {
    protected static int BASE_INDENT = 2;
    protected static int MAX_LINE = 80;
    private int line;
    protected CLContainer mContainer;
    private final char[] mContent;
    protected long start = -1;
    protected long end = LocationRequestCompat.PASSIVE_INTERVAL;

    public String toFormattedJSON(int i, int i2) {
        return "";
    }

    public String toJSON() {
        return "";
    }

    public CLElement(char[] cArr) {
        this.mContent = cArr;
    }

    public boolean notStarted() {
        return this.start == -1;
    }

    public void setLine(int i) {
        this.line = i;
    }

    public int getLine() {
        return this.line;
    }

    public void setStart(long j) {
        this.start = j;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long j) {
        if (this.end != LocationRequestCompat.PASSIVE_INTERVAL) {
            return;
        }
        this.end = j;
        if (CLParser.DEBUG) {
            PrintStream printStream = System.out;
            printStream.println("closing " + hashCode() + " -> " + this);
        }
        CLContainer cLContainer = this.mContainer;
        if (cLContainer == null) {
            return;
        }
        cLContainer.add(this);
    }

    public void addIndent(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(' ');
        }
    }

    public String toString() {
        long j = this.start;
        long j2 = this.end;
        if (j > j2 || j2 == LocationRequestCompat.PASSIVE_INTERVAL) {
            return getClass() + " (INVALID, " + this.start + "-" + this.end + ")";
        }
        String substring = new String(this.mContent).substring((int) this.start, ((int) this.end) + 1);
        return getStrClass() + " (" + this.start + " : " + this.end + ") <<" + substring + ">>";
    }

    public String getStrClass() {
        String cls = getClass().toString();
        return cls.substring(cls.lastIndexOf(46) + 1);
    }

    public String getDebugName() {
        if (CLParser.DEBUG) {
            return getStrClass() + " -> ";
        }
        return "";
    }

    public String content() {
        String str = new String(this.mContent);
        long j = this.end;
        if (j != LocationRequestCompat.PASSIVE_INTERVAL) {
            long j2 = this.start;
            if (j >= j2) {
                return str.substring((int) j2, ((int) j) + 1);
            }
        }
        long j3 = this.start;
        return str.substring((int) j3, ((int) j3) + 1);
    }

    public boolean isDone() {
        return this.end != LocationRequestCompat.PASSIVE_INTERVAL;
    }

    public void setContainer(CLContainer cLContainer) {
        this.mContainer = cLContainer;
    }

    public CLElement getContainer() {
        return this.mContainer;
    }

    public boolean isStarted() {
        return this.start > -1;
    }

    public int getInt() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getInt();
        }
        return 0;
    }

    public float getFloat() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getFloat();
        }
        return Float.NaN;
    }
}
