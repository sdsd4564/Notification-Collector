package hanbat.encho.com.notificationcollactor.AppListDialog.Adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.AppListDialog.AppInfoDialog;
import hanbat.encho.com.notificationcollactor.Application;
import hanbat.encho.com.notificationcollactor.DiffCallback.AppInfoDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.PreferenceManager;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.DialogAllowedItemBinding;

/**
 * Created by Encho on 2017-08-10.
 */

public class DialogConfirmListAdapter extends RecyclerView.Adapter<DialogConfirmListAdapter.DialogViewHolder> {

    private OnMyItemCheckedChange mOnMyItemCheckedChange;

    private ArrayList<AppInfo> confirmApps;
    private Context mContext;

    public DialogConfirmListAdapter(ArrayList<AppInfo> confirmApps, Context mContext) {
        this.confirmApps = confirmApps;
        this.mContext = mContext;
    }

    public void setmOnMyItemCheckedChange(OnMyItemCheckedChange onMyItemCheckedChange) {
        this.mOnMyItemCheckedChange = onMyItemCheckedChange;
    }

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_allowed_item, parent, false);
        return new DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DialogViewHolder holder, int position) {
        final AppInfo app = confirmApps.get(position);
        holder.binding.setApp(app);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnMyItemCheckedChange.onItemCheckedChange(app);
            }
        });
    }

    @Override
    public int getItemCount() {
        return confirmApps == null ? 0 : confirmApps.size();
    }

    @Override
    public long getItemId(int position) {
        return confirmApps.get(position).getPackageName().hashCode();
    }

    public void updateConfirmedAppListItem(ArrayList<AppInfo> confirmApps) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AppInfoDiffCallback(this.confirmApps, confirmApps));

        this.confirmApps.clear();
        this.confirmApps.addAll(confirmApps);
        diffResult.dispatchUpdatesTo(DialogConfirmListAdapter.this);
    }

    public void onAppCheckConfirm(View view) {
        ArrayList<String> apps = new ArrayList<>();
        for (AppInfo app : confirmApps) {
            Bitmap bitmap = convertDrawableToBitmap(app.getIcon());

            int redBucket = 0;
            int greenBucket = 0;
            int blueBucket = 0;
            int pixelCount = 0;
            for (int y = 0; y < bitmap.getHeight(); ++y) {
                for (int x = 0; x < bitmap.getWidth(); ++x) {
                    int c = bitmap.getPixel(x, y);
                    pixelCount++;
                    redBucket += Color.red(c);
                    greenBucket += Color.green(c);
                    blueBucket += Color.blue(c);
                }
            }
            int averageColor = Color.rgb(redBucket / pixelCount, greenBucket / pixelCount, blueBucket / pixelCount);

            apps.add(app.getPackageName() + "," + app.getName() + "," + averageColor);
            saveBitmapToFile(bitmap, app.getPackageName());
        }

        PreferenceManager.getInstance().setStringArrayPref(mContext, "Packages", apps);
        ((AppInfoDialog) mContext).setResult(Activity.RESULT_OK);
        ((AppInfoDialog) mContext).finish();

    }

    private Bitmap convertDrawableToBitmap(Drawable d) {
        if (d instanceof BitmapDrawable)
            return ((BitmapDrawable) d).getBitmap();

        Bitmap b = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);

        return b;
    }

    private void saveBitmapToFile(Bitmap bm, String filePath) {
        File iconImage = new File(Application.getAppContext().getFilesDir(), filePath);
        try {
            iconImage.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(iconImage);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(Application.getAppContext(), "알림 아이콘의 이미지 처리를 실패했습니다", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnMyItemCheckedChange {
        void onItemCheckedChange(AppInfo appInfo);
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {
        DialogAllowedItemBinding binding;

        DialogViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
