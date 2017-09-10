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
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

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
    ArrayList<AppInfo> items, itemsClone;
    ArrayList<AppInfo> confirmedApps, confirmedAppsClone;
    ProgressDialog loadingDialog;
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

        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(binding.dialogApplist);

        pm = Application.getAppContext().getPackageManager();
        sort = new AscPackages();

        loadingDialog = new ProgressDialog(this, R.style.MyTheme);
        loadingDialog.setCancelable(false);
        loadingDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loadingDialog.show();

        getWindow().getAttributes().width = (int) (Application.getAppContext().getResources().getDisplayMetrics().widthPixels * 0.9);
        getWindow().getAttributes().height = (int) (Application.getAppContext().getResources().getDisplayMetrics().heightPixels * 0.9);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                items = getPackageList();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                        confirmedApps = new ArrayList<>();
                        itemsClone = new ArrayList<>();
                        confirmedAppsClone = new ArrayList<>();
                        ArrayList<String> packages = PreferenceManager.getInstance().getStringArrayPref(AppInfoDialog.this, "Packages");

                        //설치된 앱 리스트에서 승인된 앱 리스트에 추가///////////////
                        for (AppInfo appInfo : items)
                            if (packages.contains(appInfo.getPackageName()))
                                confirmedApps.add(appInfo);
                        //설치된 앱 리스트에서 승인앱 제외////////////////////////
                        for (AppInfo app : confirmedApps)
                            items.remove(app);
                        ////////////////////////////////////////////////////

                        itemsClone.addAll(items);
                        confirmedAppsClone.addAll(confirmedApps);

                        mAdapter = new DialogListAdapter(items);
                        mConfirmedAdapter = new DialogConfirmListAdapter(confirmedApps, AppInfoDialog.this);
                        binding.setConfirmAdapter(mConfirmedAdapter);
                        binding.dialogConfirmedApp.setLayoutManager(new GridLayoutManager(AppInfoDialog.this, 4));
                        binding.dialogConfirmedApp.setAdapter(mConfirmedAdapter);
                        binding.dialogApplist.setLayoutManager(new GridLayoutManager(AppInfoDialog.this, 4));
                        binding.dialogApplist.setNestedScrollingEnabled(false);
                        binding.dialogApplist.setAdapter(mAdapter);

                        mAdapter.setmOnMyItemCheckedChanged(new DialogListAdapter.OnMyItemCheckedChanged() {
                            @Override
                            public void onItemCheckedChanged(AppInfo app) {
                                confirmedAppsClone.add(app);
                                Collections.sort(confirmedAppsClone, sort);
                                itemsClone.remove(app);
                                updateLayout(itemsClone, confirmedAppsClone);
                            }
                        });

                        mConfirmedAdapter.setmOnMyItemCheckedChange(new DialogConfirmListAdapter.OnMyItemCheckedChange() {
                            @Override
                            public void onItemCheckedChange(AppInfo appInfo) {
                                itemsClone.add(appInfo);
                                Collections.sort(itemsClone, sort);
                                confirmedAppsClone.remove(appInfo);
                                updateLayout(itemsClone, confirmedAppsClone);
                            }
                        });
                    }
                });
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

    private void updateLayout(ArrayList<AppInfo> items, ArrayList<AppInfo> confirmedApps) {
        mAdapter.updateAppListItem(items);
        mConfirmedAdapter.updateConfirmedAppListItem(confirmedApps);
        binding.countComfirmedApps.setText(String.valueOf(mConfirmedAdapter.getItemCount()));
        binding.countComfirmedApps.setVisibility(mConfirmedAdapter.getItemCount() != 0 ? View.VISIBLE : View.INVISIBLE);
    }

    private class AscPackages implements Comparator<AppInfo> {

        @Override
        public int compare(AppInfo appInfo, AppInfo t1) {
            return appInfo.getName().compareTo(t1.getName());
        }
    }
}
