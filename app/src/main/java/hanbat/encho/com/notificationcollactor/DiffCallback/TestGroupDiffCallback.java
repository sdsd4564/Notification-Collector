package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.TestGroup;

/**
 * Created by Encho on 2017-09-16.
 */

public class TestGroupDiffCallback extends DiffUtil.Callback {
    private ArrayList<TestGroup> mOldList;
    private ArrayList<TestGroup> mNewList;

    public TestGroupDiffCallback(ArrayList<TestGroup> mOldList, ArrayList<TestGroup> mNewList) {
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
        final TestGroup mOld = mOldList.get(i);
        final TestGroup mNew = mNewList.get(i1);

        return mOld.getCount() == mNew.getCount()
                && mOld.getAppName().equals(mNew.getAppName());
    }
}
