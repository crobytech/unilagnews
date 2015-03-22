package teliov.com.unilagnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by teliov on 3/20/15.
 */
public class UnilagNewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "unilagnews.db";

    public UnilagNewsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_UNILAG_NEWS_TABLE = "CREATE TABLE " + UnilagNewsContract.UnilagNewsEntry.TABLE_NAME + " (" +
                UnilagNewsContract.UnilagNewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UnilagNewsContract.UnilagNewsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_ID + " TEXT NOT NULL, " +
                UnilagNewsContract.UnilagNewsEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                UnilagNewsContract.UnilagNewsEntry.COLUMN_DATE_ADDED + " TEXT NOT NULL, " +
                UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_URL + " TEXT NOT NULL, " +

                "UNIQUE (" + UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_ID +") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_UNILAG_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UnilagNewsContract.UnilagNewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
