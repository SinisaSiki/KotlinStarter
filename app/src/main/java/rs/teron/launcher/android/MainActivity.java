package rs.teron.launcher.android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.device.DeviceManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.android.otainstaller.IInstall;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import rs.teron.launcher.android.CheckForUpdates;
import rs.teron.launcher.android.DownloadTask;
import rs.teron.launcher.android.MainActivity;
import rs.teron.launcher.android.model.Update;
import rs.teron.launcher.android.model.Updates;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    private static final String[] CustomApps = {"com.itsteam.bus_ticketing.android", "rs.lasta.prodaja.lastasplash", "rs.galeb.prodaja"};
    private static final String DEFAULT_PIN = "361975";
    private static final String PREFERENCES_BUILD_TYPE = "BuildType";
    private static final String PREFERENCES_NAME = "Preferences";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private Button btnDownload;
    private Button btnEsir;
    private Button btnLpfr;
    private ProgressBar pbCheckingForUpdates;
    private TextView tvInternetNotAvailable;
    private Updates updates;
    private IInstall datecsInstall = null;
    private ServiceConnection connection = new ServiceConnection() { // from class: rs.teron.launcher.android.MainActivity.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainActivity.this.datecsInstall = IInstall.Stub.asInterface(iBinder);
            MainActivity.this.showSerialNumber();
            MainActivity.this.checkForUpdates();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            MainActivity.this.datecsInstall = null;
        }
    };

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (isDatecs()) {
            bindService(new Intent("com.android.otainstaller.InstallService").setPackage("com.android.otainstaller"), this.connection, Context.BIND_AUTO_CREATE);
        }
        this.updates = new Updates();
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        ImageView imageView = (ImageView) findViewById(R.id.imgLogo);
        if (!isBundledLpfrEsirDevice() || isGaleb() || isDatecs()) {
            imageView.setOnLongClickListener(new View.OnLongClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return MainActivity.this.m26lambda$onCreate$0$rsteronlauncherandroidMainActivity(view);
                }
            });
        }
        imageView.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m27lambda$onCreate$1$rsteronlauncherandroidMainActivity(view);
            }
        });
        showSerialNumber();
        TextView textView = (TextView) findViewById(R.id.tvFirmwareVersion);
        this.btnEsir = (Button) findViewById(R.id.btnEsir);
        this.btnLpfr = (Button) findViewById(R.id.btnLpfr);
        ((TextView) findViewById(R.id.tvVersionCode)).setText("Build: 22110301");
        textView.setText("Firmware version: V" + getFirmwareVersion() + "_" + Build.DISPLAY);
        ((TextView) findViewById(R.id.tvSerialNumber)).setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m28lambda$onCreate$2$rsteronlauncherandroidMainActivity(view);
            }
        });
        if (!isBundledLpfrEsirDevice()) {
            Button button = this.btnEsir;
            button.setText(getBrand() + " ЕСИР");
            this.btnEsir.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda14
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.this.m29lambda$onCreate$3$rsteronlauncherandroidMainActivity(view);
                }
            });
            Button button2 = this.btnLpfr;
            button2.setText(getBrand() + " ЛПФР");
            this.btnLpfr.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda15
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.this.m30lambda$onCreate$4$rsteronlauncherandroidMainActivity(view);
                }
            });
        } else {
            textView.setVisibility(View.GONE);
            Button button3 = this.btnEsir;
            button3.setText(getBrand() + " ЛПФР+ЕСИР");
            this.btnEsir.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda16
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.this.m31lambda$onCreate$5$rsteronlauncherandroidMainActivity(view);
                }
            });
        }
        Button button4 = (Button) findViewById(R.id.btnDownload);
        this.btnDownload = button4;
        button4.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m32lambda$onCreate$6$rsteronlauncherandroidMainActivity(view);
            }
        });
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbCheckingForUpdates);
        this.pbCheckingForUpdates = progressBar;
        progressBar.setVisibility(View.GONE);
        TextView textView2 = (TextView) findViewById(R.id.tvInternetNotAvailable);
        this.tvInternetNotAvailable = textView2;
        textView2.setVisibility(View.GONE);
        this.tvInternetNotAvailable.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m33lambda$onCreate$7$rsteronlauncherandroidMainActivity(view);
            }
        });
        registerNetworkChanges();
        setDefaultSettings(false);
        if (askForStoragePermissionIfNeeded()) {
            cleanDownloadedUpdates();
        }
    }

    /* renamed from: lambda$onCreate$0$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ boolean m26lambda$onCreate$0$rsteronlauncherandroidMainActivity(View view) {
        showMenu();
        return true;
    }

    /* renamed from: lambda$onCreate$1$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m27lambda$onCreate$1$rsteronlauncherandroidMainActivity(View view) {
        checkForUpdates();
    }

    /* renamed from: lambda$onCreate$2$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m28lambda$onCreate$2$rsteronlauncherandroidMainActivity(View view) {
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Serijski broj", getSerialNumberPretty()));
        Toast.makeText(this, "Серијски број је копиран", Toast.LENGTH_LONG).show();
    }

    /* renamed from: lambda$onCreate$3$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m29lambda$onCreate$3$rsteronlauncherandroidMainActivity(View view) {
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("rs.teron.esir.android");
        if (launchIntentForPackage != null) {
            startActivity(launchIntentForPackage);
            return;
        }
        Toast.makeText(this, getBrand() + " ЕСИР није инсталиран", Toast.LENGTH_LONG).show();
    }

    /* renamed from: lambda$onCreate$4$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m30lambda$onCreate$4$rsteronlauncherandroidMainActivity(View view) {
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("rs.teron.lpfr.android");
        if (launchIntentForPackage != null) {
            startActivity(launchIntentForPackage);
            return;
        }
        Toast.makeText(this, getBrand() + " ЛПФР није инсталиран", Toast.LENGTH_LONG).show();
    }

    /* renamed from: lambda$onCreate$5$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m31lambda$onCreate$5$rsteronlauncherandroidMainActivity(View view) {
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("rs.teron.esir.android");
        if (launchIntentForPackage != null) {
            startActivity(launchIntentForPackage);
            return;
        }
        Toast.makeText(this, getBrand() + " ЕСИР није инсталиран", Toast.LENGTH_LONG).show();
    }

    /* renamed from: lambda$onCreate$6$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m32lambda$onCreate$6$rsteronlauncherandroidMainActivity(View view) {
        performUpdate();
    }

    /* renamed from: lambda$onCreate$7$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m33lambda$onCreate$7$rsteronlauncherandroidMainActivity(View view) {
        checkForUpdates();
    }

    private void registerNetworkChanges() {
        ((ConnectivityManager) getSystemService(ConnectivityManager.class)).
                requestNetwork(new NetworkRequest.Builder().
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI).
                addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build(), new AnonymousClass2());
    }

    /* renamed from: rs.teron.launcher.android.MainActivity$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends ConnectivityManager.NetworkCallback {
        AnonymousClass2() {
            //TODO sta je ovo r1
//            MainActivity.this = r1;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            System.out.println("Network available");
            super.onAvailable(network);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: rs.teron.launcher.android.MainActivity$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.AnonymousClass2.this.m39lambda$onAvailable$0$rsteronlauncherandroidMainActivity$2();
                }
            });
        }

        /* renamed from: lambda$onAvailable$0$rs-teron-launcher-android-MainActivity$2 */
        public /* synthetic */ void m39lambda$onAvailable$0$rsteronlauncherandroidMainActivity$2() {
            MainActivity.this.checkForUpdates();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            System.out.println("Network not available");
            super.onLost(network);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: rs.teron.launcher.android.MainActivity$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.AnonymousClass2.this.m40lambda$onLost$1$rsteronlauncherandroidMainActivity$2();
                }
            });
        }

        /* renamed from: lambda$onLost$1$rs-teron-launcher-android-MainActivity$2 */
        public /* synthetic */ void m40lambda$onLost$1$rsteronlauncherandroidMainActivity$2() {
            MainActivity.this.checkForUpdates();
        }
    }

    public void checkForUpdates() {
        if (getSerialNumber() == null) {
            return;
        }
        this.btnDownload.setVisibility(View.GONE);
        this.tvInternetNotAvailable.setVisibility(View.GONE);
        this.pbCheckingForUpdates.setVisibility(View.VISIBLE);
        try {
            new CheckForUpdates(getPackageManager(), getSerialNumber(), getPackageName(), new CheckForUpdates.Response() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda6
                @Override // rs.teron.launcher.android.CheckForUpdates.Response
                public final void process(Updates updates) {
                    MainActivity.this.m24lambda$checkForUpdates$8$rsteronlauncherandroidMainActivity(updates);
                }
            }).execute(new Void[0]);
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    /* renamed from: lambda$checkForUpdates$8$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m24lambda$checkForUpdates$8$rsteronlauncherandroidMainActivity(Updates updates) {
        this.pbCheckingForUpdates.setVisibility(View.GONE);
        if (updates == null) {
            this.tvInternetNotAvailable.setVisibility(View.VISIBLE);
            this.updates = new Updates();
            return;
        }
        this.updates = updates;
        if (updates.size() <= 0) {
            return;
        }
        Update update = this.updates.get(0);
        if (update.type == Update.Type.Firmware) {
            Button button = this.btnDownload;
            button.setText("Преузми унапређења оперативног система: \n\n" + rebrandString(update.name));
        } else {
            Button button2 = this.btnDownload;
            button2.setText("Преузми нову верзију апликације:\n\n" + rebrandString(update.name));
        }
        this.btnDownload.setVisibility(View.VISIBLE);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        checkTeronApps();
        checkCustomApps();
        checkForUpdates();
    }

    private void performUpdate() {
        if (!askForStoragePermissionIfNeeded()) {
            return;
        }
        try {
            this.btnDownload.setVisibility(View.GONE);
            if (this.updates.isEmpty()) {
                return;
            }
            Update update = this.updates.get(0);
            PrintStream printStream = System.out;
            printStream.println("Updating " + update.name);
            downloadUpdate(update);
        } catch (Exception e) {
            PrintStream printStream2 = System.out;
            printStream2.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    private void downloadUpdate(final Update update) {
        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Преузимање " + rebrandString(update.name));
            new DownloadTask((PowerManager) getSystemService(PowerManager.class), getSerialNumber(), progressDialog, new DownloadTask.Response() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda7
                @Override // rs.teron.launcher.android.DownloadTask.Response
                public final void process(boolean z, String str) {
                    MainActivity.this.m25lambda$downloadUpdate$9$rsteronlauncherandroidMainActivity(update, z, str);
                }
            }).execute(update);
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    /* renamed from: lambda$downloadUpdate$9$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m25lambda$downloadUpdate$9$rsteronlauncherandroidMainActivity(Update update, boolean z, String str) {
        if (!z) {
            checkForUpdates();
        } else if (update.type == Update.Type.Firmware) {
            updateFirmware(str);
        } else {
            updateApplication(str);
        }
    }

    private void updateApplication(String str) {
        if (isDatecs()) {
            Intent intent = new Intent("com.android.otainstaller.intent.action.INSTALL");
            intent.putExtra("com.android.otainstaller.intent.extra.PATH", str);
            startActivity(intent);
            return;
        }
        Uri uriForFile = FileProvider.getUriForFile(this, "rs.teron.launcher.android.provider", new File(str));
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.setData(uriForFile);
        startActivity(intent2);
    }

    private void updateFirmware(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Унапређење оперативног система je спремно");
        builder.setMessage("Током инсталације унапређења оперативног система веома је битно да не угасите уређај као и да батерија буде попуњена 20% или више.\n\nДа ли сте сигурни да желите да наставите?");
        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m37lambda$updateFirmware$10$rsteronlauncherandroidMainActivity(str, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Не", new DialogInterface.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m38lambda$updateFirmware$11$rsteronlauncherandroidMainActivity(dialogInterface, i);
            }
        });
        builder.create().show();
    }

    /* renamed from: lambda$updateFirmware$10$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m37lambda$updateFirmware$10$rsteronlauncherandroidMainActivity(String str, DialogInterface dialogInterface, int i) {
        try {
            dialogInterface.dismiss();
            String str2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/update.zip";
            System.out.println("Copying file " + str + " to " + str2);
            Utils.copyFile(new File(str), new File(str2));
            Intent intent = new Intent("com.telpo.syh.upgradeservice.BROADCAST");
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            sendBroadcast(intent);
        } catch (IOException e) {
            System.out.println("Error while copying firmware update file: " + e);
            Toast.makeText(this, "Грешка током припреме података. Оперативни систем није ажуриран.", Toast.LENGTH_LONG).show();
        }
    }

    /* renamed from: lambda$updateFirmware$11$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m38lambda$updateFirmware$11$rsteronlauncherandroidMainActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        checkForUpdates();
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.imgLogo));
        popupMenu.getMenuInflater().inflate(R.menu.admin, popupMenu.getMenu());
        if (isTelpoTps900()) {
            popupMenu.getMenu().findItem(R.id.adminMenuWirelessSettings).setVisible(false);
        } else if (isUrovoI9100()) {
            popupMenu.getMenu().findItem(R.id.adminMenuDefaultSettings).setVisible(false);
        } else {
            popupMenu.getMenu().findItem(R.id.adminMenuWirelessSettings).setVisible(false);
            popupMenu.getMenu().findItem(R.id.adminMenuDefaultSettings).setVisible(false);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda5
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return MainActivity.this.m34lambda$showMenu$12$rsteronlauncherandroidMainActivity(menuItem);
            }
        });
        popupMenu.show();
    }

    /* renamed from: lambda$showMenu$12$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ boolean m34lambda$showMenu$12$rsteronlauncherandroidMainActivity(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.adminMenuDefaultSettings /* 2131230791 */:
                setDefaultSettings(true);
                Toast.makeText(getBaseContext(), "Примењена стандардна подешавања уређаја", Toast.LENGTH_LONG).show();
                return true;
            case R.id.adminMenuSystemSettings /* 2131230792 */:
                showSettings();
                return true;
            case R.id.adminMenuWirelessSettings /* 2131230793 */:
                showWirelessSettings();
                return true;
            default:
                return false;
        }
    }

    private void showWirelessSettings() {
        startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
    }

    private void showSettings() {
        final EditText editText = new EditText(this);
        editText.setInputType(18);
        new AlertDialog.Builder(this).setTitle("Административни приступ").setMessage("Унесите шифру за овај уређај").setView(editText).setPositiveButton("Прихвати", new DialogInterface.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m35lambda$showSettings$13$rsteronlauncherandroidMainActivity(editText, dialogInterface, i);
            }
        }).setNegativeButton("Одустани", new DialogInterface.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m36lambda$showSettings$14$rsteronlauncherandroidMainActivity(dialogInterface, i);
            }
        }).show();
        editText.requestFocus();
    }

    /* renamed from: lambda$showSettings$13$rs-teron-launcher-android-MainActivity */
    @SuppressLint("WrongConstant")
    public /* synthetic */ void m35lambda$showSettings$13$rsteronlauncherandroidMainActivity(EditText editText, DialogInterface dialogInterface, int i) {
        if (checkPin(editText.getText().toString())) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
            intent.setFlags(335544320);
            startActivity(intent);
        }
        checkForUpdates();
    }

    /* renamed from: lambda$showSettings$14$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m36lambda$showSettings$14$rsteronlauncherandroidMainActivity(DialogInterface dialogInterface, int i) {
        checkForUpdates();
    }

    private boolean checkPin(String str) {
        return str.equals(getPinForDevice()) || str.equals(getMasterPin()) || str.equals(getMonthPin()) || str.equals(getDayPin());
    }

    private String getPinForDevice() {
        try {
            String byteArrayToHexString = Utils.byteArrayToHexString(MessageDigest.getInstance("SHA-512").digest(getSerialNumber().getBytes()));
            return new String(new char[]{hexToDigit(byteArrayToHexString.charAt(3)), hexToDigit(byteArrayToHexString.charAt(6)), hexToDigit(byteArrayToHexString.charAt(1)), hexToDigit(byteArrayToHexString.charAt(9)), hexToDigit(byteArrayToHexString.charAt(7)), hexToDigit(byteArrayToHexString.charAt(5))});
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_PIN;
        }
    }

    private String getDatePin(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            String byteArrayToHexString = Utils.byteArrayToHexString(messageDigest.digest(("361975361975" + new SimpleDateFormat(str).format(new Date())).getBytes()));
            return new String(new char[]{hexToDigit(byteArrayToHexString.charAt(3)), hexToDigit(byteArrayToHexString.charAt(6)), hexToDigit(byteArrayToHexString.charAt(1)), hexToDigit(byteArrayToHexString.charAt(9)), hexToDigit(byteArrayToHexString.charAt(7)), hexToDigit(byteArrayToHexString.charAt(5))});
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_PIN;
        }
    }

    private String getDayPin() {
        return getDatePin("YYYYMMdd");
    }

    private String getMonthPin() {
        return getDatePin("YYYYMM");
    }

    private String getMasterPin() {
        try {
            String byteArrayToHexString = Utils.byteArrayToHexString(MessageDigest.getInstance("SHA-512").digest("361975361975".getBytes()));
            return new String(new char[]{hexToDigit(byteArrayToHexString.charAt(3)), hexToDigit(byteArrayToHexString.charAt(6)), hexToDigit(byteArrayToHexString.charAt(1)), hexToDigit(byteArrayToHexString.charAt(9)), hexToDigit(byteArrayToHexString.charAt(7)), hexToDigit(byteArrayToHexString.charAt(5))});
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_PIN;
        }
    }

    private char hexToDigit(char c) throws Exception {
        if (c < '0' || c > '9') {
            char c2 = 'A';
            if (c < 'A' || c > 'Z') {
                c2 = 'a';
                if (c < 'a' || c > 'z') {
                    throw new Exception("Invalid hex character '" + c + "'");
                }
            }
            return (char) ((c - c2) + 48);
        }
        return c;
    }

    private String getSerialNumber() {
        if (isTelpoTps900()) {
            return getSerialNumberTelpoTps900();
        }
        if (isUrovoI9100()) {
            return getSerialUrovoI9100();
        }
        if (isIMin()) {
            return getSerialIMin();
        }
        if (isSunmi()) {
            return getSerialSunmi();
        }
        if (isSp101()) {
            return getSerialSp101();
        }
        if (isDatecs()) {
            return getSerialDatecs();
        }
        return getSerialGeneric();
    }

    private String getSerialNumberPretty() {
        try {
            String serialNumber = getSerialNumber();
            if (serialNumber == null) {
                throw new Exception("Invalid serial number (null)");
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < serialNumber.length()) {
                if (sb.length() > 0) {
                    sb.append('-');
                }
                int i2 = i + 4;
                sb.append(serialNumber.substring(i, Math.min(i2, serialNumber.length())));
                i = i2;
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "?";
        }
    }

    private String getSerialNumberTelpoTps900() {
        return Utils.getSystemProperty("ro.serialno");
    }

    private String getSerialUrovoI9100() {
        return new DeviceManager().getDeviceId();
    }

    public String getSerialIMin() {
        if (Build.VERSION.SDK_INT >= 26) {
            return Settings.Secure.getString(getContentResolver(), "android_id").toUpperCase();
        }
        return Build.SERIAL;
    }

    private String getSerialSunmi() {
        if (Build.VERSION.SDK_INT >= 26) {
            return Settings.Secure.getString(getContentResolver(), "android_id").toUpperCase();
        }
        return Build.SERIAL;
    }

    private String getSerialSp101() {
        return Build.SERIAL;
    }

    private String getSerialDatecs() {
        try {
            IInstall iInstall = this.datecsInstall;
            if (iInstall == null) {
                return null;
            }
            return iInstall.getSerialNumber();
        } catch (RemoteException unused) {
            return null;
        }
    }

    private String getSerialGeneric() {
        return Settings.Secure.getString(getContentResolver(), "android_id").toUpperCase();
    }

    private String getFirmwareVersion() {
        return Utils.getSystemProperty("ro.product.version");
    }

    private boolean askForStoragePermissionIfNeeded() {
        if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Неопходна додатна права за рад апликације");
        builder.setMessage("Да би унапређенје апликација било могуће неопходно је да на наредном екрану дозволите овој апликацији додатна права.");
        builder.setCancelable(false);
        builder.setPositiveButton("У реду", new DialogInterface.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.m22xd4007b65(dialogInterface, i);
            }
        });
        builder.create().show();
        return false;
    }

    /* renamed from: lambda$askForStoragePermissionIfNeeded$15$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m22xd4007b65(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    private void setDefaultSettings(boolean z) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_NAME, 0);
        if (z || !BuildConfig.BUILD_TYPE.equals(sharedPreferences.getString(PREFERENCES_BUILD_TYPE, null))) {
            if (isTelpoTps900()) {
                System.out.println("Applying default settings");
                Configurator.setScreenBrightness(this, Configurator.SCREEN_BRIGHTNESS_83P);
                Configurator.setScreenTimeout(this, Configurator.SCREEN_TIMEOUT_30_MIN);
                Configurator.setScreenLock(this, "off");
                Configurator.setTouchKey(this, Configurator.TOUCH_KEY_BACK_KEY, "on");
                Configurator.setTouchKey(this, Configurator.TOUCH_KEY_HOME_KEY, "on");
                Configurator.setTouchKey(this, Configurator.TOUCH_KEY_RECENT_KEY, "on");
                Configurator.setFunctionKeyEnabled(this, "off");
                Configurator.setLanguageList(this, "sr-Latn-RS,sr-Cyrl-RS");
                Configurator.setLanguage(this, "sr-Latn-RS");
                Configurator.setLauncherApplication(this, "rs.teron.launcher.android/MainActivity");
                String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/splash.img";
                try {
                    InputStream openRawResource = getResources().openRawResource(R.raw.boot);
                    FileOutputStream fileOutputStream = new FileOutputStream(str);
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = openRawResource.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileOutputStream.close();
                        Configurator.setDeviceLogo(this, str);
                        fileOutputStream.close();
                        if (openRawResource != null) {
                            openRawResource.close();
                        }
                    } catch (Throwable th) {
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sharedPreferences.edit().putString(PREFERENCES_BUILD_TYPE, BuildConfig.BUILD_TYPE).apply();
        }
    }

    private void checkTeronApps() {
        if (isBundledLpfrEsirDevice()) {
            this.btnEsir.setVisibility(getPackageManager().getLaunchIntentForPackage("rs.teron.esir.android") != null ? View.VISIBLE : View.GONE);
            this.btnLpfr.setVisibility(View.GONE);
        }
    }

    private void checkCustomApps() {
        String[] strArr;
        Button button = (Button) findViewById(R.id.btnCustom);
        button.setVisibility(View.GONE);
        for (String str : CustomApps) {
            final Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(str);
            if (launchIntentForPackage != null) {
                System.out.println("Found custom app: " + str);
                PackageManager packageManager = getPackageManager();
                try {
                    str = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, PackageManager.GET_META_DATA));
                } catch (PackageManager.NameNotFoundException unused) {
                }
                button.setText(str);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() { // from class: rs.teron.launcher.android.MainActivity$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        MainActivity.this.m23lambda$checkCustomApps$16$rsteronlauncherandroidMainActivity(launchIntentForPackage, view);
                    }
                });
                return;
            }
        }
    }

    /* renamed from: lambda$checkCustomApps$16$rs-teron-launcher-android-MainActivity */
    public /* synthetic */ void m23lambda$checkCustomApps$16$rsteronlauncherandroidMainActivity(Intent intent, View view) {
        startActivity(intent);
    }

    private void cleanDownloadedUpdates() {
        File[] listFiles;
        for (File file : new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()).listFiles()) {
            if (file.getName().startsWith("rs-teron-") && file.getName().endsWith(".apk")) {
                System.out.println("Removing file " + file.getName());
                file.delete();
            }
        }
    }

    public void showSerialNumber() {
        TextView textView = (TextView) findViewById(R.id.tvSerialNumber);
        String serialNumberPretty = getSerialNumberPretty();
        try {
            textView.setText("SN: " + serialNumberPretty);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText("?");
        }
    }

    private boolean isTelpoTps900() {
        return Build.MODEL.equals("TPS900");
    }

    private boolean isUrovoI9100() {
        return Build.MODEL.equals("i9100/W");
    }

    private boolean isSunmi() {
        return Build.MANUFACTURER.equals("SUNMI");
    }

    private boolean isSp101() {
        return Build.PRODUCT.startsWith("SP101");
    }

    private boolean isIMin() {
        return Build.MANUFACTURER.equals("iMin") || Build.MANUFACTURER.equals("rockchip");
    }

    private boolean isDatecs() {
        return Build.MANUFACTURER.equals("Datecs");
    }

    private boolean isGaleb() {
        return getBrand().equals("Галеб");
    }

    private boolean isBundledLpfrEsirDevice() {
        return (!isTelpoTps900() && !isUrovoI9100()) || isGaleb();
    }

    private String getBrand() {
        return getString(R.string.app_brand);
    }

    private String rebrandString(String str) {
        if (isBundledLpfrEsirDevice() && "Терон ЕСИР".equals(str)) {
            str = "Терон ЛПФР+ЕСИР";
        }
        return str.replace("Терон", getBrand());
    }
}
