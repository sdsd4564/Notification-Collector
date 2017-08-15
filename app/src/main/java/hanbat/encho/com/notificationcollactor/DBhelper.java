package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by Encho on 2017-07-20.
 */

public class DBhelper extends SQLiteOpenHelper {

    private Context mContext;

    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sb = "CREATE TABLE tempp ( " +
                "title TEXT , " +
                "text TEXT," +
                "subtext TEXT," +
                "smallicon INTEGER," +
                "largeicon BLOB," +
                "posttime INTEGER," +
                "packagename TEXT ) ";

        db.execSQL("DROP TABLE IF EXISTS tempp");
        db.execSQL(sb);

        Toast.makeText(mContext, "데이터베이스 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "데이터베이스를 수정했습니다", Toast.LENGTH_SHORT).show();
    }


    public void addNotification(NotificationObject object) {
        SQLiteDatabase db = getWritableDatabase();
        Toast.makeText(Application.getAppContext(), "is it work?", Toast.LENGTH_SHORT).show();
        String sb = "INSERT INTO tempp " +
                "(title, text, subtext, smallicon, largeicon, posttime, packagename) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement st = db.compileStatement(sb);
        st.bindString(1, object.getTitle() == null ? "No Title" : object.getTitle());
        st.bindString(2, String.valueOf(object.getText()));
        st.bindString(3, String.valueOf(object.getSubText() == null ? "No Sub-Text" : object.getSubText()));
        st.bindLong(4, object.getSmallIcon());
        Drawable d = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
        st.bindBlob(5, object.getLargeIcon() == null ? getByteArrayFromBitmap(((BitmapDrawable) d).getBitmap()) : getByteArrayFromBitmap(object.getLargeIcon()));
        st.bindLong(6, object.getPostTime());
        st.bindString(7, object.getPackageName());
//        db.execSQL(sb, new Object[]{
//                object.getTitle(),
//                object.getText(),
//                object.getSubText(),
//                object.getSmallIcon(),
//                object.getPostTime(),
//                object.getPackageName()
//        });
        st.execute();
    }

    public ArrayList<NotificationObject> dropAllNotifications() {
        String query = "DELETE FROM tempp";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        return getAllNotifications();
    }

    public ArrayList<NotificationObject> getAllNotifications() {
        String query = "SELECT * FROM tempp";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<NotificationObject> list = new ArrayList<>();
        NotificationObject obj;

        while (cursor.moveToNext()) {
            Log.w("HanLOG check blob", cursor.getBlob(4) == null ? "No Photo" : "Has Photo");
            byte[] largeIcon = cursor.getBlob(4);


            obj = new NotificationObject();
            obj.setTitle(cursor.getString(0));
            obj.setText(cursor.getString(1));
            obj.setSubText(cursor.getString(2));
            obj.setSmallIcon(cursor.getInt(3));

            obj.setLargeIcon(getImage(largeIcon));
            obj.setPostTime(cursor.getLong(5));
            obj.setPackageName(cursor.getString(6));

            list.add(obj);
        }

        cursor.close();

        return list;
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        return stream.toByteArray();
    }

    private Bitmap getImage(byte[] ba) {
        return BitmapFactory.decodeByteArray(ba, 0, ba.length);
    }
}
