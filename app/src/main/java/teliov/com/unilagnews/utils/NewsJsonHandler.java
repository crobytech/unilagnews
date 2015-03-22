package teliov.com.unilagnews.utils;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Vector;

import teliov.com.unilagnews.data.UnilagNewsContract;

/**
 * Created by teliov on 3/21/15.
 */
public class NewsJsonHandler {

    private String newsJSONStr;
    private static final String LOG_TAG = NewsJsonHandler.class.getSimpleName();

    public NewsJsonHandler(String newsJsonStr){
        newsJSONStr = newsJsonStr;
    }

    public Vector<ContentValues> getNewsFromJSON() throws JSONException{
        String DESCRIPTION = "body_brief";
        String DATE_ADDED = "date_added";
        String URL_LINK = "full_link";
        String NEWS_ID = "news_id";
        String TITLE = "title";
        String newsListKey = "objects";



        JSONObject newsJSON = new JSONObject(newsJSONStr);
        JSONArray newsJSONArray = newsJSON.getJSONArray(newsListKey);
        int arrayLength = newsJSONArray.length();


        Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(arrayLength);

        for(int i = 0; i< arrayLength; i++){
            String description;
            String dateAdded;
            String urlLink ;
            String newsId ;
            String title ;

            JSONObject newsItem = newsJSONArray.getJSONObject(i);

            description = newsItem.getString(DESCRIPTION);
            dateAdded = newsItem.getString(DATE_ADDED);
            urlLink = newsItem.getString(URL_LINK);
            newsId = newsItem.getString(NEWS_ID);
            title = newsItem.getString(TITLE);

            try{
                dateAdded = Utility.saveDbDateFormat(dateAdded);
            }catch (ParseException e){
                Log.d(LOG_TAG, e.toString());
            }

            ContentValues newsItemValues = new ContentValues();

            newsItemValues.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_TITLE, title);
            newsItemValues.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_DATE_ADDED, dateAdded);
            newsItemValues.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_URL, urlLink);
            newsItemValues.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_ID, newsId);
            newsItemValues.put(UnilagNewsContract.UnilagNewsEntry.COLUMN_SUMMARY, description);

            contentValuesVector.add(newsItemValues);
        }

        return  contentValuesVector;
    }
}
