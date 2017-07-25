package hanbat.encho.com.notificationcollactor;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hanbat.encho.com.notificationcollactor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    private DBhelper db;
    MainListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setMain(this);

        Set<PackageInfo> selectedApp = new ArraySet<>();
        //todo 앱 실행시 걸러낼 어플




        if (!isPermissionAllowed()) {
            Toast.makeText(this, R.string.permission_check_message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        db = new DBhelper(this, "NC", null, 1);

        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<NotificationObject> items = db.getAllNotifications();
        mAdapter = new MainListAdapter(items);
        mainBinding.recyclerview.setAdapter(mAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {

        }
    }

    public void onRefreshTouched(View view) {
        mAdapter.setList(db.getAllNotifications());
        mAdapter.notifyDataSetChanged();
        Log.w("hanlog list size", mAdapter.getItemCount() + "");
            Intent toDialog = new Intent(this, AppInfoDialog.class);
            ArrayList<ApplicationInfo> apps = getPackageList();
            toDialog.putParcelableArrayListExtra("apps", apps);
            startActivityForResult(toDialog, 123);
    }

    public void onDeleteTouched(View view) {
        mAdapter.setList(db.dropAllNotifications());
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<ApplicationInfo> getPackageList() {
        final PackageManager pm = Application.getAppContext().getPackageManager();
        ArrayList<ApplicationInfo> data = new ArrayList<>();
        List<ApplicationInfo> packs = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : packs) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
            if (intent != null) {
                data.add(appInfo);
            }
        }

        return data;
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
}
