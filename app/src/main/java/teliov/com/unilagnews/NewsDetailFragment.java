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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

import teliov.com.unilagnews.data.UnilagNewsContract;
import teliov.com.unilagnews.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NewsDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView titleView;
    private TextView dateView;
    private TextView summaryView;
    Button viewOnWebBtn;

    private long rowID = -1;
    private String ROW_ID_key = "row_id_key";

    private static String LOG_TAG = NewsDetailFragment.class.getSimpleName();

    Cursor mCursor;

    private static int DETAIL_LOADER = 0;


    public NewsDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle arguments = getArguments();
        if (arguments != null){
            rowID = arguments.getLong(NewsList.ROW_ID);
        }

        if (savedInstanceState != null){

        }
        View rootView = inflater.inflate(R.layout.fragment_news_detail, container, false);
        titleView = (TextView)rootView.findViewById(R.id.detail_title_view);
        dateView = (TextView)rootView.findViewById(R.id.detail_date_view);
        summaryView = (TextView)rootView.findViewById(R.id.detail_summary_view);
        viewOnWebBtn = (Button)rootView.findViewById(R.id.view_detail_on_web);
        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Bundle arguments = getArguments();
        //if (arguments != null && arguments.containsKey(NewsList.ROW_ID) && rowID!=-1 ){
        //    getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        //}
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(ROW_ID_key, rowID);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            rowID = savedInstanceState.getLong(ROW_ID_key);
        }
        Bundle arguments = getArguments();
        if (arguments == null){
            Log.e(LOG_TAG, "error, arguments is null");
        }


        if (arguments != null && arguments.containsKey(NewsList.ROW_ID)){

            getLoaderManager().initLoader(DETAIL_LOADER, arguments, this);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if (bundle == null || !bundle.containsKey(NewsList.ROW_ID)){
            return null;
        }

        rowID = getArguments().getLong(NewsList.ROW_ID);

        Uri mUri = UnilagNewsContract.UnilagNewsEntry.buildNewsUri(rowID);

        return new CursorLoader(
                getActivity(),
                mUri,
                NewsListFragment.NEWS_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()){
                String title = cursor.getString(cursor.getColumnIndex(UnilagNewsContract.UnilagNewsEntry.COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(UnilagNewsContract.UnilagNewsEntry.COLUMN_DATE_ADDED));
                String summary = cursor.getString(cursor.getColumnIndex(UnilagNewsContract.UnilagNewsEntry.COLUMN_SUMMARY));
                final String url = cursor.getString(cursor.getColumnIndex(UnilagNewsContract.UnilagNewsEntry.COLUMN_NEWS_URL));

                titleView.setText(title);
            try{
                date = Utility.getFriendlyTextFormat(date);
            }catch (ParseException e){
                Log.e(LOG_TAG, e.toString());
            }
                dateView.setText(date);
                summaryView.setText(summary);
                viewOnWebBtn.setVisibility(View.VISIBLE);

                viewOnWebBtn.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        Uri browserUri = Uri.parse(url);
                        Intent browseIntent = new Intent(Intent.ACTION_VIEW, browserUri);

                        String title = "Open Webpage With";
                        Intent chooser = Intent.createChooser(browseIntent, title);

                        if (browseIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(chooser);
                        }else{
                            Toast.makeText(getActivity(), "No App to view WebPages", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
