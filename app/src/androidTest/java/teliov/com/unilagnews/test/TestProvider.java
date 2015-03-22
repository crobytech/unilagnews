package teliov.com.unilagnews.test;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import teliov.com.unilagnews.data.UnilagNewsContract;

/**
 * Created by teliov on 3/20/15.
 */
public class TestProvider extends AndroidTestCase{
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteAllRecords(){
        mContext.getContentResolver().delete(
                UnilagNewsContract.UnilagNewsEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                UnilagNewsContract.UnilagNewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(cursor.getCount(), 0);
        cursor.close();
    }

    public void testGetType(){
        //TODO: test type for filtering by date, and title

        String type = mContext.getContentResolver().getType(UnilagNewsContract.UnilagNewsEntry.CONTENT_URI);
        assertEquals(UnilagNewsContract.UnilagNewsEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(UnilagNewsContract.UnilagNewsEntry.buildNewsUri(1L));
        assertEquals(type, UnilagNewsContract.UnilagNewsEntry.CONTENT_ITEM_TYPE);
    }

    public void testUpdateNews(){
        testDeleteAllRecords();
        ContentValues testValues = TestDb.createNewsItem();

        Uri newsUri = mContext.getContentResolver().insert(UnilagNewsContract.UnilagNewsEntry.CONTENT_URI, testValues);

        long newsRowId = ContentUris.parseId(newsUri);

        assertTrue(newsRowId != -1);

        ContentValues testValues2 = new ContentValues(testValues);
        testValues2.put(UnilagNewsContract.UnilagNewsEntry._ID, newsRowId);
        testValues2.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_TITLE, "Eating beans");

        String [] selectionArgs = new String [] {Long.toString(newsRowId)};

        int affected = mContext.getContentResolver().update(UnilagNewsContract.UnilagNewsEntry.CONTENT_URI, testValues2, UnilagNewsContract.UnilagNewsEntry._ID + " =? ", selectionArgs);
        assertEquals(affected, 1);

        Cursor cursor = mContext.getContentResolver().query(
                UnilagNewsContract.UnilagNewsEntry.buildNewsUri(newsRowId),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(cursor, testValues2);
        cursor.close();
    }

    public void testInsertReadProvider(){
        ContentValues testValues = TestDb.createNewsItem();
        Uri newsUri = mContext.getContentResolver().insert(UnilagNewsContract.UnilagNewsEntry.CONTENT_URI, testValues);
        long newsRowId = ContentUris.parseId(newsUri);

        assertTrue(newsRowId != -1);
        Log.d(LOG_TAG, "New row id: " + newsRowId);

        Cursor cursor = mContext.getContentResolver().query(
                UnilagNewsContract.UnilagNewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(cursor, testValues);

        cursor = mContext.getContentResolver().query(
                UnilagNewsContract.UnilagNewsEntry.buildNewsUri(newsRowId),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(cursor, testValues);

        cursor.close();
    }
}
