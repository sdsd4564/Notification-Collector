package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import java.util.List;

import hanbat.encho.com.notificationcollactor.Model.AppInfo;


public class AppInfoDiffCallback extends DiffUtil.Callback {

    private List<AppInfo> mOldAppInfos;
    private List<AppInfo> mNewAppInfos;

    public AppInfoDiffCallback(List<AppInfo> mOldAppInfos, List<AppInfo> mNewAppInfos) {
        this.mOldAppInfos = mOldAppInfos;
        this.mNewAppInfos = mNewAppInfos;
    }

    @Override
    public int getOldListSize() {
        return mOldAppInfos == null ? 0 : mOldAppInfos.size();
    }

    @Override
    public int getNewListSize() {
        return mNewAppInfos == null ? 0 : mNewAppInfos.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldAppInfos.get(oldItemPosition).equals(mNewAppInfos.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final AppInfo oldAppInfo = mOldAppInfos.get(oldItemPosition);
        final AppInfo newAppInfo = mNewAppInfos.get(newItemPosition);

        return oldAppInfo.getName().equals(newAppInfo.getName())
                && oldAppInfo.getIcon() == newAppInfo.getIcon()
                && oldAppInfo.getPackageName().equals(newAppInfo.getPackageName());
    }
}
