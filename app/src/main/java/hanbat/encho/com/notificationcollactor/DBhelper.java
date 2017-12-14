package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NOTIFICATION_COLLECTOR";
    private static final String TABLE_NAME = "notification_store";
    private static final int VERSION = 1;
    private static DBhelper f = null;

    private DBhelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    static synchronized DBhelper getInstance() {
        if (f == null) {
            f = new DBhelper(Application.getAppContext());
        }
        return f;
    }

    private ArrayList<NotificationGroup> getGroups(ArrayList<NotificationObject> items) {
        String query = "SELECT packagename, count() as count FROM "
                + TABLE_NAME
                + " GROUP BY packagename";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        Map<String, Integer> packagesCount = new HashMap<>();

        while (cursor.moveToNext()) {
            packagesCount.put(cursor.getString(0), cursor.getInt(1));
        }
        cursor.close();

        ArrayList<NotificationGroup> groups = new ArrayList<>();
        for (String app : PreferenceManager.getInstance().getStringArrayPref(Application.getAppContext(), "Packages")) {
            String[] row = app.split(",");
            ArrayList<NotificationObject> separatedItems = new ArrayList<>();
            for (NotificationObject object : items) {
                if (row[0].equals(object.getPackageName())) {
                    separatedItems.add(object);
                }
            }
            if (separatedItems.size() != 0) {
                if (packagesCount.get(row[0]) != separatedItems.size())
                    separatedItems.add(null);
                groups.add(new NotificationGroup(row[1], row[0], separatedItems, packagesCount.get(row[0])/*, Integer.parseInt(row[2])*/));
            }
        }

        return groups;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sb = "CREATE TABLE " + TABLE_NAME + " ( " +
                "title TEXT , " +
                "text TEXT," +
                "subtext TEXT," +
                "smallicon INTEGER," +
                "color INTEGER," +
                "largeicon BLOB," +
                "posttime INTEGER," +
                "packagename TEXT," +
                "appname TEXT) ";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(sb);

        Toast.makeText(Application.getAppContext(), "데이터베이스 생성 완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(Application.getAppContext(), "데이터베이스를 수정했습니다", Toast.LENGTH_SHORT).show();
    }

    void addNotification(NotificationObject object) {
        SQLiteDatabase db = getWritableDatabase();

        String noDuplicated = "SELECT title, text, subtext, posttime, packagename FROM " + TABLE_NAME +
                " WHERE title = ? AND" +
                " text = ? AND" +
                " posttime = ? AND" +
                " packagename = ?;";

        Cursor cursor = db.rawQuery(noDuplicated,
                new String[]{
                        object.getTitle(),
                        String.valueOf(object.getText()),
                        String.valueOf(object.getPostTime()),
                        object.getPackageName()});

        boolean isDuplicated = false;
        if (cursor.moveToNext()) {
            /*
            * 중복 체크 조건, 조건 만족시 true
            * 1. title, text, packageName이 같을 경우
            * 2. 위 조건을 만족한 상태에서 postTime까지 같을경우
            * 중복으로 확인
            **/
            isDuplicated = (cursor.getString(0).equals(object.getTitle())
                    && cursor.getString(1).equals(String.valueOf(object.getText()))
                    && cursor.getString(3).equals(object.getPackageName()))
                    && (cursor.getLong(2) == object.getPostTime());
        }
        cursor.close();

        if (!isDuplicated) {
            String sb = "INSERT INTO " + TABLE_NAME +
                    " (title, text, subtext, smallicon, color, largeicon, posttime, packagename, appname)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            SQLiteStatement st = db.compileStatement(sb);
            st.bindString(1, object.getTitle() == null ? "" : object.getTitle());
            st.bindString(2, String.valueOf(object.getText()));
            st.bindString(3, String.valueOf(object.getSubText() == null ? "" : object.getSubText()));
            st.bindLong(4, object.getSmallIcon());
            st.bindLong(5, object.getColor());
            Bitmap emptyImage = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(emptyImage);
            canvas.drawColor(Color.TRANSPARENT);
            st.bindBlob(6, object.getLargeIcon() == null ? new byte[0] : getByteArrayFromBitmap(object.getLargeIcon()));
            st.bindLong(7, object.getPostTime());
            st.bindString(8, object.getPackageName());
            st.bindString(9, String.valueOf(object.getAppName()));

            st.execute();
        }
    }

    ArrayList<NotificationGroup> deleteNotification(String packageName, long postTime) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE packagename = ? AND posttime = ?";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query, new String[]{packageName, String.valueOf(postTime)});
        return getAllNotifications();
    }

    ArrayList<NotificationGroup> deleteGroupNotification(String packageName) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE packagename = ?";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query, new String[]{packageName});
        return getAllNotifications();
    }

    ArrayList<NotificationGroup> deleteNotificationsExceededValidate(int validateTile) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE posttime < ?";
        Date date = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(validateTile));
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query, new String[]{String.valueOf(date.getTime())});
        return getAllNotifications();
    }

    ArrayList<NotificationGroup> getAllNotifications() {
        ArrayList<NotificationObject> list = new ArrayList<>();
        for (String s : PreferenceManager.getInstance().getStringArrayPref(Application.getAppContext(), "Packages")) {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE packagename = ? ORDER BY posttime DESC LIMIT 15";

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(query, new String[]{s.split(",")[0]});

            NotificationObject obj;

            while (cursor.moveToNext()) {
                obj = new NotificationObject();
                obj.setTitle(cursor.getString(0));
                obj.setText(cursor.getString(1));
                obj.setSubText(cursor.getString(2));
                obj.setSmallIcon(cursor.getInt(3));
                obj.setColor(cursor.getInt(4));
                byte[] largeIcon = cursor.getBlob(5);
                obj.setLargeIcon(getImage(largeIcon));
                obj.setPostTime(cursor.getLong(6));
                obj.setPackageName(cursor.getString(7));
                obj.setAppName(cursor.getString(8));

                list.add(obj);
            }

            cursor.close();
        }

        return getGroups(list);
    }

    ArrayList<NotificationObject> getNotification(String packageName, int offset) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE packagename = ? ORDER BY posttime DESC LIMIT 15 OFFSET ?";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{packageName, String.valueOf(offset)});

        NotificationObject obj;
        ArrayList<NotificationObject> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            obj = new NotificationObject();
            obj.setTitle(cursor.getString(0));
            obj.setText(cursor.getString(1));
            obj.setSubText(cursor.getString(2));
            obj.setSmallIcon(cursor.getInt(3));
            obj.setColor(cursor.getInt(4));
            byte[] largeIcon = cursor.getBlob(5);
            obj.setLargeIcon(getImage(largeIcon));
            obj.setPostTime(cursor.getLong(6));
            obj.setPackageName(cursor.getString(7));
            obj.setAppName(cursor.getString(8));

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
