package teliov.com.unilagnews;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class NewsList extends ActionBarActivity implements NewsListFragment.Callback {


    public static String appTag = NewsList.class.getSimpleName();

    public static String ROW_ID;

    private boolean mTwoPane;

    @Override
    public void onItemSelected(String rowId) {
        if (mTwoPane){
            Bundle args = new Bundle();
            args.putLong(ROW_ID, Long.parseLong(rowId));

            NewsDetailFragment fragment = new NewsDetailFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, fragment)
                    .commit();
        }else{
            Log.e(appTag, " Row ID" + Long.parseLong(rowId));
            Intent intent = new Intent(this, NewsDetail.class)
                    .putExtra(ROW_ID, Long.parseLong(rowId));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        if (findViewById(R.id.container_detail) != null){
            mTwoPane = true;

            if (savedInstanceState == null){
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_detail, new NewsDetailFragment())
                        .commit();
            }
        }else{
            mTwoPane = false;
        }
        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new NewsListFragment())
                    .commit();
        }*/
        Log.d(appTag, "Two pane is : " + mTwoPane);
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
