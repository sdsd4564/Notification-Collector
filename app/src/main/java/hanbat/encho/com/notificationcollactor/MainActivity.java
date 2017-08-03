package hanbat.encho.com.notificationcollactor;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
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


//        Set<PackageInfo> selectedApp = new ArraySet<>();
        ArrayList<String> selectedApps = new PreferenceManager().getStringArrayPref(this, "Packages");
        Toast.makeText(this, selectedApps.isEmpty() ? "0" : selectedApps.size() + "", Toast.LENGTH_SHORT).show();
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

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                if (data.getParcelableArrayListExtra("apps").get(0) instanceof ApplicationInfo) {
                    Log.d("hanlog item check", "어플리케이션 인포");
                } else if (data.getParcelableArrayListExtra("apps").get(0) instanceof AppInfo) {
                    Log.w("hanlog item check : ", "앱 인포");
                }
//                ArrayList<AppInfo> selectedApps = data.getParcelableArrayListExtra("apps");
//                int size = selectedApps.size();
//                Toast.makeText(this, size + "", Toast.LENGTH_SHORT).show();
//                Log.d("hanlog item check", selectedApps.get(0).getName());
//                PreferenceManager manager = new PreferenceManager();
//                ArrayList<String> selectedAppNames = new ArrayList<>();
//                for (AppInfo app : selectedApps) {
//                    selectedAppNames.add(app.getPackageName());
//                }
//                manager.setStringArrayPref(this, "Packages", selectedAppNames);
            }
        }
    }

    public void onRefreshTouched(View view) {
        mAdapter.setList(db.getAllNotifications());
        mAdapter.notifyDataSetChanged();
        Intent toDialog = new Intent(this, AppInfoDialog.class);
        startActivityForResult(toDialog, 123);
    }

    public void onDeleteTouched(View view) {
        mAdapter.setList(db.dropAllNotifications());
        mAdapter.notifyDataSetChanged();
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
