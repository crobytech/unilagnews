package teliov.com.unilagnews.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import teliov.com.unilagnews.data.UnilagNewsContract;
import teliov.com.unilagnews.data.UnilagNewsDbHelper;

/**
 * Created by teliov on 3/20/15.
 */
public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable{
        mContext.deleteDatabase(UnilagNewsDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new UnilagNewsDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb(){
        UnilagNewsDbHelper dbHelper = new UnilagNewsDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createNewsItem();

        long newsRowId = db.insert(UnilagNewsContract.UnilagNewsEntry.TABLE_NAME, null, testValues);

        assertTrue(newsRowId != -1);

        Log.d(LOG_TAG, "New row id: " + newsRowId);

        Cursor cursor = db.query(
                UnilagNewsContract.UnilagNewsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        validateCursor(cursor, testValues);
        dbHelper.close();
    }


    public static ContentValues createNewsItem (){
        ContentValues newsItem = new ContentValues();
        newsItem.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_DATE_ADDED, "20150319");
        newsItem.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_ID, "2481");
        newsItem.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_URL, "http:unilag.edu.ng/news?newsID=2481");
        newsItem.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_SUMMARY, "You will have to go get yout details now");
        newsItem.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_TITLE, "Get yours Now");

        return newsItem;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}
