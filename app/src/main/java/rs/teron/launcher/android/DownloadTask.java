package rs.teron.launcher.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.PowerManager;
import rs.teron.launcher.android.model.Update;

/* loaded from: classes.dex */
public class DownloadTask extends AsyncTask<Update, Integer, Boolean> {
    private String filename;
    private final PowerManager powerManager;
    private final ProgressDialog progressDialog;
    private final Response response;
    private final String serialNumber;
    private PowerManager.WakeLock wakeLock;

    /* loaded from: classes.dex */
    public interface Response {
        void process(boolean z, String str);
    }

    public DownloadTask(PowerManager powerManager, String str, ProgressDialog progressDialog, Response response) {
        this.powerManager = powerManager;
        this.serialNumber = str;
        this.progressDialog = progressDialog;
        this.response = response;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager.WakeLock newWakeLock = this.powerManager.newWakeLock(1, getClass().getName());
        this.wakeLock = newWakeLock;
        newWakeLock.acquire();
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setProgressStyle(1);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    public void onProgressUpdate(Integer... numArr) {
//        super.onProgressUpdate((Object[]) numArr);
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setMax(100);
        this.progressDialog.setProgress(numArr[0].intValue());
    }

    public void onPostExecute(Boolean bool) {
        this.wakeLock.release();
        this.progressDialog.dismiss();
        this.response.process(bool.booleanValue(), this.filename);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00f1, code lost:
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00f4, code lost:
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00f7, code lost:
        if (r5 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00f9, code lost:
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0120, code lost:
        r15 = r5;
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0128, code lost:
        if (r15 == null) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x012a, code lost:
        r15.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0133, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0135, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0145, code lost:
        r4 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0149, code lost:
        r4 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0175 A[Catch: IOException -> 0x0178, TRY_LEAVE, TryCatch #0 {IOException -> 0x0178, blocks: (B:70:0x0170, B:72:0x0175), top: B:88:0x0170 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0186 A[Catch: IOException -> 0x0189, TRY_LEAVE, TryCatch #9 {IOException -> 0x0189, blocks: (B:78:0x0181, B:80:0x0186), top: B:92:0x0181 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0170 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0181 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Boolean doInBackground(rs.teron.launcher.android.model.Update... r17) {
        /*
            Method dump skipped, instructions count: 399
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: rs.teron.launcher.android.DownloadTask.doInBackground(rs.teron.launcher.android.model.Update[]):java.lang.Boolean");
    }
}
