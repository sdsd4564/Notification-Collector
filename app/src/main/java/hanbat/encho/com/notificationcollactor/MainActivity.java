package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
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
import hanbat.encho.com.notificationcollactor.Model.NotiTest;
import hanbat.encho.com.notificationcollactor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    TestAdapter testAdapter;
    ArrayList<String> confirmedApps;
    private DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isPermissionAllowed()) {
            Toast.makeText(this, R.string.permission_check_message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            this.finishAffinity();
        } else {
            mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            mainBinding.setMain(this);

            SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
            snapHelper.attachToRecyclerView(mainBinding.recyclerview);

            db = DBhelper.getInstance();
            confirmedApps = PreferenceManager.getInstance().getStringArrayPref(this, "Packages");
            ArrayList<NotiTest> groups = Application.getGroupNotifications(db.getAllNotifications());

            if (confirmedApps.isEmpty()) {
                startActivityForResult(new Intent(this, AppInfoDialog.class), 123);
            } else {
                Toast.makeText(this, "선택된 앱 갯수 : " + confirmedApps.size(), Toast.LENGTH_SHORT).show();
            }

            testAdapter = new TestAdapter(groups, this);
            mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
            mainBinding.recyclerview.setAdapter(testAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        testAdapter.updateGroupItem(Application.getGroupNotifications(db.getAllNotifications()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> selectedApps = PreferenceManager.getInstance().getStringArrayPref(this, "Packages");
                Toast.makeText(MainActivity.this, "선택된 어플 갯수 : " + selectedApps.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        testAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        testAdapter.onRestoreInstanceState(savedInstanceState);
    }

    public void onRefreshTouched(View view) {
//        testAdapter.updateGroupItem(Application.getGroupNotifications(db.getAllNotifications()));
    }

    public void onDeleteTouched(View view) {
//        testAdapter.updateGroupItem(Application.getGroupNotifications(db.dropAllNotifications()));
    }

    public void onCheckConfirmedApps(View view) {
        Intent toDialog = new Intent(this, AppInfoDialog.class);
        startActivityForResult(toDialog, 123);
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
