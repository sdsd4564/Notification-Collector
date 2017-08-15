package hanbat.encho.com.notificationcollactor.AppListDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hanbat.encho.com.notificationcollactor.AppListDialog.Adapter.DialogConfirmListAdapter;
import hanbat.encho.com.notificationcollactor.AppListDialog.Adapter.DialogListAdapter;
import hanbat.encho.com.notificationcollactor.Application;
import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.PreferenceManager;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.CheckAllowedApplistBinding;

/**
 * Created by Encho on 2017-07-25.
 */

public class AppInfoDialog extends Activity {

    CheckAllowedApplistBinding binding;
    PackageManager pm;
    ArrayList<AppInfo> items;
    ArrayList<AppInfo> confirmedApps;
    ProgressDialog loadingDialog;
    Handler handler;
    DialogListAdapter mAdapter;
    DialogConfirmListAdapter mConfirmedAdapter;
    AscPackages sort;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.check_allowed_applist);
        binding.setDialog(this);
        pm = Application.getAppContext().getPackageManager();
        sort = new AscPackages();

        loadingDialog = new ProgressDialog(this, R.style.MyTheme);
        loadingDialog.setCancelable(false);
        loadingDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loadingDialog.show();

        getWindow().getAttributes().width = (int) (Application.getAppContext().getResources().getDisplayMetrics().widthPixels);
        getWindow().getAttributes().height = (int) (Application.getAppContext().getResources().getDisplayMetrics().heightPixels * 0.9);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                loadingDialog.dismiss();

                confirmedApps = new ArrayList<>();
                ArrayList<String> packages = PreferenceManager.getInstance().getStringArrayPref(AppInfoDialog.this, "Packages");

                //설치된 앱 리스트에서 승인된 앱 리스트에 추가///////////////
                for (AppInfo appInfo : items)
                    if (packages.contains(appInfo.getPackageName()))
                        confirmedApps.add(appInfo);
                //설치된 앱 리스트에서 승인앱 제외////////////////////////
                for (AppInfo app : confirmedApps)
                    items.remove(app);
                ////////////////////////////////////////////////////

                mAdapter = new DialogListAdapter(AppInfoDialog.this, items);
                mConfirmedAdapter = new DialogConfirmListAdapter(confirmedApps, AppInfoDialog.this);
                binding.setConfirmAdapter(mConfirmedAdapter);
                binding.dialogConfirmedApp.setLayoutManager(new GridLayoutManager(AppInfoDialog.this, 4));
                binding.dialogConfirmedApp.setAdapter(mConfirmedAdapter);
                binding.dialogApplist.setLayoutManager(new GridLayoutManager(AppInfoDialog.this, 4));
                binding.dialogApplist.setAdapter(mAdapter);

                mAdapter.setmOnMyItemCheckedChanged(new DialogListAdapter.OnMyItemCheckedChanged() {
                    @Override
                    public void onItemCheckedChanged(AppInfo app, int position) {
                        confirmedApps.add(app);
                        Collections.sort(confirmedApps, sort);
                        items.remove(app);
                        mAdapter.updateList(items, position, DialogListAdapter.NOTIFY_REMOVE);
                        mConfirmedAdapter.updateList(confirmedApps, position, DialogConfirmListAdapter.NOTIFY_INSERT);
                    }
                });

                mConfirmedAdapter.setmOnMyItemCheckedChange(new DialogConfirmListAdapter.OnMyItemCheckedChange() {
                    @Override
                    public void onItemCheckedChange(AppInfo appInfo, int position) {
                        items.add(appInfo);
                        Collections.sort(items, sort);
                        confirmedApps.remove(appInfo);
                        mAdapter.updateList(items, position, DialogListAdapter.NOTIFY_INSERT);
                        mConfirmedAdapter.updateList(confirmedApps, position, DialogConfirmListAdapter.NOTIFY_REMOVE);
                    }
                });
            }
        };
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                items = getPackageList();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    // 기기에 저장된 어플 리스트를 불러온다
    private ArrayList<AppInfo> getPackageList() {
        ArrayList<AppInfo> data = new ArrayList<>();
        List<ApplicationInfo> packs = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : packs) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
            if (intent != null) {
                AppInfo item = new AppInfo(appInfo.loadIcon(pm),
                        appInfo.loadLabel(pm).toString(),
                        appInfo.packageName);
                data.add(item);
            }
        }

        Collections.sort(data, sort);

        return data;
    }

    private class AscPackages implements Comparator<AppInfo> {

        @Override
        public int compare(AppInfo appInfo, AppInfo t1) {
            return appInfo.getName().compareTo(t1.getName());
        }
    }
}
