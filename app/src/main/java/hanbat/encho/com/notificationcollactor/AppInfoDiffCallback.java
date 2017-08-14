package hanbat.encho.com.notificationcollactor;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import hanbat.encho.com.notificationcollactor.Model.AppInfo;

/**
 * Created by USER on 2017-08-14.
 */

public class AppInfoDiffCallback extends DiffUtil.Callback {

    private final List<AppInfo> mOldAppInfos;
    private final List<AppInfo> mNewAppInfos;

    public AppInfoDiffCallback(List<AppInfo> mOldAppInfos, List<AppInfo> mNewAppInfos) {
        this.mOldAppInfos = mOldAppInfos;
        this.mNewAppInfos = mNewAppInfos;
    }

    @Override
    public int getOldListSize() {
        return mOldAppInfos.size();
    }

    @Override
    public int getNewListSize() {
        return mNewAppInfos.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldAppInfos.get(oldItemPosition).getPackageName().equals(mNewAppInfos.get(newItemPosition).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final AppInfo oldAppInfo = mOldAppInfos.get(oldItemPosition);
        final AppInfo newAppInfo = mNewAppInfos.get(newItemPosition);

        return oldAppInfo.getName().equals(newAppInfo.getName())
                && oldAppInfo.getIcon().equals(newAppInfo.getIcon());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
