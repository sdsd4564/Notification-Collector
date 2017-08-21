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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by Encho on 2017-07-20.
 */

class DBhelper extends SQLiteOpenHelper {

    private Context mContext;

    DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
                "packagename TEXT," +
                "appname TEXT) ";

        db.execSQL("DROP TABLE IF EXISTS tempp");
        db.execSQL(sb);

        Toast.makeText(mContext, "데이터베이스 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "데이터베이스를 수정했습니다", Toast.LENGTH_SHORT).show();
    }


    void addNotification(NotificationObject object) {
        SQLiteDatabase db = getWritableDatabase();
        String sb = "INSERT INTO tempp " +
                "(title, text, subtext, smallicon, largeicon, posttime, packagename, appname) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement st = db.compileStatement(sb);
        st.bindString(1, object.getTitle() == null ? "" : object.getTitle());
        st.bindString(2, String.valueOf(object.getText()));
        st.bindString(3, String.valueOf(object.getSubText() == null ? "" : object.getSubText()));
        st.bindLong(4, object.getSmallIcon());
        Drawable d = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
        st.bindBlob(5, object.getLargeIcon() == null ? getByteArrayFromBitmap(((BitmapDrawable) d).getBitmap()) : getByteArrayFromBitmap(object.getLargeIcon()));
        st.bindLong(6, object.getPostTime());
        st.bindString(7, object.getPackageName());
        st.bindString(8, String.valueOf(object.getAppName()));

        st.execute();
    }

    ArrayList<NotificationObject> dropAllNotifications() {
        String query = "DELETE FROM tempp";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        return getAllNotifications();
    }

    ArrayList<NotificationObject> getAllNotifications() {
        String query = "SELECT * FROM tempp";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<NotificationObject> list = new ArrayList<>();
        NotificationObject obj;

        while (cursor.moveToNext()) {
            byte[] largeIcon = cursor.getBlob(4);

            obj = new NotificationObject();
            obj.setTitle(cursor.getString(0));
            obj.setText(cursor.getString(1));
            obj.setSubText(cursor.getString(2));
            obj.setSmallIcon(cursor.getInt(3));
            Log.d("hanlog icon data check", cursor.getInt(3)+ "");
            obj.setLargeIcon(getImage(largeIcon));
            obj.setPostTime(cursor.getLong(5));
            obj.setPackageName(cursor.getString(6));
            obj.setAppName(cursor.getString(7));

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
