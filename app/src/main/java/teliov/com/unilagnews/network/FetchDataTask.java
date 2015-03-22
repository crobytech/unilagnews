package teliov.com.unilagnews.network;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import teliov.com.unilagnews.data.UnilagNewsContract;
import teliov.com.unilagnews.utils.NewsJsonHandler;

/**
 * Created by teliov on 3/21/15.
 */
public class FetchDataTask extends AsyncTask<String, Void, Void>{

    private final static String LOG_TAG = FetchDataTask.class.getSimpleName();

    private final Context mContext;

    String newsJsonStr = null;

    public FetchDataTask(Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length == 0){
            return null;
        }

        String randomData = params[0];

        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;

        String dataStr = null;

        try{
            //final String NEWS_BASE_URL = "http://196.45.48.245:8080/api/news_items/?format=json";
            final String NEWS_BASE_URL = "http://10.0.2.2:8000/api/news_items/?format=json";

            URL mUrl = new URL(NEWS_BASE_URL);

            urlConnection = (HttpURLConnection)mUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream mInputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (mInputStream == null){
                Log.d(LOG_TAG, "Returning Null Empty Input Stream");
            }

            reader = new BufferedReader(new InputStreamReader(mInputStream));
            String line;

            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                Log.d(LOG_TAG, "Returning Null Empty String Buffer");
                return null;
            }

            dataStr = buffer.toString();

        }catch (IOException e){
            Log.e(LOG_TAG, e.toString());
            Log.d(LOG_TAG, "Error while getting JSON data");
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    Log.e(LOG_TAG, e.toString());
                }
            }
        }

        try{
            Log.d(LOG_TAG, dataStr);
            Vector<ContentValues> newsData = new NewsJsonHandler(dataStr).getNewsFromJSON();
            Log.d(LOG_TAG, newsData.elementAt(0).toString());
            addDataToDatabase(newsData);
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString());

        }

        return null;
    }

    private void addDataToDatabase(Vector<ContentValues> newsData){
        if (newsData.size() > 0){
            ContentValues[]  contentValuesArray = new ContentValues[newsData.size()];
            newsData.toArray(contentValuesArray);

            int rowsInserted = mContext.getContentResolver().bulkInsert(UnilagNewsContract.UnilagNewsEntry.CONTENT_URI, contentValuesArray);
            Log.d(LOG_TAG, rowsInserted + " Rows Inserted!");
        }
    }
}
