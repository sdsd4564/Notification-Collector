package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.Set;

import hanbat.encho.com.notificationcollactor.AppListDialog.AppInfoDialog;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.Model.TestGroup;
import hanbat.encho.com.notificationcollactor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    TestAdapter mAdapter;
    ArrayList<String> confirmedApps = new ArrayList<>();
    ArrayList<TestGroup> testGroups;
    PackageManager pm;
    private DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pm = Application.getAppContext().getPackageManager();

        if (!isPermissionAllowed()) {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            Toast.makeText(this, R.string.permission_check_message, Toast.LENGTH_LONG).show();
            this.finishAffinity();
        } else {
            mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            mainBinding.setMain(this);

            AdRequest adRequest = new AdRequest.Builder().build();
            mainBinding.adView.loadAd(adRequest);

            db = DBhelper.getInstance();
            for (String str : PreferenceManager.getInstance().getStringArrayPref(this, "Packages")) {
                confirmedApps.add(str.split(",")[0]);
            }

            if (confirmedApps.isEmpty()) {
                startActivityForResult(new Intent(this, AppInfoDialog.class), 123);
            }

//            groups = Application.getGroupNotifications(db.getAllNotifications());
            testGroups = Application.getTestGroups(db.getAllNotifications());

            mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            mAdapter = new TestAdapter(testGroups, MainActivity.this);
            mAdapter.setHasStableIds(true);
            mainBinding.recyclerview.setAdapter(mAdapter);
            mainBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testGroups = Application.getTestGroups(db.getAllNotifications());
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
                testGroups = Application.getTestGroups(db.getAllNotifications());
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
            case R.id.permitted_apps: {
                onCheckConfirmedApps();
                break;
            }
            case R.id.collect_notification: {
                onCheckActiveNotification();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    confirmedApps.clear();
                    for (String s : PreferenceManager.getInstance().getStringArrayPref(MainActivity.this, "Packages")) {
                        confirmedApps.add(s.split(",")[0]);
                    }
                    final ArrayList<TestGroup> groups = Application.getTestGroups(db.getAllNotifications());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setGroups(groups);
                        }
                    });
                }
            }).start();
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
                Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
                long postTime = sbn.getPostTime();
                String packageName = sbn.getPackageName();

                CharSequence appName = pm.getApplicationLabel(app);

                NotificationObject obj = new NotificationObject(title, largeIcon, text, subText, postTime, packageName, appName);

                if (text.length() > 0 && title.length() > 0) {
                    db.addNotification(obj);
                }
            }
        }
        mAdapter.setGroups(Application.getTestGroups(db.getAllNotifications()));
        Toast.makeText(this, "현재 띄워져있는 알림을 수집했습니다", Toast.LENGTH_SHORT).show();
    }

    public void onCheckConfirmedApps() {
        Intent toDialog = new Intent(this, AppInfoDialog.class);
        startActivityForResult(toDialog, 123);
    }
}
