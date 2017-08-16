package hanbat.encho.com.notificationcollactor;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.DiffCallback.NotificationObjectDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;

/**
 * Created by USER on 2017-07-19.
 */

class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {

    private ArrayList<NotificationObject> items;

    MainListAdapter(ArrayList<NotificationObject> items) {
        this.items = items;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, int position) {
        final NotificationObject obj = items.get(position);
        holder.binding.setNoti(obj);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding binding;

        MainViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    void updateNotificationList(ArrayList<NotificationObject> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotificationObjectDiffCallback(this.items, items));

        this.items.clear();
        this.items.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }
}
