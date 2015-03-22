package teliov.com.unilagnews.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

import teliov.com.unilagnews.NewsListFragment;
import teliov.com.unilagnews.R;
import teliov.com.unilagnews.utils.Utility;

/**
 * Created by teliov on 3/21/15.
 */
public class NewsAdapter extends CursorAdapter {

    public static String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        viewHolder.iconView.setImageResource(R.drawable.ic_list_icon);

        String title = cursor.getString(NewsListFragment.COL_NEWS_TITLE);
        viewHolder.titleView.setText(title);

        String dateAdded = cursor.getString(NewsListFragment.COL_NEWS_DATE);
        try{
            dateAdded = Utility.getFriendlyTextFormat(dateAdded);
        }catch (ParseException e){
            Log.e(LOG_TAG, e.toString());
        }


        viewHolder.dateView.setText("Added on: " + dateAdded);

    }


    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView titleView;
        public final TextView dateView;

        public ViewHolder(View view){
            iconView = (ImageView)view.findViewById(R.id.news_list_item_icon);
            titleView = (TextView)view.findViewById(R.id.news_list_item_title);
            dateView = (TextView)view.findViewById(R.id.news_list_item_date_added);
        }

    }
}
