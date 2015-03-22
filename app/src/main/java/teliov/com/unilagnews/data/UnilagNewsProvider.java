package teliov.com.unilagnews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by teliov on 3/20/15.
 */
public class UnilagNewsProvider extends ContentProvider {

    private static final int UNILAG_NEWS = 100;
    private static final int UNILAG_NEWS_WITH_DATE = 101;
    private static final int UNILAG_NEWS_ID = 102;

    private static final String LOG_TAG = UnilagNewsProvider.class.getSimpleName();


    private static UriMatcher mUriMatcher = buildUriMatcher();
    private static UnilagNewsDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new UnilagNewsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match = mUriMatcher.match(uri);

        switch (match){
            case UNILAG_NEWS:
                retCursor = mDbHelper.getReadableDatabase().query(
                        UnilagNewsContract.UnilagNewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case UNILAG_NEWS_WITH_DATE:
                //TODO: Fix this!!!
                retCursor = null;
                break;

            case UNILAG_NEWS_ID:
                selection = UnilagNewsContract.UnilagNewsEntry._ID + " = " + ContentUris.parseId(uri);
                retCursor = mDbHelper.getReadableDatabase().query(
                        UnilagNewsContract.UnilagNewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Uri unknown: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return  retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        String uriType;

        switch (match){
            case UNILAG_NEWS:
                uriType = UnilagNewsContract.UnilagNewsEntry.CONTENT_TYPE;
                break;

            case UNILAG_NEWS_WITH_DATE:
                uriType = UnilagNewsContract.UnilagNewsEntry.CONTENT_TYPE;
                break;

            case UNILAG_NEWS_ID:
                uriType = UnilagNewsContract.UnilagNewsEntry.CONTENT_ITEM_TYPE;
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        return uriType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case UNILAG_NEWS:
                long _id = db.insert(UnilagNewsContract.UnilagNewsEntry.TABLE_NAME, null, contentValues);
                if (_id > 0){
                    returnUri = UnilagNewsContract.UnilagNewsEntry.buildNewsUri(_id);
                }else{
                    throw new SQLException("Failed to insert row into Datbase with URI: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        Log.d(LOG_TAG, "Match type for delete in provider " + match);
        int affected;

        switch (match){
            case UNILAG_NEWS:
                affected = db.delete(UnilagNewsContract.UnilagNewsEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (selection == null || affected != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int affected;

        switch (match){
            case UNILAG_NEWS:
                affected = db.update(UnilagNewsContract.UnilagNewsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (affected != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affected;
    }

    private static UriMatcher buildUriMatcher(){
        final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UnilagNewsContract.CONTENT_AUTHORITY;

        mUriMatcher.addURI(authority, UnilagNewsContract.PATH_NEWS, UNILAG_NEWS);
        mUriMatcher.addURI(authority, UnilagNewsContract.PATH_NEWS + "/#", UNILAG_NEWS_ID);
        mUriMatcher.addURI(authority, UnilagNewsContract.PATH_NEWS_WITH_START_DATE + "/*", UNILAG_NEWS_WITH_DATE);

        return mUriMatcher;
    }

    private static Cursor getUnilagNewsWithDate(Uri uri, String projection, String sortOrder){
        //TODO: implement this method
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        getContext().getContentResolver().notifyChange(uri, null);
        return super.bulkInsert(uri, values);
    }
}
