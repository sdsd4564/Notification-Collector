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

        db = new DBhelper(this, "NC", null, 1);

        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<NotificationObject> items = db.getAllNotifications();
        mAdapter = new MainListAdapter(items);
        mainBinding.recyclerview.setAdapter(mAdapter);

    }

    public void onRefreshTouched(View view) {
        mAdapter.setList(db.getAllNotifications());
        mAdapter.notifyDataSetChanged();
        Log.w("hanlog", mAdapter.getItemCount() + "");
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
