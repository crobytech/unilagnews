package teliov.com.unilagnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class NewsDetail extends ActionBarActivity {

    public static String appTag = NewsDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        if (savedInstanceState == null) {

            long rowID = getIntent().getLongExtra(NewsList.ROW_ID, 1L);

            Bundle arguments = new Bundle();
            arguments.putLong(NewsList.ROW_ID, rowID);

            Log.e(appTag, "Row ID " +rowID);

            NewsDetailFragment fragment = new NewsDetailFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.container_detail, fragment)
                    .commit();
        }
    }


    @Override
    protected void onStart() {
        Log.d(appTag, "On start has been called");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(appTag, "On stop has been called");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d(appTag, "On Resume has been called");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(appTag, "On Destroy has been called");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(appTag, "On pause has been called");
        super.onPause();
    }
}
