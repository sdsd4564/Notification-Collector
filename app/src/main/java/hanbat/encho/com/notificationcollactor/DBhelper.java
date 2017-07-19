package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

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
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE tempp ( ");
        sb.append("title TEXT , ");
        sb.append("text TEXT,");
        sb.append("subtext TEXT,");
        sb.append("smallicon INTEGER ) ");

        db.execSQL(sb.toString());

        Toast.makeText(mContext, "데이터베이스 생성 완료", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "데이터베이스를 수정했습니다", Toast.LENGTH_SHORT).show();
    }

    public void dbTest() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addNotification(NotificationObject object) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();

        sb.append("INSERT INTO tempp ")
                .append("(title, text, subtext, smallicon) ")
                .append("VALUES (?, ?, ?, ?)");

        db.execSQL(sb.toString(), new Object[]{
                object.getTitle(),
                object.getText(),
                object.getSubText(),
                object.getSmallIcon()
        });
        Toast.makeText(mContext, "INSERT 완료", Toast.LENGTH_SHORT).show();
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }
}
