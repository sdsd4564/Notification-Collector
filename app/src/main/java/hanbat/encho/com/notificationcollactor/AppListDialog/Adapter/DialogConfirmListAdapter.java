package hanbat.encho.com.notificationcollactor.AppListDialog.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.AppInfoDiffCallback;
import hanbat.encho.com.notificationcollactor.AppListDialog.AppInfoDialog;
import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.PreferenceManager;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.DialogAllowedItemBinding;

/**
 * Created by Encho on 2017-08-10.
 */

public class DialogConfirmListAdapter extends RecyclerView.Adapter<DialogConfirmListAdapter.DialogViewHolder> {

    public interface OnMyItemCheckedChange {
        void onItemCheckedChange(AppInfo appInfo, int position);
    }

    private OnMyItemCheckedChange mOnMyItemCheckedChange;
    private ArrayList<AppInfo> confirmApps;
    private Context mContext;

    public void setmOnMyItemCheckedChange(OnMyItemCheckedChange onMyItemCheckedChange) {
        this.mOnMyItemCheckedChange = onMyItemCheckedChange;
    }

    public DialogConfirmListAdapter(ArrayList<AppInfo> confirmApps, Context mContext) {
        this.confirmApps = confirmApps;
        this.mContext = mContext;
    }

    public void updateList(ArrayList<AppInfo> apps) {
        confirmApps = apps;
        notifyDataSetChanged();
    }

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_allowed_item, parent, false);
        return new DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogViewHolder holder, final int position) {
        final AppInfo app = confirmApps.get(position);
        holder.binding.setApp(app);
        holder.binding.appinfoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnMyItemCheckedChange.onItemCheckedChange(app, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return confirmApps == null ? 0 : confirmApps.size();
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {
        DialogAllowedItemBinding binding;

        DialogViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void updateConfirmedAppListItem(ArrayList<AppInfo> apps) {
        final AppInfoDiffCallback diffCallback = new AppInfoDiffCallback(this.confirmApps, apps);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.confirmApps.clear();
        this.confirmApps.addAll(apps);
        diffResult.dispatchUpdatesTo(this);
    }

    public void onAppCheckConfirm(View view) {
        Intent intent = new Intent();
        ArrayList<String> confirmedApps = new ArrayList<>();
//        for (AppInfo app : items)
//            if (app.isSelected())
//                confirmedApps.add(app.getPackageName());


        PreferenceManager.getInstance().setStringArrayPref(mContext, "Packages", confirmedApps);
        ((AppInfoDialog) mContext).setResult(Activity.RESULT_OK, intent);
        ((AppInfoDialog) mContext).finish();
    }
}
