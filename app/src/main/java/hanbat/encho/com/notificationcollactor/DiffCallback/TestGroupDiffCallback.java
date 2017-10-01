package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;

/**
 * Created by Encho on 2017-09-16.
 */

public class TestGroupDiffCallback extends DiffUtil.Callback {
    private ArrayList<NotificationGroup> mOldList;
    private ArrayList<NotificationGroup> mNewList;

    public TestGroupDiffCallback(ArrayList<NotificationGroup> mOldList, ArrayList<NotificationGroup> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return mNewList.get(i1).getPackageName().equals(mOldList.get(i).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        final NotificationGroup mOld = mOldList.get(i);
        final NotificationGroup mNew = mNewList.get(i1);

        return mOld.getCount() == mNew.getCount()
                && mOld.getAppName().equals(mNew.getAppName());
    }
}
