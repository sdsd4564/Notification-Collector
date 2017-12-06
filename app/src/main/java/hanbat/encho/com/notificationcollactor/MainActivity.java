package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.Set;

import hanbat.encho.com.notificationcollactor.AppListDialog.AppInfoDialog;
import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    ArrayList<String> confirmedApps = new ArrayList<>();
    ArrayList<NotificationGroup> testGroups;
    PackageManager pm;
    private NotificationAdapter mAdapter;
    private DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pm = Application.getAppContext().getPackageManager();

        if (!isPermissionAllowed()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            Toast mToast = new Toast(Application.getAppContext());
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            LayoutInflater mInflater = (LayoutInflater) Application.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.permission_notice_toast, null);
            mToast.setView(view);
            mToast.show();
            this.finishAffinity();
        } else {
            mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            mainBinding.setMain(this);
            setTitle("알림 내용");

            AdRequest adRequest = new AdRequest.Builder().build();
            mainBinding.adView.loadAd(adRequest);

            db = DBhelper.getInstance();
            for (String str : PreferenceManager.getInstance().getStringArrayPref(this, "Packages")) {
                confirmedApps.add(str.split(",")[0]);
            }

            if (confirmedApps.isEmpty()) {
                startActivityForResult(new Intent(this, AppInfoDialog.class), 123);
            }

            int validatedNumber = PreferenceManager.getInstance().getIntegerPref(this, "Validate");
            testGroups = validatedNumber == 61
                    ? db.getAllNotifications()
                    : db.deleteNotificationsExceededValidate(validatedNumber);

            mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            mAdapter = new NotificationAdapter(testGroups, MainActivity.this, mainBinding);
            mAdapter.setHasStableIds(true);
            mainBinding.recyclerview.setAdapter(mAdapter);

            mainBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testGroups = db.getAllNotifications();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setGroups(testGroups);
                                    mainBinding.swipeLayout.setRefreshing(false);
                                }
                            });
                        }
                    }).start();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                testGroups = db.getAllNotifications();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setGroups(testGroups);
                        mainBinding.swipeLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 허용 앱 설정
            case R.id.permitted_apps:
                onCheckConfirmedApps();
                break;

            // 현재 알림 수집
            case R.id.collect_notification:
                onCheckActiveNotification();
                break;

            // 설정
            case R.id.setting_menu:
                startActivityForResult(new Intent(this, SettingActivity.class), SettingActivity.SETTING_FINISHED);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppInfoDialog.APP_LIST_CONFIRMED:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            confirmedApps.clear();
                            for (String s : PreferenceManager.getInstance().getStringArrayPref(MainActivity.this, "Packages")) {
                                confirmedApps.add(s.split(",")[0]);
                            }
                            final ArrayList<NotificationGroup> groups = db.getAllNotifications();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setGroups(groups);
                                }
                            });
                        }
                    }).start();
                    break;
                case SettingActivity.SETTING_FINISHED:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int validatedNumber = PreferenceManager.getInstance().getIntegerPref(MainActivity.this, "Validate");
                            final ArrayList<NotificationGroup> groups = validatedNumber == 61
                                    ? db.getAllNotifications()
                                    : db.deleteNotificationsExceededValidate(validatedNumber);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setGroups(groups);
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    }

    private boolean isPermissionAllowed() {
        Set<String> notiListerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();

        for (String packageName : notiListerSet) {
            if (packageName == null) continue;
            if (packageName.equals(myPackageName)) return true;
        }

        return false;
    }

    public void onCheckActiveNotification() {
        try {
            for (StatusBarNotification sbn : NotificationListener.mNotificationListenerService.getActiveNotifications()) {
                Notification mNotification = sbn.getNotification();
                Bundle extras = mNotification.extras;

                ApplicationInfo app = null;


                if (confirmedApps.contains(sbn.getPackageName())) {

                    for (String _key : extras.keySet()) {
                        if (extras.get(_key) instanceof ApplicationInfo) {
                            app = (ApplicationInfo) extras.get(_key);
                            break;
                        }
                    }

                    String title = String.valueOf(extras.get(Notification.EXTRA_TITLE));
                    CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT) != null ? extras.getCharSequence(Notification.EXTRA_TEXT) : "";
                    CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
                    int smallIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
                    int color = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                            ? mNotification.color
                            : -1;
                    Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
                    long postTime = sbn.getPostTime();
                    String packageName = sbn.getPackageName();

                    CharSequence appName = pm.getApplicationLabel(app);

                    NotificationObject obj = new NotificationObject(title, smallIcon, color, largeIcon, text, subText, postTime, packageName, appName);

                    if (text.length() > 0 && title.length() > 0) {
                        db.addNotification(obj);
                    }
                }
            }
            mAdapter.setGroups(db.getAllNotifications());
            Toast.makeText(this, "현재 띄워져있는 알림을 수집했습니다", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(this, "수집 가능한 알림이 없습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    public void onCheckConfirmedApps() {
        Intent toDialog = new Intent(this, AppInfoDialog.class);
        startActivityForResult(toDialog, AppInfoDialog.APP_LIST_CONFIRMED);
    }
}
