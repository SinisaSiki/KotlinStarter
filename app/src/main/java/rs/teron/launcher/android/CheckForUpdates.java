package rs.teron.launcher.android;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
import rs.teron.launcher.android.model.Update;
import rs.teron.launcher.android.model.Updates;

/* loaded from: classes.dex */
public class CheckForUpdates extends AsyncTask<Void, Void, Updates> {
    private static final String APPLICATIONS_URL = "https://fis.teron.rs/download/apps.json";
    private static final String DEFAULT_API_KEY = "RhgyGmqPbH4bf6dU9uQW8r8y3auxvTBq";
    private static final String FIRMWARE_ID_PREFIX = "firmware-update-for-";
    private static final String HASH_SALT = "Y6CWH[Y3Qs@y)NGp";
    private static final String LAUNCHER_APP_ID = "rs.teron.launcher.android";
    private static final String[] TERON_APPLICATIONS = {"rs.teron.lpfr.android", "rs.teron.esir.android", "rs.teron.launcher.android"};
    private final String packageName;
    private final PackageManager pm;
    private final Response response;
    private final String serialNumber;

    /* loaded from: classes.dex */
    public interface Response {
        void process(Updates updates);
    }

    public CheckForUpdates(PackageManager packageManager, String str, String str2, Response response) {
        this.pm = packageManager;
        this.serialNumber = str;
        this.packageName = str2;
        this.response = response;
    }

    public void onPostExecute(Updates updates) {
        this.response.process(updates);
    }

    public Updates doInBackground(Void... voidArr) {
        System.out.println("Checking for updates");
        try {
            Updates updates = new Updates();
            Updates applicationsLocal = getApplicationsLocal();
            Iterator it = getUpdates().iterator();
            while (it.hasNext()) {
                Update update = (Update) it.next();
                if (update != null && update.id != null && update.name != null && update.url != null && update.type != null) {
                    if (update.type == Update.Type.Firmware) {
                        String substring = update.id.substring(20);
                        System.out.println("Local firmware " + Build.DISPLAY + ", update for " + substring);
                        if (Build.DISPLAY.equals(substring)) {
                            System.out.println("Firmware update detected " + update.name);
                            updates.add(update);
                        }
                    } else {
                        boolean z = true;
                        if (update.id.equals("rs.teron.launcher.android") && !update.id.equals(this.packageName)) {
                            System.out.printf("Skipping launcher updated (%s <> %s)%n", "rs.teron.launcher.android", this.packageName);
                        } else if (update.isEnabled()) {
                            Iterator it2 = applicationsLocal.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    z = false;
                                    break;
                                }
                                Update update2 = (Update) it2.next();
                                if (update2.id.equals(update.id) && !update.isNewer(update2)) {
                                    System.out.println("Already installed " + update.name + ", version " + update.version);
                                    break;
                                }
                            }
                            if (!z) {
                                System.out.println("Application update detected " + update.name + ", version " + update.version);
                                updates.add(update);
                            }
                        }
                    }
                }
                System.out.println("Invalid update record: " + toJson(update));
            }
            return updates;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return null;
        }
    }

    private Updates getApplicationsLocal() {
        Updates updates = new Updates();
        for (ApplicationInfo applicationInfo : this.pm.getInstalledApplications(PackageManager.GET_META_DATA)) {
            if (isTeronApplication(applicationInfo.packageName)) {
                try {
                    updates.add(new Update(applicationInfo.packageName, applicationInfo.loadLabel(this.pm).toString(), Integer.toString(this.pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_META_DATA).versionCode), Update.Type.Application));
                } catch (PackageManager.NameNotFoundException e) {
                    PrintStream printStream = System.out;
                    printStream.println("Exception: " + e);
                }
            }
        }
        return updates;
    }

    private boolean isTeronApplication(String str) {
        for (String str2 : TERON_APPLICATIONS) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    private Updates getUpdates() throws IOException {
        Updates applicationsLocal = getApplicationsLocal();
        applicationsLocal.add(new Update("firmware-update-for", "Firmware " + Build.DISPLAY, Build.DISPLAY, Update.Type.Firmware));
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(APPLICATIONS_URL).openConnection();
        httpsURLConnection.setInstanceFollowRedirects(false);
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setConnectTimeout(10000);
        httpsURLConnection.setReadTimeout(10000);
        httpsURLConnection.setRequestProperty("X-Teron-SerialNumber", this.serialNumber);
        httpsURLConnection.setRequestProperty("X-Teron-DeviceModel", Build.MODEL);
        httpsURLConnection.setRequestProperty("Authorization", "Bearer " + calculateApiKey());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        byte[] bytes = objectMapper.writeValueAsString(applicationsLocal).getBytes(StandardCharsets.UTF_8);
        httpsURLConnection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
        httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpsURLConnection.setDoOutput(true);
        OutputStream outputStream = httpsURLConnection.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[4096];
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                    ObjectMapper objectMapper2 = new ObjectMapper();
                    objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    Updates updates = (Updates) objectMapper2.readValue(byteArrayOutputStream2, Updates.class);
                    byteArrayOutputStream.close();
                    bufferedInputStream.close();
                    return updates;
                }
            }
        } catch (Throwable th) {
            try {
                bufferedInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private String calculateApiKey() {
        try {
            return generatePassword(new int[]{118, 55, 69, 85, 125, 106, 45, 93, 83, 82, 85, 82, 68, 115, 39, 51, 59, 71, 1, 5, 53, 116, 61, 25, 56, 73, 38, 44, 11, 32, 8, 16});
        } catch (NoSuchAlgorithmException unused) {
            return DEFAULT_API_KEY;
        }
    }

    private String generatePassword(int[] iArr) throws NoSuchAlgorithmException {
        String lowerCase = Utils.byteArrayToHexString(MessageDigest.getInstance("SHA-512").digest((HASH_SALT + this.serialNumber).getBytes())).toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i : iArr) {
            sb.append(lowerCase.charAt(i));
        }
        return sb.toString();
    }

    private static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            PrintStream printStream = System.out;
            printStream.println("Exception: " + e);
            return "";
        }
    }
}
