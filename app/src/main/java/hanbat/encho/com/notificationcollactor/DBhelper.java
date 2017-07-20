package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
                "smallicon INTEGER ) ";

        db.execSQL(sb);

        Toast.makeText(mContext, "데이터베이스 생성 완료", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "데이터베이스를 수정했습니다", Toast.LENGTH_SHORT).show();
    }


    public void addNotification(NotificationObject object) {
        SQLiteDatabase db = getWritableDatabase();
        String sb = "INSERT INTO tempp " +
                "(title, text, subtext, smallicon) " +
                "VALUES (?, ?, ?, ?)";

        db.execSQL(sb, new Object[]{
                object.getTitle(),
                object.getText(),
                object.getSubText(),
                object.getSmallIcon()
        });

        Toast.makeText(mContext, "INSERT 완료", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<NotificationObject> getAllNotifications() {
        String query = "SELECT * FROM tempp";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<NotificationObject> list = new ArrayList<>();
        NotificationObject obj;

        while (cursor.moveToNext()) {
            obj = new NotificationObject();
            obj.setTitle(cursor.getString(0));
            obj.setText(cursor.getString(1));
            obj.setSubText(cursor.getString(2));
            obj.setSmallIcon(cursor.getInt(3));

            list.add(obj);
        }

        cursor.close();

        return list;
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }
}
