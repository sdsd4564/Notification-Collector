package hanbat.encho.com.notificationcollactor;

import android.content.Intent;
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

        if (!isPermissionAllowed()) {
            Toast.makeText(this, R.string.permission_check_message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        ArrayList<String> confirmedApps = PreferenceManager.getInstance().getStringArrayPref(this, "Packages");
        if (confirmedApps.isEmpty()) {
            Intent toDialog = new Intent(this, AppInfoDialog.class);
            startActivityForResult(toDialog, 123);
        } else
            Toast.makeText(this, "선택된 앱 갯수 : " + confirmedApps.size(), Toast.LENGTH_SHORT).show();




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
                ArrayList<String> selectedApps = PreferenceManager.getInstance().getStringArrayPref(this, "Packages");
                Toast.makeText(MainActivity.this, "선택된 어플 갯수 : " + selectedApps.size(), Toast.LENGTH_SHORT).show();
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
    }

    public void onDeleteTouched(View view) {
        mAdapter.setList(db.dropAllNotifications());
        mAdapter.notifyDataSetChanged();
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
}
