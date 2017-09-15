package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.Set;

import hanbat.encho.com.notificationcollactor.AppListDialog.AppInfoDialog;
import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    NotificationAdapter notificationAdapter;
    ArrayList<String> confirmedApps = new ArrayList<>();
    ArrayList<NotificationGroup> groups;
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

            SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
            snapHelper.attachToRecyclerView(mainBinding.recyclerview);

            db = DBhelper.getInstance();
            for (String str : PreferenceManager.getInstance().getStringArrayPref(this, "Packages")) {
                confirmedApps.add(str.split(",")[0]);
            }

            if (confirmedApps.isEmpty()) {
                startActivityForResult(new Intent(this, AppInfoDialog.class), 123);
            }

            groups = Application.getGroupNotifications(db.getAllNotifications());

            mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            notificationAdapter = new NotificationAdapter(groups, MainActivity.this);
            mainBinding.recyclerview.setAdapter(notificationAdapter);

            mainBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            groups = Application.getGroupNotifications(db.getAllNotifications());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notificationAdapter.setGroups(groups);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    groups = Application.getGroupNotifications(db.getAllNotifications());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificationAdapter.setGroups(groups);
                        }
                    });
                }
            }).start();
        }
    }

    public void onCheckActiveNotification(View view) {
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

    public void onCheckConfirmedApps(View view) {
        Intent toDialog = new Intent(this, AppInfoDialog.class);
        startActivityForResult(toDialog, 123);
    }

    public void notificationBuild(View view) {
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.permission_check_message))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification))
                .setAutoCancel(false);

        NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.notify(151, mBuilder.build());
    }
}
