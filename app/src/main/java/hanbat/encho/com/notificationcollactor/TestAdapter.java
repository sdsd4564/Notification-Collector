package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.TestGroup;
import hanbat.encho.com.notificationcollactor.databinding.TestParentBinding;

/**
 * Created by Encho on 2017-09-04.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private ArrayList<TestGroup> groups;
    private Context mContext;
    private DBhelper db = DBhelper.getInstance();
    private RecyclerView mRecyclerView;

    public TestAdapter(ArrayList<TestGroup> groups, Context mContext) {
        this.groups = groups;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_parent, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final TestGroup group = groups.get(i);
        viewHolder.parentBinding.setNoti(group);
        //todo SetAdapter here
    }

    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TestParentBinding parentBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            parentBinding = DataBindingUtil.bind(itemView);
        }
    }
}
