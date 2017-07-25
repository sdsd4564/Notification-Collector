package hanbat.encho.com.notificationcollactor;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Encho on 2017-07-25.
 */

public class AppInfoDialog extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<ApplicationInfo> items = getIntent().getParcelableArrayListExtra("apps");
        Toast.makeText(this, ":: " + items.size(), Toast.LENGTH_SHORT).show();
    }
}
