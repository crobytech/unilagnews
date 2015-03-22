package teliov.com.unilagnews;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import teliov.com.unilagnews.adapter.NewsAdapter;
import teliov.com.unilagnews.data.UnilagNewsContract;
import teliov.com.unilagnews.network.FetchDataTask;
import teliov.com.unilagnews.services.UnilagNewsService;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = NewsListFragment.class.getSimpleName();

    private NewsAdapter mNewsAdapter;

    public static final String[] NEWS_COLUMNS = {
            UnilagNewsContract.UnilagNewsEntry.TABLE_NAME + "." + UnilagNewsContract.UnilagNewsEntry._ID,
            UnilagNewsContract.UnilagNewsEntry.COLUMN_TITLE,
            UnilagNewsContract.UnilagNewsEntry.COLUMN_DATE_ADDED,
            UnilagNewsContract.UnilagNewsEntry.COLUMN_SUMMARY,
            UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_URL,
            UnilagNewsContract.UnilagNewsEntry._ID
    };

    public static final int COL_NEWS_ID = 0;
    public static final int COL_NEWS_TITLE = 1;
    public static final int COL_NEWS_DATE = 2;
    public static final int COL_NEWS_SUMMARY =3;
    public static final int COL_NEWS_LINK = 4;
    public static final int COL_ID = 5;

    private static final int NEWS_LOADER = 0;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        mNewsAdapter = new NewsAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        final ListView mListView = (ListView)rootView.findViewById(R.id.news_listview);
        mListView.setAdapter(mNewsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO Add Hook to NewsDetailActivity
                NewsAdapter adapter = (NewsAdapter)adapterView.getAdapter();

                Cursor aCursor = adapter.getCursor();
                if (aCursor != null && aCursor.moveToPosition(i)){
                    ((Callback)getActivity()).onItemSelected(aCursor.getString(COL_ID));
                }


            }
        });

        return rootView;
    }

    private void fetchNews(){
        Intent intent = new Intent(getActivity(), UnilagNewsService.class);
        intent.putExtra(UnilagNewsService.HANDLE_INTENT, "test string");
        getActivity().startService(intent);
        //new FetchDataTask(getActivity()).execute("testString");
    }

    @Override
    public void onStart() {
        super.onStart();
        Cursor mCursor = getActivity().getContentResolver().query(
                UnilagNewsContract.UnilagNewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (!mCursor.moveToFirst()){
            fetchNews();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //setHasOptionsMenu(true);
        inflater.inflate(R.menu.list_view_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            fetchNews();
            return true;
        }

        else {
            return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Uri newsUri = UnilagNewsContract.UnilagNewsEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                newsUri,
                NEWS_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mNewsAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    public interface Callback{
        public void onItemSelected (String rowId);
    }
}
