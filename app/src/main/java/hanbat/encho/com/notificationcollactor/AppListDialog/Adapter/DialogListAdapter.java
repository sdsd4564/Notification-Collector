package hanbat.encho.com.notificationcollactor.AppListDialog.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hanbat.encho.com.notificationcollactor.AppInfoDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.DialogAllowedItemBinding;

/**
 * Created by USER on 2017-07-27.
 */

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.DialogViewHolder> {

    public static final int NOTIFY_REMOVE = 15;
    public static final int NOTIFY_INSERT = 16;

    public interface OnMyItemCheckedChanged {
        void onItemCheckedChanged(AppInfo app, int position);
    }

    private OnMyItemCheckedChanged mOnMyItemCheckedChanged;
    private ArrayList<AppInfo> items;
    private Context mContext;

    public void setmOnMyItemCheckedChanged(OnMyItemCheckedChanged onMyItemCheckedChanged) {
        this.mOnMyItemCheckedChanged = onMyItemCheckedChanged;
    }

    public DialogListAdapter(Context mContext, ArrayList<AppInfo> items) {
        this.mContext = mContext;
        this.items = items;
    }

    public void updateList(ArrayList<AppInfo> apps, int position, int notifyMode) {
        items = apps;
        switch (notifyMode) {
            case NOTIFY_REMOVE:
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                break;
            case NOTIFY_INSERT:
                notifyDataSetChanged();
                break;
        }
    }

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_allowed_item, parent, false);
        return new DialogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogViewHolder holder, final int position) {
        final AppInfo app = items.get(position);
        holder.allowItemBinding.setApp(app);
        holder.allowItemBinding.appinfoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnMyItemCheckedChanged.onItemCheckedChanged(app, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {
        DialogAllowedItemBinding allowItemBinding;

        DialogViewHolder(View itemView) {
            super(itemView);
            allowItemBinding = DataBindingUtil.bind(itemView);
        }
    }

    public void updateAppListItem(List<AppInfo> apps) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AppInfoDiffCallback(this.items, apps));

        this.items.clear();
        this.items.addAll(apps);
        diffResult.dispatchUpdatesTo(this);
    }
}
