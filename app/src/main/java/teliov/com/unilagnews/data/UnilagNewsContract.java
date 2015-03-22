package teliov.com.unilagnews.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by teliov on 3/20/15.
 */
public class UnilagNewsContract {

    public static final String CONTENT_AUTHORITY = "teliov.com.unilagnews";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NEWS = "news";

    public static final String PATH_NEWS_WITH_START_DATE = "news/date";


    public static final class UnilagNewsEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        public static final String TABLE_NAME = "unilagnews";

        public static final String COLUMN_DATE_ADDED = "date";

        public static final String COLUMN_NEWS_ID = "news_id";

        public static final String COLUMN_NEWS_URL = "news_url";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_SUMMARY = "summary";

        public static Uri buildNewsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildNewsWithDateAdded(String dateAdded){
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_DATE_ADDED, dateAdded).build();
        }
    }

    public final static  String DATE_FORMAT = "yyyyMMdd";

    public static String getDbDateString (Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date getDateFromDb(String dateText){
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try{
            return dbDateFormat.parse(dateText);
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}
