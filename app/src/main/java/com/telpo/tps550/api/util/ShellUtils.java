package com.telpo.tps550.api.util;

import java.util.List;

/* loaded from: classes.dex */
public class ShellUtils {
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_SU = "su";

    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    public static CommandResult execCommand(String str, boolean z) {
        return execCommand(new String[]{str}, z, true);
    }

    public static CommandResult execCommand(List<String> list, boolean z) {
        return execCommand(list == null ? null : (String[]) list.toArray(new String[0]), z, true);
    }

    public static CommandResult execCommand(String[] strArr, boolean z) {
        return execCommand(strArr, z, true);
    }

    public static CommandResult execCommand(String str, boolean z, boolean z2) {
        return execCommand(new String[]{str}, z, z2);
    }

    public static CommandResult execCommand(List<String> list, boolean z, boolean z2) {
        return execCommand(list == null ? null : (String[]) list.toArray(new String[0]), z, z2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:112:0x015d, code lost:
        if (r10 != null) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x015f, code lost:
        r10.destroy();
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0162, code lost:
        r8 = r9;
        r10 = r1;
        r1 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0139, code lost:
        if (r10 != null) goto L113;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0151 A[Catch: IOException -> 0x014d, TryCatch #7 {IOException -> 0x014d, blocks: (B:104:0x0149, B:108:0x0151, B:110:0x0156), top: B:139:0x0149 }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0156 A[Catch: IOException -> 0x014d, TRY_LEAVE, TryCatch #7 {IOException -> 0x014d, blocks: (B:104:0x0149, B:108:0x0151, B:110:0x0156), top: B:139:0x0149 }] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0189 A[Catch: IOException -> 0x0185, TryCatch #16 {IOException -> 0x0185, blocks: (B:126:0x0181, B:130:0x0189, B:132:0x018e), top: B:147:0x0181 }] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x018e A[Catch: IOException -> 0x0185, TRY_LEAVE, TryCatch #16 {IOException -> 0x0185, blocks: (B:126:0x0181, B:130:0x0189, B:132:0x018e), top: B:147:0x0181 }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0149 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0125 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0181 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x012d A[Catch: IOException -> 0x0129, TryCatch #12 {IOException -> 0x0129, blocks: (B:89:0x0125, B:93:0x012d, B:95:0x0132), top: B:143:0x0125 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0132 A[Catch: IOException -> 0x0129, TRY_LEAVE, TryCatch #12 {IOException -> 0x0129, blocks: (B:89:0x0125, B:93:0x012d, B:95:0x0132), top: B:143:0x0125 }] */
    /* JADX WARN: Type inference failed for: r10v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r10v18 */
    /* JADX WARN: Type inference failed for: r10v19 */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v24, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r10v25 */
    /* JADX WARN: Type inference failed for: r10v3, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r10v4 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v36 */
    /* JADX WARN: Type inference failed for: r3v39 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.io.DataOutputStream] */
    /* JADX WARN: Type inference failed for: r3v9, types: [java.io.DataOutputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.telpo.tps550.api.util.ShellUtils.CommandResult execCommand(java.lang.String[] r8, boolean r9, boolean r10) {
        /*
            Method dump skipped, instructions count: 417
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.util.ShellUtils.execCommand(java.lang.String[], boolean, boolean):com.telpo.tps550.api.util.ShellUtils$CommandResult");
    }

    /* loaded from: classes.dex */
    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int i) {
            this.result = i;
        }

        public CommandResult(int i, String str, String str2) {
            this.result = i;
            this.successMsg = str;
            this.errorMsg = str2;
        }
    }
}
