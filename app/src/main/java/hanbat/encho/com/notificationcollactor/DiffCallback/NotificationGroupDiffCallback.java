package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import hanbat.encho.com.notificationcollactor.Model.NotiTest;

/**
 * Created by USER on 2017-08-25.
 */

public class NotificationGroupDiffCallback extends DiffUtil.Callback {
    private ArrayList<NotiTest> mOldList;
    private ArrayList<NotiTest> mNewList;

    public NotificationGroupDiffCallback(ArrayList<NotiTest> mOldList, ArrayList<NotiTest> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList == null ? 0 : mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList == null ? 0 : mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getPackageName().equals(mNewList.get(newItemPosition).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final NotiTest mOld = mOldList.get(oldItemPosition);
        final NotiTest mNew = mNewList.get(newItemPosition);

        return mNew.getItems() == mOld.getItems()
                && mNew.getAppName().equals(mOld.getAppName())
                && mNew.getItemCount() == mOld.getItemCount();
    }
}
