package hanbat.encho.com.notificationcollactor;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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

        if (!isPermissionAllowed()) {
            Toast.makeText(this, R.string.permission_check_message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        db = new DBhelper(this, "NC", null, 1);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<NotificationObject> items = db.getAllNotifications();
        mAdapter = new MainListAdapter(items);
        mainBinding.recyclerview.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("hanlog", mAdapter.getItemCount() + "");
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
