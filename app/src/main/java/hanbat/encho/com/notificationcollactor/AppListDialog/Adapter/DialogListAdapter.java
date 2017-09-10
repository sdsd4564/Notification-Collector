package hanbat.encho.com.notificationcollactor.AppListDialog.Adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.DiffCallback.AppInfoDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.DialogAllowedItemBinding;

/**
 * Created by USER on 2017-07-27.
 */

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.DialogViewHolder> {

    private OnMyItemCheckedChanged mOnMyItemCheckedChanged;
    private ArrayList<AppInfo> items;

    public DialogListAdapter(ArrayList<AppInfo> items) {
        this.items = items;
    }

    public void setmOnMyItemCheckedChanged(OnMyItemCheckedChanged onMyItemCheckedChanged) {
        this.mOnMyItemCheckedChanged = onMyItemCheckedChanged;
    }

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_allowed_item, parent, false);
        return new DialogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DialogViewHolder holder, int position) {
        final AppInfo app = items.get(position);
        holder.allowItemBinding.setApp(app);
        holder.allowItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnMyItemCheckedChanged.onItemCheckedChanged(app);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    public void updateAppListItem(ArrayList<AppInfo> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AppInfoDiffCallback(this.items, items));

        this.items.clear();
        this.items.addAll(items);

        diffResult.dispatchUpdatesTo(DialogListAdapter.this);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public interface OnMyItemCheckedChanged {
        void onItemCheckedChanged(AppInfo app);
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {
        DialogAllowedItemBinding allowItemBinding;

        DialogViewHolder(View itemView) {
            super(itemView);
            allowItemBinding = DataBindingUtil.bind(itemView);
        }
    }
}
