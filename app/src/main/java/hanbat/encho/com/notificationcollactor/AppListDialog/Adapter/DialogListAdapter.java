package hanbat.encho.com.notificationcollactor.AppListDialog.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.AppListDialog.AppInfoDialog;
import hanbat.encho.com.notificationcollactor.PreferenceManager;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.DialogAllowedItemBinding;

/**
 * Created by USER on 2017-07-27.
 */

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.DialogViewHolder> {

    public interface OnMyItemCheckedChanged {
        public void onItemCheckedChanged(AppInfo app);
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

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_allowed_item, parent, false);
        return new DialogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogViewHolder holder, int position) {
        AppInfo app = items.get(position);
        holder.allowItemBinding.setApp(app);
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
