package teliov.com.unilagnews.utils;

import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by teliov on 3/22/15.
 */
public class Utility {
    public static String LOG_TAG = Utility.class.getSimpleName();
    public static String DATE_FORMAT = "ddMMyyyy";

    public static String getFriendlyTextFormat(String dateText) throws ParseException{

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date mDate;
        mDate = sdf.parse(dateText);
        long timemills = mDate.getTime();
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(timemills);
    }

    public static String saveDbDateFormat(String dateText) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date mDate;
        mDate = sdf.parse(dateText);
        long timemills = mDate.getTime();
        String dateFormatted= sdf.format(timemills);
        return dateFormatted;
    }
}
